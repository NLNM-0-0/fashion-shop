package com.fashion.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
		name = "stock_report_detail"
)
public class StockReportDetail {
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

	@Column(name = "initial")
	private int initial;

	@Column(name = "sell")
	private int sell;

	@Column(name = "increase")
	private int increase;

	@Column(name = "decrease")
	private int decrease;

	@Column(name = "payback")
	private int payback;

	@Column(name = "final")
	private int finalQty;
}
