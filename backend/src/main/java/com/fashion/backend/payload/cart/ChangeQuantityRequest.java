package com.fashion.backend.payload.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeQuantityRequest {
	@Schema(
			name = "quantityChange",
			example = "1"
	)
	private int quantityChange;
}
