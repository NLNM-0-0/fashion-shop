package com.fashion.backend.payload.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CustomerOrderListResponse {
	@Schema(name = "pending")
	private List<OrderResponse> pending;

	@Schema(name = "confirmed")
	private List<OrderResponse> confirmed;

	@Schema(name = "shipped")
	private List<OrderResponse> shipped;

	@Schema(name = "done")
	private List<OrderResponse> done;

	@Schema(name = "cancel")
	private List<OrderResponse> cancel;
}
