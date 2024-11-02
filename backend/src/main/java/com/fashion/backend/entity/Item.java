package com.fashion.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
		name = "item",
		uniqueConstraints = {@UniqueConstraint(
				columnNames = {"name"},
				name = "Item name"
		)}
)
public class Item {
	@Getter @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

	@Column(nullable = false) @Length(
			min = 1,
			max = 200
	) private String name;

	@Column(
			nullable = false,
			columnDefinition = "text"
	) private String image;

	@Column(
			nullable = false
	)
	@Min(0)
	private int unitPrice;

	@Column(name = "quantity")
	@Min(0)
	private int quantity;

	@Column(nullable = false)
	private boolean isDeleted = false;
}
