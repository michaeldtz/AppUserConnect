package com.md.appuserconnect.core.services.internal;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class LoginLinks extends HttpServlet {


	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setContentType("text/html");
		
		UserService userService = UserServiceFactory.getUserService();

		if (userService.isUserLoggedIn()) {
			resp.getWriter().println("<a href='" + userService.createLogoutURL("/index.html") + "'>Logout</a>");

		} else {

			resp.getWriter().println("<a href='" + userService.createLoginURL("/index.html") + "'>Login</a>");

		}
	}

	
}
