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
public class AccountUpdate extends HttpServlet {

	private QNObjectManager objmgr = QNObjectManager.getInstance();

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		if (RRServices.checkUserHasValidAccount(req, resp)) {

			HashMap<String, Object> params = RRServices.loadInputParameters(req, false);
			String qnid    = (String)params.get("qnid");
			String nick    = (String)params.get("nickname");
			String extSeed = (String)params.get("externalSeed");
			
			if (qnid != null) {
				Account account = objmgr.getAccMgr().getAccountByQNID(qnid);
				if (account != null) {

					if (nick != null)
						account.setNickname(nick);

					if(extSeed != null)
						account.setExternalSeedMD5(extSeed);
					
					objmgr.getAccMgr().updateAccount(account);
					RRServices.repsondResultAsJSON(resp, "Account updated");

				} else 
					RRServices.repsondErrorAsJSON(resp, "Account has not been found");
				
			} else
				RRServices.repsondErrorAsJSON(resp, "Parameter qnid supplied");

		}
	}
}
