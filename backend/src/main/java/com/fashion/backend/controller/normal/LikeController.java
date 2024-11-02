package com.fashion.backend.controller.normal;

import com.fashion.backend.payload.SimpleListResponse;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.item.SimpleItemResponse;
import com.fashion.backend.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
@Tag(
		name = "Like"
)
public class LikeController {
	private final LikeService likeService;

	@GetMapping
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Fetch liked items",
			description = "Fetch liked items of current user"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('USER')")
	public ResponseEntity<SimpleListResponse<SimpleItemResponse>> getLikedItems() {
		return new ResponseEntity<>(likeService.getLikedItems(), HttpStatus.OK);
	}

	@PostMapping("like/{itemId}")
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Like item",
			description = "Like one item"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('USER')")
	public ResponseEntity<SimpleResponse> likeItem(
			@PathVariable Long itemId) {
		return new ResponseEntity<>(likeService.likeItem(itemId), HttpStatus.OK);
	}

	@DeleteMapping("unlike/{itemId}")
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Unlike item",
			description = "Unlike one item"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('USER')")
	public ResponseEntity<SimpleResponse> deleteCartItem(
			@PathVariable Long itemId) {
		return new ResponseEntity<>(likeService.unlikeItem(itemId), HttpStatus.OK);
	}
}
