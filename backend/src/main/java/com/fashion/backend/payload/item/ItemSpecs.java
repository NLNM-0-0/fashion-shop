package com.fashion.backend.payload.item;

import com.fashion.backend.entity.Item;
import org.springframework.data.jpa.domain.Specification;

public class ItemSpecs {
	public static Specification<Item> hasName(String name) {
		return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
	}

	public static Specification<Item> isNotDeleted() {
		return (root, query, builder) -> builder.isFalse(root.get("isDeleted"));
	}
}