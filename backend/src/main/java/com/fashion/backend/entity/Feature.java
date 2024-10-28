package com.fashion.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
		name = "feature",
		uniqueConstraints = {@UniqueConstraint(
				columnNames = {"name"},
				name = "Feature Name"
		)}
)
public class Feature {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(
			nullable = false,
			unique = true
	)
	@Length(max = 32)
	private String code;

	@Column(nullable = false)
	private String name;
}
