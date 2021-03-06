package com.md.appuserconnect.core.services.internal.apps;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.md.appuserconnect.core.model.QNObjectManager;
import com.md.appuserconnect.core.model.apps.App;
import com.md.appuserconnect.core.utils.RRServices;

@SuppressWarnings("serial")
public class AppDelete extends HttpServlet {

	private QNObjectManager objmgr = QNObjectManager.getInstance();

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		if (RRServices.checkUserHasValidAccount(req, resp)) {

			HashMap<String, Object> params = RRServices.loadInputParameters(req, false);
			String qnid  = (String)params.get("qnid");
			String appid = (String)params.get("appid");

			if (qnid != null && appid != null) {

				App app = objmgr.getAppMgr().getAppByAppID(appid);

				if (app != null) {
					if (app.checkAuthorizationOfUser()) {
						objmgr.getAppMgr().deleteApp(app);
						RRServices.repsondResultAsJSON(resp, "App deleted");
					} else
						RRServices.repsondErrorAsJSON(resp, "Not authorized to read this app");
				} else
					RRServices.repsondErrorAsJSON(resp, "App not found");

			} else
				RRServices.repsondErrorAsJSON(resp, "Required parameteres qnid and appid are not supplied");
		}
	}
	
	

}
