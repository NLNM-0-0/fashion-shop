package com.fashion.backend.payload.customer;

import com.fashion.backend.constant.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerFilter {
	@Schema(
			name = "name",
			example = "Tên người dùng"
	)
	@Length(
			max = 200,
			message = Message.User.NAME_FILTER_VALIDATE
	)
	private String name;

	@Schema(
			name = "email",
			example = "a@gmail.com"
	)
	private String email;

	@Schema(
			name = "phone",
			example = "0123456789"
	)
	@Length(
			max = 11,
			message = Message.User.PHONE_FILTER_VALIDATE
	)
	private String phone;
}