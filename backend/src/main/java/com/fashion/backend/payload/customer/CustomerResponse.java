package com.fashion.backend.payload.customer;

import com.fashion.backend.constant.ApplicationConst;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {
	@Schema(
			name = "id",
			example = "1"
	)
	private Long id;

	@Schema(
			name = "email",
			example = "admin@gmail.com"
	)
	private String email;

	@Schema(
			name = "name",
			example = "Nguyễn Văn A"
	)
	private String name;

	@Schema(
			name = "phone",
			example = "0123456789"
	)
	private String phone;

	@Schema(
			name = "dob",
			example = "12/12/2000"
	)
	private String dob;

	@Schema(
			name = "address",
			example = "TPHCM"
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
	private boolean male;
}
