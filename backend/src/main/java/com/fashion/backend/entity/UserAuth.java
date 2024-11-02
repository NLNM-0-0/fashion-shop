package com.fashion.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
		name = "user_auth"
)
public class UserAuth implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Email
	private String email;

	@Length(
			min = 10,
			max = 11
	)
	private String phone;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private boolean isDeleted = false;

	@Column(nullable = false)
	private boolean isVerified = false;

	@Column(nullable = false)
	private boolean isAdmin = false;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		if (isAdmin) {
			authorities.add(new SimpleGrantedAuthority("ADMIN"));
		}
		if (email != null) {
			authorities.add(new SimpleGrantedAuthority("STAFF"));
		} else {
			authorities.add(new SimpleGrantedAuthority("USER"));
		}
		return authorities;
	}

	@Override
	public String getUsername() {
		if (phone != null) {
			return phone;
		}
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
