package com.md.appuserconnect.core.model.messages;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.md.appuserconnect.core.model.apps.App;
import com.md.appuserconnect.core.model.messageslanguage.MessageLanguage;
import com.md.appuserconnect.core.model.persistence.PMF;
import com.md.appuserconnect.core.utils.UUIDGenerator;

public class MessageManager {

	public Message createMessage(String qnid, App app, String language, String messageTitle, String messageTxt) {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		Message message = new Message();
		message.setMessageID(UUIDGenerator.getUUID());
		message.createNewLanguage(language);
		message.setAppID(app.getAppID());
		message.setQnid(qnid);
		message.setDateCreated(new Long(new Date().getTime() / 1000).toString());
		message.setTitle(messageTitle);
		message.setPosition("DEFAULT");
		message.setStatusCode("0");
		message.setValidFrom(new Long(new Date().getTime() / 1000).toString());
		message.setValidTo(new Long((new Date().getTime() / 1000) + 432000).toString());

		try {
			pm.makePersistent(message);
		} finally {
			pm.flush();
			pm.close();
		}

		return message;
	}

	@SuppressWarnings("unchecked")
	public Message[] getAllMessages() {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			Query query = pm.newQuery(Message.class);
			List<Message> messages = (List<Message>) query.execute();
			return messages.toArray(new Message[messages.size()]);
		} finally {
			pm.close();
		}

	}

	@SuppressWarnings("unchecked")
	public Message[] getRelevantMessages(String qnid, String appid, long lastMessageNumber) {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			String queryStr = "SELECT FROM " + Message.class.getName();
			queryStr += " WHERE qnid == '" + qnid + "' AND appID == '" + appid + "' AND messageNumber > " + lastMessageNumber + "";

			Query query = pm.newQuery(queryStr);

			List<Message> messages = (List<Message>) query.execute();
			return messages.toArray(new Message[messages.size()]);
		} finally {
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	public Message[] getMessagesOfApp(String appID) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			String queryStr = "SELECT FROM " + Message.class.getName();
			queryStr += " WHERE appID == '" + appID + "'";

			Query query = pm.newQuery(queryStr);

			List<Message> messages = (List<Message>) query.execute();
			return messages.toArray(new Message[messages.size()]);
		} finally {
			pm.close();
		}

	}

	@SuppressWarnings("unchecked")
	public Message getMessageByID(String msgid) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Message msg = null;
		try {
			String queryStr = "SELECT FROM " + Message.class.getName();
			queryStr += " WHERE messageID == '" + msgid + "'";

			Query query = pm.newQuery(queryStr);

			List<Message> messages = (List<Message>) query.execute();

			if (!messages.isEmpty())
				msg = messages.get(0);
		} finally {
			pm.close();
		}
		return msg;
	}

	@SuppressWarnings("unchecked")
	public Message[] getMessagesOfQNID(String qnid) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			String queryStr = "SELECT FROM " + Message.class.getName();
			queryStr += " WHERE qnid == '" + qnid + "'";

			Query query = pm.newQuery(queryStr);

			List<Message> messages = (List<Message>) query.execute();
			return messages.toArray(new Message[messages.size()]);

		} finally {
			pm.close();
		}
	}

	/**
	 * Update
	 * 
	 * @param account
	 */
	public void updateMessage(Message msg) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		//Save Messages
		for (String langs : msg.getLanguages()) {
			MessageLanguage language = msg.getMessageLanguage(langs);
			updateMessageLanguage(language);
		}

		try {
			pm.makePersistent(msg);
		} finally {
			pm.flush();
			pm.close();
		}
	}

	public void deleteMessage(Message msg) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Message msgToDel = (Message) pm.getObjectById(Message.class, msg.getMessageID());
			pm.deletePersistent(msgToDel);
		} finally {
			pm.close();
		}
	}

	/*
	 * 
	 * Message Langauges
	 */

	public MessageLanguage createMessageLangauge(Message message, String lang) {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		MessageLanguage language = new MessageLanguage(lang, message);
		language.setMessageText("New Message: " + lang);

		try {
			pm.makePersistent(language);
		} finally {
			pm.flush();
			pm.close();
		}

		return language;

	}

	public void deleteMessageLanguage(MessageLanguage messageLanguage) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			MessageLanguage msgToDel = (MessageLanguage) pm.getObjectById(MessageLanguage.class, messageLanguage.getId());
			pm.deletePersistent(msgToDel);
		} finally {
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	public MessageLanguage getLanguageOfMessage(Message message, String language) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		MessageLanguage lang = null;

		try {
			String queryStr = "SELECT FROM " + MessageLanguage.class.getName();
			queryStr += " WHERE messageID == '" + message.getMessageID() + "'";
			queryStr += "    && language  == '" + language + "'";

			Query query = pm.newQuery(queryStr);

			List<MessageLanguage> languages = (List<MessageLanguage>) query.execute();
			if (!languages.isEmpty())
				lang = languages.get(0);
			return lang;

		} finally {
			pm.close();
		}
	}

	public void updateMessageLanguage(MessageLanguage lang) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(lang);
	}

	@SuppressWarnings("unchecked")
	public MessageLanguage[] getLanguagesOfMessage(Message message) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			String queryStr = "SELECT FROM " + MessageLanguage.class.getName();
			queryStr += " WHERE messageID == '" + message.getMessageID() + "'";

			Query query = pm.newQuery(queryStr);

			List<MessageLanguage> languages = (List<MessageLanguage>) query.execute();
			return languages.toArray(new MessageLanguage[languages.size()]);

		} finally {
			pm.close();
		}

	}

}
