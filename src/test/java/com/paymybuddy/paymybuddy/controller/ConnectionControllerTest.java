package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.model.Connection;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.service.ConnectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConnectionController.class)
class ConnectionControllerTest {
    // configure LocalDateTime.now() to 18th July 2022, 10:00:00
    private final static LocalDateTime LOCAL_DATE_NOW = LocalDateTime.of(2022, 7, 18, 10, 0, 0);
    private final        Connection    connection     = new Connection();
    private final        Integer       id             = 1;
    @MockBean
    Clock             clock;
    @Autowired
    MockMvc           mockMvc;
    @MockBean
    ConnectionService connectionService;

    @BeforeEach
    void setUp() {
        // Use same test users as in UserControllerTest
        User testUser = new User();
        testUser.setId(id);
        testUser.setFirstName("Chandler");
        testUser.setLastName("Bing");
        testUser.setPassword("CouldIBeAnyMoreBored");
        testUser.setEmail("bingchandler@friends.com");
        testUser.setBalance(new BigDecimal("2509.56"));

        User otherUser = new User();
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

        connection.setId(id);
        connection.setInitializer(testUser);
        connection.setReceiver(otherUser);
        connection.setStartingDate(LOCAL_DATE_NOW);
    }

    @Test
    void getConnections() throws Exception {
        when(connectionService.getConnections())
                .thenReturn(List.of(ConnectionService.connectionToViewModel(connection)));
        mockMvc.perform(get("/connection"))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    void getConnectionById() throws Exception {
        when(connectionService.getConnectionById(id))
                .thenReturn(Optional.of(ConnectionService.connectionToViewModel(connection)));
        mockMvc.perform(get("/connection/" + id))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    void getConnectionById_shouldReturn_emptyOptional() throws Exception {
        when(connectionService.getConnectionById(id))
                .thenReturn(Optional.empty());
        mockMvc.perform(get("/connection/" + id))
               .andDo(print())
               .andExpect(status().isOk());
    }
}