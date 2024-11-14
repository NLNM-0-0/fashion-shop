package com.fashion.backend.payload.cart;

import com.fashion.backend.constant.Color;
import com.fashion.backend.constant.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCartRequest {
	@Schema(
			name = "size",
			example = "S"
	)
	@NotEmpty(message = Message.Cart.CART_ITEM_MUST_HAVE_SIZE)
	@NotNull(message = Message.Cart.CART_ITEM_MUST_HAVE_SIZE)
	private String size;

	@Schema(
			name = "color",
			example = "BLACK"
	)
	@NotNull(message = Message.Cart.CART_ITEM_MUST_HAVE_COLOR)
	private Color color;

	@Schema(
			name = "quantity",
			example = "1"
	)
	@Min(
			value = 1,
			message = Message.Cart.CART_CAN_NOT_HAVE_NO_ITEM
	)
	@NotNull(message = Message.Cart.CART_CAN_NOT_HAVE_NO_ITEM)
	private int quantity;
}
