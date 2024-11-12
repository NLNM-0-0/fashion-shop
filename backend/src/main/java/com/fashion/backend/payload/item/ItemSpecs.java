package com.fashion.backend.payload.item;

import com.fashion.backend.entity.Item;
import org.springframework.data.jpa.domain.Specification;

public class ItemSpecs {
	public static Specification<Item> hasName(String name) {
		return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
	}

	public static Specification<Item> hasGender(String gender) {
		return (root, query, cb) -> cb.equal(root.get("gender"), gender);
	}

	public static Specification<Item> hasSeason(String season) {
		return (root, query, cb) -> cb.equal(root.get("season"), season);
	}

	public static Specification<Item> hasCategoryName(String categoryName) {
		return (root, query, cb) -> cb.like(root.get("categories").get("name"), "%" + categoryName + "%");
	}

	public static Specification<Item> isNotDeleted() {
		return (root, query, builder) -> builder.isFalse(root.get("isDeleted"));
	}
}