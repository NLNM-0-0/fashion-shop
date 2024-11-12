package com.fashion.backend.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckedFilter {
	@Schema(name = "field")
	private String name;

	@Schema(name = "checked")
	private boolean checked;
}
