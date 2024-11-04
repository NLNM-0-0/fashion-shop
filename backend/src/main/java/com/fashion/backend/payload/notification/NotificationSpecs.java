package com.fashion.backend.payload.notification;

import com.fashion.backend.constant.Message;
import com.fashion.backend.entity.Notification;
import com.fashion.backend.exception.AppException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
