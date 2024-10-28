package com.fashion.backend.repository;

import com.fashion.backend.entity.StockChangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface StockChangeHistoryRepository extends JpaRepository<StockChangeHistory, Long> {
	@Query(
			"SELECT s FROM StockChangeHistory s " +
			"WHERE s.item.id = :itemId " +
			"AND s.createdAt >= :timeFrom " +
			"AND s.createdAt <= :timeTo " +
			"ORDER BY s.createdAt"
	)
	List<StockChangeHistory> findAllStockChangesForReport(
			Long itemId,
			Date timeFrom,
			Date timeTo);

	@Query(
			"SELECT s FROM StockChangeHistory s " +
			"WHERE s.item.id = :itemId " +
			"AND s.createdAt <= :timeRequest " +
			"ORDER BY s.createdAt DESC"
	)
	Optional<StockChangeHistory> getNearlyStockChangeHistory(
			Long itemId,
			Date timeRequest);
}
