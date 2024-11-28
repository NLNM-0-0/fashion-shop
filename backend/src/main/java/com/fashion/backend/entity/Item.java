package com.fashion.backend.entity;

import com.fashion.backend.constant.Gender;
import com.fashion.backend.constant.Season;
import com.fashion.backend.utils.converter.ListStringConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
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
@EntityListeners(AuditingEntityListener.class)
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

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Season season;

	@Column(nullable = false)
	private boolean isDeleted = false;

	@CreatedDate
	@Column(
			name = "created_at",
			nullable = false,
			updatable = false
	)
	private Date createdAt;
}
