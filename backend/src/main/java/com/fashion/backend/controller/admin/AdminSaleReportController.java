package com.fashion.backend.controller.admin;

import com.fashion.backend.payload.salereport.FindSaleReportRequest;
import com.fashion.backend.payload.salereport.SaleReportResponse;
import com.fashion.backend.service.SaleReportService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/saleReport")
@RequiredArgsConstructor
@Tag(
		name = "Sale report"
)
@Validated
public class AdminSaleReportController {
	private final SaleReportService saleReportService;

	@PostMapping
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Fetch sale report",
			description = ""
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
	public ResponseEntity<SaleReportResponse> findSaleReport(
			@Valid FindSaleReportRequest request) {
		return new ResponseEntity<>(saleReportService.findSaleReport(request), HttpStatus.OK);
	}
}
