package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.exceptions.BuddyNotFoundException;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.ConnectionRepository;
import com.paymybuddy.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import com.paymybuddy.paymybuddy.service.ConnectionService;
import com.paymybuddy.paymybuddy.service.TransactionService;
import com.paymybuddy.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @MockBean
    private UserService          userService;
    @MockBean
    private UserRepository       userRepository;
    @MockBean
    private ConnectionService    connectionService;
    @MockBean
    private ConnectionRepository connectionRepository;
    @MockBean
    private TransactionService   transactionService;
    @MockBean
    TransactionRepository transactionRepository;

    @Autowired
    private MockMvc mockMvc;

    private       User    testUser;
    private       User    otherUser;
    private final Integer id = 150;

    // configure LocalDateTime.now() to 18th July 2022, 10:00:00
    public final static LocalDateTime LOCAL_DATE_NOW = LocalDateTime.of(2022, 7, 18, 10, 0, 0);
    @MockBean
    Clock clock;

    @BeforeEach
    public void initUsers() {
        testUser = new User();
        testUser.setId(id);
        testUser.setFirstName("Chandler");
        testUser.setLastName("Bing");
        testUser.setPassword("CouldIBeAnyMoreBored");
        testUser.setEmail("bingchandler@friends.com");
        testUser.setBalance(new BigDecimal("2509.56"));

        otherUser = new User();
        otherUser.setId(15);
        otherUser.setFirstName("Joey");
        otherUser.setLastName("Tribbiani");
        otherUser.setPassword("HowUDoin");
        otherUser.setEmail("otheremail@mail.com");


        // Configure a fixed clock to have fixed LocalDate.now()
        Clock fixedClock = Clock.fixed(LOCAL_DATE_NOW.atZone(ZoneId.systemDefault()).toInstant(),
                                       ZoneId.systemDefault());
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());
    }

    @Test
    public void createUser_shouldCreate_User() throws Exception {

        String email    = "test@mail.com";
        String password = "rawPassword";

        mockMvc.perform(post("/user")
                                .param("email", email)
                                .param("password", password)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated());
    }


    @Test
    public void getUsers_shouldReturn_ListOfAllPeople() throws Exception {
        when(userService.getUsers())
                .thenReturn(List.of(UserService.userToViewModel(testUser),
                                    UserService.userToViewModel(otherUser)));
        mockMvc.perform(get("/user"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)));
    }


    @Test
    void getUserById() throws Exception {
        when(userService.getUserById(id))
                .thenReturn(Optional.ofNullable(testUser));
        mockMvc.perform(get("/user/" + id))
               .andDo(print())
               .andExpect(status().isOk());
    }


    @Test
    void deposit() throws Exception {
        when(userService.getUserById(id)).thenReturn(Optional.of(testUser));
        mockMvc.perform(put("/user/" + id + "/deposit")
                                .param("amount", "50.00"))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    void withdraw() throws Exception {
        when(userService.getUserById(id)).thenReturn(Optional.of(testUser));
        mockMvc.perform(put("/user/" + id + "/withdraw")
                                .param("amount", "50.00"))
               .andDo(print())
               .andExpect(status().isOk());
    }


    @Test
    void addConnection() throws Exception {
        when(userService.getUserById(id)).thenReturn(Optional.of(testUser));
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(testUser));

        mockMvc.perform(post("/user/" + id + "/add-connection")
                                .param("email", otherUser.getEmail()))
               .andDo(print())
               .andExpect(status().isCreated());
    }

    @Test
    void getConnections() throws Exception {
        when(userService.getUserById(id))
                .thenReturn(Optional.ofNullable(testUser));
        mockMvc.perform(get("/user/" + id + "/connections"))
               .andDo(print())
               .andExpect(status().isOk());
    }
    @Test
    void getTransactions() throws Exception {
        when(userService.getUserById(id))
                .thenReturn(Optional.ofNullable(testUser));
        when(transactionRepository
                     .findByIssuerOrPayee(testUser, testUser)).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/user/" + id + "/transactions"))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    void payABuddy() throws Exception {
        when(userService.getUserById(id)).thenReturn(Optional.of(testUser));
        when(userService.getUserByEmail(otherUser.getEmail())).thenReturn(Optional.of(otherUser));
        when(connectionService.getUserConnections(testUser)).thenReturn(List.of(UserService.userToViewModel(otherUser)));

        mockMvc.perform(post("/user/" + id + "/pay")
                                .param("email", otherUser.getEmail())
                                .param("description", "Pay a buddy test")
                                .param("amount", "8.93"))
               .andDo(print())
               .andExpect(status().isCreated());
    }

    @Test
    void payABuddy_shouldThrow_exception() throws Exception {
        when(userService.getUserById(id)).thenReturn(Optional.of(testUser));
        when(userService.getUserByEmail(otherUser.getEmail())).thenReturn(Optional.empty());
        when(connectionService.getUserConnections(testUser)).thenReturn(List.of(UserService.userToViewModel(otherUser)));

        mockMvc.perform(post("/user/" + id + "/pay")
                                .param("email", otherUser.getEmail())
                                .param("description", "Pay a buddy test")
                                .param("amount", "8.93"))
               .andDo(print())
               .andExpect(status().isNotFound())
               .andExpect(result -> assertTrue(result.getResolvedException() instanceof BuddyNotFoundException));
    }
}