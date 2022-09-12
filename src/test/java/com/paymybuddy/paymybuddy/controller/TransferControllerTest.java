package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.service.ConnectionService;
import com.paymybuddy.paymybuddy.service.TransactionService;
import com.paymybuddy.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.paymybuddy.paymybuddy.config.UrlConfig.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransferControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    TransactionService transactionService;
    @MockBean
    ConnectionService connectionService;
    @MockBean
    UserService       userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Get(\"/transfer\") should show transfer page")
    void showTransferPage() throws Exception {
        mockMvc.perform(get(TRANSFER))
               .andDo(print())
               .andExpect(status().isFound());
    }

    @Test
    @DisplayName("Get(\"/transfer/pay\") should show pay form")
    void showPayForm() throws Exception {
        mockMvc.perform(get(TRANSFER + PAY))
               .andDo(print())
               .andExpect(status().isFound());
    }

    @Test
    @DisplayName("Get(\"/transfer/add-connection\") should show pay form")
    void showAddConnectionForm() throws Exception {
        mockMvc.perform(get(TRANSFER + ADD_CONNECTION))
               .andDo(print())
               .andExpect(status().isFound());
    }

    @Test
    void addConnection() {
    }

}