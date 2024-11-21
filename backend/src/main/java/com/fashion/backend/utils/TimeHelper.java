package com.fashion.backend.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeHelper {
	public static long convertDateToNumber(Date date) {
		return date.getTime() / 1000;
	}

	public static String formatDate(Date date) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			return formatter.format(date);
		} catch (Exception e) {
			return null;
		}
	}
}
