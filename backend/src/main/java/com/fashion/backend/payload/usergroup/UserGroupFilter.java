package com.fashion.backend.payload.usergroup;

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
public class UserGroupFilter {
	@Schema(
			name = "name",
			example = "Tên nhóm người dùng"
	)
	@Length(
			max = 100,
			message = Message.UserGroup.USER_GROUP_NAME_FILTER_VALIDATE
	)
	private String name;
}
