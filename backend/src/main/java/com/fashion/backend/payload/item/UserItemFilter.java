package com.fashion.backend.payload.item;

import com.fashion.backend.constant.Gender;
import com.fashion.backend.constant.Message;
import com.fashion.backend.payload.CheckedFilter;
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
			name = "genders"
	)
	private List<CheckedFilter> genders;

	@Schema(
			name = "seasons"
	)
	private List<CheckedFilter> seasons;

	@Schema(
			name = "sizes"
	)
	private List<CheckedFilter> sizes;

	@Schema(
			name = "prices"
	)
	private List<CheckedFilter> prices;

	@Schema(
			name = "colors"
	)
	private List<CheckedFilter> colors;
}
