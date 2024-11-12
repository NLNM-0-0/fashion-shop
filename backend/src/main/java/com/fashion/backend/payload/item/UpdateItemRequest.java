package com.fashion.backend.payload.item;

import com.fashion.backend.constant.Color;
import com.fashion.backend.constant.Gender;
import com.fashion.backend.constant.Message;
import com.fashion.backend.constant.Season;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class UpdateItemRequest {
	@Schema(
			name = "name",
			example = "item name"
	)
	@Length(
			min = 1,
			max = 200,
			message = Message.Item.NAME_VALIDATE
	)
	private String name;

	@Schema(
			name = "gender"
	)
	private Gender gender;

	@Schema(
			name = "images"
	)
	private List<String> images;

	@Schema(
			name = "colors"
	)
	private List<Color> colors;

	@Schema(
			name = "sizes"
	)
	private List<CreateItemSizeRequest> sizes;

	@Schema(
			name = "categories",
			example = "[1, 2]"
	)
	private List<Long> categories;

	@Schema(
			name = "season"
	)
	private Season season;

	@Schema(
			name = "unitPrice",
			example = "12000"
	)
	@Min(
			value = 0,
			message = Message.Item.UNIT_PRICE_VALIDATE
	)
	private Integer unitPrice;

	@Schema(
			name = "quantity",
			example = "10"
	)
	@Min(
			value = 0,
			message = Message.Item.QUANTITY_VALIDATE
	)
	private Integer quantity;
}
