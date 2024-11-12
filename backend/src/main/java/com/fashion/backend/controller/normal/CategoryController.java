package com.fashion.backend.controller.normal;

import com.fashion.backend.payload.SimpleListResponse;
import com.fashion.backend.payload.category.CategoryResponse;
import com.fashion.backend.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@Tag(
		name = "Category"
)
@Validated
public class CategoryController {
	private final CategoryService categoryService;

	@GetMapping
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Fetch all categories"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('USER')")
	public ResponseEntity<SimpleListResponse<CategoryResponse>> getAllCategories() {
		return new ResponseEntity<>(categoryService.getCategories(), HttpStatus.OK);
	}
}
