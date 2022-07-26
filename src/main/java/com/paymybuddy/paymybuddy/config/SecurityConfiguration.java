package com.paymybuddy.paymybuddy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("springuser")
				.password(bCryptPasswordEncoder().encode("spring123"))
				.roles("USER")
				.and()
				.withUser("springadmin")
				.password(bCryptPasswordEncoder().encode("admin123"))
				.roles("ADMIN", "USER");
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// Allow users with ADMIN role only to access /admin page
		http
				.authorizeRequests()
				.antMatchers("/admin").hasRole("ADMIN")
				.antMatchers("/user").hasRole("USER")
				.anyRequest().authenticated()
				.and()
				.formLogin();
	}

	@Bean
	public PasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
