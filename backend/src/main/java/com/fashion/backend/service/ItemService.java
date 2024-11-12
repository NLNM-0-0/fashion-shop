package com.fashion.backend.service;

import com.fashion.backend.constant.ApplicationConst;
import com.fashion.backend.constant.StockChangeType;
import com.fashion.backend.entity.*;
import com.fashion.backend.payload.ListResponse;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.category.CategoryResponse;
import com.fashion.backend.payload.item.*;
import com.fashion.backend.payload.page.AppPageRequest;
import com.fashion.backend.payload.page.AppPageResponse;
import com.fashion.backend.repository.CategoryRepository;
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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
	private final ItemRepository itemRepository;
	private final CategoryRepository categoryRepository;
	private final StockChangeHistoryRepository stockChangeHistoryRepository;

	@Transactional
	public ListResponse<SimpleItemResponse, UserItemFilter> userGetItems(AppPageRequest page, UserItemFilter filter) {
		Pageable pageable = PageRequest.of(page.getPage() - 1,
										   page.getLimit(),
										   Sort.by(Sort.Direction.ASC, "name"));
		Specification<Item> spec = userFilterItems(filter);

		Page<Item> itemPage = itemRepository.findAllNotDelete(spec, pageable);

		List<Item> items = itemPage.getContent();

		List<SimpleItemResponse> data = items.stream().map(this::mapToDTOSimple).toList();

		return ListResponse.<SimpleItemResponse, UserItemFilter>builder()
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

	private Specification<Item> userFilterItems(UserItemFilter filter) {
		Specification<Item> spec = Specification.where(null);
		if (filter.getCategoryId() != null) {
			spec = spec.and(ItemSpecs.hasCategoryId(filter.getCategoryId()));
		}
		if (filter.getName() != null) {
			spec = spec.and(ItemSpecs.hasName(filter.getName()));
		}
//		if (filter.getSeasons() != null && !filter.getSeasons().isEmpty()) {
//			spec = spec.and(ItemSpecs.hasSeason(filter.getSeasons()));
//		}
//		if (filter.getGenders() != null) {
//			spec = spec.and(ItemSpecs.hasGender(filter.getGenders()));
//		}
//		if (filter.getColors() != null) {
//			spec = spec.and(ItemSpecs.hasColor(filter.getColors()));
//		}
		return spec;
	}

	@Transactional
	public ListResponse<SimpleItemResponse, AdminItemFilter> staffGetItems(AppPageRequest page, AdminItemFilter filter) {
		Pageable pageable = PageRequest.of(page.getPage() - 1,
										   page.getLimit(),
										   Sort.by(Sort.Direction.ASC, "name"));
		Specification<Item> spec = staffFilterItems(filter);

		Page<Item> itemPage = itemRepository.findAllNotDelete(spec, pageable);

		List<Item> items = itemPage.getContent();

		List<SimpleItemResponse> data = items.stream().map(this::mapToDTOSimple).toList();

		return ListResponse.<SimpleItemResponse, AdminItemFilter>builder()
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

	private Specification<Item> staffFilterItems(AdminItemFilter filter) {
		Specification<Item> spec = Specification.where(null);
		if (filter.getName() != null) {
			spec = spec.and(ItemSpecs.hasName(filter.getName()));
		}
//		if (filter.getGender() != null) {
//		    spec = spec.and(ItemSpecs.hasGender(filter.getGender().name()));
//		}
//		if (filter.getSeason() != null) {
//			spec = spec.and(ItemSpecs.hasGender(filter.getSeason().name()));
//		}
//		if (filter.getCategoryName() != null) {
//			spec = spec.and(ItemSpecs.hasGender(filter.getCategoryName()));
//		}
		return spec;
	}

	@Transactional
	public ItemResponse getItem(Long itemId) {
		Item item = Common.findItemById(itemId, itemRepository);

		return mapToDTO(item);
	}

	@Transactional
	public ItemResponse createItem(CreateItemRequest request) {
		List<Category> categories = Common.findCategoryByIds(request.getCategories(), categoryRepository);

		handleImage(request);
		Item item = Item.builder()
						.name(request.getName())
						.season(request.getSeason())
						.sizes(request.getSizes().stream().map(this::mapToEntity).toList())
						.colors(request.getColors().stream().map(this::mapToEntity).toList())
						.quantity(request.getQuantity())
						.categories(categories)
						.unitPrice(request.getUnitPrice())
						.images(request.getImages().stream().map(this::mapToEntity).toList())
						.isDeleted(false)
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
		if (request.getImages().isEmpty()) {
			request.setImages(new ArrayList<>(List.of(ItemImageDTO.builder()
																  .image(ApplicationConst.DEFAULT_IMAGE)
																  .build())));
		}
	}

	@Transactional
	public ItemResponse updateItem(Long itemId, UpdateItemRequest request) {
		List<Category> categories = Common.findCategoryByIds(request.getCategories(), categoryRepository);

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
		Common.updateIfNotNull(request.getGender(), item::setGender);
		Common.updateIfNotNull(request.getSeason(), item::setSeason);
		Common.updateIfNotNull(request.getSizes().stream().map(this::mapToEntity).toList(), item::setSizes);
		Common.updateIfNotNull(request.getColors().stream().map(this::mapToEntity).toList(), item::setColors);
		Common.updateIfNotNull(categories, item::setCategories);
		Common.updateIfNotNull(request.getQuantity(), item::setQuantity);
		Common.updateIfNotNull(request.getUnitPrice(), item::setUnitPrice);

		List<ItemImage> images = request.getImages().stream().map(this::mapToEntity).toList();
		Common.updateIfNotNull(images, item::setImages);

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
								 .images(item.getImages().stream().map(this::mapToDTO).toList())
								 .isDeleted(item.isDeleted())
								 .build();
	}

	private ItemResponse mapToDTO(Item item) {
		return ItemResponse.builder()
						   .id(item.getId())
						   .name(item.getName())
						   .gender(item.getGender())
						   .season(item.getSeason())
						   .colors(item.getColors().stream().map(this::mapToDTO).toList())
						   .sizes(item.getSizes().stream().map(this::mapToDTO).toList())
						   .categories(item.getCategories().stream().map(this::mapToDTO).toList())
						   .unitPrice(item.getUnitPrice())
						   .images(item.getImages().stream().map(this::mapToDTO).toList())
						   .isDeleted(item.isDeleted())
						   .build();
	}

	private ItemImageDTO mapToDTO(ItemImage image) {
		return ItemImageDTO.builder()
						   .image(image.getImage())
						   .build();
	}

	private ItemImage mapToEntity(ItemImageDTO image) {
		return ItemImage.builder()
						.image(image.getImage())
						.build();
	}

	private ItemSize mapToEntity(CreateItemSizeRequest size) {
		return ItemSize.builder()
					   .name(size.getName())
					   .build();
	}

	private ItemColor mapToEntity(CreateItemColorRequest color) {
		return ItemColor.builder()
						.name(color.getName())
						.hex(color.getHex())
						.build();
	}

	private ItemSizeDTO mapToDTO(ItemSize size) {
		return ItemSizeDTO.builder()
						  .name(size.getName())
						  .build();
	}

	private ItemColorDTO mapToDTO(ItemColor color) {
		return ItemColorDTO.builder()
						   .name(color.getName())
						   .hex(color.getHex())
						   .build();
	}

	private CategoryResponse mapToDTO(Category category) {
		return CategoryResponse.builder()
							   .id(category.getId())
							   .name(category.getName())
							   .build();
	}
}
