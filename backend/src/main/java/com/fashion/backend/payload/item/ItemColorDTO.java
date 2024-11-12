package com.fashion.backend.payload.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemColorDTO {
	@Schema(
			name = "name",
			example = "color name"
	)
	private String name;

	@Schema(
			name = "hex",
			example = "000000"
	)
	private String hex;
}
