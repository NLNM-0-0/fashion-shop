package com.fashion.backend.payload.usergroup;

import com.fashion.backend.constant.ApplicationConst;
import com.fashion.backend.entity.UserGroup;
import org.springframework.data.jpa.domain.Specification;

public class UserGroupSpecs {
	public static Specification<UserGroup> notIncludeAdmin() {
		return (root, query, cb) -> cb.notEqual(root.get("name"), ApplicationConst.ADMIN_USER_GROUP_NAME);
	}

	public static Specification<UserGroup> hasName(String name) {
		return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
	}
}
