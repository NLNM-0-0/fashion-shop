package com.fashion.backend.repository;

import com.fashion.backend.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OTP, Long> {
	Optional<OTP> findByUserIdAndOtp(Long userId, String otp);

	Optional<OTP> findByUserId(Long userId);
}
