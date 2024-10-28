package com.fashion.backend.payload.usergroup;

import com.fashion.backend.constant.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class UpdateUserGroupRequest {
	@Schema(
			name = "name",
			example = "Tên nhóm người dùng"
	)
	@Length(
			min = 1,
			max = 100,
			message = Message.UserGroup.USER_GROUP_NAME_VALIDATE
	)
	private String name;

	@Schema(
			name = "features",
			example = "[0, 1]"
	)
	private List<Long> features;
}
