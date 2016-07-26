package com.harsha;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class UpdateFeed {

	@Persistent
	private String feed;
	
	@Persistent
	private String userMail;
	
	@Persistent
	private long date;
	
	public String getUserMail() {
		
		return userMail;
	}

	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public void setFeed(String feedText) {
		// TODO Auto-generated method stub
		this.feed=feedText;
	}
	public String getFeed()
	{
		return feed;
	}
	
}
