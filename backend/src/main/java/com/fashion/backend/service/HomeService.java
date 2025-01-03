package com.fashion.backend.service;

import com.fashion.backend.entity.Category;
import com.fashion.backend.entity.Item;
import com.fashion.backend.entity.Like;
import com.fashion.backend.payload.SimpleListResponse;
import com.fashion.backend.payload.category.CategoryResponse;
import com.fashion.backend.payload.item.SimpleItemDetail;
import com.fashion.backend.repository.ItemRepository;
import com.fashion.backend.repository.LikeRepository;
import com.fashion.backend.repository.UserAuthRepository;
import com.fashion.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HomeService {
	private static final int TOP_BEST_SELLER = 5;
	private static final int TOP_LATEST = 5;

	private final ItemRepository itemRepository;
	private final UserRepository userRepository;
	private final UserAuthRepository userAuthRepository;
	private final LikeRepository likeRepository;

	public SimpleListResponse<SimpleItemDetail> getTopSellers() {
		return getTopSellers(TOP_BEST_SELLER);
	}

	public SimpleListResponse<SimpleItemDetail> getTopSellers(int number) {
		PageRequest pageRequest = PageRequest.of(0, number);
		List<Item> bestSellerItems = itemRepository.findTopBySold(pageRequest);

		return mapItemResponse(bestSellerItems);
	}

	public SimpleListResponse<SimpleItemDetail> getLatest() {
		return getLatest(TOP_LATEST);
	}

	public SimpleListResponse<SimpleItemDetail> getLatest(int number) {
		PageRequest pageRequest = PageRequest.of(0, number);
		List<Item> bestSellerItems = itemRepository.findLatestItems(pageRequest);

		return mapItemResponse(bestSellerItems);
	}

	private SimpleListResponse<SimpleItemDetail> mapItemResponse(List<Item> items) {
		Optional<Long> userId = Common.getUserLoginId(userRepository, userAuthRepository);
		return new SimpleListResponse<>(
				userId.map(aLong -> items.stream()
										 .map(item -> {
											 Optional<Like> like = likeRepository.findFirstByUserIdAndItemId(aLong,
																											 item.getId());
											 return mapToDTO(item, like.isPresent());
										 })
										 .toList()).orElseGet(() -> items.stream()
																		 .map(item -> mapToDTO(item, false))
																		 .toList())
		);
	}

	private SimpleItemDetail mapToDTO(Item item, boolean liked) {
		return SimpleItemDetail.builder()
							   .id(item.getId())
							   .name(item.getName())
							   .unitPrice(item.getUnitPrice())
							   .images(item.getImages())
							   .categories(item.getCategories().stream().map(this::mapToDTO).toList())
							   .gender(item.getGender())
							   .isDeleted(item.isDeleted())
							   .liked(liked)
							   .build();
	}

	private CategoryResponse mapToDTO(Category category) {
		return CategoryResponse.builder()
							   .id(category.getId())
							   .name(category.getName())
							   .build();
	}
}
