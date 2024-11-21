package com.fashion.backend.entity;

import jakarta.persistence.*;
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
		name = "like_item",
		uniqueConstraints = {@UniqueConstraint(
				columnNames = {"user_id", "item_id"},
				name = "Item in liked list"
		)}
)
@EntityListeners(AuditingEntityListener.class)
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

	@CreatedDate
	@Column(name = "createdAt")
	private Date createdAt;
}
