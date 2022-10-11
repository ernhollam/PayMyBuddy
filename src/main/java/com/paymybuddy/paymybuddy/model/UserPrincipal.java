package com.paymybuddy.paymybuddy.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * UserPrincipal.
 */
@Getter
public class UserPrincipal implements UserDetails {
	private User user;

	private BigDecimal balance;

	/**
	 * Constructor with args.
	 * @param user loaded user.
	 */
	public UserPrincipal(User user) {
		this.user = user;
	}

	/**
	 * No args constructor.
	 */
	public UserPrincipal() {
	}

	@Override public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override public String getPassword() {
		return user.getPassword();
	}

	@Override public String getUsername() {
		return user.getEmail();
	}

	@Override public boolean isAccountNonExpired() {
		return true;
	}

	@Override public boolean isAccountNonLocked() {
		return true;
	}

	@Override public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override public boolean isEnabled() {
		return true;
	}
}
