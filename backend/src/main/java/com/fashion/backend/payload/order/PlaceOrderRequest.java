package com.fashion.backend.payload.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class PlaceOrderRequest {
	@Schema(
			name = "details"
	)
	private List<OrderDetailRequest> details;
}
