package com.fashion.backend.service;

import com.fashion.backend.constant.Color;
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
import com.fashion.backend.payload.cart.UpdateCartRequest;
import com.fashion.backend.payload.item.SimpleItemResponse;
import com.fashion.backend.payload.notification.NumberNotificationNotSeenResponse;
import com.fashion.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
				Sort.by(Sort.Direction.DESC, "createdAt"));

		List<CartDetailResponse> responses = new ArrayList<>();
		for (Cart cartDetail : cartDetails) {
			CartDetailResponse response;

			Optional<ItemQuantity> itemQuantity
					= itemQuantityRepository.findFirstByItemIdAndColorAndAndSize(cartDetail.getItem().getId(),
																				 cartDetail.getColor(),
																				 cartDetail.getSize());

			if (itemQuantity.isEmpty()) {
				response = mapToDTO(cartDetail, 0, false);
			} else {
				response = mapToDTO(cartDetail, itemQuantity.get().getQuantity(), true);
			}

			responses.add(response);
		}

		return SimpleListResponse.<CartDetailResponse>builder()
								 .data(responses)
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

	private Optional<Cart> findOptionalCart(Long userId, Long itemId, String size, Color color) {
		return cartRepository.findFirstByUserIdAndItemIdAndSizeAndColor(userId,
																		itemId,
																		size,
																		color);
	}

	@Transactional
	public SimpleResponse addCartItem(AddToCartRequest request) {
		User user = Common.findCurrUser(userRepository, userAuthRepository);

		Item item = Common.findItemById(request.getItemId(), itemRepository);
		ItemQuantity itemQuantity = Common.findItemQuantity(item.getId(),
															request.getSize(),
															request.getColor(),
															itemQuantityRepository);

		Optional<Cart> cartItemOptional = findOptionalCart(user.getId(),
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

		if (currentQuantityInCart > itemQuantity.getQuantity()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Cart.CAN_NOT_ADD_OVER_CURRENT_QUANTITY);
		}

		cartRepository.save(cartItem);

		return new SimpleResponse();
	}

	@Transactional
	public SimpleResponse updateCartItem(Long cartId, UpdateCartRequest request) {
		Cart cart = Common.findCartById(cartId, cartRepository);
		User user = Common.findCurrUser(userRepository, userAuthRepository);

		ItemQuantity itemQuantity = Common.findItemQuantity(cart.getItem().getId(),
															request.getSize(),
															request.getColor(),
															itemQuantityRepository);

		if (request.getQuantity() > itemQuantity.getQuantity()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Cart.CAN_NOT_ADD_OVER_CURRENT_QUANTITY);
		}

		Optional<Cart> requestedCartItemOptional = this.findOptionalCart(user.getId(),
																		 cart.getItem().getId(),
																		 request.getSize(),
																		 request.getColor());

		if (requestedCartItemOptional.isEmpty()) {
			cart.setSize(request.getSize());
			cart.setColor(request.getColor());
			cart.setQuantity(request.getQuantity());
			cartRepository.save(cart);
		} else {
			Cart requestedCartItem = requestedCartItemOptional.get();

			requestedCartItem.setQuantity(request.getQuantity() + requestedCartItem.getQuantity());

			cartRepository.save(requestedCartItem);

			if (!Objects.equals(requestedCartItem.getId(), cartId)) {
				cartRepository.delete(cart);
			}
		}

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

		ItemQuantity itemQuantity = Common.findItemQuantity(cart.getItem().getId(),
															cart.getSize(),
															cart.getColor(),
															itemQuantityRepository);

		cart.setQuantity(cart.getQuantity() + request.getQuantityChange());
		if (cart.getQuantity() < 0) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Cart.QUANTITY_MIN_VALIDATE);
		} else if (cart.getQuantity() > itemQuantity.getQuantity()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Cart.CAN_NOT_ADD_OVER_CURRENT_QUANTITY);
		} else if (cart.getQuantity() == 0) {
			cartRepository.delete(cart);
			return new SimpleResponse();
		}

		cartRepository.save(cart);

		return new SimpleResponse();
	}

	private CartDetailResponse mapToDTO(Cart cart, int itemQuantity, boolean isExist) {
		return CartDetailResponse.builder()
								 .id(cart.getId())
								 .createdAt(cart.getCreatedAt())
								 .quantity(cart.getQuantity())
								 .item(mapToDTO(cart.getItem()))
								 .size(cart.getSize())
								 .color(cart.getColor())
								 .itemQuantity(itemQuantity)
								 .isExist(isExist)
								 .build();
	}

	private SimpleItemResponse mapToDTO(Item item) {
		return SimpleItemResponse.builder()
								 .id(item.getId())
								 .name(item.getName())
								 .unitPrice(item.getUnitPrice())
								 .images(item.getImages())
								 .isDeleted(item.isDeleted())
								 .build();
	}
}
