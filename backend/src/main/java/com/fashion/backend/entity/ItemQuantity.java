package com.fashion.backend.entity;

import com.fashion.backend.constant.Color;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
		name = "item_quantity",
		uniqueConstraints = {@UniqueConstraint(
				columnNames = {"name"},
				name = "Item's name"
		)}
)
@Embeddable
public class ItemQuantity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

	@ManyToOne()
	private Item item;

	@Column(
			name = "size",
			columnDefinition = "text"
	)
	private String size;

	@Column(
			name = "color",
			columnDefinition = "text"
	)
	@Enumerated(EnumType.STRING)
	private Color color;

	@Column(
			name = "quantity"
	)
	@Min(0)
	private int quantity;
}
