package com.fashion.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
		name = "user_group",
		uniqueConstraints = {@UniqueConstraint(
				columnNames = {"name"},
				name = "User group name"
		)}
)
public class UserGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@Length(
			min = 1,
			max = 100
	)
	private String name;

	@OneToMany(
			fetch = FetchType.EAGER,
			mappedBy = "userGroup",
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			targetEntity = UserGroupFeature.class
	)
	private Set<UserGroupFeature> userGroupFeatures;
}
