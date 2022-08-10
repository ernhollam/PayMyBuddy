package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.model.Connection;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.ConnectionRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ConnectionService {

	@Autowired
	ConnectionRepository connectionRepository;

	@Autowired
	UserRepository userRepository;

	public List<User> getUserConnections(User user) {
		Integer    userId      = user.getId();
		List<User> connections = new ArrayList<>();
		// Get all connections where user is involved
		List<Connection> connectionsWhereUserIsInvolved = connectionRepository
				.findByInitializerOrReceiver(user, user);
		log.debug("Found connections involving " + user.getEmail() + ":\n" + connectionsWhereUserIsInvolved);
		// If user was initializer, then add the receiver to list and vice versa
		for (Connection connection : connectionsWhereUserIsInvolved) {
			User initializer = connection.getInitializer();
			User receiver    = connection.getReceiver();
			if (userId.equals(initializer.getId())) {
				connections.add(receiver);
			} else if (userId.equals(receiver.getId())) {
				connections.add(initializer);
			}
		}
		log.info("Connections for " + user.getEmail() + ":\n" + connections);
		return connections;
	}

	@Transactional
	public Connection saveConnection(Connection connection) {
		return connectionRepository.save(connection);
	}

	@Transactional
	public void deleteInitiatedConnections(User user) {
		connectionRepository.deleteByInitializer(user);
	}

	@Transactional
	public void deleteReceivedConnections(User user) {
		connectionRepository.deleteByReceiver(user);
	}

}
