package com.fashion.backend.entity;

import com.fashion.backend.constant.Color;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
		name = "cart",
		uniqueConstraints = {@UniqueConstraint(
				columnNames = {"user_id", "item_id", "color", "size"},
				name = "Cart item"
		)}
)
@EntityListeners(AuditingEntityListener.class)
public class Cart {
	@Getter @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

	@ManyToOne
	@JoinColumn(
			name = "user_id",
			nullable = false,
			updatable = false
	)
	private User user;

	@ManyToOne
	@JoinColumn(
			name = "item_id",
			nullable = false,
			updatable = false
	)
	private Item item;

	@Column(
			name = "size",
			columnDefinition = "text"
	)
	private String size;

	@Enumerated(EnumType.STRING)
	@Column(name = "color")
	private Color color;

	@Column(name = "quantity")
	@Min(0)
	private int quantity;

	@CreatedDate
	@Schema(name = "created_at")
	private Date createdAt;
}
