package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.model.User;
import com.paymybuddy.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.paymybuddy.paymybuddy.config.UrlConfig.LOGIN;
import static com.paymybuddy.paymybuddy.config.UrlConfig.SIGNUP;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SignUpController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SignUpControllerTest {
	@Autowired MockMvc     mockMvc;
	@MockBean  UserService userService;

	private       User   testUser;

	@BeforeAll
	void init() {
		testUser = new User();
		testUser.setEmail("abc@domain.com");
		testUser.setPassword("rawPassword");
	}

	@Test
	@DisplayName("Get(\"/signup\") should show sign up form")
	void showSignUpForm() throws Exception {
		mockMvc.perform(get(SIGNUP))
				.andDo(print())
				.andExpect(status().isFound());
	}

	@Test
	@DisplayName("Clicking on submit with right information should redirect to login page")
	void submit() throws Exception {
		when(userService.createUser(any(String.class), any(String.class))).thenReturn(testUser);
		String BASE_URL = "http://localhost";
		mockMvc.perform(post(SIGNUP)
						.param("user", testUser.toString())
						.with(csrf()))
				.andDo(print())
				.andExpect(status().isFound())
				.andExpect(redirectedUrl(BASE_URL + LOGIN));
	}
}