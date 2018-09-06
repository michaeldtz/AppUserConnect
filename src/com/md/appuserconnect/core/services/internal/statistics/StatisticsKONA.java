package com.md.appuserconnect.core.services.internal.statistics;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.md.appuserconnect.core.model.QNObjectManager;
import com.md.appuserconnect.core.model.accounts.Account;
import com.md.appuserconnect.core.model.statistics.StatisticsManager;
import com.md.appuserconnect.core.model.statistics.StatisticsTransfer;
import com.md.appuserconnect.core.utils.RRServices;

@SuppressWarnings("serial")
public class StatisticsKONA extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		if (RRServices.checkUserIsAdministrator(req, resp)) {
			PrintWriter writ = resp.getWriter();

			HashMap<String, Object> params = RRServices.loadInputParameters(req, false);
			String action = (String) params.get("action");
			String dateFrom = (String) params.get("datefrom");
			String dateTo = (String) params.get("dateto");
			String appBundle = (String) params.get("appbundle");

			if (action != null && action.equalsIgnoreCase("transfer")) {

				Date fromDate = new Date((new Date().getTime()) - 1209600000);
				if (dateFrom != null) {
					try {
						fromDate = StatisticsManager.convert2Date(dateFrom);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}

				Date toDate = new Date();
				if (dateTo != null) {
					try {
						toDate = StatisticsManager.convert2Date(dateTo);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				resp.setContentType("text/html");

				Account acc = QNObjectManager.getInstance().getAccMgr().getAccountOfUser();
				if (acc != null) {
					StatisticsTransfer transfer = new StatisticsTransfer();
					String answer = transfer.transferStatistics(acc, fromDate, toDate, appBundle);
					resp.getWriter().println(answer);
				}

			} else {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
				Date fromDate = new Date((new Date().getTime()) - 1209600000);
				Date toDate = new Date();

				resp.setContentType("text/html");

				writ.println("<html><head><title>Load Statistics</title></head><body>");
				writ.println("<form method='GET'><table>");
				writ.println("<tr><td>From Date</td><td><input type='input' name='datefrom' value='" + dateFormat.format(fromDate) + "'/></td></tr>");
				writ.println("<tr><td>To Date</td><td><input type='input' name='dateto' value='" + dateFormat.format(toDate) + "'/></td></tr>");
				writ.println("<tr><td>AppBundle</td><td><input type='input' name='appbundle'/></td></tr>");
				writ.println("</table>");
				writ.println("<input type='hidden' name='action' value='transfer'/><input type='submit' value='select'/></form>");
				writ.println("</body></html>");
			}
		}
	}
}
