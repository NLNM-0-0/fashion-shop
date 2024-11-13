package com.fashion.backend.payload.item;

import com.fashion.backend.constant.Color;
import com.fashion.backend.constant.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ItemQuantityRequest {
	@Schema(
			name = "size",
			example = "S"
	)
	@Length(
			min = 1,
			max = 20,
			message = Message.Item.SIZE_NAME_VALIDATE
	)
	@NotNull(message = Message.Item.SIZE_NAME_VALIDATE)
	private String size;

	@Schema(
			name = "color"
	)
	@NotNull(message = Message.Item.COLOR_VALIDATE)
	private Color color;

	@Schema(
			name = "quantity"
	)
	@Min(
			value = 0,
			message = Message.Item.QUANTITY_VALIDATE
	)
	@NotNull(message = Message.Item.QUANTITY_VALIDATE)
	private int quantity;
}
