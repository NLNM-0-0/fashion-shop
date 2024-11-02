package com.fashion.backend.service;

import com.fashion.backend.constant.Message;
import com.fashion.backend.entity.Cart;
import com.fashion.backend.entity.Item;
import com.fashion.backend.entity.User;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.payload.SimpleListResponse;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.cart.AddToCartRequest;
import com.fashion.backend.payload.cart.CartDetailResponse;
import com.fashion.backend.payload.cart.ChangeQuantityRequest;
import com.fashion.backend.payload.item.SimpleItemResponse;
import com.fashion.backend.payload.notification.NumberNotificationNotSeenResponse;
import com.fashion.backend.repository.CartRepository;
import com.fashion.backend.repository.ItemRepository;
import com.fashion.backend.repository.UserAuthRepository;
import com.fashion.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
	private final UserAuthRepository userAuthRepository;
	private final UserRepository userRepository;
	private final CartRepository cartRepository;
	private final ItemRepository itemRepository;

	@Transactional
	public SimpleListResponse<CartDetailResponse> getCart() {
		User user = Common.findCurrUser(userRepository, userAuthRepository);

		List<Cart> cartDetails = cartRepository.findAllByUserId(
				user.getId(),
				Sort.by(Sort.Direction.DESC, "updatedAt"));

		List<CartDetailResponse> data = cartDetails.stream().map(this::mapToDTO).toList();

		return SimpleListResponse.<CartDetailResponse>builder()
								 .data(data)
								 .build();
	}

	@Transactional
	public NumberNotificationNotSeenResponse getNumberCartItems() {
		User user = Common.findCurrUser(userRepository, userAuthRepository);

		return NumberNotificationNotSeenResponse.builder()
												.number(cartRepository.countByUserId(
														user.getId()))
												.build();
	}

	@Transactional
	public SimpleResponse deleteCartItem(Long itemId) {
		User user = Common.findCurrUser(userRepository, userAuthRepository);

		Cart cart = cartRepository.findFirstByUserIdAndItemId(user.getId(), itemId)
								  .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
																	  Message.Cart.ITEM_NOT_IN_CART));

		cartRepository.delete(cart);

		return new SimpleResponse();
	}

	@Transactional
	public SimpleResponse addCartItem(AddToCartRequest request) {
		User user = Common.findCurrUser(userRepository, userAuthRepository);

		Item item = Common.findItemById(request.getItemId(), itemRepository);

		Optional<Cart> cartItemOptional = cartRepository.findFirstByUserIdAndItemId(user.getId(), item.getId());

		Cart cartItem;
		if (cartItemOptional.isEmpty()) {
			cartItem = Cart.builder()
						   .item(item)
						   .user(user)
						   .quantity(request.getQuantity())
						   .build();
		} else {
			cartItem = cartItemOptional.get();
			cartItem.setQuantity(request.getQuantity() + cartItem.getQuantity());
		}

		cartRepository.save(cartItem);

		return new SimpleResponse();
	}

	@Transactional
	public SimpleResponse changeQuantityCartItem(Long itemId, ChangeQuantityRequest request) {
		if (request.getQuantityChange() == 0) {
			return new SimpleResponse();
		}

		User user = Common.findCurrUser(userRepository, userAuthRepository);

		Cart cart = cartRepository.findFirstByUserIdAndItemId(user.getId(), itemId)
								  .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
																	  Message.Cart.ITEM_NOT_IN_CART));
		if (cart.getItem().isDeleted()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Item.ITEM_IS_DELETED);
		}

		cart.setQuantity(cart.getQuantity() + request.getQuantityChange());
		if (cart.getQuantity() < 0) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Cart.QUANTITY_MIN_VALIDATE);
		} else if (cart.getQuantity() == 0) {
			cartRepository.delete(cart);
			return new SimpleResponse();
		}

		cartRepository.save(cart);

		return new SimpleResponse();
	}

	private CartDetailResponse mapToDTO(Cart cart) {
		return CartDetailResponse.builder()
								 .id(cart.getId())
								 .updatedAt(cart.getUpdatedAt())
								 .quantity(cart.getQuantity())
								 .item(mapToDTO(cart.getItem()))
								 .build();
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
