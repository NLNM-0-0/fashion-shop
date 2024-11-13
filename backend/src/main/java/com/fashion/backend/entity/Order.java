package com.fashion.backend.entity;

import com.fashion.backend.constant.OrderStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
	@Getter @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(
			name = "customer_id",
			updatable = false
	)
	private User customer;

	@ManyToOne
	@JoinColumn(
			name = "staff_id",
			updatable = false
	)
	private User staff;

	@Column(name = "total_price")
	private int totalPrice;

	@Column(name = "total_quantity")
	private int totalQuantity;

	@OneToMany(
			fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			targetEntity = OrderDetail.class
	)
	@JsonManagedReference
	private List<OrderDetail> orderDetails;

	@Enumerated(EnumType.STRING)
	@Column(
			name = "status",
			nullable = false
	)
	private OrderStatus status;

	@CreatedDate
	@Column(
			name = "created_at",
			nullable = false,
			updatable = false
	)
	private Date createdAt;

	@Column(
			name = "confirmed_at"
	)
	private Date confirmedAt;

	@Column(
			name = "shipping_at"
	)
	private Date shippingAt;

	@Column(
			name = "done_at"
	)
	private Date doneAt;

	@Column(
			name = "canceled_at"
	)
	private Date cancelledAt;

	@LastModifiedDate
	@Schema(name = "updated_at")
	private Date updatedAt;
}