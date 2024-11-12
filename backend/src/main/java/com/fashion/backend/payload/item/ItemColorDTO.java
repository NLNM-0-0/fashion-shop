package com.fashion.backend.payload.item;

import com.fashion.backend.constant.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class ItemColorDTO {
	@Schema(
			name = "name",
			example = "color name"
	)
	private String name;

	@Schema(
			name = "hex",
			example = "000000"
	)
	private String hex;
}
