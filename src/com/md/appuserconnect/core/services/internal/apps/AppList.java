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
public class AppList extends HttpServlet {

	private QNObjectManager objmgr = QNObjectManager.getInstance();

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		if (RRServices.checkUserHasValidAccount(req, resp)) {

			HashMap<String, Object> params = RRServices.loadInputParameters(req, false);
			String qnid = (String)params.get("qnid");

			if (qnid != null) {
				App[] apps = objmgr.getAppMgr().getAllAppsofQNID(qnid);
				if (apps.length > 0)
					App.sendArrayAsJSON(resp, apps);
				else
					RRServices.repsondErrorAsJSON(resp, "No apps found");
			} else
				RRServices.repsondErrorAsJSON(resp, "No parameters supplied");
		}
	}
}
