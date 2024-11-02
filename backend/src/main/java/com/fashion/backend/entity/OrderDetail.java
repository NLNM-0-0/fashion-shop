package com.fashion.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
		name = "orders_detail"
)
public class OrderDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(
			name = "item_id",
			nullable = false,
			updatable = false
	)
	private Item item;

	@Column(name = "quantity")
	@Min(1)
	private int quantity;

	@Column(name = "unit_price")
	@Min(0)
	private int unitPrice;
}