package com.fashion.backend.payload.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class NotificationResponse {
	@Schema(
			name = "id",
			example = "1"
	)
	private Long id;

	@Schema(
			name = "title",
			example = "Tiêu đề bài viết"
	)
	private String title;

	@Schema(
			name = "description",
			example = "Mô tả bài viết"
	)
	private String description;

	@Schema(name = "from")
	private SimpleNotiUserResponse from;

	@Schema(name = "to")
	private SimpleNotiUserResponse to;

	@Schema(name = "createdAt")
	private long createdAt;

	@Schema(name = "seen")
	private Boolean seen;
}
