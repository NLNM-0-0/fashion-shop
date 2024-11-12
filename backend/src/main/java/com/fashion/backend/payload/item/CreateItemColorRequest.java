package com.fashion.backend.payload.item;

import com.fashion.backend.constant.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateItemColorRequest {
	@Schema(
			name = "name",
			example = "color name"
	)
	@Length(
			min = 1,
			max = 20,
			message = Message.Item.COLOR_NAME_VALIDATE
	)
	@NotNull(message = Message.Item.COLOR_NAME_VALIDATE)
	private String name;

	@Schema(
			name = "hex",
			example = "000000"
	)
	@Length(
			min = 6,
			max = 6,
			message = Message.Item.COLOR_HEX_VALIDATE
	)
	@NotNull(message = Message.Item.COLOR_HEX_VALIDATE)
	private String hex;
}
