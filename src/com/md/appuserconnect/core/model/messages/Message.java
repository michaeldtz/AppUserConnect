package com.md.appuserconnect.core.model.messages;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.md.appuserconnect.core.model.QNObjectManager;
import com.md.appuserconnect.core.model.accounts.Account;
import com.md.appuserconnect.core.model.apps.App;
import com.md.appuserconnect.core.model.delivery.DeliveryStats;
import com.md.appuserconnect.core.model.messageslanguage.MessageLanguage;
import com.md.appuserconnect.core.utils.JSONArray2;
import com.md.appuserconnect.core.utils.JSONObject2;
import com.md.appuserconnect.core.utils.RRServices;

@SuppressWarnings("serial")
@PersistenceCapable
public class Message implements Serializable {

	@PrimaryKey
	private Key messageID;

	@Persistent
	private String qnid;

	@Persistent
	private String appID;

	@Persistent
	private String title = "";  

	@Persistent
	private String actionButton1 = "";

	@Persistent
	private String actionButton2 = "";

	@Persistent
	private String actionButton3 = "";

	@Persistent
	private String delayedStart = "0";
	
	@Persistent
	private String repeatCount = "0";

	@Persistent
	private String position = "";

	@Persistent
	private String defaultLanguage;

	@Persistent
	private String dateCreated;

	@Persistent
	private String validFrom;

	@Persistent
	private String validTo;

	@Persistent
	private String statusCode = "0";

	@Persistent
	private List<String> languages = new ArrayList<String>();

	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");

	@NotPersistent
	private MessageLanguage[] cachedLanguageList;

	/**
	 * Constructor
	 */
	public Message() {
		super();
		defaultLanguage = "EN";
	}

	public List<String> getLanguages() {
		return languages;
	}

	public boolean hasLanguage(String language) {
		for (String lang : getLanguages()) {
			if (lang.equalsIgnoreCase(language)) {
				return true;
			}
		}
		return false;
	}

	public MessageLanguage createNewLanguage(String language) {
		if (!hasLanguage(language)) {
			languages.add(language);
			MessageLanguage lang = QNObjectManager.getInstance().getMsgMgr().createMessageLangauge(this, language);
			QNObjectManager.getInstance().getMsgMgr().updateMessage(this);
			refreshLanguageListCache();
			return lang;
		}
		return getMessageLanguage(language);
	}

	public void deleteLanguage(String language) {
		if(hasLanguage(language)){
			languages.remove(language);
			MessageLanguage messLang = getMessageLanguage(language);
			QNObjectManager.getInstance().getMsgMgr().deleteMessageLanguage(messLang);
			refreshLanguageListCache();
		}
	}	

	/*
	 * Getters and Setters
	 */

	public String getStatusText() {
		if (getStatusCode().equals("0")) {
			return "0 - In Preparation";
		} else if (getStatusCode().equals("1")) {
			return "1 - Active";
		} else {
			return "2 - Inactive";
		}
	}

	public void setStatusText(String statusText) {
		if (statusText.substring(0, 1).equals("0")) {
			setStatusCode("0");
		} else if (statusText.substring(0, 1).equals("1")) {
			setStatusCode("1");
		} else {
			setStatusCode("2");
		}
	}

	public App getApp() {
		return QNObjectManager.getInstance().getAppMgr().getAppByAppID(appID);
	}

	public MessageLanguage getMessageLanguage(String lang) {
		if(cachedLanguageList == null)
			refreshLanguageListCache( );
			
		for(MessageLanguage language: cachedLanguageList){
			if(language.getLanguage().equalsIgnoreCase(lang)){
				return language;
			}
		}
		return null;
	}
	
	public void refreshLanguageListCache(){
		cachedLanguageList = QNObjectManager.getInstance().getMsgMgr().getLanguagesOfMessage(this);
	}

	public String getAppBundleID() {
		return getApp().getAppBundleID();
	}

	public String getAppName() {
		return getApp().getAppName();
	}

	public String getQnid() {
		return qnid;
	}

