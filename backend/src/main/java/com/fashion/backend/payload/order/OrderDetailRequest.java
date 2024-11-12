package com.fashion.backend.payload.order;

import com.fashion.backend.constant.Color;
import com.fashion.backend.constant.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderDetailRequest {
	@Schema(
			name = "itemId",
			example = "1"
	)
	@NotNull(message = Message.Order.ORDER_CAN_NOT_HAVE_NO_ITEM)
	private Long itemId;

	@Schema(
			name = "size",
			example = "S"
	)
	@NotEmpty(message = Message.Order.ORDER_ITEM_MUST_HAVE_SIZE)
	@NotNull(message = Message.Order.ORDER_ITEM_MUST_HAVE_SIZE)
	private String size;

	@Schema(
			name = "color",
			example = "BLACK"
	)
	@NotNull(message = Message.Order.ORDER_ITEM_MUST_HAVE_COLOR)
	private Color color;

	@Schema(
			name = "quantity",
			example = "1"
	)
	@Min(
			value = 1,
			message = Message.Order.ORDER_CAN_NOT_HAVE_NO_ITEM
	)
	@NotNull(message = Message.Order.ORDER_CAN_NOT_HAVE_NO_ITEM)
	private int quantity;
}
