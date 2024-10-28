package com.fashion.backend.controller.admin;

import com.fashion.backend.payload.SimpleListResponse;
import com.fashion.backend.payload.feature.SimpleFeatureResponse;
import com.fashion.backend.service.FeatureService;
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
@RequestMapping("/api/v1/admin/feature")
@RequiredArgsConstructor
@Tag(
		name = "Feature"
)
@Validated
public class AdminFeatureController {
	private final FeatureService featureService;

	@GetMapping
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Fetch all features",
			description = "Note:\n" +
						  "- Get all feature except ADMIN feature"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<SimpleListResponse<SimpleFeatureResponse>> getAllFeatures() {
		return new ResponseEntity<>(featureService.getAllFeatures(), HttpStatus.OK);
	}
}
