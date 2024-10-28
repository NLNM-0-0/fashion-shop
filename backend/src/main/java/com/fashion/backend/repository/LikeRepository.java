package com.fashion.backend.repository;

import com.fashion.backend.entity.Like;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
	List<Like> findAllByUserId(Long userId, Sort sort);

	@Query("SELECT l FROM Like l WHERE l.user.id = :userId AND l.item.id = :itemId")
	Optional<Like> findFirstByUserIdAndItemId(Long userId, Long itemId);
}
