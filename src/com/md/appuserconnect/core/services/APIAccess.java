package com.md.appuserconnect.core.services;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.md.appuserconnect.core.model.QNObjectManager;

@SuppressWarnings("serial")
public class APIAccess extends HttpServlet{

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setContentType("text/html");
		PrintWriter pw = resp.getWriter();

		pw.println("<html><head><title>QNotify API</title></head><body>");
		pw.println("<table>");

		UserService userService = UserServiceFactory.getUserService();

		if (userService.isUserLoggedIn()) {

			pw.println("<tr><td><a href='" + userService.createLogoutURL("/api") + "'>Logout</a></td></tr>");
			pw.println("<tr><td><a href='api/logincheck'>Login Information</a></td></tr>");
			
			if (QNObjectManager.getInstance().getAccMgr().getAccountOfUser() == null) {
				pw.println("<tr><td><br></td></tr>");
				pw.println("<tr><th>Account API</th></tr>");
				pw.println("<tr><td><a href='api/useraccount/create'>Create User Account</a></td></tr>");

			} else {
				pw.println("<tr><td><br></td></tr>");
				pw.println("<tr><th>Account API</th></tr>");
				pw.println("<tr><td><a href='api/account/create'>Create Account</a></td></tr>");
				pw.println("<tr><td><a href='api/account/read'>Read Account</a></td></tr>");
				pw.println("<tr><td><a href='api/account/update'>Update Account</a></td></tr>");
				pw.println("<tr><td><a href='api/account/delete'>Delete Account</a></td></tr>");

				pw.println("<tr><td><br></td></tr>");
				pw.println("<tr><th>Apps API</th></tr>");
				pw.println("<tr><td><a href='api/app/list'>List Apps</a></td></tr>");
				pw.println("<tr><td><a href='api/app/create'>Create App</a></td></tr>");
				pw.println("<tr><td><a href='api/app/read'>Read App</a></td></tr>");
				pw.println("<tr><td><a href='api/app/update'>Update App</a></td></tr>");
				pw.println("<tr><td><a href='api/app/delete'>Delete App</a></td></tr>");

				pw.println("<tr><td><a href='api/app/addversion'>Add App Version</a></td></tr>");
				pw.println("<tr><td><a href='api/app/addposition'>Add App Position</a></td></tr>");

				pw.println("<tr><td><br></td></tr>");
				pw.println("<tr><th>Message API</th></tr>");
				pw.println("<tr><td><a href='api/message/list'>List Messages</a></td></tr>");
				pw.println("<tr><td><a href='api/message/create'>Create Message</a></td></tr>");
				pw.println("<tr><td><a href='api/message/update'>Update Message</a></td></tr>");
				pw.println("<tr><td><a href='api/message/delete'>Delete Message</a></td></tr>");

				pw.println("<tr><td><br></td></tr>");
				pw.println("<tr><th>Statistics API</th></tr>");
				pw.println("<tr><td><a href='api/statistics/csv'>Statistics CSV</a></td></tr>");
				pw.println("<tr><td><a href='api/statistics/kona'>Statistics KONA Transfer</a></td></tr>");
			}

		} else {

			pw.println("<tr><td><a href='" + userService.createLoginURL("/api") + "'>Login</a></td></tr>");

		}

		pw.println("</table>");
		pw.println("</body></html>");
	}

}
