package com.fashion.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
		name = "stock_report"
)
public class StockReport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "time_from")
	private Date timeFrom;

	@Column(name = "time_to")
	private Date timeTo;

	@Column(name = "initial")
	private int initial;

	@Column(name = "sell")
	private int sell;

	@Column(name = "increase")
	private int increase;

	@Column(name = "decrease")
	private int decrease;

	@Column(name = "final")
	private int finalQty;

	@OneToMany(
			mappedBy = "report",
			fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			targetEntity = StockReportDetail.class
	)
	private List<StockReportDetail> details;
}
