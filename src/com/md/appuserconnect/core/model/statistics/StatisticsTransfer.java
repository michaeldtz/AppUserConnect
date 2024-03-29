package com.md.appuserconnect.core.model.statistics;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.md.appuserconnect.core.connectors.KONAConnector;
import com.md.appuserconnect.core.model.QNObjectManager;
import com.md.appuserconnect.core.model.accounts.Account;

public class StatisticsTransfer {

	public String transferStatistics(Account acc, Date from, Date to, String appBundle){
		
		//Load Pure Statistic Data
		StatisticsManager statistics = QNObjectManager.getInstance().getStaMgr();
		Statistic[] stats = statistics.getStatistics(acc, from, to, appBundle);
		
		String xmlData1 = generateStatisticsXML(stats);
		
		//HashMap<String, Statistic> map = new HashMap<String, Statistic>();
		
		//String xmlData2 = generateAccessStatisticsXML(stats);
		
		KONAConnector kona = new KONAConnector(acc.getKonaUser(), acc.getKonaPass());
		try {
			String result = "OK\n";
			if(acc.getKonaStat1ID() != null & !acc.getKonaStat1ID().equals(""))
				result += kona.updateExistingDataset("AccessStats",acc.getKonaStat1ID(), xmlData1);
			else
				result += kona.createNewDataset("AccessStats", xmlData1);
			
			return result;
		} catch (IOException e) {
			return e.toString();
		}
		
	}
	
	private String generateStatisticsXML(Statistic[] stats){
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		
		StringBuffer xmlData = new StringBuffer();
		xmlData.append("<table><fields>");
		xmlData.append("<field name='StatID'      type='string'><qualification type='dimension'/></field>");
		xmlData.append("<field name='ClientID'    type='string'><qualification type='dimension'/></field>");
		xmlData.append("<field name='AccessDate'  type='string'><qualification type='dimension'/></field>");
		xmlData.append("<field name='Appbundle'   type='string'><qualification type='dimension'/></field>");
		xmlData.append("<field name='AppName'     type='string'><qualification type='dimension'/></field>");
		xmlData.append("<field name='AppVersion'  type='string'><qualification type='dimension'/></field>");
		xmlData.append("<field name='Position'    type='string'><qualification type='dimension'/></field>");
		xmlData.append("<field name='CountryCode' type='string'><qualification type='dimension'/></field>");
		xmlData.append("<field name='Language'    type='string'><qualification type='dimension'/></field>");
		xmlData.append("<field name='AccessCount' type='number'><qualification type='measure' aggregation='sum'/></field>");
		xmlData.append("</fields><rows>");
		for(Statistic stat : stats){
			xmlData.append("<r>");
			xmlData.append("<v>" + stat.getStaticticID().toString() + "</v>");
			xmlData.append("<v>" + stat.getClientID() + "</v>");
			xmlData.append("<v>" + dateFormat.format(new Date(new Long(stat.getDate()).longValue() * 1000)) + "</v>");
			xmlData.append("<v>" + stat.getAppbundle() + "</v>");
			xmlData.append("<v>" + stat.getAppname() + "</v>");
			xmlData.append("<v>" + stat.getAppversion() + "</v>");
			xmlData.append("<v>" + stat.getPosition() + "</v>");
			xmlData.append("<v>" + stat.getCountryCode() + "</v>");
			xmlData.append("<v>" + stat.getLanguage() + "</v>");
			xmlData.append("<v>1</v>");
			xmlData.append("</r>");
		}
		xmlData.append("</rows></table>");
		return xmlData.toString();
	}
	
	
}
