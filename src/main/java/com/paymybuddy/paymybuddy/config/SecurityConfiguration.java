package com.paymybuddy.paymybuddy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.paymybuddy.paymybuddy.config.UrlConfig.*;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * Spring Security needs to have a PasswordEncoder defined.
     *
     * @return PasswordEncoder that uses the BCrypt
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Allow users with ADMIN role only to access /admin page
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage(LOGIN)
                .usernameParameter("email")
                .defaultSuccessUrl(HOME)
                .and()
            .rememberMe()
                .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT))
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)        // set invalidation state when logout
                .deleteCookies("JSESSIONID")
                .and()
            .exceptionHandling()
                .accessDeniedPage(ACCESS_DENIED);
    }

}
