package com.md.appuserconnect.core;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.md.appuserconnect.core.model.QNObjectManager;
import com.md.appuserconnect.core.model.accounts.Account;
import com.md.appuserconnect.core.model.apps.App;
import com.md.appuserconnect.core.model.messages.Message;
import com.md.appuserconnect.core.utils.RRServices;

@SuppressWarnings("serial")
public class TestService extends HttpServlet {

	/**
	 * @param args
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		

		if (RRServices.checkUserIsAdministrator(req, resp)) {
			QNObjectManager mgr = QNObjectManager.getInstance();

			Account[] accs = mgr.getAccMgr().getAllAccounts();
			for (Account ac : accs) {
				resp.getWriter().println(
						"\nAccount, QNID: " + ac.getQnid() + ", Email: " + ac.getEmail() + ", Nickname: " + ac.getNickname() + ", Created: "
								+ ac.getDateCreated());

				App[] apps = ac.getApps();
				for (App ap : apps) {
					resp.getWriter().println(
							"-->App, BundleID: " + ap.getAppBundleID() + ", AppID: " + ap.getAppID() + ", AppName: " + ap.getAppName()
									+ ", Created: " + ap.getDateCreated());

					Message[] msgs = ap.getMessages();

					for (Message ms : msgs) {
						resp.getWriter().println(
								"----->Message, MessageID: " + ms.getMessageID() + ", DefLang: "
										+ ms.getDefaultLanguage());
					}
				}
			}
		}
	}
}
