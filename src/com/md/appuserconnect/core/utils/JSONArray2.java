package com.md.appuserconnect.core.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;


public class JSONArray2 extends JSONArray {

	public void sendAsRepsonse(HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println(this.toString());	
	}

}
