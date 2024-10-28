package com.fashion.backend.repository;

import com.fashion.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
	@Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :timeFrom AND :timeTo")
	List<Order> findAllByTimeFromAndTimeTo(Date timeFrom, Date timeTo);
}
