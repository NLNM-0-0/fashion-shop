package com.fashion.backend.entity;

import com.fashion.backend.entity.IdClass.UserGroupFeatureId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
		name = "user_group_feature"
)
@IdClass(UserGroupFeatureId.class)
public class UserGroupFeature {
	@ManyToOne(
			fetch = FetchType.LAZY,
			targetEntity = UserGroup.class
	)
	@Id
	private UserGroup userGroup;

	@ManyToOne(
			fetch = FetchType.EAGER,
			targetEntity = Feature.class
	)
	@Id
	private Feature feature;
}
