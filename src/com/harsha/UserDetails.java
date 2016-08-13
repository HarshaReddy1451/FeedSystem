package com.harsha;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class UserDetails {

	@Persistent
	private String signUpUserName;

	@Persistent
	private String signUpPassword;

	@Persistent
	private String signUpEmail;

	@Persistent
	private long date;
	
	@Persistent
	private boolean isDelete;

	@Persistent
	private String source;

	@Persistent
	private String profilePicture;
	
	public String getSignUpUserName() {
		return signUpUserName;
	}

	public void setSignUpUserName(String signUpUserName) {
		this.signUpUserName = signUpUserName;
	}

	public String getSignUpPassword() {
		return signUpPassword;
	}

	public void setSignUpPassword(String signUpPassword) {
		this.signUpPassword = signUpPassword;
	}

	public String getSignUpEmail() {
		return signUpEmail;
	}

	public void setSignUpEmail(String signUpEmail) {
		this.signUpEmail = signUpEmail;
	}

	public void setDate(long l) {
		// TODO Auto-generated method stub
		this.date = l;
	}

	public long getDate() {
		return date;
	}

	public void setIsDelete(boolean b) {
		// TODO Auto-generated method stub
		this.isDelete=b;
	}
	public boolean getIsDelete()
	{
		return isDelete;
	}

	public void setSource(String source) {
		// TODO Auto-generated method stub
		this.source=source;
	}
	public String getSource()
	{
		return source;
	}

	public void setProfilePic(String picture) {
		// TODO Auto-generated method stub
		this.profilePicture=picture;
	}
	
	public String getProfilePic()
	{
		return profilePicture;
	}
}
