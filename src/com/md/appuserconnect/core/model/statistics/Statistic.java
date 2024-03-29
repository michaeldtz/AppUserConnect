package com.md.appuserconnect.core.model.statistics;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Statistic {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key staticticID;
	
	@Persistent
	private String qnid;
	
	@Persistent 
	private String clientID;
	
	@Persistent
	private String appbundle; 
	
	@Persistent
	private String appname;
	 
	@Persistent
	private String appversion;
	
	@Persistent
	private String position;
	
	@Persistent
	private String countryCode;
	
	@Persistent
	private String language;
	
	@Persistent
	private String date;

	
	
	public Statistic(String qnid, String clientID, String appbundle, String appname, String appversion, String position, String countryCode,
			String language) {
		super();
		this.qnid = qnid;
		this.clientID = clientID;
		this.appbundle = appbundle;
		this.appname = appname;
		this.appversion = appversion;
		this.position = position;
		this.countryCode = countryCode;
		this.language = language;
	}

	public Key getStaticticID() {
		return staticticID;
	}

	public void setStaticticID(Key staticticID) {
		this.staticticID = staticticID;
	}

	public String getQnid() {
		return qnid;
	}

	public void setQnid(String qnid) {
		this.qnid = qnid;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getAppbundle() {
		return appbundle;
	}

	public void setAppbundle(String appbundle) {
		this.appbundle = appbundle;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getAppversion() {
		return appversion;
	}

	public void setAppversion(String appversion) {
		this.appversion = appversion;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}



}

