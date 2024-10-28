package com.fashion.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
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
		name = "user",
		uniqueConstraints = {
				@UniqueConstraint(
						columnNames = {"phone"},
						name = "Phone"
				),
				@UniqueConstraint(
						columnNames = {"email"},
						name = "Email"
				)
		}
)
public class User {
	@Id
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "user_auth_id",
			nullable = false
	)
	private UserAuth userAuth;

	@Column(nullable = false) @Length(
			min = 1,
			max = 200
	) private String name;

	@Email @NotEmpty private String email;

	@Column(
			nullable = false,
			columnDefinition = "text"
	) private String image;

	@Column(nullable = false) @Length(
			min = 10,
			max = 10
	) private String dob;

	@Column(nullable = false) @Length(
			min = 1,
			max = 50
	) private String address;

	@Length(
			min = 10,
			max = 11
	) private String phone;

	@Column(nullable = false) private boolean male;
}
