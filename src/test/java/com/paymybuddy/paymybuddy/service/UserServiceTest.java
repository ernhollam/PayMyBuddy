package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(UserService.class)
class UserServiceTest {
	/**
	 * Class under test.
	 */
	@Autowired
	UserService userService;

	@MockBean UserRepository userRepository;
	@MockBean PasswordEncoder passwordEncoder;

	// TODO cas passant pour la  création d'un utilisateur
	/*@Test
	@DisplayName("Save a user with unique email should save new user")
	void createUser_shouldReturn_newUser() {
		// GIVEN a new user with unique email
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
		when(userRepository.findByEmail(any(String.class)))
				.thenReturn(Optional.empty());
		when(passwordEncoder.encode(any(String.class)))
				.thenReturn("321FEDCBA");
		// WHEN
		userService.createUser(testUser);
		// THEN
		assertTrue(testUser.getPassword().equalsIgnoreCase("321FEDCBA"));
	}*/

	// TODO cas d'inscription avec un email existant
	/*@Test
	@DisplayName("Save a user with duplicate email should throw exception")
	void createUser_shouldThrow_excetion() {
		// GIVEN a new user with unique email
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
		when(userRepository.findByEmail(any(String.class)))
				.thenReturn(Optional.of(testUser));
		// THEN
		assertThrows(EmailAlreadyUsedException.class, () -> userService.createUser(testUser));
	}*/
}