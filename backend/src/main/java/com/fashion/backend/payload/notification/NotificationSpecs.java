package com.fashion.backend.payload.notification;

import com.fashion.backend.entity.Notification;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Date;

public class NotificationSpecs {
	public static Specification<Notification> hasSeen(Boolean seen) {
		return (root, query, cb) -> cb.equal(root.get("seen"), seen);
	}

	public static Specification<Notification> hasReceiver(String receiverId) {
		return (root, query, cb) -> cb.equal(root.get("toUser").get("id"), receiverId);
	}

	public static Specification<Notification> hasSenderName(String name) {
		return (root, query, cb) -> cb.equal(root.get("fromUser").get("name"), name);
	}

	public static Specification<Notification> isDateCreatedAfter(Integer timeFrom) {
		return (root, query, cb) -> {
			Date timeFromDate = new Date((long) timeFrom * 1000);
			return cb.greaterThanOrEqualTo(root.get("createdAt"), timeFromDate);
		};
	}

	public static Specification<Notification> isDateCreatedBefore(Integer timeTo) {
		return (root, query, cb) -> {
			Date timeToDate = new Date((long) timeTo * 1000);
			return cb.lessThanOrEqualTo(root.get("createdAt"), timeToDate);
		};
	}
}
