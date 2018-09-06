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
public class AppPositionAdd extends HttpServlet {

	private QNObjectManager objmgr = QNObjectManager.getInstance();

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		if (RRServices.checkUserHasValidAccount(req, resp)) {

			HashMap<String, Object> params = RRServices.loadInputParameters(req, false);
			String appID       = (String)params.get("appid");
			String appPosition = (String)params.get("appposition");

			// Load App by these parameters
			App app = objmgr.getAppMgr().getAppByAppID(appID);

			if (app != null) {
				if (app.checkAuthorizationOfUser()) {
					
					if (!app.getPositions().contains(appPosition)) {
						app.addPosition(appPosition);
						objmgr.getAppMgr().updateApp(app);
						App.sendAsJSON(resp, app);
					} else
						RRServices.repsondErrorAsJSON(resp, "Position already existing");
				} else
					RRServices.repsondErrorAsJSON(resp, "Not authorized to update this app");
			} else
				RRServices.repsondErrorAsJSON(resp, "No app found or no parameters supplied");

		}
	}
}
