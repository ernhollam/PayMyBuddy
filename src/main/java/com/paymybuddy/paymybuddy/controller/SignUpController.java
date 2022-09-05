package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.paymybuddy.paymybuddy.config.UrlConfig.SIGNUP;

@Controller
@RequestMapping(SIGNUP)
public class SignUpController {
	@Autowired UserService userService;

	@GetMapping
	public String showSignUpForm() {
		return "signup";
	}

	@PostMapping
	public String submit(String email, String password, BindingResult result, Model model) {
		User savedUser = userService.createUser(email, password);

		if (result.hasErrors()) return "signup";
		return "redirect:/login";
	}
}
