package com.fashion.backend.payload.cart;

import com.fashion.backend.constant.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddToCartRequest {
	@Schema(
			name = "itemId",
			example = "1"
	)
	private Long itemId;

	@Schema(
			name = "quantity",
			example = "1"
	)
	@Min(
			value = 0,
			message = Message.Cart.QUANTITY_MIN_VALIDATE
	)
	private int quantity;
}
