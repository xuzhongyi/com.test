package com.test.shiro;



import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.test.pojo.AgentUser;
import com.test.service.UserService;
import com.test.util.ThreadUtil;


public class UserRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String)token.getPrincipal();

		AgentUser user = userService.getUserByUserName(username);

        if(user == null) {
            throw new UnknownAccountException();//没找到帐号
        }
        
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getMobileNo(), //用户名
                user.getPassword(), //密码
                ByteSource.Util.bytes(user.getSalt()),//加盐
                getName()  //realm name
        );
        
        //线程塞入用户信息
        ThreadLocal<AgentUser> usera=new ThreadLocal<AgentUser>();
		usera.set(user);
		ThreadUtil.setUser(usera);
		
        return authenticationInfo;
	}
}
