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

	public static Specification<Notification> hasSender(String senderId) {
		return (root, query, cb) -> cb.equal(root.get("fromUser").get("id"), senderId);
	}

	public static Specification<Notification> isDateCreatedAfter(String from) {
		Date fromDate;
		try {
			fromDate = Date.valueOf(LocalDate.parse(from,
													DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		} catch (Exception e) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.TIME_INVALID_FORMAT_DD_MM_YYYY);
		}
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate);
	}

	public static Specification<Notification> isDateCreatedBefore(String to) {
		Timestamp toDate;
		try {
			LocalDate date = LocalDate.parse(to, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
			toDate = Timestamp.valueOf(endOfDay);
		} catch (Exception e) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.TIME_INVALID_FORMAT_DD_MM_YYYY);
		}
		return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("createdAt"), toDate);
	}
}
