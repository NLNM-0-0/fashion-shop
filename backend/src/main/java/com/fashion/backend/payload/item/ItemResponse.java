package com.fashion.backend.payload.item;

import com.fashion.backend.constant.ApplicationConst;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemResponse {
	@Schema(
			name = "id",
			example = "1"
	)
	private Long id;

	@Schema(
			name = "name",
			example = "Item 1"
	)
	private String name;

	@Schema(
			name = "image",
			example = ApplicationConst.DEFAULT_AVATAR
	)
	private String image;

	@Schema(
			name = "isDeleted",
			example = "false"
	)
	private boolean isDeleted;
}
