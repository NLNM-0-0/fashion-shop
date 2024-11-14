package com.fashion.backend.payload.cart;

import com.fashion.backend.constant.Color;
import com.fashion.backend.constant.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeQuantityRequest {
	@Schema(
			name = "quantityChange",
			example = "1"
	)
	@NotNull(message = Message.Cart.CART_UPDATE_NEED_TO_CHANGE_QUANTITY)
	private int quantityChange;
}
