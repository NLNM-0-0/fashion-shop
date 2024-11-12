package com.fashion.backend.payload.item;

import com.fashion.backend.constant.Gender;
import com.fashion.backend.constant.Message;
import com.fashion.backend.constant.Season;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemFilter {
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
			name = "gender",
			example = "BOYS"
	)
	private Gender gender;

	@Schema(
			name = "season",
			example = "SPRING"
	)
	private Season season;

	@Schema(
			name = "category",
			example = "Category name"
	)
	private String category;
}