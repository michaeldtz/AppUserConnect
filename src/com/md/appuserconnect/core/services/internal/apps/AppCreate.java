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
public class AppCreate extends HttpServlet {

	private QNObjectManager objmgr = QNObjectManager.getInstance();

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		if (RRServices.checkUserHasValidAccount(req, resp)) {

			HashMap<String, Object> params = RRServices.loadInputParameters(req, false);
			String qnid      = (String)params.get("qnid");
			String appBundle = (String)params.get("appbundle");
			String appName   = (String)params.get("appname");

			if (qnid != null && appBundle != null) {
				App app = objmgr.getAppMgr().createNewApp(qnid, appBundle, appName);

				if (app != null)
					App.sendAsJSON(resp, app);
				else
					RRServices.repsondErrorAsJSON(resp, "App has not been created");

			} else
				RRServices.repsondErrorAsJSON(resp, "Required parameter appbundle is not supplied");

		}
	}

}
