package com.paymybuddy.paymybuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.paymybuddy.paymybuddy.config.UrlConfig.HOME;

@Controller
@RequestMapping(HOME)
public class HomeController {
	@GetMapping
	public String home(Model model)	{ return "home"; }
}
