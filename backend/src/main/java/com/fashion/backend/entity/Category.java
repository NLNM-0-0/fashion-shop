package com.fashion.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
		name = "category"
)
public class Category {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

	@Column(
			nullable = false,
			columnDefinition = "text"
	)
	@Length(max = 200)
	private String name;
}
