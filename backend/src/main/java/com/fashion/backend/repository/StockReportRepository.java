package com.fashion.backend.repository;

import com.fashion.backend.entity.StockReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.Optional;

public interface StockReportRepository extends JpaRepository<StockReport, Long> {
	@Query("SELECT r FROM StockReport r WHERE r.timeFrom = :timeFrom AND r.timeTo = :timeTo")
	Optional<StockReport> findFirstByTimeFromAndTimeTo(Date timeFrom, Date timeTo);
}
