package com.fashion.backend.payload.page;

import com.fashion.backend.constant.ApplicationConst;
import com.fashion.backend.constant.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppPageRequest {
	@Schema(
			name = "page",
			example = "1"
	)
	@Min(
			value = 1,
			message = Message.Page.PAGE_VALIDATE
	)
	private int page = ApplicationConst.DEFAULT_PAGE;

	@Schema(
			name = "limit",
			example = "10"
	)
	@Min(
			value = 1,
			message = Message.Page.PAGE_LIMIT
	)
	private int limit = ApplicationConst.DEFAULT_LIMIT;
}
