package com.fashion.backend.service;

import com.fashion.backend.constant.Color;
import com.fashion.backend.constant.Message;
import com.fashion.backend.entity.*;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.payload.SimpleListResponse;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.cart.AddToCartRequest;
import com.fashion.backend.payload.cart.CartDetailResponse;
import com.fashion.backend.payload.cart.ChangeQuantityRequest;
import com.fashion.backend.payload.cart.UpdateCartRequest;
import com.fashion.backend.payload.category.CategoryResponse;
import com.fashion.backend.payload.item.ItemColorDTO;
import com.fashion.backend.payload.item.ItemResponse;
import com.fashion.backend.payload.item.ItemSizeDTO;
import com.fashion.backend.payload.notification.NumberNotificationNotSeenResponse;
import com.fashion.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
		User user = Common.findCurrentUser(userRepository, userAuthRepository);

		List<Cart> carts = cartRepository.findAllByUserId(
				user.getId(),
				Sort.by(Sort.Direction.DESC, "createdAt"));

		List<CartDetailResponse> cartDTOs = new ArrayList<>();
		for (Cart cart : carts) {
			CartDetailResponse cartDTO;

			Optional<ItemQuantity> itemQuantity
					= itemQuantityRepository.findFirstByItemIdAndColorAndSize(cart.getItem().getId(),
																				 cart.getColor(),
																				 cart.getSize());

			if (itemQuantity.isEmpty()) {
				cartDTO = mapToDTO(cart, 0, false);
			} else {
				cartDTO = mapToDTO(cart, itemQuantity.get().getQuantity(), true);
			}

			cartDTOs.add(cartDTO);
		}

		return SimpleListResponse.<CartDetailResponse>builder()
								 .data(cartDTOs)
								 .build();
	}

	@Transactional
	public NumberNotificationNotSeenResponse getNumberCartItems() {
		User user = Common.findCurrentUser(userRepository, userAuthRepository);

		return NumberNotificationNotSeenResponse.builder()
												.number(cartRepository.getCartNumbers(
														user.getId()))
												.build();
	}

	@Transactional
	public SimpleResponse deleteCartItem(Long cartId) {
		Cart cart = Common.findCartById(cartId, cartRepository);

		cartRepository.delete(cart);

		return new SimpleResponse();
	}

	@Transactional
	public SimpleResponse addCartItem(AddToCartRequest request) {

		Item item = Common.findActiveItemById(request.getItemId(), itemRepository);
		ItemQuantity itemQuantity = Common.findItemQuantity(item.getId(),
															request.getSize(),
															request.getColor(),
															itemQuantityRepository);

		User user = Common.findCurrentUser(userRepository, userAuthRepository);
		Optional<Cart> checkedCartItem = cartRepository.findFirstByUserIdAndItemIdAndSizeAndColor(user.getId(),
														   item.getId(),
														   request.getSize(),
														   request.getColor());

		Cart cartItem;
		if (checkedCartItem.isEmpty()) {
			cartItem = Cart.builder()
						   .item(item)
						   .user(user)
						   .size(request.getSize())
						   .color(request.getColor())
						   .quantity(request.getQuantity())
						   .build();
		} else {
			cartItem = checkedCartItem.get();
			cartItem.setQuantity(request.getQuantity() + cartItem.getQuantity());
		}

		if (cartItem.getQuantity() > itemQuantity.getQuantity()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Cart.CAN_NOT_ADD_OVER_CURRENT_QUANTITY);
		}

		cartRepository.save(cartItem);

		return new SimpleResponse();
	}

	@Transactional
	public SimpleResponse updateCartItem(Long cartId, UpdateCartRequest request) {
		Cart oldCart = Common.findCartById(cartId, cartRepository);
		User user = Common.findCurrentUser(userRepository, userAuthRepository);

		ItemQuantity itemQuantity = Common.findItemQuantity(oldCart.getItem().getId(),
															request.getSize(),
															request.getColor(),
															itemQuantityRepository);

		if (request.getQuantity() > itemQuantity.getQuantity()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Cart.CAN_NOT_ADD_OVER_CURRENT_QUANTITY);
		}

		Optional<Cart> checkedCartItem = cartRepository.findFirstByUserIdAndItemIdAndSizeAndColor(user.getId(),
																		 oldCart.getItem().getId(),
																		 request.getSize(),
																		 request.getColor());

		if (checkedCartItem.isEmpty()) {
			oldCart.setSize(request.getSize());
			oldCart.setColor(request.getColor());
			oldCart.setQuantity(request.getQuantity());
			cartRepository.save(oldCart);
		} else {
			Cart cart = checkedCartItem.get();

			cart.setQuantity(request.getQuantity() + cart.getQuantity());

			cartRepository.save(cart);

			if (!Objects.equals(cart.getId(), cartId)) {
				cartRepository.delete(oldCart);
			}
		}

		return new SimpleResponse();
	}

	@Transactional
	public SimpleResponse changeQuantityCartItem(Long cartId, ChangeQuantityRequest request) {
		if (request.getQuantityChange() == 0) {
			return new SimpleResponse();
		}

		Cart cart = Common.findCartById(cartId, cartRepository);


		if (cart.getItem().isDeleted()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Item.ITEM_IS_DELETED);
		}

		ItemQuantity itemQuantity = Common.findItemQuantity(cart.getItem().getId(),
															cart.getSize(),
															cart.getColor(),
															itemQuantityRepository);

		int currQuantity = cart.getQuantity() + request.getQuantityChange();
		if (currQuantity < 0) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Cart.QUANTITY_MIN_VALIDATE);
		} else if (currQuantity > itemQuantity.getQuantity()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Cart.CAN_NOT_ADD_OVER_CURRENT_QUANTITY);
		} else if (currQuantity == 0) {
			cartRepository.delete(cart);
			return new SimpleResponse();
		}

		cart.setQuantity(currQuantity);
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
