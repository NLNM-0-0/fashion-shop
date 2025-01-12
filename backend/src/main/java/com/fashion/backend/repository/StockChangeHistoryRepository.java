package com.fashion.backend.repository;

import com.fashion.backend.entity.StockChangeHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface StockChangeHistoryRepository extends JpaRepository<StockChangeHistory, Long> {

	List<StockChangeHistory> findAllByItemIdAndCreatedAtAfterAndCreatedAtBefore(
			Long itemId,
			Date timeFrom,
			Date timeTo);


	List<StockChangeHistory> findByItemIdAndCreatedAtLessThanEqual(
			Long itemId,
			Date timeRequest,
			Pageable pageable
	);
}
