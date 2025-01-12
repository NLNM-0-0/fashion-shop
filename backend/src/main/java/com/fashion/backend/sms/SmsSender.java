package com.fashion.backend.sms;

import com.fashion.backend.constant.Message;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.utils.SpeedSMSAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class SmsSender {

	private final SpeedSMSAPI provider;
	@Value("${sms.speedsms.apiKey}")
	private String apiKey;
	@Value("${sms.speedsms.phone}")
	private String apiPhone;

	public SmsSender() {
		provider = new SpeedSMSAPI(apiKey);
	}

	public boolean sendOtp(String phoneNumber, String otp) {
		String message = "Your OTP code is: " + otp;
		try {
			String response = provider.sendSMS(phoneNumber, message, 2, apiPhone);
			if (response.contains("success")) {
				return true;
			}
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, Message.SMS.SMS_SEND_FAIL);
		} catch (Exception e) {
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, Message.SMS.SMS_SEND_FAIL);
		}
	}
}
