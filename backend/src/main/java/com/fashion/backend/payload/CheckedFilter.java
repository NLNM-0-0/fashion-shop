package com.fashion.backend.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckedFilter<T> {
	@Schema(name = "data")
	private T data;

	@Schema(name = "checked")
	private boolean checked;
}
