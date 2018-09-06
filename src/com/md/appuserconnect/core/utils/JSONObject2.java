package com.md.appuserconnect.core.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;


public class JSONObject2 extends JSONObject {

	public void sendAsRepsonse(HttpServletResponse response) throws IOException{
		response.setContentType("text/plain");
		response.getWriter().println(this.toString());
	}
	
}
