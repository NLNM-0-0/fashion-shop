package com.fashion.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
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
				columnNames = {"user_id", "item_id"},
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

	@Column(name = "quantity")
	@Min(0)
	private int quantity;

	@LastModifiedDate
	@Schema(name = "updatedAt")
	private Date updatedAt;
}
