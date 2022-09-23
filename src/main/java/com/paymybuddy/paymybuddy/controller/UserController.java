package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.exceptions.BuddyNotFoundException;
import com.paymybuddy.paymybuddy.model.Transaction;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.model.viewmodel.ConnectionViewModel;
import com.paymybuddy.paymybuddy.model.viewmodel.UserViewModel;
import com.paymybuddy.paymybuddy.service.ConnectionService;
import com.paymybuddy.paymybuddy.service.TransactionService;
import com.paymybuddy.paymybuddy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ConnectionService  connectionService;
    @Autowired
    private TransactionService transactionService;

    /**
     * Add new user.
     *
     * @param email
     *         user email
     * @param password
     *         user password
     *
     * @return User saved or exception if email already used
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestParam String email, @RequestParam String password) {
        return userService.createUser(email, password);
    }

    /**
     * Lists all users.
     *
     * @return List of all users.
     */
    @GetMapping
    public List<UserViewModel> getUsers() {
        return userService.getUsers();
    }


    /**
     * Gets user by email
     *
     * @param id
     *         email of user to find
     *
     * @return optional user
     */
    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable(name = "id") Integer id) {
        return userService.getUserById(id);
    }


    /**
     * Deposits money to user account.
     *
     * @param id
     *         id of user to deposit money to
     * @param amount
     *         amount to deposit
     */
    @PutMapping("/{id}/deposit")
    public void deposit(@PathVariable Integer id, @RequestParam String amount) {
        userService.deposit(getUser(id), amount);
    }

    /**
     * Withdraws money to user account.
     *
     * @param id
     *         id of user to withdraw money from
     * @param amount
     *         amount to withdraw
     */
    @PutMapping("/{id}/withdraw")
    public void withdraw(@PathVariable Integer id, @RequestParam String amount) {
        userService.withdraw(getUser(id), amount);
    }

    /**
     * Adds a connection to a user
     *
     * @param id
     *         user initiating the connection
     * @param email
     *         email of buddy to add
     *
     * @return new connection object
     */
    @PostMapping("/add-connection")
    @ResponseStatus(HttpStatus.CREATED)
    public ConnectionViewModel addConnection(@PathVariable Integer id, @RequestParam String email) {
        return ConnectionService.connectionToViewModel(connectionService.createConnectionBetweenTwoUsers(getUser(id),
                                                                                                         email));
    }

    /**
     * Get user connections.
     *
     * @param id
     *         user for which the connections are wanted
     *
     * @return a list of users
     */
    @GetMapping("/{id}/connections")
    public List<UserViewModel> getConnections(@PathVariable Integer id) {
        return connectionService.getUserConnections(getUser(id));
    }

    /**
     * Creates a transaction involving the user and the buddy behind the specified email.
     *
     * @param email
     *         transaction receiver email
     * @param description
     *         short description for transaction
     * @param amount
     *         amount of transaction
     *
     * @return a transaction object
     */
    @PostMapping("/pay")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction payABuddy(@RequestParam String email,
                                 @RequestParam String description,
                                 @RequestParam double amount) {
        if (userService.getUserByEmail(email).isEmpty()) {
            String errorMessage = "The buddy with " +
                                  "email (" + email + ") does not exist.";
            log.error(errorMessage);
            throw new BuddyNotFoundException(errorMessage);
        }
        return transactionService.createTransaction(userService.getCurrentUser(),
                                                    userService.getUserByEmail(email).get(),
                                                    description,
                                                    amount);
    }


    /**
     * Useful function to get a User object thanks to an ID
     *
     * @param id
     *         id of user to return
     *
     * @return a User object
     */
    protected User getUser(Integer id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            String errorMessage = "User with ID " + id + " does not exist.";
            log.error(errorMessage);
            throw new BuddyNotFoundException(errorMessage);
        }
        return user.get();
    }

}
