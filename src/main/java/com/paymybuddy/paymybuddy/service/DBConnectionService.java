package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.model.Connection;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.ConnectionRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DBConnectionService implements ConnectionService {

	@Autowired
	ConnectionRepository connectionRepository;

	@Autowired
	UserRepository userRepository;

	public List<User> getUserConnections(User user) {
		Integer   userId      = user.getId();
		List<User> connections = new ArrayList<>();
		// Get all connections where user appears
		List<Connection> connectionsWhereUserIsInvolved = connectionRepository
				.findByInitializerIdOrReceiverId(userId, userId);

		for (Connection connection : connectionsWhereUserIsInvolved) {
			User initializer = connection.getInitializer();
			User receiver    = connection.getReceiver();
			// If user was initializer, then add the receiver and vice versa
			if (userId.equals(initializer.getId())) {
				connections.add(receiver);
			} else if (userId.equals(receiver.getId())) {
				connections.add(initializer);
			}
		}
		return connections;
	}
}
