package com.fashion.backend.payload.auth;

import com.fashion.backend.constant.ApplicationConst;
import com.fashion.backend.constant.Message;
import com.fashion.backend.utils.validation.date.ValidDDMMYYYYFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
	@Schema(
			name = "name",
			example = "user"
	)
	@Length(
			min = 1,
			max = 200,
			message = Message.User.NAME_VALIDATE
	)
	@NotNull(message = Message.User.NAME_VALIDATE)
	private String name;

	@Schema(
			name = "email",
			example = "user@gmail.com"
	)
	@Email(message = Message.EMAIL_VALIDATE)
	@NotNull(message = Message.EMAIL_VALIDATE)
	private String email;

	@Schema(
			name = "phone",
			example = "0123456789"
	)
	@Pattern(
			regexp = "\\d{10,11}",
			message = Message.PHONE_VALIDATE
	)
	@NotNull(message = Message.PHONE_VALIDATE)
	private String phone;

	@Schema(
			name = "dob",
			example = "12/12/2000"
	)
	@ValidDDMMYYYYFormat(message = Message.User.DOB_VALIDATE)
	@NotNull(message = Message.User.DOB_VALIDATE)
	private String dob;

	@Schema(
			name = "address",
			example = "TPHCM"
	)
	@Length(
			min = 1,
			max = 50,
			message = Message.User.ADDRESS_VALIDATE
	)
	@NotNull(message = Message.User.ADDRESS_VALIDATE)
	private String address;

	@Schema(
			name = "image",
			example = ApplicationConst.DEFAULT_AVATAR
	)
	private String image;

	@Schema(
			name = "male",
			example = "true"
	)
	@NotNull(message = Message.User.GENDER_VALIDATE)
	private Boolean male;

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
