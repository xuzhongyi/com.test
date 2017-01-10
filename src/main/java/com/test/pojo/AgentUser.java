package com.test.pojo;

import com.test.valid.PamaField;
import com.test.valid.PamaFieldValid;

public class AgentUser {

	private String userName;
	
	@PamaFieldValid(pamaField=PamaField.ISMOBILE)
	private String mobileNo;
	
	@PamaFieldValid(pamaField=PamaField.REQUEST)
	private String password;
	
	private Long removeTime;
	/**
	 * 盐（shrio）
	 */
	private String salt;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public Long getRemoveTime() {
		return removeTime;
	}
	public void setRemoveTime(Long removeTime) {
		this.removeTime = removeTime;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	
	
	
}
