package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.exceptions.AlreadyABuddyException;
import com.paymybuddy.paymybuddy.exceptions.BuddyNotFoundException;
import com.paymybuddy.paymybuddy.model.Connection;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.ConnectionRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ConnectionService {

    @Autowired
    ConnectionRepository connectionRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    Clock          clock;

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
    public Connection addConnection(User initializer, String email) {
        if (UserService.isInvalidEmail(email)) {
            String invalidEmailMessage = "The email provided is invalid.";
            log.error(invalidEmailMessage);
            throw new IllegalArgumentException(invalidEmailMessage);
        }

        Optional<User> optionalReceiver = userRepository.findByEmail(email);
        if (optionalReceiver.isEmpty()) {
            // Check if a user with specified email exists
            String errorMessage = "Email " + email + " does not match any buddy.";
            log.error(errorMessage);
            throw new BuddyNotFoundException(errorMessage);
        }
        User receiver = optionalReceiver.get();
        if (getUserConnections(initializer).contains(receiver)) {
            String errorMessage = receiver.getFirstName() + " " + receiver.getLastName() + " is already a Buddy " +
                                  "of yours!.";
            log.error(errorMessage);
            throw new AlreadyABuddyException(errorMessage);
        } else {
            // Create connection with both users					;
            return saveConnection(createConnection(initializer, receiver));
        }

    }

    protected Connection createConnection(User initializer, User receiver) {
        Connection connection = new Connection();
        connection.setInitializer(initializer);
        connection.setReceiver(receiver);
        connection.setStartingDate(LocalDateTime.now(clock));
        // Add connection to initializer's initiatedConnections
        initializer.getInitializedConnections().add(connection);
        // Add connection to receiver's receivedConnections
        receiver.getReceivedConnections().add(connection);
        return connection;
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
