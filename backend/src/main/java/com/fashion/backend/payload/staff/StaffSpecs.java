package com.fashion.backend.payload.staff;

import com.fashion.backend.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class StaffSpecs {
	public static Specification<User> hasName(String name) {
		return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
	}

	public static Specification<User> hasEmail(String email) {
		return (root, query, cb) -> cb.like(root.get("userAuth").get("email"), "%" + email + "%");
	}

	public static Specification<User> notHasEmail(String email) {
		return (root, query, cb) -> cb.notEqual(root.get("userAuth").get("email"), email);
	}

	public static Specification<User> isAdmin(Boolean isAdmin) {
		return (root, query, cb) -> cb.equal(root.get("userAuth").get("isAdmin"), isAdmin);
	}

	public static Specification<User> isMale(Boolean male) {
		return (root, query, cb) -> cb.equal(root.get("male"), male);
	}

	public static Specification<User> hasDOBinMonth(Integer dobMonth) {
		String formattedNumber = String.format("%02d", dobMonth);
		return (root, query, cb) -> cb.like(root.get("dob"), "___" + formattedNumber + "_____");
	}

	public static Specification<User> hasDOBinYear(Integer dobYear) {
		String formattedNumber = String.format("%02d", dobYear);
			return (root, query, cb) -> cb.like(root.get("dob"), "______" + formattedNumber);
	}

	public static Specification<User> isNotDeleted() {
		return (root, query, builder) -> builder.isFalse(root.get("userAuth").get("isDeleted"));
	}

	public static Specification<User> isStaff() {
		return (root, query, builder) -> builder.isNotNull(root.get("userAuth").get("email"));
	}
}