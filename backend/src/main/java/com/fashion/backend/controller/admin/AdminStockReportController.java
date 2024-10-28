package com.fashion.backend.controller.admin;

import com.fashion.backend.payload.stockreport.FindStockReportRequest;
import com.fashion.backend.payload.stockreport.StockReportResponse;
import com.fashion.backend.service.StockReportService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/stockReport")
@RequiredArgsConstructor
@Tag(
		name = "Stock report"
)
@Validated
public class AdminStockReportController {
	private final StockReportService stockReportService;

	@PostMapping
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Fetch stock report",
			description = ""
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<StockReportResponse> findStockReport(
			@Valid FindStockReportRequest request) {
		return new ResponseEntity<>(stockReportService.findStockReport(request), HttpStatus.OK);
	}
}
