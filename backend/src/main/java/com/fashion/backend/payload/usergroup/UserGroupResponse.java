package com.fashion.backend.payload.usergroup;

import com.fashion.backend.payload.feature.FeatureResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGroupResponse {
	@Schema(
			name = "id",
			example = "0"
	)
	private Long id;

	@Schema(
			name = "name",
			example = "Tên nhóm người dùng"
	)
	private String name;

	@Schema(name = "features")
	private List<FeatureResponse> features;
}
