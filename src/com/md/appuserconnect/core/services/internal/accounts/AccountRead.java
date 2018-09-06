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
public class AccountRead extends HttpServlet {

	private QNObjectManager objmgr = QNObjectManager.getInstance();

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		if (RRServices.checkUserHasValidAccount(req, resp)) {

			HashMap<String, Object> params = RRServices.loadInputParameters(req, false);
			String qnid = (String)params.get("qnid");
			String mail = (String)params.get("email");

			// Read Account by Input Data
			Account account = null;
			if (qnid != null)
				account = objmgr.getAccMgr().getAccountByQNID(qnid);
			else if (mail != null)
				account = objmgr.getAccMgr().getAccountByMail(mail);

			if (account != null)
				account.sendAsJSON(resp);
			else
				RRServices.repsondErrorAsJSON(resp, "No parameters supplied or no account found");

		}
	}

}
