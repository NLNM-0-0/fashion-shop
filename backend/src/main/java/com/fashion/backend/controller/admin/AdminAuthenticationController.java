package com.fashion.backend.controller.admin;

import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.auth.AuthenticationResponse;
import com.fashion.backend.payload.auth.EmailAuthenticationRequest;
import com.fashion.backend.payload.auth.EmailRequest;
import com.fashion.backend.payload.auth.ResetPasswordRequest;
import com.fashion.backend.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/auth")
@RequiredArgsConstructor
@Tag(
		name = "Auth"
)
@Validated
public class AdminAuthenticationController {
	private final AuthenticationService authService;

	@Operation(
			summary = "Login",
			description = "Login to use system"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PostMapping("/authenticate")
	@PermitAll
	public ResponseEntity<AuthenticationResponse> authenticate(
			@Valid @RequestBody EmailAuthenticationRequest request
	) {
		return ResponseEntity.ok(authService.authenticateByEmail(request));
	}

	@Operation(
			summary = "Send email to reset password",
			description = "Send email to reset password"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PostMapping("/reset_password")
	@PermitAll
	public ResponseEntity<SimpleResponse> sendEmailToResetPassword(
			@Valid @RequestBody EmailRequest request
	) {
		return ResponseEntity.ok(authService.sendEmailToResetPassword(request));
	}

	@Operation(
			summary = "Reset password",
			description = "Reset password by reset password token"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PostMapping("/reset_password/{token}")
	@PermitAll
	public ResponseEntity<SimpleResponse> resetPassword(
			@Valid @RequestBody ResetPasswordRequest request,
			@PathVariable String token) {
		return ResponseEntity.ok(authService.resetPasswordByEmail(request, token));
	}
}
