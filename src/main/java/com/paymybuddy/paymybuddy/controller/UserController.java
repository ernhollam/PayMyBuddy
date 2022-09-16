package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.exceptions.BuddyNotFoundException;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.model.viewmodel.UserViewModel;
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



    protected User getUser(Integer id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            String errorMessage = "User with ID " + id + "does not exist.";
            log.error(errorMessage);
            throw new BuddyNotFoundException(errorMessage);
        }
        return user.get();
    }

}
