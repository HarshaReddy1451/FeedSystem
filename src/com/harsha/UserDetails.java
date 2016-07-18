package com.harsha;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class UserDetails {

	@Persistent
	private String signUpUserName;
	
	@Persistent
	private String signUpPassword;
	
	@Persistent
	private String signUpConfirmPassword;
	
	@Persistent
	private String signUpEmail;

	
	private Date date;
	
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
	public String getSignUpConfirmPassword() {
		return signUpConfirmPassword;
	}
	public void setSignUpConfirmPassword(String signUpConfirmPassword) {
		this.signUpConfirmPassword = signUpConfirmPassword;
	}
	public String getSignUpEmail() {
		return signUpEmail;
	}
	public void setSignUpEmail(String signUpEmail) {
		this.signUpEmail = signUpEmail;
	}
	public void setDate(Date date) {
		// TODO Auto-generated method stub
		this.date=date;
	}
	public Date getDate()
	{
		return date;
	}
	
	
}
