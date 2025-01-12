package com.fashion.backend.repository;

import com.fashion.backend.entity.StockReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.Optional;

public interface StockReportRepository extends JpaRepository<StockReport, Long> {
	Optional<StockReport> findFirstByTimeFromAndTimeTo(Date timeFrom, Date timeTo);
}
