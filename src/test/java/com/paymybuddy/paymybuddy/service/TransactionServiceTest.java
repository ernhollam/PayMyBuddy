package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.constants.Fee;
import com.paymybuddy.paymybuddy.exceptions.InsufficientBalanceException;
import com.paymybuddy.paymybuddy.exceptions.InvalidAmountException;
import com.paymybuddy.paymybuddy.exceptions.InvalidPayeeException;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.TransactionRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    // configure LocalDateTime.now() to 18th July 2022, 10:00:00
    public final static LocalDateTime LOCAL_DATE_NOW = LocalDateTime.of(2022, 7, 18, 10, 0, 0);

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

    @BeforeEach
    public void initClock() {
        // Configure a fixed clock to have fixed LocalDate.now()
        Clock fixedClock = Clock.fixed(LOCAL_DATE_NOW.atZone(ZoneId.systemDefault()).toInstant(),
                                       ZoneId.systemDefault());
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());
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

    @Test
    @DisplayName("The payee should be one of issuer's buddies")
    void createTransaction_whenPayee_notInIssuersBuddies() {
        amount = 50;
        when(connectionService.getUserConnections(any(User.class)))
                .thenReturn(Collections.emptyList());
        assertThrows(InvalidPayeeException.class,
                     () -> transactionService.createTransaction(issuer,
                                                                payee,
                                                                "payee not a buddy",
                                                                amount));
    }

    @Test
    @DisplayName("Issuer's balance is withdrawn with correct fee after transaction")
    void createTransaction_shouldUpdate_issuersBalance() {
        amount = 100;
        BigDecimal issuersBalanceBefore = issuer.getBalance();
        double     fee                  = amount * Fee.TRANSACTION_FEE;
        BigDecimal totalAmount = new BigDecimal(Double.toString(amount + fee)).setScale(Fee.SCALE,
                                                                                        RoundingMode.HALF_UP);
        when(connectionService.getUserConnections(issuer))
                .thenReturn(List.of(payee));

        transactionService.createTransaction(issuer,
                                             payee,
                                             "issuer's balance check",
                                             amount);

        assertThat(issuer.getBalance()).isEqualTo(issuersBalanceBefore.subtract(totalAmount));
    }

    @Test
    @DisplayName("Payee's balance is updated after transaction")
    void createTransaction_shouldUpdate_payeesBalance() {
        amount = 100;
        BigDecimal payeesBalanceBefore = payee.getBalance();
        when(connectionService.getUserConnections(issuer))
                .thenReturn(List.of(payee));

        transactionService.createTransaction(issuer,
                                             payee,
                                             "payee's balance check",
                                             amount);

        assertThat(payee.getBalance()).isEqualTo(payeesBalanceBefore.add(new BigDecimal(amount).setScale(Fee.SCALE,
                                                                                                         RoundingMode.HALF_UP)));
    }

    @Test
    @DisplayName("Transaction is registered in both issuer and payee's transaction list.")
    void createTransaction_shouldUpdate_issuerAndPayeesTransactionList() {
        when(connectionService.getUserConnections(issuer))
                .thenReturn(List.of(payee));
        amount = 100;

        transactionService.createTransaction(issuer,
                                             payee,
                                             "transaction added to issuer's and payee's " +
                                             "list of transaction test",
                                             amount);

        assertThat(issuer.getInitiatedTransactions()).isNotNull();
        assertThat(payee.getReceivedTransactions()).isNotNull();
    }

}