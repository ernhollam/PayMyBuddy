package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.model.Connection;
import com.paymybuddy.paymybuddy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class DBConnectionServiceTest {
	// configure LocalDateTime.now() to 18th July 2022, 10:00:00
	public final static LocalDateTime LOCAL_DATE_NOW = LocalDateTime.of(2022, 7, 18, 10, 0, 0);

	@Autowired
	DBConnectionService connectionService;

	@MockBean
	Clock clock;

	@BeforeEach
	public void init() {
		// Configure a fixed clock to have fixed LocalDate.now()
		Clock fixedClock = Clock.fixed(LOCAL_DATE_NOW.atZone(ZoneId.systemDefault()).toInstant(),
				ZoneId.systemDefault());
		when(clock.instant()).thenReturn(fixedClock.instant());
		when(clock.getZone()).thenReturn(fixedClock.getZone());
	}

	@Test
	void getUserConnections() {
		// Given three users
		User testUser = new User(1,
				"dummy@mail.com",
				"ABCDEF123",
				"Jean",
				"Valjean",
				new BigDecimal("215.64"),
				new ArrayList<>(),
				new ArrayList<>(),
				new ArrayList<>(),
				new ArrayList<>());

		User initializer = new User(2,
				"abc@email.com",
				"111444555",
				"Victor",
				"Hugo",
				new BigDecimal("32.19"),
				new ArrayList<>(),
				new ArrayList<>(),
				new ArrayList<>(),
				new ArrayList<>());

		User receiver = new User(3,
				"abc@email.com",
				"111444555",
				"Victor",
				"Hugo",
				new BigDecimal("-140.19"),
				new ArrayList<>(),
				new ArrayList<>(),
				new ArrayList<>(),
				new ArrayList<>());

		Connection connection1 = new Connection(1, testUser, receiver, LocalDateTime.now(clock));
		Connection connection2 = new Connection(2, initializer, testUser, LocalDateTime.now(clock));

		// WHEN getting connections from testUser
		List<User> userConnections = connectionService.getUserConnections(testUser);

		// THEN testUser should have two connections, one they initiated and one they received
		assertThat(userConnections.contains(receiver)).isTrue();
		assertThat(userConnections.contains(initializer)).isTrue();
	}

}