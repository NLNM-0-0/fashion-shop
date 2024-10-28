package com.fashion.backend.payload.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileLinkResponse {
	@Schema(
			name = "file",
			example = "https://firebasestorage.googleapis.com/v0/b/fashion-shop-94e75.appspot.com/o/Default%2Fdefault-avatar.png?alt=media"
	)
	private String file;
}
