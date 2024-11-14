package com.fashion.backend.payload.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SimpleItemResponse {
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
			name = "images"
	)
	private List<String> images;

	@Schema(
			name = "unitPrice"
	)
	private int unitPrice;

	@Schema(
			name = "isDeleted",
			example = "false"
	)
	private boolean isDeleted;
}
