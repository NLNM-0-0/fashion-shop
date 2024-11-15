package com.fashion.backend.controller.admin;

import com.fashion.backend.payload.ListResponse;
import com.fashion.backend.payload.ListResponseWithoutFilter;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.page.AppPageRequest;
import com.fashion.backend.payload.staff.CreateStaffRequest;
import com.fashion.backend.payload.staff.StaffFilter;
import com.fashion.backend.payload.staff.StaffResponse;
import com.fashion.backend.payload.staff.UpdateStaffRequest;
import com.fashion.backend.service.StaffService;
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
@RequestMapping("/api/v1/admin/staff")
@RequiredArgsConstructor
@Tag(
		name = "Staff"
)
@Validated
public class AdminStaffController {
	private final StaffService staffService;

	@GetMapping
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Fetch staffs",
			description = "Note: Fetch all staffs are not deleted and not current user"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<ListResponse<StaffResponse, StaffFilter>> getStaffs(
			@Valid AppPageRequest page,
			@Valid StaffFilter filter) {
		return new ResponseEntity<>(staffService.getStaffs(page, filter), HttpStatus.OK);
	}

	@GetMapping("/all")
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Fetch all staffs",
			description = "Note: Fetch all staffs even admin and is deleted"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	public ResponseEntity<ListResponseWithoutFilter<StaffResponse>> getListStaffs(
			@Valid AppPageRequest page) {
		return new ResponseEntity<>(staffService.getAllStaff(page), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Fetch detail staff",
			description = "Note:\n" +
						  "- Can not reach admin's info"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<StaffResponse> getStaff(@PathVariable Long id) {
		return new ResponseEntity<>(staffService.getStaff(id), HttpStatus.OK);
	}

	@PostMapping
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Create staff",
			description = "Create new staff"
	)
	@ApiResponse(
			responseCode = "201",
			description = "Http Status is 201 CREATED"
	)
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<StaffResponse> createStaff(
			@Valid @RequestBody CreateStaffRequest request
	) {
		return new ResponseEntity<>(staffService.createStaff(request), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Update staff",
			description = "Note:\n" +
						  "- Can not update admin's info"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<StaffResponse> updateStaff(
			@PathVariable Long id,
			@Valid @RequestBody UpdateStaffRequest request
	) {
		return new ResponseEntity<>(staffService.updateStaff(id, request), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Delete staff",
			description = "Note:\n" +
						  "- Can not delete admin"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<SimpleResponse> deleteStaff(
			@PathVariable Long id
	) {
		return new ResponseEntity<>(staffService.deleteStaff(id), HttpStatus.OK);
	}
}
