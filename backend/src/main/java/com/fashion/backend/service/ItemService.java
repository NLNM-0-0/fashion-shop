package com.fashion.backend.service;

import com.fashion.backend.constant.*;
import com.fashion.backend.entity.Category;
import com.fashion.backend.entity.Item;
import com.fashion.backend.entity.ItemQuantity;
import com.fashion.backend.entity.StockChangeHistory;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.payload.CheckedFilter;
import com.fashion.backend.payload.ListResponse;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.category.CategoryResponse;
import com.fashion.backend.payload.item.*;
import com.fashion.backend.payload.page.AppPageRequest;
import com.fashion.backend.payload.page.AppPageResponse;
import com.fashion.backend.repository.CategoryRepository;
import com.fashion.backend.repository.ItemQuantityRepository;
import com.fashion.backend.repository.ItemRepository;
import com.fashion.backend.repository.StockChangeHistoryRepository;
import com.fashion.backend.utils.tuple.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
	private final ItemRepository itemRepository;
	private final CategoryRepository categoryRepository;
	private final StockChangeHistoryRepository stockChangeHistoryRepository;
	private final ItemQuantityRepository itemQuantityRepository;

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
		Pair<Boolean, List<CheckedFilter<Season>>> filteredSeasonsPair = getFilteredSeason(filter.getSeasons());
		Boolean isNeedToFilterSeason = filteredSeasonsPair.first();
		List<CheckedFilter<Season>> filterSeasons = filteredSeasonsPair.second();

		List<Item> items = itemRepository.findAllNotDelete();
		Pair<Boolean, List<CheckedFilter<ItemSizeDTO>>> filterSizesPair = getFilteredSizes(items, filter.getSizes());
		Boolean isNeedToFilterSize = filterSizesPair.first();
		List<CheckedFilter<ItemSizeDTO>> filterSizes = filterSizesPair.second();

		Pair<Boolean, List<CheckedFilter<Gender>>> filterGendersPair = getFilteredGender(filter.getGenders());
		Boolean isNeedToFilterGender = filterGendersPair.first();
		List<CheckedFilter<Gender>> filterGenders = filterGendersPair.second();

		Pair<Boolean, List<CheckedFilter<ItemColorDTO>>> filterColorsPair = getFilteredColors(filter.getColors());
		Boolean isNeedToFilterColor = filterColorsPair.first();
		List<CheckedFilter<ItemColorDTO>> filterColors = filterColorsPair.second();

		Pair<Boolean, List<CheckedFilter<PriceFilter>>> filterPricesPair = getFilteredPrice(filter.getPrices());
		Boolean isNeedToFilterPrice = filterPricesPair.first();
		List<CheckedFilter<PriceFilter>> filterPrices = filterPricesPair.second();

		Specification<Item> spec = Specification.where(null);
		if (filter.getCategoryId() != null) {
			spec = spec.and(ItemSpecs.hasCategoryId(filter.getCategoryId()));
		}
		if (filter.getName() != null) {
			spec = spec.and(ItemSpecs.hasName(filter.getName()));
		}
		if (filter.getSeasons() != null && isNeedToFilterSeason) {
			spec = spec.and(ItemSpecs.hasSeason(filterSeasons));
		}
		if (filter.getSizes() != null && isNeedToFilterSize) {
			spec = spec.and(ItemSpecs.hasSize(filterSizes));
		}
		if (filter.getGenders() != null && isNeedToFilterGender) {
			spec = spec.and(ItemSpecs.hasGender(filterGenders));
		}
		if (filter.getColors() != null && isNeedToFilterColor) {
			spec = spec.and(ItemSpecs.hasColor(filterColors));
		}
		if (filter.getPrices() != null && isNeedToFilterPrice) {
			Integer minValue = null, maxValue = null;
			for (CheckedFilter<PriceFilter> price : filterPrices) {
				if (!price.isChecked()) {
					continue;
				}
				switch (price.getData()) {
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
				break;
			}
			spec = spec.and(ItemSpecs.hasPrice(minValue, maxValue));
		}

		filter.setSizes(filterSizes);
		filter.setColors(filterColors);
		filter.setGenders(filterGenders);
		filter.setSeasons(filterSeasons);
		filter.setPrices(filterPrices);

		return spec;
	}

	private Pair<Boolean, List<CheckedFilter<ItemSizeDTO>>> getFilteredSizes(List<Item> items,
																			 List<CheckedFilter<ItemSizeDTO>> filter) {
		List<ItemSizeDTO> itemSizes = items.stream()
										   .flatMap(item -> itemQuantityRepository.findAllByItemId(item.getId())
																				  .stream())
										   .map(this::mapToSizeDTO)
										   .distinct()
										   .toList();

		Set<String> definedSizes = filter.stream()
										 .filter(CheckedFilter::isChecked)
										 .map(size -> size.getData().getName())
										 .collect(Collectors.toSet());

		return new Pair<>(!definedSizes.isEmpty(), itemSizes.stream()
															.map(itemSize -> CheckedFilter.<ItemSizeDTO>builder()
																						  .data(itemSize)
																						  .checked(definedSizes.contains(
																								  itemSize.getName()))
																						  .build())
															.toList());
	}

	private Pair<Boolean, List<CheckedFilter<ItemColorDTO>>> getFilteredColors(List<CheckedFilter<ItemColorDTO>> filter) {
		List<ItemColorDTO> colors = new ArrayList<>(List.of(Color.values()))
				.stream()
				.map(color -> ItemColorDTO.builder()
										  .name(color.name())
										  .hex(color.getHexValue())
										  .build())
				.toList();

		Set<String> definedColors = filter.stream()
										  .filter(CheckedFilter::isChecked)
										  .map(color -> color.getData().getName())
										  .collect(Collectors.toSet());

		return new Pair<>(!definedColors.isEmpty(), colors.stream()
														  .map(color -> CheckedFilter.<ItemColorDTO>builder()
																					 .data(color)
																					 .checked(definedColors.contains(
																							 color.getName()))
																					 .build())
														  .toList());
	}

	private Pair<Boolean, List<CheckedFilter<Season>>> getFilteredSeason(List<CheckedFilter<Season>> filter) {
		List<Season> seasons = new ArrayList<>(List.of(Season.values()));

		Set<String> definedSeasons = filter.stream()
										   .filter(CheckedFilter::isChecked)
										   .map(season -> season.getData().name())
										   .collect(Collectors.toSet());

		return new Pair<>(!definedSeasons.isEmpty(), seasons.stream()
															.map(season -> CheckedFilter.<Season>builder()
																						.data(season)
																						.checked(definedSeasons.contains(
																								season.name()))
																						.build())
															.toList());
	}

	private Pair<Boolean, List<CheckedFilter<Gender>>> getFilteredGender(List<CheckedFilter<Gender>> filter) {
		List<Gender> genders = new ArrayList<>(List.of(Gender.values()));

		Set<String> definedGenders = filter.stream()
										   .filter(CheckedFilter::isChecked)
										   .map(gender -> gender.getData().name())
										   .collect(Collectors.toSet());

		return new Pair<>(!definedGenders.isEmpty(), genders.stream()
															.map(gender -> CheckedFilter.<Gender>builder()
																						.data(gender)
																						.checked(definedGenders.contains(
																								gender.name()))
																						.build())
															.toList());
	}

	private Pair<Boolean, List<CheckedFilter<PriceFilter>>> getFilteredPrice(List<CheckedFilter<PriceFilter>> filter) {
		List<PriceFilter> prices = new ArrayList<>(List.of(PriceFilter.values()));

		Set<String> definedPrices = filter.stream()
										  .filter(CheckedFilter::isChecked)
										  .map(price -> price.getData().name())
										  .collect(Collectors.toSet());

		if (definedPrices.size() > 2) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Item.CAN_NOT_FILTER_2_TYPE_PRICE_FILTER);
		}
		if (definedPrices.isEmpty()) {
			definedPrices.add(PriceFilter.ALL.name());
		}

		return new Pair<>(true, prices.stream()
									  .map(price -> CheckedFilter.<PriceFilter>builder()
																 .data(price)
																 .checked(definedPrices.contains(
																		 price.name()))
																 .build())
									  .toList());
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
	public ItemResponse getItem(Long itemId) {
		Item item = Common.findItemById(itemId, itemRepository);

		return mapToDTO(item);
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
		for (ItemQuantityRequest quantity : request.getQuantities()) {
			ItemQuantity quantityEntity = mapToEntity(quantity);
			quantityEntity.setItem(item);

			quantities.add(quantityEntity);
		}
		itemQuantityRepository.saveAll(quantities);

		int totalQuantity = this.calcTotalQuantityFromItemId(item.getId());

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

		return new SimpleResponse();
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
