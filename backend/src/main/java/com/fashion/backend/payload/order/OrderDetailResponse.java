package com.fashion.backend.payload.order;

import com.fashion.backend.constant.Color;
import com.fashion.backend.payload.item.SimpleItemResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetailResponse {
	@Schema(name = "item")
	private SimpleItemResponse item;

	@Schema(
			name = "size",
			example = "S"
	)
	private String size;

	@Schema(
			name = "color",
			example = "BLACK"
	)
	private Color color;

	@Schema(name = "quantity")
	private int quantity;

	@Schema(name = "unitPrice")
	private int unitPrice;

	@Schema(name = "totalSubPrice")
	private int totalSubPrice;
}
