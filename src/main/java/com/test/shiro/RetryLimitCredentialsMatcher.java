package com.test.shiro;



import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher {

	//集群中可能会导致出现验证多过20次的现象，因为AtomicInteger只能保证单节点并发    
	private Cache<String, AtomicInteger> passwordRetryCache;

	    public RetryLimitCredentialsMatcher(CacheManager cacheManager) {
	        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
	    }

	    @Override
	    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
	        String username = (String)token.getPrincipal();
	        //retry count + 1
	        AtomicInteger retryCount = passwordRetryCache.get(username);
	        if(null == retryCount) {
	            retryCount = new AtomicInteger(0);
	            passwordRetryCache.put(username, retryCount);
	        }
	        if(retryCount.incrementAndGet() > 20) {
	            throw new ExcessiveAttemptsException("username: " + username + " tried to login more than 5 times in period"
	); } 
	         boolean matches = super.doCredentialsMatch(token, info); 
	         if(matches) {
	              //clear retry data
	              passwordRetryCache.remove(username); 
	         } 
	         return matches; 
	    }
	}
