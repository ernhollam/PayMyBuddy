package com.paymybuddy.paymybuddy.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.paymybuddy.paymybuddy.config.UrlConfig.HOME;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HomeController.class)
class HomeControllerTest {
	@Autowired MockMvc mockMvc;

	@Test
	void home() throws Exception {
		mockMvc.perform(get(HOME))
				.andDo(print())
				.andExpect(status().isFound());
	}
}