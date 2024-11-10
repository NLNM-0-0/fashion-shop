package com.fashion.backend.payload.order;

import com.fashion.backend.constant.OrderStatus;
import com.fashion.backend.payload.customer.SimpleCustomerResponse;
import com.fashion.backend.payload.staff.SimpleStaffResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SimpleOrderResponse {
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

	@Schema(name = "updatedAt")
	private Date updatedAt;

	@Schema(name = "orderStatus")
	private OrderStatus orderStatus;
}
