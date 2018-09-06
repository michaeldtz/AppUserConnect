package com.md.appuserconnect.core.model.apps;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.md.appuserconnect.core.model.QNObjectManager;
import com.md.appuserconnect.core.model.accounts.Account;
import com.md.appuserconnect.core.model.messages.Message;
import com.md.appuserconnect.core.utils.JSONArray2;
import com.md.appuserconnect.core.utils.JSONObject2;
import com.md.appuserconnect.core.utils.RRServices;

@SuppressWarnings("serial")
@PersistenceCapable
public class App implements Serializable {

	@PrimaryKey
	@Persistent
	private String appID;

	@Persistent
	private String qnid;  

	@Persistent
	private String appName;

	@Persistent
	private String appBundleID;

	@Persistent(serialized = "true", defaultFetchGroup = "true")
	private List<String> positions = new ArrayList<String>();

	@Persistent(serialized = "true", defaultFetchGroup = "true")
	private List<Double> versions = new ArrayList<Double>();

	@Persistent
	private String dateCreated;

	@Persistent
	private String testClientID = "";
	
	@Persistent
	private Boolean shutdown;
	
	/**
	 * Constructor
	 */ 
	public App() {
		super();
		positions = new ArrayList<String>();
	}

	/*
	 * Message Access Methods
	 */
	public Message[] getMessages() {
		return QNObjectManager.getInstance().getMsgMgr().getMessagesOfApp(appID);
	}

	public Message createMessage(String messageTxt, String language) {
		return QNObjectManager.getInstance().getMsgMgr().createMessage(qnid, this, language, messageTxt, messageTxt);
	}
	
	public Message createMessage() {
		return createMessage("NEW", "EN");
	}

	/*
	 * Getters and Setters
	 */
	public String getAppID() {
		return appID;
	}

	public void setAppID(String appId) {
		this.appID = appId;
	}

	public String getQnid() {
		return qnid;
	}

	public void setQnid(String qnid) {
		this.qnid = qnid;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppBundleID() {
		return appBundleID;
	}

	public void setAppBundleID(String appBundleID) {
		this.appBundleID = appBundleID;
	}

	public List<String> getPositions() {
		return positions;
	}

	protected void setPositions(ArrayList<String> positions) {
		this.positions = positions;
	}

	public void addPosition(String position) {
		this.positions.add(position);
	}

	public List<Double> getVersions() {
		return versions;
	}

	protected void setVersions(ArrayList<Double> versions) {
		this.versions = versions;
	}

	public void addVersion(Double version) {
		this.versions.add(version);
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getTestClientID() {
		return testClientID;
	}

	public void setTestClientID(String testClientID) {
		this.testClientID = testClientID;
	}

	public Boolean getShutdown() {
		return shutdown;
	}

	public void setShutdown(Boolean shutdown) {
		this.shutdown = shutdown;
	}

	/*
	 * Auth Check
	 */
	public boolean checkAuthorizationOfUser() {
		if (RRServices.isUserIsAdministrator())
			return true;

		Account account = QNObjectManager.getInstance().getAccMgr().getAccountOfUser();
		if (getQnid().equals(account.getQnid()))
			return true;

		return false;
	}

	/*
	 * Sending as JSON Response
	 */
	public static void sendAsJSON(HttpServletResponse resp, App app) throws IOException {
		try {
			JSONObject2 json = new JSONObject2();
			json.put("AppID", app.getAppID());
			json.put("AppBundleID", app.getAppBundleID());
			json.put("AppName", app.getAppName());
			json.put("AccountQNID", app.getQnid());
			json.put("DateCreated", app.getDateCreated());
			json.put("Versions", app.getVersions());
			json.put("Positions", app.getPositions());
			json.sendAsRepsonse(resp);
		} catch (JSONException e) {
			RRServices.repsondErrorAsJSON(resp, e.toString());
		}
	}

	/*
	 * Sending as JSON Response
	 */
	public static void sendArrayAsJSON(HttpServletResponse resp, App[] apps) throws IOException {
		try {
			JSONArray2 jsonArray = new JSONArray2();
			for (App app : apps) {
				JSONObject2 json = new JSONObject2();
				json.put("AppID", app.getAppID());
				json.put("AppBundleID", app.getAppBundleID()); 
				json.put("AppName", app.getAppName());
				json.put("AccountQNID", app.getQnid());
				json.put("DateCreated", app.getDateCreated());
				json.put("Versions", app.getVersions());
				json.put("Positions", app.getPositions());
				jsonArray.put(json);
			}
			jsonArray.sendAsRepsonse(resp);
		} catch (JSONException e) {
			RRServices.repsondErrorAsJSON(resp, e.toString());
		}
	}

	
}
