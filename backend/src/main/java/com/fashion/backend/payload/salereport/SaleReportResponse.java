package com.fashion.backend.payload.salereport;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
@Builder
public class SaleReportResponse {
	@Schema(
			name = "timeFrom",
			example = "2023-12-03T15:02:19.62113565Z"
	)
	private Date timeFrom;

	@Schema(
			name = "timeTo",
			example = "2023-12-03T15:02:19.62113565Z"
	)
	private Date timeTo;

	@Schema(
			name = "total",
			example = "100000"
	)
	private int total;

	@Schema(
			name = "amount",
			example = "10"
	)
	private int amount;

	@Schema(name = "details")
	private List<SaleReportDetailResponse> details;
}
