package com.md.appuserconnect.core.connectors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.appengine.repackaged.com.google.common.util.Base64;

public class KONAConnector {

	String CRLF = "\r\n";
	private String username = "mi.dietz@sap.com";
	private String password = "walldorf45";

	public KONAConnector(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public void getListOfDatasets() throws IOException {

		String userpassword = username + ":" + password;
		String encodedAuthorization = Base64.encode(userpassword.getBytes());

		URL url = new URL("https://bi.ondemand.com/v1/items/1ddseGI3vW-kj1KQH6mOrBK31X");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("POST");
		conn.setUseCaches(false);
		conn.setReadTimeout(15 * 1000);
		conn.setDoOutput(true);
		conn.setRequestProperty("Authorization", "Basic " + encodedAuthorization);

		// conn.getHeaderFields()
	}

	public String updateExistingDataset(String datasetName, String datasetId, String xmlData) throws IOException {

		String userpassword = username + ":" + password;
		String encodedAuthorization = Base64.encode(userpassword.getBytes());

		String data = "<dataset name='" + datasetName + "' visibility='public'><source>QNotify</source><description>QNotify Statistics</description>"
				+ xmlData + "</dataset>";

		URL url = new URL("https://bi.ondemand.com/v1/datasets/" + datasetId);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("PUT");
		conn.setUseCaches(false);
		conn.setReadTimeout(15 * 1000);
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "text/xml");
		conn.setRequestProperty("Content-Size", new Integer(data.length()).toString());
		conn.setRequestProperty("Authorization", "Basic " + encodedAuthorization);

		PrintWriter pw = new PrintWriter(new OutputStreamWriter(conn.getOutputStream()), true);
		pw.println(data);
		pw.flush();
		pw.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuffer buffer = new StringBuffer();
		String thisLine;
		while ((thisLine = br.readLine()) != null) {
			buffer.append(thisLine);
		}
		return buffer.toString();
	}

	public String createNewDataset(String datasetName, String xmlData) throws IOException {

		String userpassword = username + ":" + password;
		String encodedAuthorization = Base64.encode(userpassword.getBytes());

		String data = "<dataset name='" + datasetName + "' visibility='public'><source>QNotify</source><description>QNotify Statistics</description>"
				+ xmlData + "</dataset>";

		URL url = new URL("https://bi.ondemand.com/v1/datasets");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("POST");
		conn.setUseCaches(false);
		conn.setReadTimeout(15 * 1000);
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "text/xml");
		conn.setRequestProperty("Content-Size", new Integer(data.length()).toString());
		conn.setRequestProperty("Authorization", "Basic " + encodedAuthorization);

		PrintWriter pw = new PrintWriter(new OutputStreamWriter(conn.getOutputStream()), true);
		pw.println(data);
		pw.flush();
		pw.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuffer buffer = new StringBuffer();
		String thisLine;
		while ((thisLine = br.readLine()) != null) {
			buffer.append(thisLine);
		}
		return buffer.toString();
	}

	/*
	 * public void sendDataToKona() {
	 * 
	 * try {
	 * 
	 * String metadata = "<?xml version='1.0' encoding='UTF-8'?>" +
	 * "<item type='dataset'><name>Sales</name>" +
	 * "<parent_folder id='my_stuff'/>" + "<description></description>" +
	 * "<dataset type='file'>" + "<source>test data</source>" +
	 * "</dataset></item>";
	 * 
	 * String data =
	 * "<table><fields><field name='Country' type='string'><qualification type='dimension'/></field></fields><rows><r><v>Region 1</v></r></rows></table>"
	 * ; String contentData = "metadata=" + metadata + "&data=" + data; String
	 * charset = "UTF-8";
	 * 
	 * String userpassword = username + ":" + password; String
	 * encodedAuthorization = Base64.encode(userpassword.getBytes()); String
	 * boundary = Long.toHexString(System.currentTimeMillis());
	 * 
	 * //URL url = new
	 * URL("https://resources.ondemand.com/biod/developer/session_management.html"
	 * ); URL url = new URL("https://bi.ondemand.com/v2/items");
	 * 
	 * System.out.println("LOADING URL: "+ url.getPath()); HttpURLConnection
	 * conn = (HttpURLConnection) url.openConnection();
	 * 
	 * conn.setRequestMethod("POST"); conn.setUseCaches(false);
	 * conn.setReadTimeout(15*1000); conn.setDoOutput(true);
	 * conn.setRequestProperty("Content-Type", "multipart/form-data; boundary="
	 * + boundary); // conn.setRequestProperty("Content-Type",
	 * "application/xml"); conn.setRequestProperty("Content-Size", new
	 * Integer(contentData.length()).toString());
	 * conn.setRequestProperty("User-Agent", "JAVA");
	 * conn.setRequestProperty("Authorization", "Basic " +
	 * encodedAuthorization);
	 * 
	 * PrintWriter pw = new PrintWriter(new
	 * OutputStreamWriter(conn.getOutputStream()), true);
	 * 
	 * pw.append("--" + boundary).append(CRLF);
	 * pw.append("Content-Disposition: form-data; name=\"metadata\""
	 * ).append(CRLF); pw.append("Content-Type: application/xml").append(CRLF);
	 * pw.append(CRLF); pw.append(metadata).append(CRLF).flush();
	 * 
	 * // Send text file. pw.append("--" + boundary).append(CRLF);
	 * pw.append("Content-Disposition: form-data; name=\"data\"").append(CRLF);
	 * pw.append("Content-Type: application/xml").append(CRLF);
	 * pw.append(CRLF).flush(); pw.append(data).append(CRLF).flush();
	 * 
	 * pw.append(CRLF).flush(); // CRLF is important! It indicates end of binary
	 * boundary. pw.append("--" + boundary + "--").append(CRLF);
	 * 
	 * 
	 * pw.flush(); pw.close();
	 * 
	 * BufferedReader br = new BufferedReader(new
	 * InputStreamReader(conn.getInputStream())); String thisLine; while
	 * ((thisLine = br.readLine()) != null) { System.out.println(thisLine); } }
	 * catch (Exception e) { System.err.println(e.toString()); } }
	 */
}
