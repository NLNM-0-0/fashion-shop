package com.fashion.backend.payload.feature;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureResponse {
	@Schema(
			name = "id",
			example = "1"
	)
	private Long id;

	@Schema(
			name = "code",
			example = "MANAGER STAFF"
	)
	private String code;

	@Schema(
			name = "name",
			example = "Tên chức năng"
	)
	private String name;

	@Schema(
			name = "has",
			example = "false"
	)
	private boolean has;
}
