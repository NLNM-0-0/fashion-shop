package com.fashion.backend.payload.order;

import com.fashion.backend.entity.Order;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecs {
	public static Specification<Order> hasCustomerId(Long id) {
		return (root, query, cb) -> cb.equal(root.get("customer").get("id"), id);
	}

	public static Specification<Order> hasCustomerName(String name) {
		return (root, query, cb) -> cb.and(
				cb.isNotNull(root.get("customer")),
				cb.like(root.get("customer").get("name"), "%" + name + "%")
		);
	}

	public static Specification<Order> hasStaffName(String name) {
		return (root, query, cb) -> cb.and(
				cb.isNotNull(root.get("staff")),
				cb.like(root.get("staff").get("name"), "%" + name + "%")
		);
	}

	public static Specification<Order> hasStatus(String status) {
		return (root, query, cb) -> cb.equal(root.get("status"), status);
	}
}