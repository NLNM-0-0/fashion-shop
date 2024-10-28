package com.fashion.backend.repository;

import com.fashion.backend.entity.Cart;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
	List<Cart> findAllByUserId(Long userId, Sort sort);

	int countByUserId(Long userId);

	@Query("SELECT c FROM Cart c WHERE c.user.id = :userId AND c.item.id = :itemId")
	Optional<Cart> findFirstByUserIdAndItemId(Long userId, Long itemId);
}
