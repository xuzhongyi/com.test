package com.test.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.cache.RedisService;
import com.test.po.AgentMobileNo;
import com.test.po.AgentUserPo;
import com.test.pojo.AgentUser;
import com.test.pojo.ResoultData;
import com.test.service.UserService;
import com.test.util.RandomUtil;
import com.test.util.ThreadUtil;
import com.test.valid.PamaMethod;
import com.test.valid.PamaMethodValid;
import com.test.valid.PamaType;
import com.test.valid.RequestValid;

@Controller
@RequestMapping("/user")
public class LoginUser {

	@Autowired
	private RedisService redisService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private UserService userService;
	
	
	
	/**
	 * 
	 * login:(登录). <br/>
	 *
	 * @author alex shan
	 * @param loginVO
	 * @return
	 */
	@RequestMapping(value="/login",produces = {"application/json;charset=UTF-8"})
	@RequestValid()
	@ResponseBody
	public String login(@RequestBody @PamaMethodValid(pamaType=PamaType.COMPLEXTYPE)AgentUser agentUser,HttpServletResponse response)
	{
		ResoultData data=new ResoultData();
		/**
		 * 验证信息
		 */
		//用户验证的回调
		ThreadLocal<Map<String,String>> valid = ThreadUtil.getValid();
		//验证失败
		if(null!=valid && null!=valid.get()){
			Map<String, String> map = valid.get();
			for(String str:map.keySet()){
				data.setCore(str);
				data.setData(map.get(str));
			}
			JSONObject object = JSONObject.fromObject(data);
			return object.toString();
		}
		
		boolean isAuthenticated = false;
		UsernamePasswordToken token = new UsernamePasswordToken(agentUser.getMobileNo(), agentUser.getPassword());
		Subject subject = SecurityUtils.getSubject();
		try{
			subject.login(token);
			isAuthenticated = subject.isAuthenticated();
		}
		catch(UnknownAccountException e)
		{
			data.setCore("false");
			data.setData("号码未注册");
			JSONObject object = JSONObject.fromObject(data);
			return object.toString();
		}
		catch(IncorrectCredentialsException e)
		{
			data.setCore("false");
			data.setData("用户名/密码错误");
			JSONObject object = JSONObject.fromObject(data);
			return object.toString();
		}
		catch(Exception e)
		{
			data.setCore("false");
			data.setData("登录失败(可能是登录太多次失败，请等10分钟)");
			JSONObject object = JSONObject.fromObject(data);
			return object.toString();
		}
		if(!isAuthenticated)
		{
			data.setCore("false");
			data.setData("用户名密码错误");
			JSONObject object = JSONObject.fromObject(data);
			return object.toString();
		}
		
		//本地线程获取
		ThreadLocal<AgentUser> user = ThreadUtil.getUser();
		AgentUser user2 = user.get();
		
		//设置失效时间
		user2.setRemoveTime(System.currentTimeMillis());
		String id = RandomUtil.random();
		System.out.println(id);
		
		//返回tokenID TODO
		

		/*//token过期时间7200秒，即2小时
		Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.SECOND, Constants.EXPIR);
        
        //生成token
		String compactJws = Jwts.builder().setExpiration(cal.getTime()).setId(ins.getAppid()).setAudience(ins.getCode())
				  .signWith(SignatureAlgorithm.HS512, signKey).compact();
		log.info("{}生成新的token:{}",ins.getName(),compactJws);
		
		//存储到缓存
		redisService.set(Constants.TOKEN+ins.getAppid(), compactJws, (long)Constants.EXPIR);*/
		
		//去除user里面的敏感数据
		user2.setPassword("");
		user2.setSalt("");
		//从redis中去通过用户名查找是否存在正在登录的情况
		String userKey = (String)redisService.getHash("userKey", user2.getMobileNo());
		if(userKey!=null){
			redisService.deleteHash("token", userKey);
		}
		redisService.addHash("userKey", user2.getMobileNo(), id);
		redisService.addHash("token", id, user2);
		
		data.setCore("true");
		data.setData(id);
		JSONObject object = JSONObject.fromObject(data);
		return object.toString();
	}
	
	@RequestMapping(value="/toLogin")
	public String toLogin(HttpServletRequest request){
		
		//获取tokenId TODO
		
		String id = request.getSession().getId();
		System.out.println(id);
		
		//计算用户登录情况
		AgentUser user = (AgentUser)redisService.getHash("token", id);
		if(user != null){
			Long removeTime = user.getRemoveTime();
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 4);
			Date time = calendar.getTime();
			if(removeTime<time.getTime()){
				//产生新的tokenId
				String tokenId = RandomUtil.random();
				redisService.addHash("userKey", user.getMobileNo(), tokenId);
				if(null!=id){
					redisService.deleteHash("token", id);
				}
				redisService.addHash("token", tokenId, user);
				
				//返回tokenID TODO
				
			}
		}
		return "ok";
	}
	
	@RequestMapping(value="getMobileNo",produces = {"application/json;charset=UTF-8"})
	@RequestValid()
	@ResponseBody
	public String getMobileNo(@RequestBody @PamaMethodValid(pamaType=PamaType.COMPLEXTYPE) AgentMobileNo agentMobile){
		ResoultData data=new ResoultData();
		/**
		 * 验证信息
		 */
		//用户验证的回调
		ThreadLocal<Map<String,String>> valid = ThreadUtil.getValid();
		// 验证失败
		if (null != valid && null != valid.get()) {
			Map<String, String> map = valid.get();
			for (String str : map.keySet()) {
				data.setCore(str);
				data.setData(map.get(str));
			}
		} else {
			// 判断用户是否已经注册了
			AgentUser agentUser = userService.getUserByUserName(agentMobile.getMobileNo());
			if (null != agentUser) {
				data.setCore("false");
				data.setData("手机号码已注册");
				JSONObject object = JSONObject.fromObject(data);
				return object.toString();
			}
			
			//发送短信  TODO
			data.setCore("true");
			data.setData("123456");

			// 将短信发送的验证放入redis保存时间5分钟
			Object object = redisService.get(agentMobile.getMobileNo());
			if (null == object || "".equals(object)) {
				redisService.set(agentMobile.getMobileNo(), "123456", 60 * 5L);
			}
		}
		JSONObject object = JSONObject.fromObject(data);
		return object.toString();
	}
	
	@RequestMapping(value="saveUser",produces = {"application/json;charset=UTF-8"})
	@RequestValid()
	@ResponseBody
	public String saveUser(@RequestBody @PamaMethodValid(pamaType=PamaType.COMPLEXTYPE) AgentUserPo user){
		ResoultData data=new ResoultData();
		/**
		 * 验证信息
		 */
		//用户验证的回调
		ThreadLocal<Map<String,String>> valid = ThreadUtil.getValid();
		//验证失败
		if(null!=valid && null!=valid.get()){
			Map<String, String> map = valid.get();
			for(String str:map.keySet()){
				data.setCore(str);
				data.setData(map.get(str));
			}
		}else{
			//验证码验证
			Object object = redisService.get(user.getMobileNo());
			if(null!=object && !"".equals((String)object) && user.getValidNo().equals((String)object)){
				data=userService.saveUser(user);
			}else{
				data.setCore("false");
				data.setData("验证码错误");
			}
		}
		JSONObject object = JSONObject.fromObject(data);
		return object.toString();
	}
}
