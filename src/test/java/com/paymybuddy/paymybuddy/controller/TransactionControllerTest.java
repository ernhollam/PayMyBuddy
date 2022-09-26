package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.model.Transaction;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.service.TransactionService;
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

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {
    // configure LocalDateTime.now() to 18th July 2022, 10:00:00
    private final static LocalDateTime LOCAL_DATE_NOW = LocalDateTime.of(2022, 7, 18, 10, 0, 0);
    private final        Transaction    transaction     = new Transaction();
    private final        Integer       id             = 1;
    @MockBean
    Clock             clock;
    @Autowired
    MockMvc           mockMvc;
    @MockBean
    TransactionService transactionService;
    private final User testUser = new User();

    @BeforeEach
    void setUp() {
        // Use same test users as in UserControllerTest
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

        transaction.setId(id);
        transaction.setIssuer(testUser);
        transaction.setPayee(otherUser);
        transaction.setDate(LOCAL_DATE_NOW);
        transaction.setAmount(new BigDecimal("20.00"));
        transaction.setDescription("Transaction controller test");
    }

    @Test
    void getTransactions() throws Exception {
        when(transactionService.getUserTransactions(testUser.getId()))
                .thenReturn(List.of(TransactionService.transactionToViewModel(transaction)));
        mockMvc.perform(get("/transaction"))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    void getTransactionById() throws Exception {
        when(transactionService.getTransactionById(id))
                .thenReturn(Optional.of(TransactionService.transactionToViewModel(transaction)));
        mockMvc.perform(get("/transaction/" + id))
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    void getTransactionById_shouldReturn_emptyOptional() throws Exception {
        when(transactionService.getTransactionById(id))
                .thenReturn(Optional.empty());
        mockMvc.perform(get("/transaction/" + id))
               .andDo(print())
               .andExpect(status().isOk());
    }
}