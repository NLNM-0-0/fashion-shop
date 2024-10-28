package com.fashion.backend.entity;

import com.fashion.backend.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
	@Getter @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(
			name = "customer_id",
			nullable = true,
			updatable = false
	)
	private User customer;

	@Column(name = "total_price") // Changed to snake_case for consistency
	private int totalPrice;

	@ManyToOne
	@JoinColumn(
			name = "created_by",
			nullable = true,
			updatable = false
	)
	private User createdBy;

	@CreatedDate
	@Column(
			name = "created_at",
			nullable = false,
			updatable = false
	)
	private Date createdAt;

	@OneToMany(
			fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			targetEntity = OrderDetail.class
	)
	private List<OrderDetail> orderDetails;

	@Enumerated(EnumType.STRING)
	@Column(
			name = "status",
			nullable = false
	)
	private OrderStatus status;
}