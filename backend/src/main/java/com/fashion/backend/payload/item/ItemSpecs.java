package com.fashion.backend.payload.item;

import com.fashion.backend.constant.Gender;
import com.fashion.backend.constant.Season;
import com.fashion.backend.entity.Item;
import com.fashion.backend.payload.CheckedFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ItemSpecs {
	public static Specification<Item> hasName(String name) {
		return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
	}

	public static Specification<Item> hasGender(List<CheckedFilter<Gender>> genders) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			for (CheckedFilter<Gender> gender : genders) {
				if (gender.isChecked()) {
					predicates.add(cb.equal(root.get("gender"), gender.getData()));
				}
			}
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

	public static Specification<Item> hasSeason(List<CheckedFilter<Season>> seasons) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			for (CheckedFilter<Season> season : seasons) {
				if (season.isChecked()) {
					predicates.add(cb.equal(root.get("season"), season.getData()));
				}
			}
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

	public static Specification<Item> hasColor(List<CheckedFilter<ItemColorDTO>> colors) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			for (CheckedFilter<ItemColorDTO> color : colors) {
				if (color.isChecked()) {
					predicates.add(cb.equal(root.get("color"), color.getData()));
				}
			}
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

	public static Specification<Item> hasSize(List<CheckedFilter<ItemSizeDTO>> sizes) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			for (CheckedFilter<ItemSizeDTO> size : sizes) {
				if (size.isChecked()) {
					predicates.add(cb.equal(root.get("size"), size.getData()));
				}
			}
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

	public static Specification<Item> hasPrice(Integer minValue, Integer maxValue) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (minValue != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("price").get("unitPrice"), minValue));
			}

			if (maxValue != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("price").get("unitPrice"), maxValue));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
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