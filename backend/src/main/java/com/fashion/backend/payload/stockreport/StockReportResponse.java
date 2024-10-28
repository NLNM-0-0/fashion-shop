package com.fashion.backend.payload.stockreport;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Builder
@Data
public class StockReportResponse {
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
			name = "initial",
			example = "0"
	)
	private int initial;

	@Schema(
			name = "sell",
			example = "-10"
	)
	private int sell;

	@Schema(
			name = "increase",
			example = "100"
	)
	private int increase;

	@Schema(
			name = "decrease",
			example = "-20"
	)
	private int decrease;

	@Schema(
			name = "final",
			example = "70"
	)
	private int finalQty;

	@Schema(name = "details")
	private List<StockReportDetailResponse> details;
}
