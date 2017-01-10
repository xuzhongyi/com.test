package com.test.persistence.user;

import java.util.List;

import com.test.pojo.AgentUser;

public interface AgentUserMapper {	
   List<AgentUser> selectUser();

   public void saveUser(AgentUser user);

   AgentUser selectUserByUserName(String mobileNo);
}
