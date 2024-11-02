package com.fashion.backend.service;

import com.fashion.backend.constant.Message;
import com.fashion.backend.entity.Cart;
import com.fashion.backend.entity.Item;
import com.fashion.backend.entity.User;
import com.fashion.backend.entity.UserAuth;
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

@Service
@RequiredArgsConstructor
public class CartService {
	private final UserAuthRepository userAuthRepository;
	private final UserRepository userRepository;
	private final CartRepository cartRepository;
	private final ItemRepository itemRepository;

	@Transactional
	public SimpleListResponse<CartDetailResponse> getCart() {
		UserAuth userAuth = Common.findCurrUserAuth(userAuthRepository);

		List<Cart> cartDetails = cartRepository.findAllByUserId(
				userAuth.getId(),
				Sort.by(Sort.Direction.DESC, "createdAt"));

		List<CartDetailResponse> data = cartDetails.stream().map(this::mapToDTO).toList();

		return SimpleListResponse.<CartDetailResponse>builder()
								 .data(data)
								 .build();
	}

	@Transactional
	public NumberNotificationNotSeenResponse getNumberCartItems() {
		UserAuth currUserAuth = Common.findCurrUserAuth(userAuthRepository);

		return NumberNotificationNotSeenResponse.builder()
												.number(cartRepository.countByUserId(
														currUserAuth.getId()))
												.build();
	}

	@Transactional
	public SimpleResponse deleteCartItem(Long itemId) {
		UserAuth userAuth = Common.findCurrUserAuth(userAuthRepository);

		Cart cart = cartRepository.findFirstByUserIdAndItemId(userAuth.getId(), itemId)
								  .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
																	  Message.Cart.ITEM_NOT_IN_CART));

		cartRepository.delete(cart);

		return new SimpleResponse();
	}

	@Transactional
	public SimpleResponse addCartItem(AddToCartRequest request) {
		UserAuth userAuth = Common.findCurrUserAuth(userAuthRepository);
		User user = Common.findUserById(userAuth.getId(), userRepository);

		Item item = Common.findItemById(request.getItemId(), itemRepository);

		Cart cartItem = Cart.builder()
							.item(item)
							.user(user)
							.quantity(request.getQuantity())
							.build();

		cartRepository.save(cartItem);

		return new SimpleResponse();
	}

	@Transactional
	public SimpleResponse changeQuantityCartItem(Long itemId, ChangeQuantityRequest request) {
		if (request.getQuantityChange() == 0) {
			return new SimpleResponse();
		}

		UserAuth userAuth = Common.findCurrUserAuth(userAuthRepository);

		Cart cart = cartRepository.findFirstByUserIdAndItemId(userAuth.getId(), itemId)
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
		}

		cartRepository.save(cart);

		return new SimpleResponse();
	}

	private CartDetailResponse mapToDTO(Cart cart) {
		return CartDetailResponse.builder()
								 .id(cart.getId())
								 .createdAt(cart.getCreatedAt())
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
