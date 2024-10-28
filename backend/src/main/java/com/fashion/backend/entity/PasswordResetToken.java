package com.fashion.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
		name = "password_reset_token"
)
public class PasswordResetToken {
	private static final int EXPIRATION_MINUTE = 5;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String token;

	@ManyToOne(
			targetEntity = UserAuth.class,
			fetch = FetchType.EAGER
	)
	@JoinColumn(
			name = "user_id",
			nullable = false
	)
	private UserAuth user;

	private Date expiryDate;

	public PasswordResetToken(String token, UserAuth user) {
		this.token      = token;
		this.user       = user;
		this.expiryDate = getTokenExpirationTime();
	}

	private Date getTokenExpirationTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		calendar.add(Calendar.MINUTE, EXPIRATION_MINUTE);
		return new Date(calendar.getTime().getTime());
	}

	public boolean isValid() {
		Calendar calendar = Calendar.getInstance();
		return (this.getTokenExpirationTime().getTime() - calendar.getTime().getTime()) > 0;
	}
}
