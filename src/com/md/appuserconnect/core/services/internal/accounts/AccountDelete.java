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
public class AccountDelete extends HttpServlet {

	private QNObjectManager objmgr = QNObjectManager.getInstance();

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		if (RRServices.checkUserHasValidAccount(req, resp)) {

			HashMap<String, Object> params = RRServices.loadInputParameters(req, false);
			String confirmqnid = (String)params.get("confirmqnid");
			String qnid        = (String)params.get("qnid");

			if (qnid != null && confirmqnid != null && confirmqnid.equals(qnid)) {
				Account account = objmgr.getAccMgr().getAccountByQNID(qnid);
				objmgr.getAccMgr().deleteAccount(account);
				RRServices.repsondResultAsJSON(resp, "Account deleted");
			} else
				RRServices.repsondErrorAsJSON(resp, "Parameter qnid and confirmqnid not supplied");

		}
	}
	
	
}
