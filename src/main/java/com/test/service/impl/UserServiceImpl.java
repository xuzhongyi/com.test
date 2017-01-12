package com.test.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyuncs.exceptions.ClientException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.persistence.user.AgentUserMapper;
import com.test.pojo.AgentUser;
import com.test.pojo.ResoultData;
import com.test.service.UserService;
import com.test.util.AliSSM;

@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired
	private AgentUserMapper agentUserMapper;
	
	@Value("${accessKey}")
	String accessKey;
	@Value("${accessSecret}")
	String accessSecret;
	@Value("${signName}")
	String signName;
	@Value("${templateCode}")
	String templateCode;
	
	public AgentUser getUserByUserName(String username) {
		return agentUserMapper.selectUserByUserName(username);
	}
	
	public PageInfo<AgentUser> getUser(String userName,Integer pageNo,Integer pageSize){
		pageNo=pageNo == null?1:pageNo;
		pageSize=pageSize == null?10:pageSize;
	    PageHelper.startPage(pageNo, pageSize);
	    List<AgentUser> list = agentUserMapper.selectUser();
	    //用PageInfo对结果进行包装
	    PageInfo<AgentUser> page = new PageInfo<AgentUser>(list);
	    return page;
	}

	@Override
	public ResoultData saveUser(AgentUser user) {
		ResoultData data=new ResoultData();
		try{
			AgentUser agentUser = getUserByUserName(user.getMobileNo());
			if(null!=agentUser){
				data.setCore("false");
				data.setData("用户已经注册");
				return data;
			}
			//密码加密
			String pass = user.getPassword();
			SecureRandomNumberGenerator secureRandomNumberGenerator = new SecureRandomNumberGenerator();  
			String salt = secureRandomNumberGenerator.nextBytes().toHex();
			//加盐和加密次数
			String password = new Md5Hash(pass,salt,3).toHex();
			user.setPassword(password);
			user.setSalt(salt);
			//用户保存
			agentUserMapper.saveUser(user);
			data.setCore("true");
			data.setData("用户保存成功");
		}catch(Exception e){
			data.setCore("false");
			data.setData("用户保存失败");
		}
		return data;
	}
	
	/**
	 * 调用阿里的短信接口
	 * @throws ClientException 
	 * @throws UnsupportedEncodingException 
	 */
	public void sendPhone(String code,String phone) throws ClientException, UnsupportedEncodingException{
		AliSSM.SSMUtil(accessKey, accessSecret, signName, templateCode, code, phone);
	}
}