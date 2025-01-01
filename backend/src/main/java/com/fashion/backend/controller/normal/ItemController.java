package com.fashion.backend.controller.normal;

import com.fashion.backend.payload.ListResponse;
import com.fashion.backend.payload.item.ItemDetail;
import com.fashion.backend.payload.item.SimpleItemDetail;
import com.fashion.backend.payload.item.UserItemFilter;
import com.fashion.backend.payload.page.AppPageRequest;
import com.fashion.backend.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
@Tag(
		name = "Item"
)
@Validated
public class ItemController {
	private final ItemService itemService;

	@GetMapping
	@Operation(
			summary = "Fetch items",
			description = "Note: Fetch items are not deleted"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	public ResponseEntity<ListResponse<SimpleItemDetail, UserItemFilter>> getItems(
			@Valid AppPageRequest page,
			@Valid UserItemFilter filter) {
		return new ResponseEntity<>(itemService.userGetItems(page, filter), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(
			summary = "Fetch detail item"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	public ResponseEntity<ItemDetail> getItem(@PathVariable Long id) {
		return new ResponseEntity<>(itemService.userGetItem(id), HttpStatus.OK);
	}
}
