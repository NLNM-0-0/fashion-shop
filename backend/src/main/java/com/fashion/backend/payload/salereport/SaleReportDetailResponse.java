package com.fashion.backend.payload.salereport;

import com.fashion.backend.payload.item.SimpleItemResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaleReportDetailResponse {
	@Schema(
			name = "item"
	) private SimpleItemResponse item;

	@Schema(
			name = "amount",
			example = "10"
	) private int amount;

	@Schema(
			name = "totalSales",
			example = "100000"
	) private int totalSales;
}
