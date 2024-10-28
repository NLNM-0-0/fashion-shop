package com.fashion.backend.payload.stockreport;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindStockReportRequest {
	@Schema(
			name = "timeFrom",
			example = "1701388800"
	)
	private int timeFrom;

	@Schema(
			name = "timeTo",
			example = "1704067199"
	)
	private int timeTo;
}
