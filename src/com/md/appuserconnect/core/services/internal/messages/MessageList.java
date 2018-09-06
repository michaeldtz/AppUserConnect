package com.md.appuserconnect.core.services.internal.messages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.md.appuserconnect.core.model.QNObjectManager;
import com.md.appuserconnect.core.model.messages.Message;
import com.md.appuserconnect.core.utils.RRServices;

@SuppressWarnings("serial")
public class MessageList extends HttpServlet {

	private QNObjectManager objmgr = QNObjectManager.getInstance();

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		if (RRServices.checkUserHasValidAccount(req, resp)) {

			HashMap<String, Object> params = RRServices.loadInputParameters(req, false);
			String qnid  = (String)params.get("qnid");
			String appid = (String)params.get("appid");

			if (appid != null) {
				Message msgs[] = objmgr.getMsgMgr().getMessagesOfApp(appid);
				ArrayList<Message> authorizedMessages = new ArrayList<Message>();
				for (Message msg : msgs) {
					if (msg.checkAuthorizationOfUser())
						authorizedMessages.add(msg);
				}

				msgs = authorizedMessages.toArray(new Message[authorizedMessages.size()]);
				if (msgs.length > 0)
					Message.sendArrayAsJSON(resp, msgs);
				else
					RRServices.repsondResultAsJSON(resp, "No messages found");

			} else if (qnid != null) {
				Message msgs[] = objmgr.getMsgMgr().getMessagesOfQNID(qnid);
				if (msgs.length > 0)
					Message.sendArrayAsJSON(resp, msgs);
				else
					RRServices.repsondResultAsJSON(resp, "No messages found");
			} else
				RRServices.repsondErrorAsJSON(resp, "No parameters supplied");
		}
	}
}
