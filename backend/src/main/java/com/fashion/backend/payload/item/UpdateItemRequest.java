package com.fashion.backend.payload.item;

import com.fashion.backend.constant.ApplicationConst;
import com.fashion.backend.constant.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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
			name = "image",
			example = ApplicationConst.DEFAULT_IMAGE
	)
	private String image;

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