	public void setQnid(String qnid) {
		this.qnid = qnid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAppID() {
		return appID;
	}

	public void setAppID(String appNum) {
		this.appID = appNum;
	}

	public String getMessageID() {
		return messageID.getName();
	}

	

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getDelayedStart() {
		return delayedStart;
	}

	public void setDelayedStart(String delayedStart) {
		this.delayedStart = delayedStart;
	}

	public String getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(String repeatCount) {
		this.repeatCount = repeatCount;
	}

	public double getRepeatCountAsDouble() {
		return new Double(getRepeatCount()).doubleValue();
	}

	public void setRepeatCountAsDouble(double repeatCount) {
		setRepeatCount(new Double(repeatCount).toString());
	}

	public double getDelayedStartAsDouble() {
		return new Double(getDelayedStart()).doubleValue();
	}

	public void setDelayedStartAsDouble(double delayedStart) {
		setDelayedStart(new Double(delayedStart).toString());
	}
	public void setMessageID(String msgID) {
		this.messageID = KeyFactory.createKey(Message.class.getSimpleName(), msgID);
	}

	public String getDefaultLanguage() {
		return defaultLanguage;
	}

	public void setDefaultLanguage(String language) {
		this.defaultLanguage = language;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getActionButton1() {
		return actionButton1;
	}

	public void setActionButton1(String actionButton1) {
		this.actionButton1 = actionButton1;
	}

	public String getActionButton2() {
		return actionButton2;
	}

	public void setActionButton2(String actionButton2) {
		this.actionButton2 = actionButton2;
	}

	public String getActionButton3() {
		return actionButton3;
	}

	public void setActionButton3(String actionButton3) {
		this.actionButton3 = actionButton3;
	}

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getValidTo() {
		return validTo;
	}

	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}

	public Date getValidFromDate() {
		return new Date(new Long(getValidFrom()) * 1000);
	}

	public Date getValidToDate() {
		return new Date(new Long(getValidTo()) * 1000);
	}

	public void setValidFromDate(Date dateFrom) {
		String dateFromString = new Long(dateFrom.getTime() / 1000).toString();
		setValidFrom(dateFromString);
	}

	public void setValidToDate(Date dateTo) {
		String dateToString = new Long(dateTo.getTime() / 1000).toString();
		setValidTo(dateToString);
	}

	public String getValidFromFT() {
		Date date = new Date(new Long(getValidFrom()) * 1000);
		return sdf.format(date);
	}

	public String getValidToFT() {
		Date date = new Date(new Long(getValidTo()) * 1000);
		return sdf.format(date);
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

	public static JSONObject2 getAsJSONObject(Message msg) throws JSONException {
		JSONObject2 json = new JSONObject2();
		json.put("MsgID", msg.getMessageID());
		json.put("Title", msg.getTitle());
		json.put("AppID", msg.getAppID());
		json.put("AccountQNID", msg.getQnid());
		json.put("StatusCode", msg.getStatusCode());
		json.put("DateCreated", msg.getDateCreated());
		json.put("DefaultLang", msg.getDefaultLanguage());
		json.put("Button1Action", msg.getActionButton1());
		json.put("Button2Action", msg.getActionButton2());
		json.put("Button3Action", msg.getActionButton3());
		json.put("ValidFrom", msg.getValidFrom());
		json.put("ValidTo", msg.getValidTo());
		json.put("Position", msg.getPosition());
		json.put("RepeatCount", msg.getRepeatCount());

		// Statistics for this Message
		DeliveryStats stats = DeliveryStats.createDeliveryStatsForMessage(msg);
		json.put("StatTotalDelivery", stats.getTotalDelivery());
		json.put("StatTotalClients", stats.getTotalClients());
		json.put("StatClientsWithOne", stats.getClientsWithOne());
		json.put("StatClientsWithTwo", stats.getClientsWithTwo());
		json.put("StatClientsWithThr", stats.getClientsWithThree());
		json.put("StatClientsWithAll", stats.getClientsWithAll());
		json.put("StatAvgDelPerClient", stats.getAvgDelPerClient());
		json.put("StatisticSummary", stats.getSummary());

		JSONObject2 languages = new JSONObject2();
		List<String> langs = msg.getLanguages();

		if (langs != null) {
			for (String lang : langs) {
				MessageLanguage language = QNObjectManager.getInstance().getMsgMgr().getLanguageOfMessage(msg, lang);
				JSONObject2 texts = MessageLanguage.getAsJSONObject(language);
				languages.put(language.getLanguage(), texts);
			}
		}

		json.put("MessageTexts", languages);
		return json;
	}

	/*
	 * Sending as JSON Response
	 */
	public static void sendAsJSON(HttpServletResponse resp, Message msg) throws IOException {
		try {
			JSONObject2 json = Message.getAsJSONObject(msg);
			json.sendAsRepsonse(resp);
		} catch (JSONException e) {
			RRServices.repsondErrorAsJSON(resp, e.toString());
		}
	}

	/*
	 * Sending as JSON Response
	 */
	public static void sendArrayAsJSON(HttpServletResponse resp, Message[] msgs) throws IOException {
		try {
			JSONArray2 jsonArray = new JSONArray2();
			for (Message msg : msgs) {
				JSONObject2 json = Message.getAsJSONObject(msg);
				jsonArray.put(json);
			}
			jsonArray.sendAsRepsonse(resp);
		} catch (JSONException e) {
			RRServices.repsondErrorAsJSON(resp, e.toString());
		}
	}

}
