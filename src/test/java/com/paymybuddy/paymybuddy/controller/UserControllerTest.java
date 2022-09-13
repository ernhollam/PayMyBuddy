package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.model.viewmodel.UserViewModel;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import com.paymybuddy.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @MockBean
    private UserService    userService;
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private User testUser;
    private User otherUser;
    @BeforeEach
    public void initUsers() {
        testUser = new User();
        testUser.setFirstName("Chandler");
        testUser.setLastName("Bing");
        testUser.setPassword("CouldIBeAnyMoreBored");
        testUser.setEmail("bingchandler@friends.com");
        testUser.setBalance(new BigDecimal("2509.56"));

        otherUser = new User();
        otherUser.setFirstName("Joey");
        otherUser.setLastName("Tribbiani");
        otherUser.setPassword("HowUDoin");
        otherUser.setEmail("otheremail@mail.com");

    }

    @Test
    public void createUser_shouldCreate_User() throws Exception {

        String email    = "test@mail.com";
        String password = "rawPassword";

        mockMvc.perform(MockMvcRequestBuilders
                                .post("/user")
                                .param("email", email)
                                .param("password", password)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated());
    }


    @Test
    public void getUsers_shouldReturn_ListOfAllPeople() throws Exception {
        when(userService.getUsers())
                .thenReturn(List.of(new UserViewModel(testUser.getEmail(), testUser.getFirstName(),
                                                      testUser.getLastName(), testUser.getBalance()),
                            new UserViewModel(otherUser.getEmail(), otherUser.getFirstName(),
                                              otherUser.getLastName(), otherUser.getBalance())));
        mockMvc.perform(get("/user"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getUserByEmail() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deposit() {
    }

    @Test
    void withdraw() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void getUser() {
    }
}