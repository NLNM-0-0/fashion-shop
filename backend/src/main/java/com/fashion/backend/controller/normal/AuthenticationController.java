package com.fashion.backend.controller.normal;

import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.auth.*;
import com.fashion.backend.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(
		name = "Auth"
)
@Validated
public class AuthenticationController {
	private final AuthenticationService authService;

	@Operation(
			summary = "Register",
			description = "Register to create new user"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PostMapping("/register")
	@PermitAll
	public ResponseEntity<SimpleResponse> register(
			@Valid @RequestBody RegisterRequest request
	) {
		return ResponseEntity.ok(authService.registerUser(request));
	}

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
			@Valid @RequestBody PhoneAuthenticationRequest request
	) {
		return ResponseEntity.ok(authService.authenticateByPhone(request));
	}

	@Operation(
			summary = "Send otp",
			description = "Send otp"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PostMapping("/send_otp")
	@PermitAll
	public ResponseEntity<SimpleResponse> sendOtp(
			@Valid @RequestBody PhoneRequest request
	) {
			return ResponseEntity.ok(authService.sendOtp(request));
	}

	@Operation(
			summary = "Verify otp when reset password",
			description = "Verify otp to reset password"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PostMapping("/verify-reset-password")
	@PermitAll
	public ResponseEntity<SimpleResponse> verifyOtpToResetPassword(
			@Valid @RequestBody OtpVerifyRequest request
	) {
		return ResponseEntity.ok(authService.verifiedOTPToResetPassword(request));
	}

	@Operation(
			summary = "Verify otp when register",
			description = "Verify otp when register"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PostMapping("/verify")
	@PermitAll
	public ResponseEntity<SimpleResponse> verifyOtp(
			@Valid @RequestBody OtpVerifyRequest request
	) {
		return ResponseEntity.ok(authService.verifiedOTP(request));
	}

	@Operation(
			summary = "Reset password",
			description = "Reset password by token"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PostMapping("/reset_password")
	@PermitAll
	public ResponseEntity<SimpleResponse> resetPassword(
			@Valid @RequestBody OtpResetPasswordRequest request) {
		return ResponseEntity.ok(authService.resetPasswordByOTP(request));
	}
}
