package com.fashion.backend.payload.item;

import com.fashion.backend.constant.Color;
import com.fashion.backend.constant.Gender;
import com.fashion.backend.constant.Season;
import com.fashion.backend.entity.Item;
import com.fashion.backend.entity.ItemQuantity;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ItemSpecs {
	public static Specification<Item> hasName(String name) {
		return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
	}

	public static Specification<Item> hasGender(List<Gender> genders) {
		return (root, query, cb) -> cb.or(
				genders.stream()
					   .map(gender -> cb.equal(root.get("gender"), gender))
					   .toArray(Predicate[]::new)
		);
	}

	public static Specification<Item> hasSeason(List<Season> seasons) {
		return (root, query, cb) -> cb.or(
				seasons.stream()
					   .map(season -> cb.equal(root.get("season"), season))
					   .toArray(Predicate[]::new)
		);
	}

	public static Specification<Item> hasColor(List<Color> colors) {
		return (root, query, cb) -> {
			Subquery<Long> subquery = query.subquery(Long.class);
			Root<ItemQuantity> itemQuantityRoot = subquery.from(ItemQuantity.class);

			subquery.select(itemQuantityRoot.get("item").get("id"))
					.where(itemQuantityRoot.get("color").in(colors));

			return cb.in(root.get("id")).value(subquery);
		};
	}

	public static Specification<Item> hasPrice(Integer minValue, Integer maxValue) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (minValue != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("unitPrice"), minValue));
			}

			if (maxValue != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("unitPrice"), maxValue));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

	public static Specification<Item> hasCategoryId(Long categoryId) {
		return (root, query, cb) -> cb.equal(root.get("categories").get("id"), categoryId);
	}

	public static Specification<Item> isNotDeleted() {
		return (root, query, builder) -> builder.isFalse(root.get("isDeleted"));
	}
}