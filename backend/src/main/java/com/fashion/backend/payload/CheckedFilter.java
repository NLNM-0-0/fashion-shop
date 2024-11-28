package com.fashion.backend.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckedFilter<T> {
	@Schema(name = "data")
	private T data;

	@Schema(name = "checked")
	private boolean checked;
}
