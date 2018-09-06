package com.md.appuserconnect.core.model.statistics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.md.appuserconnect.core.model.accounts.Account;
import com.md.appuserconnect.core.model.persistence.PMF;

public class StatisticsManager {

	public void addStatistic(String qnid, String clientID, String appbundle, String appname, String appversion, String position, String countryCode,
			String language) {
		createStatistic(qnid, clientID, appbundle, appname, appversion, position, countryCode, language);
	}

	private void createStatistic(String qnid, String clientID, String appbundle, String appname, String appversion, String position,
			String countryCode, String language) {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		Statistic statistic = new Statistic(qnid, clientID, appbundle, appname, appversion, position, countryCode, language);
		statistic.setDate(new Long(new Date().getTime() / 1000).toString());

		try {
			pm.makePersistent(statistic);
		} finally {
			pm.flush();
			pm.close();
		}
	}

	public static Date convert2Date(String dateString) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		return dateFormat.parse(dateString);
	}

	public Statistic[] getStatistics(Account acc, Date from, Date to) {
		return getStatistics(acc, from, to, null);
	}

	@SuppressWarnings("unchecked")
	public Statistic[] getStatistics(Account acc, Date fromDate, Date toDate, String appBundle) {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Statistic[] statistics = null;

		String fromDateAsNum = new Long(fromDate.getTime() / 1000).toString();
		String toDateAsNum = new Long((toDate.getTime() + 86400000) / 1000).toString();

		try {
			String queryStr = "SELECT FROM " + Statistic.class.getName();
			queryStr += " WHERE date >= '" + fromDateAsNum + "'";
			queryStr += " &&    date <= '" + toDateAsNum + "'";

			if (appBundle != null && !appBundle.equals(""))
				queryStr += " &&    appbundle == '" + appBundle + "'";

			queryStr += " &&    qnid  == '" + acc.getQnid() + "' ORDER BY date";

			Query query = pm.newQuery(queryStr);

			List<Statistic> statList = (List<Statistic>) query.execute();
			statistics = statList.toArray(new Statistic[statList.size()]);

			query.closeAll();
		} finally {
			pm.close();
		}

		return statistics;
	}

	@SuppressWarnings("unchecked")
	public Statistic[] getStatisticSums(Account acc, Date fromDate, Date toDate, String appBundle, String grouping) {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Statistic[] statistics = null;

		String fromDateAsNum = new Long(fromDate.getTime() / 1000).toString();
		String toDateAsNum = new Long((toDate.getTime() + 86400000) / 1000).toString();

		try {
			String queryStr = "SELECT COUNT(clientID) FROM " + Statistic.class.getName();
			queryStr += " WHERE date >= '" + fromDateAsNum + "'";
			queryStr += " &&    date <= '" + toDateAsNum + "'";

			if (appBundle != null && !appBundle.equals(""))
				queryStr += " &&    appbundle == '" + appBundle + "'";

			queryStr += " &&    qnid  == '" + acc.getQnid() + "' ORDER BY date GROUP BY VERSION" ;

			Query query = pm.newQuery(queryStr);

			List<Statistic> statList = (List<Statistic>) query.execute();
			statistics = statList.toArray(new Statistic[statList.size()]);

			query.closeAll();
		} finally {
			pm.close();
		}

		return statistics;
	}
}
