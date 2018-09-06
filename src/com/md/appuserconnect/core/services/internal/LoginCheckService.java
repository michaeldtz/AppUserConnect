package com.md.appuserconnect.core.services.internal;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.md.appuserconnect.core.utils.JSONObject2;

@SuppressWarnings("serial")
public class LoginCheckService extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		UserService userService = UserServiceFactory.getUserService();

		if (userService.isUserLoggedIn()) {
			User user = userService.getCurrentUser();

			try {
				JSONObject2 json = new JSONObject2();
				json.put("Status", "Y");
				json.put("StatusText", "Logged In");
				json.put("Email", user.getEmail());
				json.put("UserID", user.getUserId());
				json.put("Nickname", user.getNickname());
				json.put("Admin", (userService.isUserAdmin() ? "Yes" : "No"));
				//json.append("LogoutURL", userService.createLogoutURL("/logincheck"));
				json.sendAsRepsonse(resp);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else {

			try {
				JSONObject2 json = new JSONObject2();
				json.put("Status", "N");
				json.put("StatusText", "Not Logged In");
				//json.append("LoginURL", userService.createLoginURL("/logincheck"));
				json.sendAsRepsonse(resp);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	}
