package com.fashion.backend.payload.customer;

import com.fashion.backend.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecs {
	public static Specification<User> hasName(String name) {
		return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
	}

	public static Specification<User> hasEmail(String email) {
		return (root, query, cb) -> cb.like(root.get("email"), "%" + email + "%");
	}

	public static Specification<User> hasPhone(String phone) {
		return (root, query, cb) -> cb.like(root.get("phone"), "%" + phone + "%");
	}

	public static Specification<User> isNotDeleted() {
		return (root, query, builder) -> builder.isFalse(root.get("isDeleted"));
	}

	public static Specification<User> isNormalUser() {
		return (root, query, builder) -> builder.isNotNull(root.get("phone"));
	}

	public static Specification<User> notHaveEmail(String email) {
		return (root, query, builder) -> builder.notEqual(root.get("email"), email);
	}
}