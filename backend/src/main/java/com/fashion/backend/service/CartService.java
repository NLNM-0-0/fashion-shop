package com.fashion.backend.service;

import com.fashion.backend.constant.Message;
import com.fashion.backend.entity.Cart;
import com.fashion.backend.entity.Item;
import com.fashion.backend.entity.ItemQuantity;
import com.fashion.backend.entity.User;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.payload.SimpleListResponse;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.cart.AddToCartRequest;
import com.fashion.backend.payload.cart.CartDetailResponse;
import com.fashion.backend.payload.cart.ChangeQuantityRequest;
import com.fashion.backend.payload.item.SimpleItemResponse;
import com.fashion.backend.payload.notification.NumberNotificationNotSeenResponse;
import com.fashion.backend.repository.*;
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
	private final ItemQuantityRepository itemQuantityRepository;

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
	public SimpleResponse deleteCartItem(Long cartId) {
		Cart cart = cartRepository.findById(cartId)
								  .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
																	  Message.Cart.ITEM_NOT_IN_CART));

		cartRepository.delete(cart);

		return new SimpleResponse();
	}

	@Transactional
	public SimpleResponse addCartItem(AddToCartRequest request) {
		User user = Common.findCurrUser(userRepository, userAuthRepository);

		Item item = Common.findItemById(request.getItemId(), itemRepository);
		ItemQuantity itemQuantity = Common.findItemQuantity(item.getId(),
															request.getSize(),
															request.getColor(),
															itemQuantityRepository);

		Optional<Cart> cartItemOptional = cartRepository.findFirstByUserIdAndItemIdAndSizeAndColor(user.getId(),
																								   item.getId(),
																								   request.getSize(),
																								   request.getColor());

		Cart cartItem;
		int currentQuantityInCart = request.getQuantity();
		if (cartItemOptional.isEmpty()) {
			cartItem = Cart.builder()
						   .item(item)
						   .user(user)
						   .size(request.getSize())
						   .color(request.getColor())
						   .quantity(currentQuantityInCart)
						   .build();
		} else {
			cartItem = cartItemOptional.get();
			currentQuantityInCart += cartItem.getQuantity();
			cartItem.setQuantity(currentQuantityInCart);
		}

		if (itemQuantity.getQuantity() < currentQuantityInCart) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Cart.CAN_NOT_ADD_OVER_CURRENT_QUANTITY);
		}

		cartRepository.save(cartItem);

		return new SimpleResponse();
	}

	@Transactional
	public SimpleResponse changeQuantityCartItem(Long cartId, ChangeQuantityRequest request) {
		if (request.getQuantityChange() == 0) {
			return new SimpleResponse();
		}

		Cart cart = cartRepository.findById(cartId)
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
								 .size(cart.getSize())
								 .color(cart.getColor())
								 .build();
	}

	private SimpleItemResponse mapToDTO(Item item) {
		return SimpleItemResponse.builder()
								 .id(item.getId())
								 .name(item.getName())
								 .images(item.getImages())
								 .isDeleted(item.isDeleted())
								 .build();
	}
}
