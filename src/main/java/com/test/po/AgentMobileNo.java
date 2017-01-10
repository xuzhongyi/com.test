package com.test.po;

import com.test.valid.PamaField;
import com.test.valid.PamaFieldValid;

public class AgentMobileNo {

	@PamaFieldValid(pamaField=PamaField.ISMOBILE)
	private String mobileNo;

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	
	
	
}
