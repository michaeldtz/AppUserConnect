package com.md.appuserconnect.core.services.internal.accounts;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.md.appuserconnect.core.model.QNObjectManager;
import com.md.appuserconnect.core.model.accounts.Account;
import com.md.appuserconnect.core.utils.RRServices;

@SuppressWarnings("serial")
public class UserAccountCreate extends HttpServlet {

	private QNObjectManager objmgr = QNObjectManager.getInstance();

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		if (RRServices.checkUserIsLoggedIn(req, resp)) {

			Account account = objmgr.getAccMgr().getAccountOfUser();

			if (account == null) {
				account = objmgr.getAccMgr().createAccountForUser();

				if (account != null)
					account.sendAsJSON(resp);
				else
					RRServices.repsondErrorAsJSON(resp, "Account has not been created");

			} else
				RRServices.repsondErrorAsJSON(resp, "Account already exists");
		}
	}
	
	
}
