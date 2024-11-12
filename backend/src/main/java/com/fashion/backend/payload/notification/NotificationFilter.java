package com.fashion.backend.payload.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationFilter {
	@Schema(
			name = "senderName",
			example = "Tên nhân viên"
	)
	private String senderName;

	@Schema(
			name = "timeFrom",
			example = "1701388800"
	)
	private Integer timeFrom;

	@Schema(
			name = "timeTo",
			example = "1704067199"
	)
	private Integer timeTo;

	@Schema(
			name = "seen",
			example = "true"
	)
	private Boolean seen;
}
