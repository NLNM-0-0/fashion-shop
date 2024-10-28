package com.fashion.backend.repository;

import com.fashion.backend.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OTP, Long> {
	@Query("SELECT otp FROM OTP otp WHERE otp.user.id = :userId")
	Optional<OTP> findByUser(Long userId);
}
