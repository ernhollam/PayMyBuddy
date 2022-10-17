package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.exceptions.EmailAlreadyUsedException;
import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.model.viewmodel.ContactViewModel;
import com.paymybuddy.paymybuddy.service.ConnectionService;
import com.paymybuddy.paymybuddy.service.TransactionService;
import com.paymybuddy.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contact")
public class ContactController {

	@Autowired
	private UserService        userService;
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private ConnectionService  connectionService;

	@GetMapping
	public String showTransferPage(Model model) {

		User connectedUser = userService.getAuthenticatedUser();

		model.addAttribute("user", connectedUser);
		model.addAttribute("page", "contact");
		model.addAttribute("contactForm", new ContactViewModel());

		return "contact";
	}

	@PostMapping
	public String send(ContactViewModel contactForm, Model model) {
		return "redirect:/contact?sent";
	}
}
