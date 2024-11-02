package com.fashion.backend.controller.admin;

import com.fashion.backend.payload.ListResponse;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.customer.CustomerFilter;
import com.fashion.backend.payload.customer.CustomerResponse;
import com.fashion.backend.payload.page.AppPageRequest;
import com.fashion.backend.service.CustomerService;
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
@RequestMapping("/api/v1/admin/customer")
@RequiredArgsConstructor
@Tag(
		name = "Customer"
)
@Validated
public class AdminCustomerController {
	private final CustomerService customerService;

	@GetMapping
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Fetch customers",
			description = "Note: Fetch all customers are not deleted"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
	public ResponseEntity<ListResponse<CustomerResponse, CustomerFilter>> getUsers(
			@Valid AppPageRequest page,
			@Valid CustomerFilter filter) {
		return new ResponseEntity<>(customerService.getStaffs(page, filter), HttpStatus.OK);
	}

	@GetMapping("/all")
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Fetch all customers",
			description = "Note: Fetch all customers even is deleted"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	public ResponseEntity<ListResponse<CustomerResponse, CustomerFilter>> getListCustomers(
			@Valid AppPageRequest page,
			@Valid CustomerFilter filter) {
		return new ResponseEntity<>(customerService.getAllCustomer(page, filter), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Fetch detail customer",
			description = "Fetch detail customer's info"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
	public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {
		return new ResponseEntity<>(customerService.getCustomer(id), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Delete customer",
			description = "Delete customer to prevent them login"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
	public ResponseEntity<SimpleResponse> deleteCustomer(
			@PathVariable Long id
	) {
		return new ResponseEntity<>(customerService.deleteCustomer(id), HttpStatus.OK);
	}
}
