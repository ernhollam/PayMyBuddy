package com.paymybuddy.paymybuddy.repository;

import com.paymybuddy.paymybuddy.model.Connection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ConnectionRepository extends CrudRepository<Connection, Integer> {
	Set<Connection> findByInitializerIdOrReceiverId(Long initializerId, Long receiverId);
}
