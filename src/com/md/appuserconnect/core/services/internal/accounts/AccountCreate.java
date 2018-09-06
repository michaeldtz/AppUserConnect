package com.md.appuserconnect.core.services.internal.accounts;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.md.appuserconnect.core.model.QNObjectManager;
import com.md.appuserconnect.core.model.accounts.Account;
import com.md.appuserconnect.core.utils.RRServices;

@SuppressWarnings("serial")
public class AccountCreate extends HttpServlet {

	private QNObjectManager objmgr = QNObjectManager.getInstance();

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		if (RRServices.checkUserIsAdministrator(req, resp)) {

			HashMap<String, Object> params = RRServices.loadInputParameters(req, false);
			String mail   = (String)params.get("email");
			String nick   = (String)params.get("nickname");
			String userid = (String)params.get("userid");
			
			if (mail != null && nick != null && userid != null) {
				Account account = objmgr.getAccMgr().getAccountByMail(mail);

				if (account == null) {
					account = objmgr.getAccMgr().createAccount(userid, mail, nick);

					if (account != null)
						account.sendAsJSON(resp);
					else
						RRServices.repsondErrorAsJSON(resp, "Account was not created");

				} else
					RRServices.repsondErrorAsJSON(resp, "Account is already existing");

			} else
				RRServices.repsondErrorAsJSON(resp, "Parameters not supplied");

		}
	}
}
