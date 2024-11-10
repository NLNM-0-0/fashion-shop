package com.fashion.backend.payload.stockreport;

import com.fashion.backend.constant.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindStockReportRequest {
	@Schema(
			name = "timeFrom",
			example = "1701388800"
	)
	@NotNull(message = Message.TIME_FROM_TIME_TO_VALIDATE)
	private Integer timeFrom;

	@Schema(
			name = "timeTo",
			example = "1704067199"
	)
	@NotNull(message = Message.TIME_FROM_TIME_TO_VALIDATE)
	private Integer timeTo;
}
