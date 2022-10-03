package com.paymybuddy.paymybuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
	@GetMapping
	public String showLoginPage(Model model) {
		model.addAttribute("email", "");
		model.addAttribute("password", "");
		return "login";
	}

	@PostMapping
	public String login(Model model) {
		return "home";
	}
}
