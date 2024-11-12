package com.fashion.backend.payload.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemSizeDTO {
	@Schema(
			name = "name",
			example = "size name"
	)
	private String name;
}
