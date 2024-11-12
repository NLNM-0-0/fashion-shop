package com.fashion.backend.entity;

import com.fashion.backend.constant.Message;
import com.fashion.backend.exception.AppException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
		name = "otp"
)
public class OTP {
	private static final int EXPIRATION_MINUTE = 5;
	private static final int MAX_RETRY = 5;

	private static final int OTP_LENGTH = 6;

	private static Random rand = new Random();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Setter
	private String otp;

	@OneToOne(
			targetEntity = UserAuth.class,
			fetch = FetchType.EAGER
	)
	@JoinColumn(
			name = "user_id",
			unique = true,
			nullable = false
	)
	private UserAuth user;

	private Date expiryDate;

	@Column(nullable = false)
	@Min(0)
	@Max(MAX_RETRY)
	private int retry = 0;

	public OTP(String otp, UserAuth user) {
		this.otp        = otp;
		this.user       = user;
		this.expiryDate = getOTPExpirationTime();
		this.retry      = 0;
	}

	public static String generateOTP() {
//		int otp = 100000 + rand.nextInt(900000);
//		return String.valueOf(otp);
		return "260703";
	}

	private Date getOTPExpirationTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		calendar.add(Calendar.MINUTE, EXPIRATION_MINUTE);
		return new Date(calendar.getTime().getTime());
	}

	public boolean isValid() {
		Calendar calendar = Calendar.getInstance();
		return (this.getOTPExpirationTime().getTime() - calendar.getTime().getTime()) > 0;
	}

	public void increaseRetry() {
		int newRetry = this.retry + 1;
		if (newRetry >= MAX_RETRY) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.OTP_OVER_LIMIT);
		}
		this.retry = newRetry;
	}

	public int getNextRetry() {
		int newRetry = this.retry + 1;
		if (newRetry >= MAX_RETRY) {
			throw new AppException(HttpStatus.BAD_REQUEST, Message.OTP_OVER_LIMIT);
		}
		return newRetry;
	}
}
