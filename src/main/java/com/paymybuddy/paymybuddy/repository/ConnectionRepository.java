package com.paymybuddy.paymybuddy.repository;

import com.paymybuddy.paymybuddy.model.Connection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRepository extends CrudRepository<Connection, Integer> {
	List<Connection> findByInitializerIdOrReceiverId(Integer initializerId, Integer receiverId);
}
