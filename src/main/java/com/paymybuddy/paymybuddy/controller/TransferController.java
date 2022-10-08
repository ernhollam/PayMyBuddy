package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.exceptions.AlreadyABuddyException;
import com.paymybuddy.paymybuddy.exceptions.BuddyNotFoundException;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.model.viewmodel.TransactionViewModel;
import com.paymybuddy.paymybuddy.model.viewmodel.UserViewModel;
import com.paymybuddy.paymybuddy.service.ConnectionService;
import com.paymybuddy.paymybuddy.service.TransactionService;
import com.paymybuddy.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    private UserService        userService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ConnectionService  connectionService;

    @GetMapping
    public String showTransferPage(Model model) {

        User                       connectedUser    = userService.getCurrentUser();
        List<TransactionViewModel> userTransactions = transactionService.getUserTransactions(connectedUser.getId());
        List<UserViewModel>        userConnections  = connectionService.getUserConnections(connectedUser);

        model.addAttribute("user", connectedUser);
        model.addAttribute("connections", userConnections);
        model.addAttribute("transactions", userTransactions);
        model.addAttribute("page", "transfer");
        model.addAttribute("amount", 0.00);

        return "transfer";
    }

    @GetMapping("/add-connection")
    public String showAddConnectionPage(Model model) {
        model.addAttribute("page", "add-connection");
        return "add-connection";
    }

    @PostMapping("/add-connection")
    public String addConnection(String email, Model model, RedirectAttributes redirAttrs) {
        try {
            connectionService.createConnectionBetweenTwoUsers(userService.getCurrentUser(),
                    email);
            redirAttrs.addFlashAttribute("added", "Congratulations, you have a new Buddy!");
            return "redirect:/transfer";
        } catch (IllegalArgumentException | BuddyNotFoundException | AlreadyABuddyException e) {
            redirAttrs.addFlashAttribute("error", e.getMessage());
            return "redirect:/transfer";
        }
    }
}
