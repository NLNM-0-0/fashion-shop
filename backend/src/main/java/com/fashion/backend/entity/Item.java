package com.fashion.backend.entity;

import com.fashion.backend.constant.Color;
import com.fashion.backend.constant.Gender;
import com.fashion.backend.constant.Season;
import com.fashion.backend.utils.converter.ListColorConverter;
import com.fashion.backend.utils.converter.ListItemSizeConverter;
import com.fashion.backend.utils.converter.ListStringConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
		name = "item",
		uniqueConstraints = {@UniqueConstraint(
				columnNames = {"name"},
				name = "Item's name"
		)}
)
public class Item {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

	@Column(nullable = false) @Length(
			min = 1,
			max = 200
	) private String name;

	@Enumerated(EnumType.STRING)
	@Column(
			name = "gender",
			nullable = false
	)
	private Gender gender;

	@Column(
			name = "images",
			columnDefinition = "text"
	)
	@Convert(converter = ListStringConverter.class)
	private List<String> images;

	@Column(
			name = "sizes",
			columnDefinition = "text"
	)
	@Convert(converter = ListItemSizeConverter.class)
	private List<ItemSize> sizes;

	@Column(
			name = "colors",
			columnDefinition = "text"
	)
	@Convert(converter = ListColorConverter.class)
	private List<Color> colors;

	@ManyToMany(
			fetch = FetchType.LAZY,
			targetEntity = Category.class
	)
	private List<Category> categories;

	@Column(
			nullable = false
	)
	@Min(0)
	private int unitPrice;

	@Column(nullable = false)
	private Season season;

	@Column(name = "quantity")
	@Min(value = 0)
	private int quantity;

	@Column(nullable = false)
	private boolean isDeleted = false;
}
