package com.fashion.backend.controller.normal;

import com.fashion.backend.payload.ListResponse;
import com.fashion.backend.payload.item.ItemWithLikedStatusResponse;
import com.fashion.backend.payload.item.SimpleItemWithLikedStatusResponse;
import com.fashion.backend.payload.item.UserItemFilter;
import com.fashion.backend.payload.page.AppPageRequest;
import com.fashion.backend.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
@Tag(
		name = "Item"
)
@Validated
public class ItemController {
	private final ItemService itemService;

	@PostMapping
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Fetch items",
			description = "Note: Fetch items are not deleted"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('USER')")
	public ResponseEntity<ListResponse<SimpleItemWithLikedStatusResponse, UserItemFilter>> getItems(
			@Valid @RequestBody AppPageRequest page,
			@Valid @RequestBody UserItemFilter filter) {
		return new ResponseEntity<>(itemService.userGetItems(page, filter), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Fetch detail item"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('USER')")
	public ResponseEntity<ItemWithLikedStatusResponse> getItem(@PathVariable Long id) {
		return new ResponseEntity<>(itemService.userGetItem(id), HttpStatus.OK);
	}
}
