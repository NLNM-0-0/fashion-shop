package com.fashion.backend.payload.item;

import com.fashion.backend.constant.Gender;
import com.fashion.backend.constant.Season;
import com.fashion.backend.payload.category.CategoryResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ItemDetail {
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
			name = "gender"
	)
	private Gender gender;

	@Schema(
			name = "colors"
	)
	private List<ItemColorDTO> colors;

	@Schema(
			name = "sizes"
	)
	private List<ItemSizeDTO> sizes;

	@Schema(
			name = "quantities"
	)
	private Map<String, Integer> quantities;

	@Schema(
			name = "categories"
	)
	private List<CategoryResponse> categories;

	@Schema(
			name = "season"
	)
	private Season season;

	@Schema(
			name = "unitPrice",
			example = "12000"
	)
	private Integer unitPrice;

	@Schema(
			name = "images"
	)
	private List<String> images;

	@Schema(
			name = "liked",
			example = "true"
	)
	private Boolean liked;

	@Schema(
			name = "isDeleted",
			example = "false"
	)
	private boolean isDeleted;
}
