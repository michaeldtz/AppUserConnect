package com.md.appuserconnect.core.services.internal.messages;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.md.appuserconnect.core.model.QNObjectManager;
import com.md.appuserconnect.core.model.messages.Message;
import com.md.appuserconnect.core.model.messageslanguage.MessageLanguage;
import com.md.appuserconnect.core.utils.RRServices;

@SuppressWarnings("serial")
public class MessageUpdate extends HttpServlet {

	private QNObjectManager objmgr = QNObjectManager.getInstance();

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doGet(req, resp);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		if (RRServices.checkUserHasValidAccount(req, resp)) {

			HashMap<String, Object> params = RRServices.loadInputParameters(req, false);
			String qnid = (String) params.get("qnid");
			String msgid = (String) params.get("msgid");

			if (qnid != null) {
				Message msg = objmgr.getMsgMgr().getMessageByID(msgid);

				if (msg != null) {

					msg.setTitle((String)           params.get("title"));
					msg.setActionButton1((String)   params.get("button1action"));
					msg.setActionButton2((String)   params.get("button2action"));
					msg.setActionButton3((String)   params.get("button3action"));
					msg.setStatusCode((String)      params.get("statuscode"));
					msg.setDefaultLanguage((String) params.get("defaultlang"));
					msg.setValidFrom((String)       params.get("validfrom"));
					msg.setValidTo((String)         params.get("validto"));
					msg.setRepeatCount((String)     params.get("repeatcount"));
					msg.setPosition((String)        params.get("position"));
				
					JSONObject texts = (JSONObject) params.get("messagetexts");
					String[] langs   = JSONObject.getNames(texts);
					for (String lang : langs) {
						try {
							JSONObject messageTexts = (JSONObject) texts.get(lang);
							
							String text     = (String) messageTexts.get("MessageText").toString();
							String header   = (String) messageTexts.get("MessageHeader").toString();
							String but1Text = (String) messageTexts.get("Button1Text").toString();
							String but2Text = (String) messageTexts.get("Button2Text").toString();
							String but3Text = (String) messageTexts.get("Button3Text").toString();
							
							
							MessageLanguage messLang = msg.getMessageLanguage(lang);
							if(messLang == null)
								messLang = msg.createNewLanguage(lang);
							
							messLang.setMessageText(text);
							messLang.setMessageTitle(header);
							messLang.setButton1Text(but1Text);
							messLang.setButton2Text(but2Text);
							messLang.setButton3Text(but3Text);
							
							
						} catch (JSONException e) {

						}
					}

					objmgr.getMsgMgr().updateMessage(msg);

					Message.sendAsJSON(resp, msg);

				} else
					RRServices.repsondErrorAsJSON(resp, "App not found");
			} else
				RRServices.repsondErrorAsJSON(resp, "Required parameters are not supplied");

		}
	}

}
