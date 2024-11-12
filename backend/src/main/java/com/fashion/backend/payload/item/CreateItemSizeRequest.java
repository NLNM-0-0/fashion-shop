package com.fashion.backend.payload.item;

import com.fashion.backend.constant.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateItemSizeRequest {
	@Schema(
			name = "name",
			example = "size name"
	)
	@Length(
			min = 1,
			max = 20,
			message = Message.Item.SIZE_NAME_VALIDATE
	)
	@NotNull(message = Message.Item.SIZE_NAME_VALIDATE)
	private String name;
}
