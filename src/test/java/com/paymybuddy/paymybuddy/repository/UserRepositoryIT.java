package com.paymybuddy.paymybuddy.repository;

import com.paymybuddy.paymybuddy.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
//@EnableJpaRepositories(basePackageClasses = UserRepository.class)
//@EntityScan(basePackageClasses = User.class)
class UserRepositoryIT {
    @Autowired
    UserRepository userRepository;

    @Test
    void createUser_savesNewUser() {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("UN");
        user.setEmail("abc@mail.com");
        user.setBalance(new BigDecimal(0));

        user = userRepository.save(user);

        assertThat(user.getId()).isNotNull();

    }
}