package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class DBUserService implements UserService {

	/**
	 * Instance of UserRepository.
	 */
	@Autowired
	private final UserRepository userRepository;

	/**
	 * Password encoder.
	 */
	@Autowired
	private final PasswordEncoder passwordEncoder;

	public DBUserService(UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Saves user to database.
	 *
	 * @param user User  to save.
	 * @return User with id.
	 */
	@Override public User createUser(User user) {
		String encryptedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encryptedPassword);
		user.setBalance(BigDecimal.ZERO);
		return userRepository.save(user);
	}

	/**
	 * List all users in database.
	 *
	 * @return a set of users.
	 */
	@Override public Iterable<User> getUsers() {
		return userRepository.findAll();
	}

	/**
	 * Finds a user by their id.
	 *
	 * @param id User id.
	 * @return found user or empty optional.
	 */
	@Override public Optional<User> getUserById(Integer id) {
		return userRepository.findById(id);
	}

	/**
	 * Finds a user by their first and last names.
	 *
	 * @param firstName User's first name.
	 * @param lastName  User's last name.
	 * @return optional user.
	 */
	@Override public Optional<User> getUserByName(String firstName, String lastName) {
		return userRepository.findByFirstNameAndLastName(firstName, lastName);
	}

	/**
	 * Finds a user by their email address.
	 *
	 * @param email User's email address.
	 * @return optional user.
	 */
	@Override public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	/**
	 * Updates a user.
	 *
	 * @param user User to update.
	 * @return Updated user.
	 */
	@Override public User updateUser(User user) {
		return userRepository.save(user);
	}

	/**
	 * Deletes user.
	 *
	 * @param user User to delete.
	 */
	@Override public void deleteUser(User user) {
		userRepository.delete(user);
	}

}
