package com.test.po;

import com.test.pojo.AgentUser;
import com.test.valid.PamaField;
import com.test.valid.PamaFieldValid;

public class AgentUserPo extends AgentUser{

	//验证码获取
	@PamaFieldValid(pamaField=PamaField.REQUEST)
	private String validNo;

	public String getValidNo() {
		return validNo;
	}

	public void setValidNo(String validNo) {
		this.validNo = validNo;
	}
	
	
}
