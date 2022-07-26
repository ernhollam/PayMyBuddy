package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User service.
 */
@Service
public interface UserService {
	/**
	 * Saves user to database.
	 * @param user User  to save.
	 * @return User with id.
	 */
	User createUser(User user);

	/**
	 * Lists all users in database.
	 * @return a set of users.
	 */
	Iterable<User> getUsers();

	/**
	 * Finds a user by their id.
	 * @param id User id.
	 * @return found user or empty optional.
	 */
	Optional<User> getUserById(Integer id);

	/**
	 * Finds a user by their first and last names.
	 * @param firstName User's first name.
	 * @param lastName User's last name.
	 * @return optional user.
	 */
	Optional<User> getUserByName(String firstName, String lastName);

	/**
	 * Finds a user by their email address.
	 * @param email User's email address.
	 * @return optional user.
	 */
	Optional<User> getUserByEmail(String email);

	/**
	 * Updates a user.
	 * @param user User to update.
	 * @return Updated user.
	 */
	User updateUser(User user);

	/**
	 * Deletes user.
	 * @param user User to delete.
	 */
	void deleteUser(User user);

}
