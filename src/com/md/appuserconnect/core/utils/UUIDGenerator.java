package com.md.appuserconnect.core.utils;

import java.util.UUID;

public class UUIDGenerator {

	public static String getUUID(){
		UUID qnidUUID = UUID.randomUUID();
		String qnid = qnidUUID.toString();
		return qnid.replaceAll("-", "");
	}
	
}
