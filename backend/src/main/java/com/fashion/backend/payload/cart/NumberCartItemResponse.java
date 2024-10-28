package com.fashion.backend.payload.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NumberCartItemResponse {
	@Schema(name = "number")
	private Integer number;
}