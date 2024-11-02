package com.fashion.backend.controller.normal;

import com.fashion.backend.payload.ListResponse;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.order.OrderResponse;
import com.fashion.backend.payload.order.PlaceOrderRequest;
import com.fashion.backend.payload.order.SimpleOrderResponse;
import com.fashion.backend.payload.order.UserOrderFilter;
import com.fashion.backend.payload.page.AppPageRequest;
import com.fashion.backend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Tag(
		name = "Order"
)
public class OrderController {
	private final OrderService orderService;

	@GetMapping
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Fetch orders",
			description = "Fetch orders from database by filter and paging"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('USER')")
	public ResponseEntity<ListResponse<SimpleOrderResponse, UserOrderFilter>> getOrders(
			@Valid AppPageRequest page,
			@Valid UserOrderFilter filter) {
		return new ResponseEntity<>(orderService.getOrders(page, filter), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Fetch detail order"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('USER')")
	public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
		return new ResponseEntity<>(orderService.getOrder(id), HttpStatus.OK);
	}

	@PostMapping
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Create order",
			description = "Create new order"
	)
	@ApiResponse(
			responseCode = "201",
			description = "Http Status is 201 CREATED"
	)
	@PreAuthorize("hasAnyAuthority('USER')")
	public ResponseEntity<OrderResponse> createOrder(
			@Valid @RequestBody PlaceOrderRequest request
	) {
		return new ResponseEntity<>(orderService.placeOrder(request, false), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Receive order"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('USER')")
	public ResponseEntity<SimpleResponse> receiveOrder(
			@PathVariable Long id
	) {
		return new ResponseEntity<>(orderService.receiveOrder(id), HttpStatus.OK);
	}
}