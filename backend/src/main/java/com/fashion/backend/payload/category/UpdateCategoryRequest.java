package com.fashion.backend.payload.category;

import com.fashion.backend.constant.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UpdateCategoryRequest {
	@Schema(
			name = "name",
			example = "category name"
	)
	@Length(
			min = 1,
			max = 200,
			message = Message.Category.NAME_VALIDATE
	)
	@NotNull(message = Message.Category.NAME_VALIDATE)
	private String name;
}
