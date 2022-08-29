package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.exceptions.BuddyNotFoundException;
import com.paymybuddy.paymybuddy.exceptions.EmailAlreadyUsedException;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import(UserService.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {
    /**
     * Class under test.
     */
    @Autowired
    UserService userService;

    @MockBean
    UserRepository        userRepository;
    @MockBean
    BCryptPasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    public void initUsers() {
        testUser = new User();
        testUser.setFirstName("Chandler");
        testUser.setLastName("Bing");
        testUser.setPassword("CouldIBeAnyMoreBored");
        testUser.setEmail("bingchandler@friends.com");
        testUser.setBalance(new BigDecimal("2509.56"));
    }

    @Test
    @DisplayName("Saving a user with unique email should create new user")
    void createUser_shouldCreate_newUser() {
        // GIVEN a new user with unique email
        doReturn(Optional.empty())
                .when(userRepository).findByEmail(any(String.class));
        when(passwordEncoder.encode(any(String.class)))
                .thenReturn("ABCDEF123");
        doReturn(testUser)
                .when(userRepository).save(any(User.class));
        // WHEN
        userService.createUser(testUser);
        // THEN
        // TODO confirmer lors de la session
        // asserting that created user is not null does not work, thus we check if the balance was actually set to
        // 0.00 during user creation
        verify(userRepository, times(1)).save(testUser);
        assertThat(testUser.getBalance()).isEqualTo(new BigDecimal("0.00"));
        assertThat(testUser).isNotNull();
    }

    @Test
    @DisplayName("Updating a non-existing user should throw an exception")
    void updateUser_shouldThrowException_whenEmailNotFound() {
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.empty());
        // THEN
        assertThrows(BuddyNotFoundException.class, () -> userService.updateUser(testUser));
    }

    @Test
    @DisplayName("Updating user's lastname should update last name")
    void updateUser() {
        doReturn(Optional.of(testUser))
                .when(userRepository).findByEmail(any(String.class));
        String lastNameAfter = "Bing-Geller";
        testUser.setLastName(lastNameAfter);
        doReturn(testUser)
                .when(userRepository).save(testUser);

        testUser = userService.updateUser(testUser);
        // THEN
        assertTrue(testUser.getLastName().equalsIgnoreCase(lastNameAfter));
    }

    @Test
    @DisplayName("Saving a user with already existing email should throw exception")
    void createUser_shouldThrow_exception() {
        // GIVEN a new user with ready used email
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.of(testUser));
        // THEN
        assertThrows(EmailAlreadyUsedException.class, () -> userService.createUser(testUser));
    }

    @Test
    @DisplayName("Deposit should add amount to user's balance")
    void deposit_shouldAdd_amount() {
        String amount = "490.44";
        userService.deposit(testUser, amount);
        assertThat(testUser.getBalance()).isEqualTo(new BigDecimal("3000.00"));
    }

    @Test
    @DisplayName("Deposit should replace any \"-\" in amount ")
    void deposit_shouldReplaceSign() {
        String amount = "-490.44";
        userService.deposit(testUser, amount);
        assertThat(testUser.getBalance()).isEqualTo(new BigDecimal("3000.00"));
    }

    @Test
    @DisplayName("Withdrawal should withdraw money from user's account")
    void withdraw_shouldWithdraw_amount() {
        String amount = "509.56";
        userService.withdraw(testUser, amount);
        assertThat(testUser.getBalance()).isEqualTo("2000.00");
    }

    @Test
    @DisplayName("Withdrawal should replace any \"-\" in amount ")
    void withdraw_shouldReplaceSign() {
        String amount = "-509.56";
        userService.withdraw(testUser, amount);
        assertThat(testUser.getBalance()).isEqualTo(new BigDecimal("2000.00"));
    }


}