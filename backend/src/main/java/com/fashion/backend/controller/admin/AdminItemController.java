package com.fashion.backend.controller.admin;

import com.fashion.backend.payload.ListResponse;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.item.*;
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
@RequestMapping("/api/v1/admin/item")
@RequiredArgsConstructor
@Tag(
		name = "Item"
)
@Validated
public class AdminItemController {
	private final ItemService itemService;

	@GetMapping
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Fetch items",
			description = "Note: Fetch all items are not deleted"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
	public ResponseEntity<ListResponse<SimpleItemResponse, AdminItemFilter>> getItems(
			@Valid AppPageRequest page,
			@Valid AdminItemFilter filter) {
		return new ResponseEntity<>(itemService.staffGetItems(page, filter), HttpStatus.OK);
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
	@PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
	public ResponseEntity<ItemResponse> getItem(@PathVariable Long id) {
		return new ResponseEntity<>(itemService.staffGetItem(id), HttpStatus.OK);
	}

	@PostMapping
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Create item",
			description = "Create new item"
	)
	@ApiResponse(
			responseCode = "201",
			description = "Http Status is 201 CREATED"
	)
	@PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
	public ResponseEntity<ItemResponse> createItem(
			@Valid @RequestBody CreateItemRequest request
	) {
		return new ResponseEntity<>(itemService.createItem(request), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Update item"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
	public ResponseEntity<ItemResponse> updateItem(
			@PathVariable Long id,
			@Valid @RequestBody UpdateItemRequest request
	) {
		return new ResponseEntity<>(itemService.updateItem(id, request), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Delete item"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
	public ResponseEntity<SimpleResponse> deleteItem(
			@PathVariable Long id
	) {
		return new ResponseEntity<>(itemService.deleteItem(id), HttpStatus.OK);
	}
}
