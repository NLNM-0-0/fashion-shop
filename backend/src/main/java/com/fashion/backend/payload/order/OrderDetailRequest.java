package com.fashion.backend.payload.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OrderDetailRequest {
	@Schema(
			name = "itemId",
			example = "1"
	)
	private Long itemId;

	@Schema(
			name = "quantity",
			example = "1"
	)
	private int quantity;
}
