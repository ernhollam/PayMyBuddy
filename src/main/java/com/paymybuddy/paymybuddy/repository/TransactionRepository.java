package com.paymybuddy.paymybuddy.repository;

import com.paymybuddy.paymybuddy.model.Transaction;
import com.paymybuddy.paymybuddy.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
	List<Transaction> findByIssuerOrPayee(User issuer, User payee);

	@Transactional
	void deleteByIssuerOrPayee(User issuer, User payee);
}
