package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.model.Transaction;
import com.paymybuddy.paymybuddy.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TransactionService {
	Transaction createTransaction(User issuer, User payee, String description, double amount);

	List<Transaction> getUserTransactions(User user);

	Optional<Transaction> getTransactionById(Integer id);

	List<Transaction> getInitiatedTransactions(User user);

	List<Transaction> getReceivedTransactions(User user);
}
