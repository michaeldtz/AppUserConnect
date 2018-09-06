 package com.md.appuserconnect.core.model;

import com.md.appuserconnect.core.model.accounts.AccountManager;
import com.md.appuserconnect.core.model.apps.AppManager;
import com.md.appuserconnect.core.model.delivery.DeliveryManager;
import com.md.appuserconnect.core.model.messages.MessageManager;
import com.md.appuserconnect.core.model.statistics.StatisticsManager;

public class QNObjectManager {

	private static QNObjectManager unique;

	private AccountManager  accMgr;
	private MessageManager  msgMgr;
	private AppManager      appMgr;
	private DeliveryManager devMgr;
	private StatisticsManager   staMgr;
	
	private QNObjectManager(){
		accMgr = new AccountManager();
		msgMgr = new MessageManager();
		appMgr = new AppManager();
		devMgr = new DeliveryManager();
		staMgr = new StatisticsManager();
	}
 
	public static QNObjectManager getInstance(){
		if(unique == null)
			unique = new QNObjectManager();
		return unique;
	}

	public AccountManager getAccMgr() {
		return accMgr;
	}

	public MessageManager getMsgMgr() {
		return msgMgr;
	}

	public AppManager getAppMgr() {
		return appMgr;
	}

	public DeliveryManager getDelMgr() {
		return devMgr;
	}

	public StatisticsManager getStaMgr() {
		return staMgr;
	}
	
	
	
}
