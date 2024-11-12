package com.fashion.backend.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckedFilter {
	private String name;
	private boolean checked;
}
