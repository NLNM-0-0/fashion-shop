package com.fashion.backend.payload.cart;

import com.fashion.backend.payload.item.SimpleItemResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CartDetailResponse {
	@Schema(
			name = "id",
			example = "1"
	)
	private Long id;

	@Schema(
			name = "item"
	)
	private SimpleItemResponse item;

	@Schema(
			name = "quantity",
			example = "1"
	)
	private int quantity;

	@Schema(name = "updatedAt")
	private Date updatedAt;
}
