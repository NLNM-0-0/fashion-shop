package com.fashion.backend.service;

import com.fashion.backend.constant.Message;
import com.fashion.backend.entity.*;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.mail.MailSender;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.feature.FeatureResponse;
import com.fashion.backend.repository.*;
import com.fashion.backend.utils.AuthHelper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Common {
	public static void updateIfNotNull(String newValue, Consumer<String> setter) {
		if (newValue != null) {
			setter.accept(newValue);
		}
	}

	public static void updateIfNotNull(Boolean newValue, Consumer<Boolean> setter) {
		if (newValue != null) {
			setter.accept(newValue);
		}
	}

	public static void updateIfNotNull(Long newValue, Consumer<Long> setter) {
		if (newValue != null) {
			setter.accept(newValue);
		}
	}

	public static void updateIfNotNull(Map<String, Object> newValue, Consumer<Map<String, Object>> setter) {
		if (newValue != null) {
			setter.accept(newValue);
		}
	}

	public static User findUserById(Long userId, UserRepository repository) {
		return repository.findById(userId)
						 .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
															 Message.User.USER_NOT_EXIST));
	}

//	public static User findUserByEmail(String email, UserRepository repository) {
//		User user = repository.findByEmail(email)
//							  .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
//																  Message.User.USER_NOT_EXIST));
//		if (user.isDeleted()) {
//			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.USER_IS_DELETED);
//		}
//
//		return user;
//	}

	public static UserAuth findUserAuthById(Long userId, UserAuthRepository repository) {
		UserAuth userAuth = repository.findById(userId)
									  .orElseThrow(() -> new AppException(
											  HttpStatus.BAD_REQUEST,
											  Message.User.USER_NOT_EXIST));
		if (userAuth.isDeleted()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.USER_IS_DELETED);
		}

		return userAuth;
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
			throw new AppException(HttpStatus.BAD_REQUEST, Message.User.USER_IS_NOT_VERIFIED);
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

	public static UserGroup findUserGroupById(Long userGroupId, UserGroupRepository repository) {
		return repository.findById(userGroupId)
						 .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
															 Message.UserGroup.USER_GROUP_NOT_EXIST));
	}

	public static UserAuth findCurrUserAuth(UserAuthRepository userAuthRepository) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName = userDetails.getUsername();

		if (AuthHelper.isNormalUser(userName)) {
			return Common.findUserAuthByPhone(userName, userAuthRepository);
		}
		return Common.findUserAuthByEmail(userName, userAuthRepository);
	}

	public static Notification findNotificationById(Long notificationId,
													NotificationRepository repository) {
		Notification notification = repository.findById(notificationId)
											  .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
																				  Message.Notification.NOTIFICATION_NOT_EXIST));

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = userDetails.getUsername();
		if (!notification.getToUser().getEmail().equals(email)) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Notification.CAN_NOT_READ_OTHER_S_NOTIFICATION);
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

	public static List<FeatureResponse> getFeatureResponse(List<Long> featureIds,
														   boolean isIncludeAdmin,
														   FeatureRepository featureRepository) {
		List<Feature> features;
		if (isIncludeAdmin) {
			features = featureRepository.findAll();
		} else {
			features = featureRepository.findAllNotAdmin();
		}

		List<Long> currFeature = new ArrayList<>();
		List<FeatureResponse> res = new ArrayList<>();
		for (Feature feature : features) {
			boolean has = featureIds.contains(feature.getId());
			if (has) {
				currFeature.add(feature.getId());
			}
			res.add(FeatureResponse.builder()
								   .id(feature.getId())
								   .name(feature.getName())
								   .code(feature.getCode())
								   .has(has).build());
		}

		for (Long id : featureIds) {
			if (!currFeature.contains(id)) {
				throw new AppException(HttpStatus.BAD_REQUEST, Message.Feature.FEATURE_NOT_EXIST);
			}
		}

		return res;
	}

	public static Item findItem(Long itemId, ItemRepository itemRepository) {
		Item item = itemRepository.findById(itemId)
								  .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
																	  Message.Item.ITEM_NOT_EXIST));

		if (item.isDeleted()) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.Item.ITEM_IS_DELETED);
		}
		return item;
	}
}
