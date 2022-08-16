package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.config.SecurityConfiguration;
import com.paymybuddy.paymybuddy.exceptions.EmailAlreadyUsedException;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import({UserService.class, SecurityConfiguration.class})
class UserServiceTest {
    /**
     * Class under test.
     */
    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;
	@MockBean
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Saving a user with unique email should create new user")
    void createUser_shouldCreate_newUser() {
        // GIVEN a new user with unique email
        User testUser = new User(1,
                                 "dummy@mail.com",
                                 "ABCDEF123",
                                 "Jean",
                                 "Valjean",
                                 new BigDecimal("215.64"),
                                 new ArrayList<>(),
                                 new ArrayList<>(),
                                 new ArrayList<>(),
                                 new ArrayList<>());
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.empty());
		when(passwordEncoder.encode(any(String.class)))
				.thenReturn("ABCDEF123");
        // WHEN
        User createdUser = userService.createUser(testUser);
        // THEN
        assertThat(createdUser).isNotNull();
    }


    @Test
    @DisplayName("Saving a user with already existing email should throw exception")
    void createUser_shouldThrow_exception() {
        // GIVEN a new user with non-unique email
        User existingUser = new User();
        existingUser.setEmail("dummy@mail.com");

        User testUser = new User(2,
                                 "dummy@mail.com",
                                 "ABCDEF123",
                                 "Jean",
                                 "Valjean",
                                 new BigDecimal("215.64"),
                                 new ArrayList<>(),
                                 new ArrayList<>(),
                                 new ArrayList<>(),
                                 new ArrayList<>());
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.of(existingUser));
        // THEN
        assertThrows(EmailAlreadyUsedException.class, () -> userService.createUser(testUser));
    }
}