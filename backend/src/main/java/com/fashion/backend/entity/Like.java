package com.fashion.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
		name = "like",
		uniqueConstraints = {@UniqueConstraint(
				columnNames = {"user_id, item_id"},
				name = "Item in liked list"
		)}
)
public class Like {
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

	@Schema(name = "createdAt")
	private Date createdAt;
}
