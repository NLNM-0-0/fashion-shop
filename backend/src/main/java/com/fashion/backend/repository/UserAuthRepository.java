package com.fashion.backend.repository;

import com.fashion.backend.entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	Optional<UserAuth> findByEmail(String email);

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	Optional<UserAuth> findByPhone(String phone);

	@Modifying
	@Query("UPDATE UserAuth u SET u.isDeleted = true WHERE u.id = :userId")
	void deleteUserAuthById(Long userId);
}
