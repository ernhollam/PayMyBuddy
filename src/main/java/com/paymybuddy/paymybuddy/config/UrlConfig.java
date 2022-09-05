package com.paymybuddy.paymybuddy.config;

public class UrlConfig {
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
	public static final String DEPOSIT_SUCCESS                 = "/deposit?success";
	public static final String DEPOSIT_FAILED                  = "/deposit?failed";
	public static final String WITHDRAW                        = "/withdraw";
	public static final String WITHDRAW_SUCCESS                = "/profile?withdrawn";
	public static final String CONTACT_US                      = "/contact";
	public static final String LOGOUT                          = "/logout";
	public static final String ACCESS_DENIED                   = "/403";

	/*@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController(LOGIN).setViewName("login");
		registry.addRedirectViewController("/", HOME);
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}*/
}
