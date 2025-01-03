package com.fashion.backend.payload.notification;

import com.fashion.backend.constant.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class CreateNotificationRequest {
	@Schema(
			name = "title",
			example = "Tiêu đề bài viết"
	)
	@Length(
			min = 1,
			max = 100,
			message = Message.Notification.TITLE_VALIDATE
	)
	@NotNull(message = Message.Notification.TITLE_VALIDATE)
	private String title;

	@Schema(
			name = "description",
			example = "Mô tả bài viết"
	)
	@Length(
			max = 200,
			message = Message.Notification.DESCRIPTION_VALIDATE
	)
	@NotNull(message = Message.Notification.DESCRIPTION_VALIDATE)
	private String description = "";

	@Schema(
			name = "receivers",
			example = "[1, 2]"
	)
	private List<Long> receivers;
}
