package com.fashion.backend.repository;

import com.fashion.backend.constant.OrderStatus;
import com.fashion.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
	List<Order> findAllByStatusAndUpdatedAtBetween(OrderStatus status,
												   Date updatedAt,
												   Date updatedAt2);
}
