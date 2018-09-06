package com.md.appuserconnect.core.services.internal.messages;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.md.appuserconnect.core.model.QNObjectManager;
import com.md.appuserconnect.core.model.messages.Message;
import com.md.appuserconnect.core.utils.RRServices;

@SuppressWarnings("serial")
public class MessageDelete extends HttpServlet {

	private QNObjectManager objmgr = QNObjectManager.getInstance();

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		if (RRServices.checkUserHasValidAccount(req, resp)) {

			HashMap<String, Object> params = RRServices.loadInputParameters(req, false);
			String qnid  = (String)params.get("qnid");
			String msgid = (String)params.get("msgid");

			if (qnid != null && msgid != null) {

				Message msg = objmgr.getMsgMgr().getMessageByID(msgid);

				if (msg != null) {
					if (msg.checkAuthorizationOfUser()) {
						objmgr.getMsgMgr().deleteMessage(msg);
						RRServices.repsondResultAsJSON(resp, "Message deleted");
					} else
						RRServices.repsondErrorAsJSON(resp, "Not authorized to read this message");
				} else
					RRServices.repsondErrorAsJSON(resp, "Message not found");

			} else
				RRServices.repsondErrorAsJSON(resp, "Required parameteres qnid and appid are not supplied");
		}
	}
	
	

}
