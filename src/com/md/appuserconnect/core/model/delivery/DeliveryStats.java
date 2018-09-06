package com.md.appuserconnect.core.model.delivery;

import java.util.HashMap;
import java.util.Iterator;

import com.md.appuserconnect.core.model.QNObjectManager;
import com.md.appuserconnect.core.model.messages.Message;

public class DeliveryStats {

	private int totalDelivery;
	private int totalClients;
	private int clientsWithOne;
	private int clientsWithTwo;
	private int clientsWithThree;
	private int clientsWithAll;
	private double avgDelPerClient;
	private String summary;

	public static DeliveryStats createDeliveryStatsForMessage(Message msg) {

		DeliveryStats deliveryStats = new DeliveryStats();
		Delivery deliveries[] = QNObjectManager.getInstance().getDelMgr().getDeliveriesOfMessage(msg.getMessageID());

		HashMap<String, Integer> clientCount = new HashMap<String, Integer>();

		for (Delivery del : deliveries) {

			Integer clCount = clientCount.get(del.getClientID());
			if (clCount == null)
				clCount = new Integer(0);
			clCount++;
			clientCount.put(del.getClientID(), clCount);
		}

		deliveryStats.totalDelivery = deliveries.length;
		deliveryStats.totalClients = clientCount.keySet().size();
		if (deliveryStats.totalClients > 0)
			deliveryStats.avgDelPerClient = ((double) deliveryStats.totalDelivery) / ((double) deliveryStats.totalClients);
		deliveryStats.clientsWithOne = 0;
		deliveryStats.clientsWithTwo = 0;
		deliveryStats.clientsWithThree = 0;
		deliveryStats.clientsWithAll = 0;

		Iterator<String> clIterator = clientCount.keySet().iterator();
		while (clIterator.hasNext()) {
			String clientID = clIterator.next();
			int clientDelCount = clientCount.get(clientID);

			if (clientDelCount == 1)
				deliveryStats.clientsWithOne++;
			if (clientDelCount == 2)
				deliveryStats.clientsWithTwo++;
			if (clientDelCount == 3)
				deliveryStats.clientsWithThree++;
			if (clientDelCount == new Integer(msg.getRepeatCount()).intValue())
				deliveryStats.clientsWithAll++;
		}

		deliveryStats.summary = "D: " + deliveryStats.totalDelivery + " | C: " + deliveryStats.totalClients + " D/C: "
				+ deliveryStats.avgDelPerClient + "\n";
		deliveryStats.summary += "C1: " + deliveryStats.clientsWithOne + " | C2: " + deliveryStats.clientsWithTwo + " | C3: "
				+ deliveryStats.clientsWithThree + " | CA: " + deliveryStats.clientsWithAll;
		return deliveryStats;
	}

	private DeliveryStats() {

	}

	public int getTotalDelivery() {
		return totalDelivery;
	}

	public int getTotalClients() {
		return totalClients;
	}

	public int getClientsWithOne() {
		return clientsWithOne;
	}

	public int getClientsWithTwo() {
		return clientsWithTwo;
	}

	public int getClientsWithThree() {
		return clientsWithThree;
	}

	public int getClientsWithAll() {
		return clientsWithAll;
	}

	public double getAvgDelPerClient() {
		return avgDelPerClient;
	}

	public String getSummary() {
		return summary;
	}

}
