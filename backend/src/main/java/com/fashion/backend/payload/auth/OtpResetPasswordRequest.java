package com.fashion.backend.payload.auth;

import com.fashion.backend.constant.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpResetPasswordRequest {
	@Schema(
			name = "phone",
			example = "0123456789"
	)
	@Pattern(
			regexp = "\\d{10,11}",
			message = Message.PHONE_VALIDATE
	)
	@NotEmpty(message = Message.PHONE_VALIDATE)
	@NotNull(message = Message.PHONE_VALIDATE)
	private String phone;

	@Schema(
			name = "otp",
			example = "012345"
	)
	@Pattern(
			regexp = "\\d{6}",
			message = Message.OTP_VALIDATE
	)
	@NotEmpty(message = Message.OTP_VALIDATE)
	@NotNull(message = Message.OTP_VALIDATE)
	private String Otp;

	@Schema(
			name = "password",
			example = "123456"
	)
	@Length(
			min = 6,
			max = 20,
			message = Message.PASSWORD_VALIDATE
	)
	@NotNull(message = Message.PASSWORD_VALIDATE)
	private String password;
}
