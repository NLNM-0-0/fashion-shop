package com.fashion.backend.payload.user;

import com.fashion.backend.constant.ApplicationConst;
import com.fashion.backend.constant.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UpdateUserStaffRequest {
	@Schema(
			name = "image",
			example = ApplicationConst.DEFAULT_AVATAR
	)
	private String image;
}
