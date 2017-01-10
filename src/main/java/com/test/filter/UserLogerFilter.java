package com.test.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.test.cache.RedisService;
import com.test.pojo.AgentUser;
import com.test.pojo.ResoultData;
import com.test.util.RandomUtil;

@Service
public class UserLogerFilter implements Filter{

	@Autowired
	private RedisService redisService;
	
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp=(HttpServletResponse)response;
		StringBuffer requestURL = req.getRequestURL();
		String[] split = StringUtils.split(requestURL.toString(),"/");
		for(String spl:split){
			
			//完成登录验证功能
			if("main".equals(spl)){
				
				//获取tokenId
				
				
				String tokenAll = req.getHeader("token");
				System.out.println(tokenAll);
				
				if(null==tokenAll || "".equals(tokenAll) || StringUtils.split(tokenAll,"@").length!=2){
					//用@来分割token
						PrintWriter writer = resp.getWriter();
						ResoultData res=new ResoultData();
						res.setCore("fail");
						res.setData("没有进行登录");
						JSONObject json = JSONObject.fromObject(res);
						writer.append(json.toString());
			            return;
				}
				String[] tokens = StringUtils.split(tokenAll,"@");
				//如果注入失败的情况下
				if (redisService == null) {//解决service为null无法注入问题 
			         System.out.println("redisService is null!!!"); 
			         BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(req.getSession().getServletContext()); 
			         redisService = (RedisService) factory.getBean("redisService"); 
			    }
				
				//计算用户登录情况
				AgentUser user = (AgentUser)redisService.getHash("token", tokens[1]);
				if(user != null && user.getMobileNo().equals(tokens[0])){
					Long removeTime = user.getRemoveTime();
					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 4);
					Date time = calendar.getTime();
					if(removeTime<time.getTime()){
						//产生新的tokenId
						String tokenId = RandomUtil.random();
						redisService.addHash("userKey", user.getMobileNo(), tokenId);
						if(null!=tokens[1]){
							redisService.deleteHash("token", tokens[1]);
						}
						//更新移除时间
						user.setRemoveTime(System.currentTimeMillis());
						redisService.addHash("token", tokenId, user);
						
						//返回tokenID 
						resp.addHeader(tokens[1], tokenId);
						chain.doFilter(request, response);
						return;
					}
				}
				PrintWriter writer = resp.getWriter();
				ResoultData res=new ResoultData();
				res.setCore("fail");
				res.setData("没有进行登录");
				JSONObject json = JSONObject.fromObject(res);
				writer.append(json.toString());
	            return;
			}
		}
		chain.doFilter(request, response);
		return;
	}

	public void destroy() {
		
	}

}
