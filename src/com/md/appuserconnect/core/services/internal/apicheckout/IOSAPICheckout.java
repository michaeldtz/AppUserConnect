package com.md.appuserconnect.core.services.internal.apicheckout;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.md.appuserconnect.core.utils.RRServices;

@SuppressWarnings("serial")
public class IOSAPICheckout extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		if (RRServices.checkUserHasValidAccount(req, resp)) {

			String type = (String) req.getParameter("type");

			File f = new File("apipackages/" + type + "apipackage.zip");

			if (f.exists()) {
				FileInputStream fis = new FileInputStream(f);
				int ch = -1;
				while (((ch = fis.read()) >= 0)) {
					resp.getOutputStream().write(ch);
				}
			} else {
				resp.setContentType("text/plain");
				PrintWriter pw = resp.getWriter();
				pw.println("Type Not Found");
			}
		}
	}
}
