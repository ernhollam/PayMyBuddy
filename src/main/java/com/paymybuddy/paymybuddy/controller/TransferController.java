package com.paymybuddy.paymybuddy.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;

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

        return "transfer";
    }
}
