package com.fashion.backend.entity;

import com.fashion.backend.constant.StockChangeType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
		name = "stock_change_history"
)
@EntityListeners(AuditingEntityListener.class)
public class StockChangeHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(
			name = "item_id",
			nullable = false,
			updatable = false
	)
	private Item item;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "quantity_left")
	@Min(0)
	private int quantityLeft;

	@Enumerated(EnumType.STRING)
	@Column(
			name = "type",
			nullable = false
	)
	private StockChangeType type;

	@CreatedDate
	@Column(
			name = "created_at",
			nullable = false,
			updatable = false
	)
	private Date createdAt;
}
