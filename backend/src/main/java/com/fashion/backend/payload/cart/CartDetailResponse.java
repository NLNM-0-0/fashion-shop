package com.fashion.backend.payload.cart;

import com.fashion.backend.constant.Color;
import com.fashion.backend.payload.item.ItemResponse;
import com.fashion.backend.payload.item.SimpleItemResponse;
import com.fashion.backend.payload.item.SimpleItemWithLikedStatusResponse;
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
	private ItemResponse item;

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

	@Schema(
			name = "quantity",
			example = "1"
	)
	private int quantity;

	@Schema(
			name = "itemQuantity",
			example = "2"
	)
	private int itemQuantity;

	@Schema(name = "isExist")
	private boolean isExist;

	@Schema(name = "createdAt")
	private Date createdAt;
}
