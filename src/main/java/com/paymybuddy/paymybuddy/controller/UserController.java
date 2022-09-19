package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.exceptions.BuddyNotFoundException;
import com.paymybuddy.paymybuddy.model.Connection;
import com.paymybuddy.paymybuddy.model.Transaction;
import com.paymybuddy.paymybuddy.model.User;
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


    @PutMapping("/{id}/deposit")
    public void deposit(@PathVariable Integer id, @RequestParam String amount) {
        userService.deposit(getUser(id), amount);
    }


    @PutMapping("/{id}/withdraw")
    public void withdraw(@PathVariable Integer id, @RequestParam String amount) {
        userService.withdraw(getUser(id), amount);
    }

    @PostMapping("/{id}/add-connection")
    @ResponseStatus(HttpStatus.CREATED)
    public Connection addConnection(@PathVariable Integer id, @RequestParam String email) {
        return connectionService.createConnectionBetweenTwoUsers(getUser(id), email);
    }

    @GetMapping("/{id}/connections")
    public List<UserViewModel> getConnections(@PathVariable Integer id) {
        return connectionService.getUserConnections(getUser(id));
    }

    @PostMapping("/{id}/pay")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction payABuddy(@PathVariable Integer id,
                                 @RequestParam String email,
                                 @RequestParam String description,
                                 @RequestParam double amount) {
        if (userService.getUserByEmail(email).isEmpty()) {
            String errorMessage = "The buddy with " +
                                  "email (" + email + ") does not exist.";
            log.error(errorMessage);
            throw new BuddyNotFoundException(errorMessage);
        }
        return transactionService.createTransaction(getUser(id),
                                                    userService.getUserByEmail(email).get(),
                                                    description,
                                                    amount);
    }


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
