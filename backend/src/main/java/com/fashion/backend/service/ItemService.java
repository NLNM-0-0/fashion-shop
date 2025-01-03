package com.fashion.backend.service;

import com.fashion.backend.constant.ApplicationConst;
import com.fashion.backend.constant.Color;
import com.fashion.backend.constant.StockChangeType;
import com.fashion.backend.entity.*;
import com.fashion.backend.payload.ListResponse;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.category.CategoryResponse;
import com.fashion.backend.payload.item.*;
import com.fashion.backend.payload.page.AppPageRequest;
import com.fashion.backend.payload.page.AppPageResponse;
import com.fashion.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemService {
	private final ItemRepository itemRepository;
	private final CategoryRepository categoryRepository;
	private final StockChangeHistoryRepository stockChangeHistoryRepository;
	private final ItemQuantityRepository itemQuantityRepository;
	private final LikeRepository likeRepository;
	private final UserRepository userRepository;
	private final UserAuthRepository userAuthRepository;

	@Transactional
	public ListResponse<SimpleItemDetail, UserItemFilter> userGetItems(AppPageRequest page,
																	   UserItemFilter filter) {
		Pageable pageable = PageRequest.of(page.getPage() - 1,
										   page.getLimit(),
										   userGetSort(filter));
		Specification<Item> spec = userFilterItems(filter);

		Page<Item> itemPage = itemRepository.findAllNotDelete(spec, pageable);

		List<Item> items = itemPage.getContent();

		List<SimpleItemDetail> data;
		Optional<Long> userId = Common.getUserLoginId(userRepository, userAuthRepository);
		data = userId.map(aLong -> items.stream()
										.map(item -> {
											Optional<Like> like = likeRepository.findFirstByUserIdAndItemId(aLong,
																											item.getId());
											return mapToDTOSimple(item, like.isPresent());
										})
										.toList()).orElseGet(() -> items.stream()
																		.map(item -> mapToDTOSimple(item, false))
																		.toList());


		return ListResponse.<SimpleItemDetail, UserItemFilter>builder()
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

	private Sort userGetSort(UserItemFilter filter) {
		List<Sort.Order> orders = new ArrayList<>();

		if (filter.getSortPrice() != null) {
			if (filter.getSortPrice()) {
				orders.add(Sort.Order.asc("unitPrice"));
			} else {
				orders.add(Sort.Order.desc("unitPrice"));
			}
		}

		if (filter.getSortNew() != null) {
			if (filter.getSortNew()) {
				orders.add(Sort.Order.desc("createdAt"));
			} else {
				orders.add(Sort.Order.asc("createdAt"));
			}
		}

		orders.add(Sort.Order.asc("name"));

		return Sort.by(orders);
	}

	private Specification<Item> userFilterItems(UserItemFilter filter) {
		Specification<Item> spec = Specification.where(null);
		if (filter.getCategoryId() != null) {
			spec = spec.and(ItemSpecs.hasCategoryId(filter.getCategoryId()));
		}
		if (filter.getName() != null) {
			spec = spec.and(ItemSpecs.hasName(filter.getName()));
		}
		if (filter.getSeasons() != null) {
			spec = spec.and(ItemSpecs.hasSeason(filter.getSeasons()));
		}
		if (filter.getGenders() != null) {
			spec = spec.and(ItemSpecs.hasGender(filter.getGenders()));
		}
		if (filter.getColors() != null) {
			spec = spec.and(ItemSpecs.hasColor(filter.getColors()));
		}
		if (filter.getPrice() != null) {
			Integer minValue = null, maxValue = null;
			switch (filter.getPrice()) {
				case BELOW199:
					maxValue = 199000;
					break;
				case FROM199TO299:
					minValue = 199000;
					maxValue = 299000;
					break;
				case FROM299TO399:
					minValue = 299000;
					maxValue = 399000;
					break;
				case FROM399TO499:
					minValue = 399000;
					maxValue = 499000;
					break;
				case FROM499TO799:
					minValue = 499000;
					maxValue = 799000;
					break;
				case FROM799TO999:
					minValue = 799000;
					maxValue = 999000;
					break;
				case ABOVE999:
					minValue = 999000;
					break;
			}
			spec = spec.and(ItemSpecs.hasPrice(minValue, maxValue));
		}

		return spec;
	}

	@Transactional
	public ListResponse<SimpleItemResponse, AdminItemFilter> staffGetItems(AppPageRequest page,
																		   AdminItemFilter filter) {
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
		return spec;
	}

	@Transactional
	public ItemResponse staffGetItem(Long itemId) {
		Item item = Common.findItemById(itemId, itemRepository);

		return mapToDTO(item);
	}

	@Transactional
	public ItemDetail userGetItem(Long itemId) {
		Optional<Long> userId = Common.getUserLoginId(userRepository, userAuthRepository);

		Item item = Common.findItemById(itemId, itemRepository);

		boolean liked = false;
		if (userId.isPresent()) {
			Optional<Like> like = likeRepository.findFirstByUserIdAndItemId(userId.get(), itemId);
			liked = like.isPresent();
		}

		return mapToDTO(item, liked);
	}

	private int calcTotalQuantityFromItemId(Long itemId) {
		List<ItemQuantity> quantities = itemQuantityRepository.findAllByItemId(itemId);

		return quantities
				.stream()
				.mapToInt(ItemQuantity::getQuantity)
				.sum();
	}

	private int calcTotalQuantityFromDTOs(List<ItemQuantityRequest> quantities) {
		return quantities
				.stream()
				.mapToInt(ItemQuantityRequest::getQuantity)
				.sum();
	}

	@Transactional
	public ItemResponse createItem(CreateItemRequest request) {
		List<Category> categories = Common.findCategoryByIds(request.getCategories(), categoryRepository);

		handleImage(request);

		Item item = Item.builder()
						.name(request.getName())
						.gender(request.getGender())
						.season(request.getSeason())
						.categories(categories)
						.unitPrice(request.getUnitPrice())
						.images(request.getImages())
						.isDeleted(false)
						.build();

		item = itemRepository.save(item);

		List<ItemQuantity> quantities = new ArrayList<>();
		int totalQuantity = 0;
		for (ItemQuantityRequest quantity : request.getQuantities()) {
			ItemQuantity quantityEntity = mapToEntity(quantity);
			quantityEntity.setItem(item);

			quantities.add(quantityEntity);

			totalQuantity += quantity.getQuantity();
		}
		itemQuantityRepository.saveAll(quantities);

		if (totalQuantity != 0) {
			StockChangeHistory stockChangeHistory = StockChangeHistory.builder()
																	  .item(item)
																	  .type(StockChangeType.INCREASE)
																	  .quantity(totalQuantity)
																	  .quantityLeft(totalQuantity)
																	  .build();

			stockChangeHistoryRepository.save(stockChangeHistory);
		}

		return mapToDTO(item);
	}

	private void handleImage(CreateItemRequest request) {
		if (request.getImages().isEmpty()) {
			request.setImages(new ArrayList<>(List.of(ApplicationConst.DEFAULT_IMAGE)));
		}
	}

	@Transactional
	public ItemResponse updateItem(Long itemId, UpdateItemRequest request) {
		List<Category> categories = Common.findCategoryByIds(request.getCategories(), categoryRepository);

		Item item = Common.findItemById(itemId, itemRepository);

		int totalItemQuantity = this.calcTotalQuantityFromItemId(itemId);
		int totalUpdatedQuantity = calcTotalQuantityFromDTOs(request.getQuantities());

		if (totalItemQuantity != totalUpdatedQuantity) {
			int diff = totalUpdatedQuantity - totalItemQuantity;
			StockChangeType type = diff > 0 ? StockChangeType.INCREASE : StockChangeType.DECREASE;

			StockChangeHistory stockChangeHistory = StockChangeHistory.builder()
																	  .item(item)
																	  .type(type)
																	  .quantity(diff)
																	  .quantityLeft(totalUpdatedQuantity)
																	  .build();

			stockChangeHistoryRepository.save(stockChangeHistory);
		}

		Common.updateIfNotNull(request.getName(), item::setName);
		Common.updateIfNotNull(request.getGender(), item::setGender);
		Common.updateIfNotNull(request.getSeason(), item::setSeason);
		Common.updateIfNotNull(categories, item::setCategories);
		Common.updateIfNotNull(request.getUnitPrice(), item::setUnitPrice);
		Common.updateIfNotNull(request.getImages(), item::setImages);

		itemQuantityRepository.deleteAllByItemId(itemId);

		List<ItemQuantity> quantities = request.getQuantities().stream().map(dto -> {
			ItemQuantity itemQuantity = mapToEntity(dto);
			itemQuantity.setItem(item);
			return itemQuantity;
		}).toList();

		itemQuantityRepository.saveAll(quantities);

		return mapToDTO(itemRepository.save(item));
	}

	@Transactional
	public SimpleResponse deleteItem(Long itemId) {
		Item item = Common.findItemById(itemId, itemRepository);

		item.setDeleted(true);

		itemRepository.save(item);

		return new SimpleResponse();
	}

	private SimpleItemDetail mapToDTOSimple(Item item, boolean liked) {
		return SimpleItemDetail.builder()
							   .id(item.getId())
							   .name(item.getName())
							   .unitPrice(item.getUnitPrice())
							   .images(item.getImages())
							   .categories(item.getCategories().stream().map(this::mapToDTO).toList())
							   .gender(item.getGender())
							   .liked(liked)
							   .isDeleted(item.isDeleted())
							   .build();
	}

	private SimpleItemResponse mapToDTOSimple(Item item) {
		return SimpleItemResponse.builder()
								 .id(item.getId())
								 .name(item.getName())
								 .unitPrice(item.getUnitPrice())
								 .images(item.getImages())
								 .isDeleted(item.isDeleted())
								 .build();
	}

	private ItemDetail mapToDTO(Item item, boolean liked) {
		ItemResponse itemResponse = mapToDTO(item);

		return ItemDetail.builder()
						 .id(itemResponse.getId())
						 .name(itemResponse.getName())
						 .gender(itemResponse.getGender())
						 .season(itemResponse.getSeason())
						 .colors(itemResponse.getColors())
						 .sizes(itemResponse.getSizes())
						 .quantities(itemResponse.getQuantities())
						 .categories(itemResponse.getCategories())
						 .unitPrice(itemResponse.getUnitPrice())
						 .images(itemResponse.getImages())
						 .isDeleted(itemResponse.isDeleted())
						 .liked(liked)
						 .build();
	}

	private ItemResponse mapToDTO(Item item) {
		List<ItemQuantity> quantities = itemQuantityRepository.findAllByItemId(item.getId());

		Set<String> sizes = new HashSet<>();
		Set<Color> colors = new HashSet<>();
		Map<String, Integer> quantitiesDTO = new HashMap<>();

		for (ItemQuantity quantity : quantities) {
			String size = quantity.getSize();
			Color color = quantity.getColor();

			String key = size + "-" + color.name();
			quantitiesDTO.put(key, quantity.getQuantity());

			sizes.add(size);
			colors.add(color);
		}

		return ItemResponse.builder()
						   .id(item.getId())
						   .name(item.getName())
						   .gender(item.getGender())
						   .season(item.getSeason())
						   .colors(colors.stream().map(this::mapToDTO).toList())
						   .sizes(sizes.stream().map(this::mapToDTO).toList())
						   .quantities(quantitiesDTO)
						   .categories(item.getCategories().stream().map(this::mapToDTO).toList())
						   .unitPrice(item.getUnitPrice())
						   .images(item.getImages())
						   .isDeleted(item.isDeleted())
						   .build();
	}

	private ItemQuantity mapToEntity(ItemQuantityRequest request) {
		return ItemQuantity.builder()
						   .color(request.getColor())
						   .quantity(request.getQuantity())
						   .size(request.getSize())
						   .build();
	}

	private ItemSizeDTO mapToSizeDTO(ItemQuantity quantityEntity) {
		return ItemSizeDTO.builder()
						  .name(quantityEntity.getSize())
						  .build();
	}

	private ItemSizeDTO mapToDTO(String size) {
		return ItemSizeDTO.builder()
						  .name(size)
						  .build();
	}

	private ItemColorDTO mapToDTO(Color color) {
		return ItemColorDTO.builder()
						   .name(color.name())
						   .hex(color.getHexValue())
						   .build();
	}

	private CategoryResponse mapToDTO(Category category) {
		return CategoryResponse.builder()
							   .id(category.getId())
							   .name(category.getName())
							   .build();
	}
}
