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

	public static User findUserById(Long userId, UserRepository repository) {
		return repository.findById(userId)
						 .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
															 Message.User.USER_NOT_EXIST));
	}

	private static UserAuth findUserAuth(String userName, boolean isByEmail, UserAuthRepository repository) {
		UserAuth userAuth;
		if (isByEmail) {
			userAuth = repository.findByEmail(userName)
								 .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
																	 Message.User.USER_NOT_EXIST));
		} else {
			userAuth = repository.findByPhone(userName)
								 .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
																	 Message.User.USER_NOT_EXIST));
		}
		return userAuth;
	}

	public static UserAuth findAvailableUserAuth(String userName, boolean isByEmail, UserAuthRepository repository) {
		UserAuth userAuth = findUserAuth(userName, isByEmail, repository);
		if (userAuth.isDeleted()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.USER_IS_DELETED);
		}
		if (!isByEmail && !userAuth.isVerified()) {
			throw new AppException(HttpStatus.FORBIDDEN, Message.User.USER_IS_NOT_VERIFIED);
		}
		return userAuth;
	}

	public static UserAuth findUserAuthByEmail(String userName, UserAuthRepository repository) {
		UserAuth userAuth = findUserAuth(userName, true, repository);
		if (userAuth.isDeleted()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.USER_IS_DELETED);
		}
		return userAuth;
	}

	public static UserAuth findUserAuthByPhone(String userName, UserAuthRepository repository) {
		UserAuth userAuth = findUserAuth(userName, false, repository);
		if (userAuth.isDeleted()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.USER_IS_DELETED);
		}
		return userAuth;
	}

	public static UserAuth findCurrUserAuth(UserAuthRepository userAuthRepository) {
		String userName = Common.getCurrUserName();

		if (AuthHelper.isNormalUser(userName)) {
			return Common.findUserAuthByPhone(userName, userAuthRepository);
		}
		return Common.findUserAuthByEmail(userName, userAuthRepository);
	}

	public static User findUserByUserAuth(Long userAuthId, UserRepository userRepository) {
		return userRepository.findFirstByUserAuthId(userAuthId)
							 .orElseThrow(() -> new AppException(HttpStatus.INTERNAL_SERVER_ERROR,
																 Message.COMMON_ERR));
	}

	public static User findCurrUser(UserRepository userRepository, UserAuthRepository userAuthRepository) {
		UserAuth userAuth = Common.findCurrUserAuth(userAuthRepository);

		return findUserByUserAuth(userAuth.getId(), userRepository);
	}

	public static Notification findNotificationById(Long notificationId,
													NotificationRepository repository) {
		Notification notification = repository.findById(notificationId)
											  .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
																				  Message.Notification.NOTIFICATION_NOT_EXIST));

		String currUserName = Common.getCurrUserName();

		String toUserUserName = notification.getToUser().getUserAuth().getEmail();
		if (toUserUserName == null) {
		    toUserUserName = notification.getToUser().getUserAuth().getPhone();
		}

		if (!toUserUserName.equals(currUserName)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Notification.CAN_NOT_READ_OTHERS_NOTIFICATION);
		}

		return notification;
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

	public static Item findItemById(Long itemId, ItemRepository itemRepository) {
		Item item = itemRepository.findById(itemId)
								  .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
																	  Message.Item.ITEM_NOT_EXIST));

		if (item.isDeleted()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Item.ITEM_IS_DELETED);
		}
		return item;
	}

	public static Category findCategoryById(Long categoryId, CategoryRepository categoryRepository) {
		return categoryRepository.findById(categoryId)
								 .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
																	 Message.Category.CATEGORY_NOT_EXIST));
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
												ItemQuantityRepository repository) {
		return repository.findFirstByItemIdAndColorAndAndSize(itemId, color, size)
						 .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
															 Message.ItemQuantity.ITEM_QUANTITY_NOT_EXIST));
	}

	public static Cart findCartById(Long cartId, CartRepository cartRepository) {
		return cartRepository.findById(cartId)
							 .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
																 Message.Cart.ITEM_NOT_IN_CART));
	}

	public static Optional<Long> getUserLoginId(UserRepository userRepository, UserAuthRepository userAuthRepository) {
		try {
			User user = Common.findCurrUser(userRepository, userAuthRepository);
			return user.getId().describeConstable();
		} catch (Exception e) {
			return Optional.empty();
		}
	}
}
