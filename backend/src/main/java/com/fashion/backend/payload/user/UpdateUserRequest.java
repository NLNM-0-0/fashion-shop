package com.fashion.backend.payload.user;

import com.fashion.backend.constant.ApplicationConst;
import com.fashion.backend.constant.Message;
import com.fashion.backend.utils.validation.date.ValidDDMMYYYYFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UpdateUserRequest {
	@Schema(
			name = "name",
			example = "user"
	)
	@Length(
			min = 1,
			max = 200,
			message = Message.User.NAME_VALIDATE
	)
	private String name;

	@Schema(
			name = "email",
			example = "user@gmail.com"
	)
	@Email(message = Message.EMAIL_VALIDATE)
	private String email;

	@Schema(
			name = "dob",
			example = "12/12/2000"
	)
	@ValidDDMMYYYYFormat(message = Message.User.DOB_VALIDATE)
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
	private Boolean male;
}
