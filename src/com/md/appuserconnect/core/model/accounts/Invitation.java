package com.md.appuserconnect.core.model.accounts;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.md.appuserconnect.core.model.QNObjectManager;

@SuppressWarnings("serial")
@PersistenceCapable
public class Invitation implements Serializable {

	@PrimaryKey
	@Persistent
	private Key invitationID;

	@Persistent
	private String email = "";

	@Persistent
	private String createdFromQNID;

	@Persistent
	private Boolean invitationUsed;

	public Key getInvitationID() {
		return invitationID;
	}

	public String getInvIDString() {
		return invitationID.getName();
	}
	
	public void setInvitationID(Key invitationID) {
		this.invitationID = invitationID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCreatedFromQNID() {
		return createdFromQNID;
	}

	public void setCreatedFromQNID(String createdFromQNID) {
		this.createdFromQNID = createdFromQNID;
	}

	public Boolean isInvitationUsed() {
		return invitationUsed;
	}

	public void setInvitationUsed(Boolean invitationUsed) {
		this.invitationUsed = invitationUsed;
	}

	public String getInvitationUsedString(){
		if(invitationUsed==null)
			return "No";
		if(invitationUsed)
			return "Yes";
		return "No";
	}
	
	public String getCreatedFromEmail(){
		return QNObjectManager.getInstance().getAccMgr().getAccountByQNID(createdFromQNID).getEmail();
	}

}
