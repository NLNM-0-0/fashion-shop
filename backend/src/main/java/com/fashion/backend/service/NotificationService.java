package com.fashion.backend.service;

import com.fashion.backend.constant.Message;
import com.fashion.backend.entity.Notification;
import com.fashion.backend.entity.User;
import com.fashion.backend.entity.UserAuth;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.mail.MailSender;
import com.fashion.backend.payload.ListResponse;
import com.fashion.backend.payload.SimpleListResponse;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.notification.*;
import com.fashion.backend.payload.page.AppPageRequest;
import com.fashion.backend.payload.page.AppPageResponse;
import com.fashion.backend.repository.NotificationRepository;
import com.fashion.backend.repository.UserAuthRepository;
import com.fashion.backend.repository.UserRepository;
import com.fashion.backend.utils.TimeHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
	private final NotificationRepository notificationRepository;
	private final UserRepository userRepository;
	private final UserAuthRepository userAuthRepository;
	private final MailSender mailSender;

	@Transactional
	public ListResponse<NotificationResponse, NotificationFilter> getNotifications(AppPageRequest page,
																				   NotificationFilter filter) {
		User user = Common.findCurrentUser(userRepository, userAuthRepository);

		Pageable pageable = PageRequest.of(page.getPage() - 1,
										   page.getLimit(),
										   Sort.by(Sort.Direction.DESC, "createdAt"));
		Specification<Notification> spec = filterNotifications(user.getId(), filter);

		Page<Notification> notificationPage = notificationRepository.findAll(spec, pageable);

		List<Notification> notifications = notificationPage.getContent();

		List<NotificationResponse> data = notifications.stream().map(this::mapToDTO).toList();

		return ListResponse.<NotificationResponse, NotificationFilter>builder()
						   .data(data)
						   .appPageResponse(AppPageResponse.builder()
														   .index(page.getPage())
														   .limit(page.getLimit())
														   .totalPages(notificationPage.getTotalPages())
														   .totalElements(notificationPage.getTotalElements())
														   .build())
						   .filter(filter)
						   .build();
	}

	private Specification<Notification> filterNotifications(Long receiverId, NotificationFilter filter) {
		Specification<Notification> spec = NotificationSpecs.hasReceiver(receiverId.toString());
		if (filter.getSenderName() != null) {
			spec = spec.and(NotificationSpecs.hasSenderName(filter.getSenderName()));
		}
		if (filter.getTimeFrom() != null) {
			spec = spec.and(NotificationSpecs.isDateCreatedAfter(filter.getTimeFrom()));
		}
		if (filter.getTimeTo() != null) {
			spec = spec.and(NotificationSpecs.isDateCreatedBefore(filter.getTimeTo()));
		}
		if (filter.getSeen() != null) {
			spec = spec.and(NotificationSpecs.hasSeen(filter.getSeen()));
		}
		return spec;
	}

	@Transactional
	public NumberNotificationNotSeenResponse getNumberUnseenNotification() {
		User user = Common.findCurrentUser(userRepository, userAuthRepository);

		return NumberNotificationNotSeenResponse.builder()
												.number(notificationRepository.countUnseenNotificationsByToUserId(
														user.getId()))
												.build();
	}

	@Transactional
	public SimpleListResponse<NotificationResponse> getUnseenNotifications() {
		User user = Common.findCurrentUser(userRepository, userAuthRepository);
		List<Notification> notifications = notificationRepository.findAllUnseenByToUserId(user.getId());

		return new SimpleListResponse<>(notifications.stream().map(this::mapToDTO).toList());
	}

	@Transactional
	public SimpleResponse sendNotification(CreateNotificationRequest request) {
		User user = Common.findCurrentUser(userRepository, userAuthRepository);

		List<User> receivers;
		if (request.getReceivers() == null || request.getReceivers().isEmpty()) {
			receivers = userRepository.findAllNotDeleted();
		} else {
			receivers = userRepository.findByIdInAndIdNotAndUserAuthPhoneIsNotNullAndUserAuthIsDeletedFalse(request.getReceivers(),
																							user.getId());
		}

		return Common.sendNotification(notificationRepository,
									   mailSender,
									   receivers,
									   user,
									   request.getTitle(),
									   request.getDescription());
	}

	@Transactional
	public NotificationResponse seeNotification(Long notificationId) {
		User user = Common.findCurrentUser(userRepository, userAuthRepository);
		Notification notification = notificationRepository.findByIdAndToUserId(notificationId, user.getId())
											  .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST,
																				  Message.Notification.NOTIFICATION_NOT_EXIST));

		notification.setSeen(true);

		return mapToDTO(notificationRepository.save(notification));
	}

	@Transactional
	public SimpleResponse seeAllNotification() {
		User user = Common.findCurrentUser(userRepository, userAuthRepository);
		List<Notification> notifications = notificationRepository.findAllByToUserId(user.getId());

		notificationRepository.saveAll(notifications.stream()
													.peek(notification -> notification.setSeen(true))
													.toList());

		return new SimpleResponse();
	}

	private NotificationResponse mapToDTO(Notification notification) {
		return NotificationResponse.builder()
								   .id(notification.getId())
								   .title(notification.getTitle())
								   .description(notification.getDescription())
								   .from(mapToDTO(notification.getFromUser()))
								   .to(mapToDTO(notification.getToUser()))
								   .seen(notification.isSeen())
								   .createdAt(TimeHelper.convertDateToNumber(notification.getCreatedAt()))
								   .build();
	}

	private SimpleNotiUserResponse mapToDTO(User user) {
		return SimpleNotiUserResponse.builder()
									 .id(user.getId())
									 .name(user.getName())
									 .email(user.getEmail())
									 .image(user.getImage())
									 .build();
	}
}
