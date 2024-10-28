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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneRequest {
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
}
