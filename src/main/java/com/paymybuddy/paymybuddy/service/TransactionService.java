package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.constants.Fee;
import com.paymybuddy.paymybuddy.exceptions.InsufficientBalanceException;
import com.paymybuddy.paymybuddy.exceptions.InvalidAmountException;
import com.paymybuddy.paymybuddy.exceptions.InvalidPayeeException;
import com.paymybuddy.paymybuddy.model.Transaction;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    ConnectionService     connectionService;
    @Autowired
    Clock                 clock;

    /**
     * Saves a new transaction.
     */

    public Transaction createTransaction(User issuer, User payee, String description, double amount) {
        // Check that amount is not negative nor 0
        if (amount < 0) {
            String errorMessage = "Transaction amount can not be negative.";
            log.error(errorMessage);
            throw new InvalidAmountException(errorMessage);
        } else if (amount == 0) {
            String errorMessage = "Transaction amount must be more than 0.";
            log.error(errorMessage);
            throw new InvalidAmountException(errorMessage);
        }
        // Calculate fee and total amount
        double fee = amount * Fee.TRANSACTION_FEE;
        BigDecimal totalAmount = new BigDecimal(Double.toString(amount + fee)).setScale(Fee.SCALE,
                                                                                        RoundingMode.HALF_UP);
        // Check that issuer has enough money for this transaction
        if (issuer.getBalance().compareTo(totalAmount) < 0) {
            String errorMessage = "Issuer has insufficient balance to make this transfer.";
            log.error(errorMessage);
            throw new InsufficientBalanceException(errorMessage);
        }
        if (connectionService.getUserConnections(issuer).contains(payee)) {
            // Withdraw amount with applied fee from issuer's balance
            issuer.setBalance(issuer.getBalance().subtract(totalAmount));
            // Credit payee
            BigDecimal transactionAmount = new BigDecimal(Double.toString(amount))
                    .setScale(Fee.SCALE, RoundingMode.HALF_UP);
            payee.setBalance(
                    payee.getBalance().add(BigDecimal.valueOf(amount).setScale(Fee.SCALE, RoundingMode.HALF_UP)));
            // Update transaction with all information before saving
            Transaction transaction = new Transaction();
            transaction.setIssuer(issuer);
            transaction.setPayee(payee);
            transaction.setAmount(transactionAmount);
            transaction.setDate(LocalDateTime.now(clock));
            transaction.setDescription(description);

            //TODO ajouter la transaction à la liste des transactions initiées pour l'émetteur
            // TODO ajouter la transaction à la liste des transactions reçues pour le bénéficiaire
            return transactionRepository.save(transaction);
        } else {
            String errorMessage = "The payee is not a buddy from issuer.";
            log.error(errorMessage);
            throw new InvalidPayeeException(errorMessage);
        }

    }

    /**
     * List all transactions.
     *
     * @return a list of all transactions where the user is involved.
     */

    public List<Transaction> getUserTransactions(User user) {
        return transactionRepository.findByIssuerOrPayee(user, user);
    }

    /**
     * Gets a transaction by ID.
     *
     * @return a transaction
     */

    public Optional<Transaction> getTransactionById(Integer id) {
        return transactionRepository.findById(id);
    }

    /**
     * Gets transactions issued by user.
     */

    public List<Transaction> getInitiatedTransactions(User user) {
        return transactionRepository.findByIssuer(user);
    }

    /**
     * Gets transactions received by user.
     */

    public List<Transaction> getReceivedTransactions(User user) {
        return transactionRepository.findByPayee(user);
    }
}

