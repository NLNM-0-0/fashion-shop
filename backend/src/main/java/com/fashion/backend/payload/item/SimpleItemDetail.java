package com.fashion.backend.payload.item;

import com.fashion.backend.constant.Gender;
import com.fashion.backend.payload.category.CategoryResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SimpleItemDetail {
	@Schema(
			name = "id",
			example = "1"
	)
	private Long id;

	@Schema(
			name = "name",
			example = "Item 1"
	)
	private String name;

	@Schema(
			name = "images"
	)
	private List<String> images;

	@Schema(
			name = "unitPrice"
	)
	private int unitPrice;

	@Schema(
			name = "isDeleted",
			example = "false"
	)
	private boolean isDeleted;

	@Schema(
			name = "gender"
	)
	private Gender gender;

	@Schema(
			name = "categories"
	)
	private List<CategoryResponse> categories;

	@Schema(
			name = "liked",
			example = "true"
	)
	private Boolean liked;
}
