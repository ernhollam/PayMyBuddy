package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class UserServiceIT {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    private       User       user;
    private       Integer    id;



    @BeforeEach
    void init() {
        user = new User();
        user.setEmail("testIT@mail.com");
        user.setFirstName("Firstname");
        user.setLastName("Lastname");
        user.setPassword("tawfzeklf");

        user = userService.createUser(user);
        id = user.getId();
    }

    @AfterEach
    void reset() {
        userService.deleteUser(user);
    }

    @Test
    @DisplayName("Deposit should add money to user's balance")
    void deposit() {
        String amount = "50";

        userService.deposit(user, amount);

        Optional<User> updatedUser = userService.getUserById(id);
        if (updatedUser.isEmpty()) fail("User was not found.");
        assertThat(updatedUser.get().getBalance()).isEqualTo(new BigDecimal("50.00"));
    }

    @Test
    @DisplayName("Withdraw should subtract money to user's balance")
    void withdraw() {
        String amount = "50";

        userService.withdraw(user, amount);

        Optional<User> updatedUser = userService.getUserById(id);
        if (updatedUser.isEmpty()) fail("User was not found.");
        assertThat(updatedUser.get().getBalance()).isEqualTo(new BigDecimal("-50.00"));
    }
}
