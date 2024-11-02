package com.fashion.backend.service;

import com.fashion.backend.constant.ApplicationConst;
import com.fashion.backend.constant.StockChangeType;
import com.fashion.backend.entity.Item;
import com.fashion.backend.entity.StockChangeHistory;
import com.fashion.backend.payload.ListResponse;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.item.*;
import com.fashion.backend.payload.page.AppPageRequest;
import com.fashion.backend.payload.page.AppPageResponse;
import com.fashion.backend.repository.ItemRepository;
import com.fashion.backend.repository.StockChangeHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
	private final ItemRepository itemRepository;
	private final StockChangeHistoryRepository stockChangeHistoryRepository;

	@Transactional
	public ListResponse<SimpleItemResponse, ItemFilter> getItems(AppPageRequest page, ItemFilter filter) {
		Pageable pageable = PageRequest.of(page.getPage() - 1,
										   page.getLimit(),
										   Sort.by(Sort.Direction.ASC, "name"));
		Specification<Item> spec = filterItems(filter);

		Page<Item> itemPage = itemRepository.findAllNotDelete(spec, pageable);

		List<Item> items = itemPage.getContent();

		List<SimpleItemResponse> data = items.stream().map(this::mapToDTOSimple).toList();

		return ListResponse.<SimpleItemResponse, ItemFilter>builder()
						   .data(data)
						   .appPageResponse(AppPageResponse.builder()
														   .index(page.getPage())
														   .limit(page.getLimit())
														   .totalPages(itemPage.getTotalPages())
														   .totalElements(itemPage.getTotalElements())
														   .build())
						   .filter(filter)
						   .build();
	}

	private Specification<Item> filterItems(ItemFilter filter) {
		Specification<Item> spec = Specification.where(null);
		if (filter.getName() != null) {
			spec = ItemSpecs.hasName(filter.getName());
		}
		return spec;
	}

	@Transactional
	public ItemResponse getItem(Long itemId) {
		Item item = Common.findItemById(itemId, itemRepository);

		return mapToDTO(item);
	}

	@Transactional
	public ItemResponse createItem(CreateItemRequest request) {
		handleImage(request);
		Item item = Item.builder()
						.name(request.getName())
						.isDeleted(false)
						.quantity(request.getQuantity())
						.unitPrice(request.getUnitPrice())
						.image(request.getImage())
						.build();

		item = itemRepository.save(item);

		if (request.getQuantity() != 0) {
			StockChangeHistory stockChangeHistory = StockChangeHistory.builder()
																	  .item(item)
																	  .type(StockChangeType.INCREASE)
																	  .quantity(request.getQuantity())
																	  .quantityLeft(request.getQuantity())
																	  .build();

			stockChangeHistoryRepository.save(stockChangeHistory);
		}

		return mapToDTO(item);
	}

	private void handleImage(CreateItemRequest request) {
		if (request.getImage().isEmpty()) {
			request.setImage(ApplicationConst.DEFAULT_IMAGE);
		}
	}

	@Transactional
	public ItemResponse updateItem(Long itemId, UpdateItemRequest request) {
		Item item = Common.findItemById(itemId, itemRepository);

		if (item.getQuantity() != request.getQuantity()) {
			int diff = request.getQuantity() - item.getQuantity();
			StockChangeType type = diff > 0 ? StockChangeType.INCREASE : StockChangeType.DECREASE;

			StockChangeHistory stockChangeHistory = StockChangeHistory.builder()
																	  .item(item)
																	  .type(type)
																	  .quantity(diff)
																	  .quantityLeft(request.getQuantity())
																	  .build();

			stockChangeHistoryRepository.save(stockChangeHistory);
		}

		Common.updateIfNotNull(request.getName(), item::setName);
		Common.updateIfNotNull(request.getQuantity(), item::setQuantity);
		Common.updateIfNotNull(request.getUnitPrice(), item::setUnitPrice);
		Common.updateIfNotNull(request.getImage(), item::setImage);

		return mapToDTO(itemRepository.save(item));
	}

	@Transactional
	public SimpleResponse deleteItem(Long itemId) {
		Item item = Common.findItemById(itemId, itemRepository);

		item.setDeleted(true);

		return new SimpleResponse();
	}

	private SimpleItemResponse mapToDTOSimple(Item item) {
		return SimpleItemResponse.builder()
								 .id(item.getId())
								 .name(item.getName())
								 .image(item.getImage())
								 .isDeleted(item.isDeleted())
								 .build();
	}

	private ItemResponse mapToDTO(Item item) {
		return ItemResponse.builder()
						   .id(item.getId())
						   .name(item.getName())
						   .image(item.getImage())
						   .isDeleted(item.isDeleted())
						   .build();
	}
}
