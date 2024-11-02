package com.fashion.backend.payload.item;

import com.fashion.backend.constant.Message;
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
}