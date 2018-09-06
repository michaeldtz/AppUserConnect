package com.md.appuserconnect.core.model.accounts;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.md.appuserconnect.core.model.QNObjectManager;
import com.md.appuserconnect.core.model.apps.App;
import com.md.appuserconnect.core.model.persistence.PMF;
import com.md.appuserconnect.core.utils.UUIDGenerator;

public class AccountManager {

	// private static final Logger log =
	// Logger.getLogger(AccountManager.class.getName());

	public Account getAccountOfUser() {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user != null)
			return this.getAccountByQNID(user.getUserId());
		return null;
	}

	public Account createAccountForUser() {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user != null)
			return this.createAccount(user.getUserId(), user.getEmail(), user.getNickname());
		return null;
	}

	@SuppressWarnings("unchecked")
	public Account[] getAllAccounts() {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Account[] accounts = null;

		try {
			Query query = pm.newQuery(Account.class);

			List<Account> accountList = (List<Account>) query.execute();
			accounts = accountList.toArray(new Account[accountList.size()]);

			query.closeAll();
		} finally {
			pm.close();
		}

		return accounts;
	}

	@SuppressWarnings("unchecked")
	public Account getAccountByMail(String mail) {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Account account = null;

		try {
			String queryStr = "SELECT FROM " + Account.class.getName();
			queryStr += " WHERE email == '" + mail + "'";

			Query query = pm.newQuery(queryStr);

			List<Account> accountList = (List<Account>) query.execute();

			if (!accountList.isEmpty())
				account = accountList.get(0);

			query.closeAll();
		} finally {
			pm.close();
		}

		return account;
	}

	@SuppressWarnings("unchecked")
	public Account getAccountByQNID(String qnid) {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Account account = null;

		try {
			String queryStr = "SELECT FROM " + Account.class.getName();
			queryStr += " WHERE qnid == '" + qnid + "'";

			Query query = pm.newQuery(queryStr);

			List<Account> accountList = (List<Account>) query.execute();

			if (!accountList.isEmpty())
				account = accountList.get(0);

			query.closeAll();
		} finally {
			pm.close();
		}

		return account;
	}

	public Account createAccount(String userid, String mail, String nickname) {

		if (getAccountByQNID(userid) != null) {
			return null;
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();

		Account account = new Account();
		account.setQnid(userid);
		account.setEmail(mail);
		account.setNickname(nickname);
		account.setDateCreated(new Long(new Date().getTime() / 1000).toString());
		account.setDateFormat("dd.MM.yyyy");
		account.setTimeFormat("hh:mm:ss");

		try {
			pm.makePersistent(account);
		} finally {
			pm.flush();
			pm.close();
		}

		return account;

	}

	public void updateAccount(Account account) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			pm.makePersistent(account);
		} finally {
			pm.flush();
			pm.close();
		}
	}

	public void deleteAccount(Account account) {

		App apps[] = account.getApps();
		for (App app : apps) {
			QNObjectManager.getInstance().getAppMgr().deleteApp(app);
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			Account accToDelete = (Account) pm.getObjectById(Account.class, account.getQnid());
			if (accToDelete != null)
				pm.deletePersistent(accToDelete);
		} finally {
			pm.flush();
			pm.close();
		}
	}

	/**
	 * Invitations
	 */

	public Invitation createInvitation(Invitation invitation) {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		Key inviID = KeyFactory.createKey(Invitation.class.getSimpleName(), UUIDGenerator.getUUID());
		invitation.setInvitationID(inviID);
		invitation.setCreatedFromQNID(getAccountOfUser().getQnid());

		try {
			pm.makePersistent(invitation);
		} finally {
			pm.flush();
			pm.close();
		}

		return invitation;
	}

	@SuppressWarnings("unchecked")
	public Invitation[] getAllInvitations() {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Invitation[] invs = null;

		try {
			Query query = pm.newQuery(Invitation.class);

			List<Invitation> invList = (List<Invitation>) query.execute();
			invs = invList.toArray(new Invitation[invList.size()]);

			query.closeAll();
		} finally {
			pm.close();
		}

		return invs;

	}

	public void deleteInvitation(Invitation invitation) {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			Invitation invToDelete = (Invitation) pm.getObjectById(Invitation.class, invitation.getInvitationID());
			if (invToDelete != null)
				pm.deletePersistent(invToDelete);
		} finally {
			pm.flush();
			pm.close();
		}
	}
}
