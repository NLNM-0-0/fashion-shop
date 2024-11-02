package com.fashion.backend.payload.stockreport;

import com.fashion.backend.payload.item.SimpleItemResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockReportDetailResponse {
	@Schema(
			name = "item"
	) private SimpleItemResponse item;

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
			name = "payback",
			example = "10"
	)
	private int payback;

	@Schema(
			name = "final",
			example = "80"
	)
	private int finalQty;
}
