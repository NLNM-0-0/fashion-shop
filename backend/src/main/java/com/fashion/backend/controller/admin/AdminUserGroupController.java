package com.fashion.backend.controller.admin;

import com.fashion.backend.payload.ListResponse;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.page.AppPageRequest;
import com.fashion.backend.payload.usergroup.*;
import com.fashion.backend.service.UserGroupService;
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
@RequestMapping("/api/v1/admin/userGroup")
@RequiredArgsConstructor
@Tag(
		name = "User group"
)
@Validated
public class AdminUserGroupController {
	private final UserGroupService userGroupService;

	@GetMapping
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Fetch user groups",
			description = "Note:\n" +
						  "- Fetch all user groups except admin user group"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	public ResponseEntity<ListResponse<SimpleUserGroupResponse, UserGroupFilter>> getUserGroups(
			@Valid AppPageRequest page,
			@Valid UserGroupFilter filter) {
		return new ResponseEntity<>(userGroupService.getUserGroups(page, filter), HttpStatus.OK);
	}

	@PostMapping
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Create user group",
			description = "Note:\n" +
						  "- Can not create user group with ADMIN feature"
	)
	@ApiResponse(
			responseCode = "201",
			description = "Http Status is 201 CREATED"
	)
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<UserGroupResponse> createUserGroup(
			@Valid @RequestBody CreateUserGroupRequest request
	) {
		return new ResponseEntity<>(userGroupService.createUserGroup(request), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Update user group",
			description = "Note:\n" +
						  "- Can not reach admin user group\n" +
						  "- Can not make user group has feature ADMIN"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<SimpleUserGroupResponse> updateUserGroup(
			@PathVariable Long id,
			@Valid @RequestBody UpdateUserGroupRequest request
	) {
		return new ResponseEntity<>(userGroupService.updateUserGroup(id, request), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Delete user group",
			description = "Note:\n" +
						  "- Can not reach admin user group"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<SimpleResponse> deleteUserGroup(
			@PathVariable Long id
	) {
		return new ResponseEntity<>(userGroupService.deleteUserGroup(id), HttpStatus.OK);
	}

}
