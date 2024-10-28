package com.fashion.backend.controller.normal;

import com.fashion.backend.payload.SimpleListResponse;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.cart.AddToCartRequest;
import com.fashion.backend.payload.cart.CartDetailResponse;
import com.fashion.backend.payload.cart.ChangeQuantityRequest;
import com.fashion.backend.payload.notification.NumberNotificationNotSeenResponse;
import com.fashion.backend.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(
		name = "Cart"
)
public class CartController {
	private final CartService cartService;

	@GetMapping
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Fetch carts",
			description = "Fetch carts of current user"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	public ResponseEntity<SimpleListResponse<CartDetailResponse>> getCart() {
		return new ResponseEntity<>(cartService.getCart(), HttpStatus.OK);
	}

	@GetMapping("/number")
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Get number cart items",
			description = "Get number of cart items in current cart"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	public ResponseEntity<NumberNotificationNotSeenResponse> getNumberCartItems() {
		return new ResponseEntity<>(cartService.getNumberCartItems(), HttpStatus.OK);
	}

	@PostMapping
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Add cart item",
			description = "Add item to card"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	public ResponseEntity<SimpleResponse> addCartItem(
			@Valid AddToCartRequest request) {
		return new ResponseEntity<>(cartService.addCartItem(request), HttpStatus.OK);
	}

	@DeleteMapping("/{itemId}")
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Delete cart item",
			description = "Remove item from cart"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	public ResponseEntity<SimpleResponse> deleteCartItem(
			@PathVariable Long itemId) {
		return new ResponseEntity<>(cartService.deleteCartItem(itemId), HttpStatus.OK);
	}

	@PostMapping("/{itemId}")
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Change quantity of cart items",
			description = "Change quantity of cart items"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	public ResponseEntity<SimpleResponse> changeQuantityCartItem(
			@PathVariable Long itemId,
			@Valid ChangeQuantityRequest request) {
		return new ResponseEntity<>(cartService.changeQuantityCartItem(itemId, request), HttpStatus.OK);
	}
}
