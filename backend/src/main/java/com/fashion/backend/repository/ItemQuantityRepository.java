package com.fashion.backend.repository;

import com.fashion.backend.constant.Color;
import com.fashion.backend.entity.ItemQuantity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemQuantityRepository extends JpaRepository<ItemQuantity, Long> {
	Optional<ItemQuantity> findFirstByItemIdAndColorAndAndSize(Long itemId, Color color, String size);
}
