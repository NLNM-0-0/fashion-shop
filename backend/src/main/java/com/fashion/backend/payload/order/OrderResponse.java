package com.fashion.backend.payload.order;

import com.fashion.backend.constant.OrderStatus;
import com.fashion.backend.payload.customer.SimpleCustomerResponse;
import com.fashion.backend.payload.staff.SimpleStaffResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class OrderResponse {
	@Schema(name = "id")
	private Long id;

	@Schema(name = "customer")
	private SimpleCustomerResponse customer;

	@Schema(name = "totalPrice")
	private int totalPrice;

	@Schema(name = "totalQuantity")
	private int totalQuantity;

	@Schema(name = "staff")
	private SimpleStaffResponse staff;

	@Schema(name = "createdAt")
	private Date createdAt;

	@Schema(name = "confirmedAt")
	private Date confirmedAt;

	@Schema(name = "shippingAt")
	private Date shippingAt;

	@Schema(name = "doneAt")
	private Date doneAt;

	@Schema(name = "canceledAt")
	private Date canceledAt;

	@Schema(name = "updatedAt")
	private Date updatedAt;

	@Schema(name = "orderStatus")
	private OrderStatus orderStatus;

	@Schema(name = "details")
	private List<OrderDetailResponse> details;
}
