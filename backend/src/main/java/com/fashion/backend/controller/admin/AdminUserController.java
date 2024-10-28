package com.fashion.backend.controller.admin;

import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.user.ChangePasswordRequest;
import com.fashion.backend.payload.user.StaffProfileResponse;
import com.fashion.backend.payload.user.UpdateUserStaffRequest;
import com.fashion.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/user")
@RequiredArgsConstructor
@Tag(
		name = "User"
)
@Validated
public class AdminUserController {
	private final UserService userService;

	@PostMapping("/password")
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Change password",
			description = "Change password for user's account"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	public ResponseEntity<SimpleResponse> changePassword(
			@Valid @RequestBody ChangePasswordRequest request
	) {
		return new ResponseEntity<>(userService.changePassword(request), HttpStatus.OK);
	}

	@PutMapping("/info")
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "Update simple info of current user",
			description = "Update dob address and image of current user"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	public ResponseEntity<StaffProfileResponse> updateUser(
			@Valid @RequestBody UpdateUserStaffRequest request
	) {
		return new ResponseEntity<>(userService.updateUserStaff(request), HttpStatus.OK);
	}

	@GetMapping
	@SecurityRequirement(
			name = "Bearer Authentication"
	)
	@Operation(
			summary = "See profile",
			description = "See user's simple info and features"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	public ResponseEntity<StaffProfileResponse> seeProfile() {
		return new ResponseEntity<>(userService.seeStaffProfile(), HttpStatus.OK);
	}
}
