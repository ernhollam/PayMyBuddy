package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.model.viewmodel.UserViewModel;
import com.paymybuddy.paymybuddy.service.ConnectionService;
import com.paymybuddy.paymybuddy.service.TransactionService;
import com.paymybuddy.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {

	@Autowired
	private UserService        userService;
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private ConnectionService  connectionService;

	@GetMapping
	public String showProfilePage(Model model) {

		User                connectedUser = userService.getCurrentUser();
		List<UserViewModel> connections   = connectionService.getUserConnections(connectedUser);

		model.addAttribute("user", connectedUser);
		model.addAttribute("balance", connectedUser.getBalance());
		model.addAttribute("page", "profile");
		model.addAttribute("connections", connections);

		return "profile";
	}

	@GetMapping("/update-balance")
	public String showUpdateBalancePage(Model model) {
		model.addAttribute("page", "update-balance");
		model.addAttribute("user", userService.getCurrentUser());
		return "update-balance";
	}

	@PostMapping("/update-balance")
	public String updateBalance(@RequestParam String action, double amount, Model model, RedirectAttributes redirAttrs) {
		try {
			String strAmount = String.valueOf(amount);
			switch (action) {
				case "deposit" -> {
					userService.deposit(userService.getCurrentUser(), strAmount);
				}
				case "withdrawal" -> {
					userService.withdraw(userService.getCurrentUser(), strAmount);
				}
			}
			redirAttrs.addFlashAttribute("success",
					"Your "+ action + " of " + strAmount + "€ was successful!");
		} catch (Exception e) {
			redirAttrs.addFlashAttribute("error", e.getMessage());
			return "redirect:/update-balance";
		}
		return "redirect:/profile";
	}
}
