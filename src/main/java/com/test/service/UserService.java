package com.test.service;

import java.io.UnsupportedEncodingException;

import com.aliyuncs.exceptions.ClientException;
import com.github.pagehelper.PageInfo;
import com.test.pojo.AgentUser;
import com.test.pojo.ResoultData;

public interface UserService {

	AgentUser getUserByUserName(String username);

	public PageInfo<AgentUser> getUser(String userName,Integer pageNo,Integer pageSize);

	ResoultData saveUser(AgentUser user);

	public void sendPhone(String code,String phone) throws ClientException, UnsupportedEncodingException;
}
