package com.fashion.backend.service;

import com.fashion.backend.constant.Message;
import com.fashion.backend.entity.Item;
import com.fashion.backend.entity.Like;
import com.fashion.backend.entity.User;
import com.fashion.backend.entity.UserAuth;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.payload.SimpleListResponse;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.item.SimpleItemResponse;
import com.fashion.backend.repository.ItemRepository;
import com.fashion.backend.repository.LikeRepository;
import com.fashion.backend.repository.UserAuthRepository;
import com.fashion.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {
	private final UserAuthRepository userAuthRepository;
	private final UserRepository userRepository;
	private final ItemRepository itemRepository;
	private final LikeRepository likeRepository;

	@Transactional
	public SimpleListResponse<SimpleItemResponse> getLikedItems() {
		UserAuth userAuth = Common.findCurrUserAuth(userAuthRepository);

		List<Like> likes = likeRepository.findAllByUserId(
				userAuth.getId(),
				Sort.by(Sort.Direction.DESC, "createdAt"));

		List<SimpleItemResponse> data = likes.stream().map(like -> mapToDTO(like.getItem())).toList();

		return SimpleListResponse.<SimpleItemResponse>builder()
								 .data(data)
								 .build();
	}

	@Transactional
	public SimpleResponse unlikeItem(Long itemId) {
		UserAuth userAuth = Common.findCurrUserAuth(userAuthRepository);

		Like like = likeRepository.findFirstByUserIdAndItemId(userAuth.getId(), itemId)
								  .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
																	  Message.Like.ITEM_NOT_IN_LIKED_LIST));

		likeRepository.delete(like);

		return new SimpleResponse();
	}

	@Transactional
	public SimpleResponse likeItem(Long itemId) {
		UserAuth userAuth = Common.findCurrUserAuth(userAuthRepository);
		User user = Common.findUserById(userAuth.getId(), userRepository);

		Item item = Common.findItemById(itemId, itemRepository);

		Like like = Like.builder()
						.item(item)
						.user(user)
						.build();

		likeRepository.save(like);

		return new SimpleResponse();
	}

	private SimpleItemResponse mapToDTO(Item item) {
		return SimpleItemResponse.builder()
								 .id(item.getId())
								 .name(item.getName())
								 .image(item.getImage())
								 .isDeleted(item.isDeleted())
								 .build();
	}
}
