package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.exceptions.AlreadyABuddyException;
import com.paymybuddy.paymybuddy.exceptions.BuddyNotFoundException;
import com.paymybuddy.paymybuddy.model.Connection;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.ConnectionRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import(ConnectionService.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConnectionServiceTest {
    // configure LocalDateTime.now() to 18th July 2022, 10:00:00
    public final static LocalDateTime LOCAL_DATE_NOW = LocalDateTime.of(2022, 7, 18, 10, 0, 0);

    /**
     * Class under test.
     */
    @Autowired
    ConnectionService connectionService;

    @MockBean
    Clock clock;

    @MockBean
    ConnectionRepository connectionRepository;

    @MockBean
    UserRepository userRepository;

    private User initializer;
    private User receiver;

    @BeforeAll
    public void initUsers() {
        initializer = new User();
        initializer.setId(1);
        initializer.setFirstName("Chandler");
        initializer.setLastName("Bing");
        initializer.setPassword("CouldIBeAnyMoreBored");
        initializer.setEmail("bingchandler@friends.com");

        receiver = new User();
        receiver.setId(2);
        receiver.setFirstName("Joey");
        receiver.setLastName("Tribbiani");
        receiver.setPassword("HowUDoin");
        receiver.setEmail("tribbianijoey@friends.com");
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
    @DisplayName("getUserConnections should return a list of two connections")
    void getUserConnections() {
        // Given three users
        User testUser = new User(3,
                                 "rossgeller@friends.com",
                                 "wewereonabreak",
                                 "Ross",
                                 "Geller",
                                 new BigDecimal("215.64"),
                                 new ArrayList<>(),
                                 new ArrayList<>(),
                                 new ArrayList<>(),
                                 new ArrayList<>());

        Connection connection1 = new Connection(1, testUser, receiver, LocalDateTime.now(clock));
        Connection connection2 = new Connection(2, initializer, testUser, LocalDateTime.now(clock));
        when(connectionRepository.findByInitializerOrReceiver(any(User.class), any(User.class)))
                .thenReturn(List.of(connection1, connection2));

        // WHEN getting connections from testUser
        List<User> userConnections = connectionService.getUserConnections(testUser);

        // THEN testUser should have two connections, one they initiated and one they received
        assertThat(userConnections.contains(receiver)).isTrue();
        assertThat(userConnections.contains(initializer)).isTrue();
    }
    @Test
    @DisplayName("Adding a connection should connect initializer and receiver")
    void addConnection_shouldConnect_initializerAndReceiver() {
        String email = "tribbianijoey@friends.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(receiver));

        connectionService.addConnection(initializer, email);

        // Assert both initializer and receiver have a connection in which they appear as such
        assertThat(initializer.getInitializedConnections()
                              .stream()
                              .filter(connection -> connection.getInitializer().equals(initializer)
                                                    && connection.getReceiver().equals(receiver))).isNotNull();
        assertThat(receiver.getReceivedConnections()
                              .stream()
                              .filter(connection -> connection.getInitializer().equals(initializer)
                                                    && connection.getReceiver().equals(receiver))).isNotNull();
    }
    @Test
    @DisplayName("Adding a connection should add a connection to initializer's list of initiated connections")
    void addConnection_shouldAdd_connectionToInitializedConnections() {
        String email = "tribbianijoey@friends.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(receiver));

        int initiatedConnectionsSizeBefore = initializer.getInitializedConnections().size();

        connectionService.addConnection(initializer, email);

        assertThat(initializer.getInitializedConnections().size()).isEqualTo(initiatedConnectionsSizeBefore + 1);
    }

    @Test
    @DisplayName("Adding a connection should add a connection to receiver's list of received connections")
    void addConnection_shouldAdd_connectionToReceivedConnections() {
        String email = "tribbianijoey@friends.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(receiver));

        int receivedConnectionsSizeBefore = receiver.getReceivedConnections().size();

        connectionService.addConnection(initializer, email);

        assertThat(receiver.getReceivedConnections().size()).isEqualTo(receivedConnectionsSizeBefore + 1);
    }

    @Test
    @DisplayName("Adding a connection with non-existent user should throw an exception")
    void addConnection_shouldThrow_exception() {
        String email = "tribbianijoey@friends.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(BuddyNotFoundException.class, () -> connectionService.addConnection(initializer, email));
    }

    @Test
    @DisplayName("Adding a connection who is already a buddy should throw an exception")
    void addConnection_withConflict_shouldThrow_exception() {
        String email = "tribbianijoey@friends.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(receiver));
        Connection existingConnection = new Connection(null, initializer, receiver, LocalDateTime.now(clock));
        when(connectionRepository
                     .findByInitializerOrReceiver(any(User.class), any(User.class)))
                .thenReturn(List.of(existingConnection));

        assertThrows(AlreadyABuddyException.class, () -> connectionService.addConnection(initializer, email));
    }

}