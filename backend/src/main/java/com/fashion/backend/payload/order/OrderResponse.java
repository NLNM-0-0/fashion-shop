package com.fashion.backend.payload.order;

import com.fashion.backend.constant.OrderStatus;
import com.fashion.backend.payload.customer.SimpleCustomerResponse;
import com.fashion.backend.payload.staff.SimpleStaffResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderResponse {
	@Schema(name = "id")
	private Long id;

	@Schema(name = "customer")
	private SimpleCustomerResponse customer;

	@Schema(name = "name")
	private String name;

	@Schema(name = "phone")
	private String phone;

	@Schema(name = "address")
	private String address;

	@Schema(name = "totalPrice")
	private int totalPrice;

	@Schema(name = "totalQuantity")
	private int totalQuantity;

	@Schema(name = "staff")
	private SimpleStaffResponse staff;

	@Schema(
			name = "createdAt",
			example = ""
	)
	private String createdAt;

	@Schema(name = "confirmedAt")
	private String confirmedAt;

	@Schema(name = "shippingAt")
	private String shippingAt;

	@Schema(name = "doneAt")
	private String doneAt;

	@Schema(name = "canceledAt")
	private String canceledAt;

	@Schema(name = "updatedAt")
	private String updatedAt;

	@Schema(name = "orderStatus")
	private OrderStatus orderStatus;

	@Schema(name = "details")
	private List<OrderDetailResponse> details;
}
