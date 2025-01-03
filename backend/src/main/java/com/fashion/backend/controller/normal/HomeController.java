package com.fashion.backend.controller.normal;

import com.fashion.backend.payload.SimpleListResponse;
import com.fashion.backend.payload.item.SimpleItemDetail;
import com.fashion.backend.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
@Tag(
		name = "Home"
)
@Validated
public class HomeController {
	private final HomeService homeService;

	@GetMapping("/bestSeller")
	@Operation(
			summary = "Get top sellers",
			description = "Note: 5"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	public ResponseEntity<SimpleListResponse<SimpleItemDetail>> getTopSellers() {
		return new ResponseEntity<>(homeService.getTopSellers(), HttpStatus.OK);
	}

	@GetMapping("/latest")
	@Operation(
			summary = "Get latest",
			description = "Note: 5"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	public ResponseEntity<SimpleListResponse<SimpleItemDetail>> getLatest() {
		return new ResponseEntity<>(homeService.getLatest(), HttpStatus.OK);
	}
}
