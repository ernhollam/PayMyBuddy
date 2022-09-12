package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.exceptions.AlreadyABuddyException;
import com.paymybuddy.paymybuddy.exceptions.BuddyNotFoundException;
import com.paymybuddy.paymybuddy.model.Transaction;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.service.ConnectionService;
import com.paymybuddy.paymybuddy.service.TransactionService;
import com.paymybuddy.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static com.paymybuddy.paymybuddy.config.UrlConfig.*;

@Controller
@RequestMapping(TRANSFER)
public class TransferController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ConnectionService  connectionService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String showTransferPage(Model model, HttpSession httpSession) {
        int            userId = (int) httpSession.getAttribute("userId");
        Optional<User> user   = userService.getUserById(userId);

        if (user.isEmpty()) return LOGIN;

        model.addAttribute("transactions", transactionService.getUserTransactions(user.get()));
        model.addAttribute("connections", connectionService.getUserConnections(user.get()));
        model.addAttribute("initializer", user.get());

        return "transfer";
    }

    @GetMapping(PAY)
    public String showPayForm(Model model) {
        return "pay-form";
    }

    @PostMapping(PAY)
    public String pay(HttpSession httpSession, Transaction transaction, BindingResult result, Model model) {
        //TODO
        int            userId = (int) httpSession.getAttribute("userId");
        Optional<User> user   = userService.getUserById(userId);

        if (user.isEmpty()) return LOGIN;




        return "redirect:" + TRANSFER;
    }

    @GetMapping(ADD_CONNECTION)
    public String showAddConnectionForm() {
        return "add-connection";
    }

    @PostMapping(ADD_CONNECTION)
    public String addConnection(@RequestParam String email, @RequestParam User user, Model model) {
        var url = ADD_CONNECTION_SUCCESS;
        try {
            connectionService.addConnection(user, email);
        } catch (BuddyNotFoundException buddyNotFoundException) {
            url = ADD_CONNECTION_ERROR_NOT_FOUND;
        } catch (AlreadyABuddyException alreadyABuddyException) {
            url = ADD_CONNECTION_ERROR_DUPLICATED;
        }
        return "redirect:" + url;
    }


}
