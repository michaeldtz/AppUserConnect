package com.md.appuserconnect.core.services.external;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.md.appuserconnect.core.model.QNObjectManager;
import com.md.appuserconnect.core.model.apps.App;
import com.md.appuserconnect.core.model.apps.AppManager;
import com.md.appuserconnect.core.model.delivery.DeliveryManager;
import com.md.appuserconnect.core.model.messages.Message;
import com.md.appuserconnect.core.model.messageslanguage.MessageLanguage;
import com.md.appuserconnect.core.model.statistics.StatisticsManager;
import com.md.appuserconnect.core.utils.JSONObject2;
import com.md.appuserconnect.core.utils.RRServices;

@SuppressWarnings("serial")
public class AppConnect extends HttpServlet {

	/*
	 * private static final Logger log = Logger.getLogger(AccountManager.class
	 * .getName());
	 */

	private QNObjectManager objmgr = QNObjectManager.getInstance();

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		HashMap<String, Object> params = RRServices.loadInputParameters(req, true);
		String qnid = (String) params.get("qnid");
		String appbundle = (String) params.get("appbundle");
		String appname = (String) params.get("appname");
		String appversion = (String) params.get("appversion");
		String clientID = (String) params.get("clientid");
		String locale = (String) params.get("locale");
		String position = (String) params.get("position");
		String language = locale.substring(0, 2);
		String countryCode = locale.substring(3, 5);

		// Get App
		App app = this.checkAndCreateApp(appbundle, appname, qnid, appversion, position);

/*		if(app.getShutdown() == true){
			resp.getWriter().println("{\"SHUTDOWN\",\"FOREVER\"}");
			return;
		}*/
		
		Message[] messages = app.getMessages();

		StatisticsManager staMgr = QNObjectManager.getInstance().getStaMgr();
		staMgr.addStatistic(qnid, clientID, appbundle, appname, appversion, position, countryCode, language);

		DeliveryManager delMgr = QNObjectManager.getInstance().getDelMgr();
		for (Message message : messages) {

			if (!message.getStatusCode().equals("1"))
				continue;

			Date currentDate = new Date();
			Date validFrom = new Date(new Long(message.getValidFrom()).longValue() * 1000);
			Date validTo = new Date(new Long(message.getValidTo()).longValue() * 1000);

			if (currentDate.after(validTo))
				continue;

			if (currentDate.before(validFrom))
				continue;

			if (!message.getPosition().equals("") && !message.getPosition().equalsIgnoreCase(position))
				continue;

			if (delMgr.willWeSendThisMessageToThisClient(clientID, message)) {

				try {
					JSONObject2 json = new JSONObject2();

					if (!message.hasLanguage(language))
						language = message.getDefaultLanguage();

					language = language.toUpperCase();
					MessageLanguage lang = message.getMessageLanguage(language);

					if (lang != null) {

						json.put("Title", lang.getMessageTitle());
						json.put("Text", lang.getMessageText());

						if (!message.getActionButton1().equals("")) {
							json.put("Button1Text", lang.getButton1Text());
							json.put("Button1Action", message.getActionButton1());
						}

						if (!message.getActionButton2().equals("")) {
							json.put("Button2Text", lang.getButton2Text());
							json.put("Button2Action", message.getActionButton2());
						}

						// if (!message.getActionButton3().equals("")) {
						json.put("Button3Text", lang.getButton3Text());
						json.put("Button3Action", message.getActionButton3());
						// }

						json.sendAsRepsonse(resp);
						delMgr.didSendMessageToClient(clientID, message);
						return;
					}
				} catch (JSONException exp) {

				}

				break;
			}
		}

		resp.getWriter().println("{ }");

	}

	public App checkAndCreateApp(String appbundle, String appname, String qnid, String appversion, String position) {

		AppManager appMgr = objmgr.getAppMgr();

		// App Loading
		App app = appMgr.getAppByBundleID(appbundle);
		if (app == null) {
			app = appMgr.createNewApp(qnid, appbundle, appname);
		}

		// QNID Check
		if (app.getQnid() == null || !app.getQnid().equals(qnid)) {
			app.setQnid(qnid);
			objmgr.getAppMgr().updateApp(app);
		}

		// App Version Check
		double appVersNum = new Double(appversion).doubleValue();
		boolean versionExt = false;
		List<Double> versions = app.getVersions();
		Iterator<Double> iterator = versions.iterator();
		while (iterator.hasNext()) {
			Double version = iterator.next();
			double versNum = version.doubleValue();

			if (appVersNum == versNum) {
				versionExt = true;
				break;
			}
		}

		// Creation of Version
		if (!versionExt) {
			app.addVersion(appVersNum);
			appMgr.updateApp(app);
		}

		// App Position Check
		boolean positionExt = false;
		List<String> positions = app.getPositions();
		Iterator<String> posIter = positions.iterator();
		while (posIter.hasNext()) {
			String appPos = posIter.next();
			if (appPos.equalsIgnoreCase(position)) {
				positionExt = true;
				break;
			}
		}

		// Creation of Version
		if (!positionExt) {
			app.addPosition(position);
			appMgr.updateApp(app);
		}

		return app;
	}

}
