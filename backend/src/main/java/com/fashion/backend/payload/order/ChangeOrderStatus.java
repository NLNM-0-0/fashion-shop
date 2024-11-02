package com.fashion.backend.payload.order;

import com.fashion.backend.constant.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ChangeOrderStatus {
	@Schema(
			name = "orderStatus",
			example = "DONE"
	)
	private OrderStatus orderStatus;
}
