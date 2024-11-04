package com.fashion.backend.utils;

import com.fashion.backend.constant.Message;
import com.fashion.backend.exception.AppException;
import org.springframework.http.HttpStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TimeHelper {
	public static long convertDateToNumber(Date date) {
		return date.getTime() / 1000;
	}
}
