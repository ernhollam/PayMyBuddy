package com.paymybuddy.paymybuddy.config;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class UrlConfig implements WebMvcConfigurer {
	public static final String LOGIN                           = "/login";
	public static final String SIGNUP                          = "/signup";
	public static final String HOME                            = "/home";
	public static final String TRANSFER                        = "/transfer";
	public static final String ADD_CONNECTION                  = "/add-connection";
	public static final String ADD_CONNECTION_SUCCESS          = "/transfer?connection";
	public static final String ADD_CONNECTION_ERROR_NOT_FOUND  = "/add-connection?error";
	public static final String ADD_CONNECTION_ERROR_DUPLICATED = "/add-connection?duplicated";
	public static final String PAY                             = "/pay";
	public static final String PAY_SUCCESS                     = "/transfer?paid";
	public static final String PROFILE                         = "/profile";
	public static final String DEPOSIT                         = "/deposit";
	public static final String DEPOSIT_SUCCESS                 = "/deposit?successfulDeposit";
	public static final String DEPOSIT_FAILED                  = "/deposit?failed";
	public static final String WITHDRAW                        = "/withdraw";
	public static final String WITHDRAW_SUCCESS                = "/profile?withdrew";
	public static final String CONTACT_US                      = "/contact";
	public static final String LOGOUT                          = "/logout";
	public static final String ACCESS_DENIED                   = "/403";

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController(LOGIN).setViewName("login");
		registry.addRedirectViewController("/", HOME);
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}
}
