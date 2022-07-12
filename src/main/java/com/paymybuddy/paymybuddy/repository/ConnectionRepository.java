package com.paymybuddy.paymybuddy.repository;

import com.paymybuddy.paymybuddy.model.Connection;
import com.paymybuddy.paymybuddy.model.Transaction;
import com.paymybuddy.paymybuddy.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ConnectionRepository extends CrudRepository<Connection, Integer> {
	/*Set<Connection> findByUserOneId(String email);
	Set<Transaction> findByUserTwoId(User user);*/
}
