package com.fashion.backend.payload.item;

import com.fashion.backend.constant.ApplicationConst;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemImageDTO {
	@Schema(
			name = "image",
			example = ApplicationConst.DEFAULT_IMAGE
	)
	private String image;
}
