package com.fashion.backend.service;

import com.fashion.backend.constant.Color;
import com.fashion.backend.constant.Message;
import com.fashion.backend.entity.*;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.mail.MailSender;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.repository.*;
import com.fashion.backend.utils.AuthHelper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Common {
	public static <T> void updateIfNotNull(T newValue, Consumer<T> setter) {
		if (newValue != null) {
			setter.accept(newValue);
		}
	}

	public static String getCurrUserName() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userDetails.getUsername();
	}

	public static User findUserById(Long userId, UserRepository userRepository) {
		User user = userRepository.findById(userId)
						 .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
															 Message.User.USER_NOT_EXIST));
		return user;
	}

	private static UserAuth findUserAuthByUserName(String userName, UserAuthRepository userAuthRepository) {
		UserAuth userAuth;
		if (AuthHelper.isNormalUser(userName)) {
			userAuth = userAuthRepository.findByPhone(userName)
									 .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
																		 Message.User.USER_NOT_EXIST));
		} else {
			userAuth = userAuthRepository.findByEmail(userName)
										 .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
																			 Message.User.USER_NOT_EXIST));
		}
		return userAuth;
	}

	public static UserAuth findAvailableUserAuth(String userName, UserAuthRepository repository) {
		UserAuth userAuth = findUserAuthByUserName(userName, repository);

		verifyUserAuth(userAuth);

		return userAuth;
	}

	private static void verifyUserAuth(UserAuth userAuth) {
		if (userAuth.isDeleted()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.USER_IS_DELETED);
		}
		if (AuthHelper.isNormalUser(userAuth.getUsername()) && !userAuth.isVerified()) {
			throw new AppException(HttpStatus.FORBIDDEN, Message.User.USER_IS_NOT_VERIFIED);
		}
	}

	public static UserAuth findActiveUserAuthByUserName(String userName, UserAuthRepository repository) {
		UserAuth userAuth = findUserAuthByUserName(userName, repository);
		if (userAuth.isDeleted()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.USER_IS_DELETED);
		}
		return userAuth;
	}

	public static User findActiveUserByUserName(String userName, UserAuthRepository userAuthRepository, UserRepository userRepository) {
		UserAuth userAuth = Common.findActiveUserAuthByUserName(userName, userAuthRepository);
		User user = Common.findUserByUserAuthId(userAuth.getId(), userRepository);
		return user;
	}

	public static UserAuth findCurrentUserAuth(UserAuthRepository userAuthRepository) {
		String userName = Common.getCurrUserName();

		UserAuth userAuth = Common.findActiveUserAuthByUserName(userName, userAuthRepository);

		return userAuth;
	}

	public static User findUserByUserAuthId(Long userAuthId, UserRepository userRepository) {
		User user = userRepository.findFirstByUserAuthId(userAuthId)
							 .orElseThrow(() -> new AppException(HttpStatus.INTERNAL_SERVER_ERROR,
																 Message.COMMON_ERR));
		return user;
	}

	public static User findCurrentUser(UserRepository userRepository, UserAuthRepository userAuthRepository) {
		UserAuth userAuth = Common.findCurrentUserAuth(userAuthRepository);

		User user = findUserByUserAuthId(userAuth.getId(), userRepository);

		return user;
	}

	public static SimpleResponse sendNotification(NotificationRepository notificationRepository,
												  MailSender mailSender,
												  List<User> receivers,
												  User sender,
												  String title,
												  String description) {
		List<Notification> notifications = receivers.stream()
													.map(receiver -> Notification.builder()
																				 .title(title)
																				 .description(description)
																				 .fromUser(sender)
																				 .toUser(receiver)
																				 .seen(false)
																				 .build())
													.toList();

		notificationRepository.saveAll(notifications);

		mailSender.sendEmail(title,
							 description,
							 receivers.stream().map(User::getEmail).toList());

		return new SimpleResponse();
	}

	public static void sendNotification(NotificationRepository notificationRepository,
										MailSender mailSender,
										User receiver,
										User sender,
										String title,
										String description) {

		Notification notification = Notification.builder()
												.title(title)
												.description(description)
												.fromUser(sender)
												.toUser(receiver)
												.seen(false)
												.build();

		notificationRepository.save(notification);

		mailSender.sendEmail(title,
							 description,
							 List.of(receiver.getEmail()));

	}

	public static Item findActiveItemById(Long itemId, ItemRepository itemRepository) {
		Item item = itemRepository.findById(itemId)
								  .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
																	  Message.Item.ITEM_NOT_EXIST));

		if (item.isDeleted()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Item.ITEM_IS_DELETED);
		}
		return item;
	}

	public static Category findCategoryById(Long categoryId, CategoryRepository categoryRepository) {
		Category category = categoryRepository.findById(categoryId)
								 .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
																	 Message.Category.CATEGORY_NOT_EXIST));
		return category;
	}

	public static List<Category> findCategoryByIds(List<Long> categoryIds, CategoryRepository categoryRepository) {
		List<Category> categories = categoryRepository.findAllById(categoryIds);
		if (categories.isEmpty() || categories.size() != categoryIds.size()) {
			throw new AppException(HttpStatus.BAD_REQUEST,
								   Message.Category.CATEGORY_NOT_EXIST);
		}
		return categories;
	}

	public static Order findOrderById(Long orderId, OrderRepository orderRepository) {
		return orderRepository.findById(orderId)
							  .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
																  Message.Order.ORDER_NOT_EXIST));
	}

	public static ItemQuantity findItemQuantity(Long itemId,
												String size,
												Color color,
												ItemQuantityRepository itemQuantityRepository) {

		ItemQuantity itemQuantity = itemQuantityRepository.findFirstByItemIdAndColorAndSize(itemId, color, size)
						 .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
															 Message.ItemQuantity.ITEM_QUANTITY_NOT_EXIST));

		return itemQuantity;
	}

	public static Cart findCartById(Long cartId, CartRepository cartRepository) {
		Cart cart = cartRepository.findById(cartId)
							 .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
																 Message.Cart.ITEM_NOT_IN_CART));
		return cart;
	}

	public static Optional<Long> getUserLoginId(UserRepository userRepository, UserAuthRepository userAuthRepository) {
		try {
			User user = Common.findCurrentUser(userRepository, userAuthRepository);
			return user.getId().describeConstable();
		} catch (Exception e) {
			return Optional.empty();
		}
	}
}
