package com.fashion.backend.payload.item;

import com.fashion.backend.entity.Item;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ItemSpecs {
	public static Specification<Item> hasName(String name) {
		return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
	}

	public static Specification<Item> hasGender(List<String> genders) {
		return (root, query, cb) -> root.get("gender").in(genders);
	}

	public static Specification<Item> hasSeason(List<String> seasons) {
		return (root, query, cb) -> root.get("season").in(seasons);
	}

	public static Specification<Item> hasColor(List<String> colors) {
		return (root, query, cb) -> root.get("color").in(colors);
	}

	public static Specification<Item> hasCategoryId(Long categoryId) {
		return (root, query, cb) -> cb.equal(root.get("categories").get("id"), categoryId);
	}

	public static Specification<Item> hasCategoryName(String categoryName) {
		return (root, query, cb) -> cb.like(root.get("categories").get("name"), "%" + categoryName + "%");
	}

	public static Specification<Item> isNotDeleted() {
		return (root, query, builder) -> builder.isFalse(root.get("isDeleted"));
	}
}