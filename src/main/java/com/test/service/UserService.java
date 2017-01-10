package com.test.service;

import com.github.pagehelper.PageInfo;
import com.test.pojo.AgentUser;
import com.test.pojo.ResoultData;

public interface UserService {

	AgentUser getUserByUserName(String username);

	public PageInfo<AgentUser> getUser(String userName,Integer pageNo,Integer pageSize);

	ResoultData saveUser(AgentUser user);
}
