package com.fashion.backend.utils;

import java.util.Date;

public class TimeHelper {
	public static long convertDateToNumber(Date date) {
		return date.getTime() / 1000;
	}
}
