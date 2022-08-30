package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.constants.EmailValidator;
import com.paymybuddy.paymybuddy.constants.Fee;
import com.paymybuddy.paymybuddy.exceptions.BuddyNotFoundException;
import com.paymybuddy.paymybuddy.exceptions.EmailAlreadyUsedException;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.model.UserPrincipal;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    /**
     * Instance of UserRepository.
     */
    @Autowired
    private final UserRepository userRepository;

    /**
     * Password encoder.
     */
    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * Saves user to database.
     *
     * @param user
     *         User  to save.
     *
     * @return User with id.
     */
    @Transactional
    public User createUser(User user) {
        if (isInvalidEmail(user.getEmail())) {
            String invalidEmailMessage = "The email provided is invalid.";
            log.error(invalidEmailMessage);
            throw new IllegalArgumentException(invalidEmailMessage);
        } else {
            // Detect if email is already used
            Optional<User> existingUserWithEmail = userRepository.findByEmail(user.getEmail());
            if (existingUserWithEmail.isPresent()) {
                String errorMessage = "Email " + user.getEmail() + " is already used." +
                                      "Please sign in with another email.";
                log.error(errorMessage);
                throw new EmailAlreadyUsedException(errorMessage);
            } else {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                user.setBalance(new BigDecimal("0.00"));
                return userRepository.save(user);
            }
        }
    }


    /**
     * Lists all users in database.
     *
     * @return a set of users.
     */
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Finds a user by their id.
     *
     * @param id
     *         User id.
     *
     * @return found user or empty optional.
     */
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    /**
     * Finds a user by their first and last names.
     *
     * @param firstName
     *         User's first name.
     * @param lastName
     *         User's last name.
     *
     * @return optional user.
     */
    public Optional<User> getUserByName(String firstName, String lastName) {
        return userRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    /**
     * Finds a user by their email address.
     *
     * @param email
     *         User's email address.
     *
     * @return optional user.
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Updates a user.
     *
     * @param user
     *         User to update.
     *
     * @return Updated user.
     */
    public User updateUser(User user) {
        if (isInvalidEmail(user.getEmail())) {
            String invalidEmailMessage = "The email provided is invalid.";
            log.error(invalidEmailMessage);
            throw new IllegalArgumentException(invalidEmailMessage);
        } else {
            Optional<User> userToUpdate = userRepository.findByEmail(user.getEmail());
            if (userToUpdate.isEmpty()) {
                String errorMessage = "The user you are trying to update does not exist.";
                log.error(errorMessage);
                throw new BuddyNotFoundException(errorMessage);
            } else {
                return userRepository.save(user);
            }
        }
    }

    /**
     * Deletes user.
     *
     * @param user
     *         User to delete.
     */
    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    /**
     * Email validator.
     *
     * @param emailAddress
     *         email address to validate
     *
     * @return true if valid, false otherwise.
     */
    public static boolean isInvalidEmail(String emailAddress) {
        return !Pattern.compile(EmailValidator.REGEX_PATTERN)
                       .matcher(emailAddress)
                       .matches();
    }

    /**
     * Deposits money to buddy account.
     */
    public void deposit(User user, String amount) {
        // if amount is still not valid after interface's validator, remove any negative signs
        amount = amount.replace("-", "");

        user.setBalance(user.getBalance().add(new BigDecimal(amount).setScale(Fee.SCALE, RoundingMode.HALF_UP)));
    }

    /**
     * Withdraws money to user's bank account.
     */
    public void withdraw(User user, String amount) {
        // if amount is still not valid after interface's validator, remove any negative signs
        amount = amount.replace("-", "");

        user.setBalance(user.getBalance().subtract(new BigDecimal(amount).setScale(Fee.SCALE, RoundingMode.HALF_UP)));
    }

    /**
     * Uses email as username for sign up.
     * @param username email
     * @return a UserPrincipal object
     * @throws UsernameNotFoundException when email not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username);
        if (user.isEmpty()) throw new BuddyNotFoundException("No buddy found for email: " + username + ".");
        return new UserPrincipal(user.get());
    }
}
