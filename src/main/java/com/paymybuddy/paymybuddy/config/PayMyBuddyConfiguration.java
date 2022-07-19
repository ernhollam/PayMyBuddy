package com.paymybuddy.paymybuddy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;

@Configuration
public class PayMyBuddyConfiguration {
	/**
	 * Creates a Clock bean to have system's default zone for LocalDate.now().
	 * @return a clock.
	 */
	@Bean
	public Clock clock() {
		return Clock.systemDefaultZone();
	}

	/**
	 * Returns a BCryptPasswordEncoder.
	 * @return a BCryptPasswordEncoder.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
