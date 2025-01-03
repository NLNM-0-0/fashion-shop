package com.fashion.backend.payload.item;

import com.fashion.backend.constant.Gender;
import com.fashion.backend.constant.Message;
import com.fashion.backend.constant.Season;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class CreateItemRequest {
	@Schema(
			name = "name",
			example = "item name"
	)
	@Length(
			min = 1,
			max = 200,
			message = Message.Item.NAME_VALIDATE
	)
	@NotEmpty(message = Message.Item.NAME_VALIDATE)
	@NotNull(message = Message.Item.NAME_VALIDATE)
	private String name;

	@Schema(
			name = "gender"
	)
	@NotNull(message = Message.Item.GENDER_VALIDATE)
	private Gender gender;

	@Schema(
			name = "images"
	)
	private List<String> images;

	@Schema(
			name = "quantities"
	)
	@NotNull(message = Message.Item.QUANTITY_VALIDATE)
	private List<ItemQuantityRequest> quantities;

	@Schema(
			name = "categories",
			example = "[1, 2]"
	)
	@NotNull(message = Message.Item.CATEGORY_VALIDATE)
	private List<Long> categories;

	@Schema(
			name = "unitPrice",
			example = "12000"
	)
	@Min(
			value = 0,
			message = Message.Item.UNIT_PRICE_VALIDATE
	)
	@NotNull(message = Message.Item.UNIT_PRICE_VALIDATE)
	private Integer unitPrice;

	@Schema(
			name = "season"
	)
	@NotNull(message = Message.Item.SEASON_VALIDATE)
	private Season season;
}
