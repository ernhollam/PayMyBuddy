package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.exceptions.InsufficientBalanceException;
import com.paymybuddy.paymybuddy.exceptions.InvalidAmountException;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Clock;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@Import(TransactionService.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionServiceTest {
    /**
     * Class under test.
     */
    @Autowired
    TransactionService transactionService;

    @MockBean
    TransactionRepository transactionRepository;

    @MockBean
    ConnectionService connectionService;

    @MockBean
    Clock clock;

    private User   issuer;
    private User   payee;
    private String description;
    private double amount;

    @BeforeAll
    void setUp() {
        issuer = new User();
        issuer.setFirstName("Chandler");
        issuer.setLastName("Bing");
        issuer.setPassword("CouldIBeAnyMoreBored");
        issuer.setEmail("bingchandler@friends.com");
        issuer.setBalance(new BigDecimal(500));

        payee = new User();
        payee.setFirstName("Joey");
        payee.setLastName("Tribbiani");
        payee.setPassword("HowUDoin");
        payee.setEmail("tribbianijoey@friends.com");
        payee.setBalance(new BigDecimal(0));
    }

    @Test
    @DisplayName("Transaction with negative amount should throw exception")
    void createTransaction_whenAmount_isNegative() {
        amount = -50;
        assertThrows(InvalidAmountException.class,
                     () -> transactionService.createTransaction(issuer,
                                                                payee,
                                                                "amount negative",
                                                                amount));
    }

    @Test
    @DisplayName("Transaction with amount equal to zero should throw exception")
    void createTransaction_whenAmount_isZero() {
        amount = 0;
        assertThrows(InvalidAmountException.class,
                     () -> transactionService.createTransaction(issuer,
                                                                payee,
                                                                "amount negative",
                                                                amount));
    }

    @Test
    @DisplayName("Exception should be thrown when issuer does not have sufficient balance")
    void createTransaction_whenIssuer_isPoor() {
        amount = 1000;
        assertThrows(InsufficientBalanceException.class,
                     () -> transactionService.createTransaction(issuer,
                                                                payee,
                                                                "amount negative",
                                                                amount));
    }

    // TODO le bénéficiaire est bien dans la liste des connexions de l'émetteur
    // TODO l'émetteur est bien débité (solde)
    // TODO le bénéficiaire est bien crédité (solde)
    // TODO la transaction est bien ajoutée dans la liste des transactions des deux utilisateurs concernés

}