package com.md.appuserconnect.core.services.internal.messages;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.md.appuserconnect.core.model.QNObjectManager;
import com.md.appuserconnect.core.model.apps.App;
import com.md.appuserconnect.core.model.messages.Message;
import com.md.appuserconnect.core.utils.RRServices;

@SuppressWarnings("serial")
public class MessageCreate extends HttpServlet {

	private QNObjectManager objmgr = QNObjectManager.getInstance();

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		if (RRServices.checkUserHasValidAccount(req, resp)) {

			HashMap<String, Object> params = RRServices.loadInputParameters(req, false);
			String qnid         = (String)params.get("qnid");
			String appid        = (String)params.get("appid");
			String messageTitle = (String)params.get("title");
			String messageTxt   = (String)params.get("text");
			String language     = (String)params.get("language");
			
			if (qnid != null && appid != null) {
				App app = objmgr.getAppMgr().getAppByAppID(appid);

				if (app != null) {
					
					if(language == null)
						language = "EN";
					
					Message msg = objmgr.getMsgMgr().createMessage(qnid, app, language, messageTitle, messageTxt);

					if (msg != null)
						Message.sendAsJSON(resp, msg);
					else
						RRServices.repsondErrorAsJSON(resp, "App has not been Created");
					
				} else
					RRServices.repsondErrorAsJSON(resp, "App not found");
			} else
				RRServices.repsondErrorAsJSON(resp, "Required parameters are not supplied");

		}
	}

}
