package com.fashion.backend.payload.item;

import com.fashion.backend.constant.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserItemFilter {
	@Schema(
			name = "name",
			example = "Tên sản phẩm"
	)
	@Length(
			max = 200,
			message = Message.Item.NAME_FILTER_VALIDATE
	)
	private String name;

	@Schema(
			name = "categoryId"
	)
	private Long categoryId;

	@Schema(
			name = "genders",
			example = "[\"MEN\"]"
	)
	private List<Gender> genders;

	@Schema(
			name = "seasons",
			example = "[\"SPRING\"]"
	)
	private List<Season> seasons;

	@Schema(
			name = "price",
			example = "ALL"
	)
	private PriceFilter price;

	@Schema(
			name = "colors",
			example = "[\"BLACK\"]"
	)
	private List<Color> colors;

	@Schema(
			name = "sortPrice"
	)
	private Boolean sortPrice;

	@Schema(
			name = "sortNew"
	)
	private Boolean sortNew;
}
