package com.fashion.backend.utils;

import com.fashion.backend.entity.UserAuth;

public class AuthHelper {
	public static boolean isNormalUser(String userName) {
		return !userName.contains("@");
	}

	public static boolean isNormalUser(UserAuth userAuth) {
		return userAuth.getPhone() != null;
	}

	public static boolean isStaff(String userName) {
		return !isNormalUser(userName);
	}

	public static boolean isStaff(UserAuth userAuth) {
		return !isNormalUser(userAuth);
	}
}
