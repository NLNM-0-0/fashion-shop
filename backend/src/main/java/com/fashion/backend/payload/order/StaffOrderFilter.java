package com.fashion.backend.payload.order;

import com.fashion.backend.constant.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffOrderFilter {
	@Schema(name = "orderStatus")
	private OrderStatus orderStatus;

	@Schema(
			name = "staffName",
			example = "Tên nhân viên"
	)
	private String staffName;

	@Schema(
			name = "customerName",
			example = "Tên khách hàng"
	)
	private String customerName;
}