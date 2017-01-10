package com.test.cache;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;



@Component("redisService")
public class SpringRedisServiceImpl implements RedisService {
    
    /** The Constant logger. */
    private static final Logger logger = LoggerFactory
            .getLogger(SpringRedisServiceImpl.class);

    // inject the actual template
    @Autowired
    private RedisTemplate<Object, Object> template;
    
    public Boolean hasKey(Object key) {
        logger.debug("Redis hasKey, key [{}]", key);
        return template.hasKey(key);
    }
    
    public void delete(Object key) {
        logger.debug("Redis delete, key [{}]", key);
        template.delete(key);
    }
    
    public void set(Object key, Object value) {
        logger.debug("Redis set, key [{}] value [{}]", key, value);
        template.opsForValue().set(key, value);
    }
    
    public void set(Object key, Object value, Long timeout) {
        template.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    public Object get(Object key) {
        logger.debug("Redis get, key [{}]", key);
        return template.opsForValue().get(key);
    }

    public Long leftPush(Object key, Object value) {
        logger.debug("Redis leftPush, key [{}] value [{}]", key, value);
        return template.opsForList().leftPush(key, value);
    }

    public Long leftPushAll(Object key, Object... values) {
        logger.debug("Redis leftPushAll, key [{}] values [{}]", key, values);
        return template.opsForList().leftPushAll(key, values);
    }

    public Object leftPop(Object key) {
        logger.debug("Redis leftPop, key [{}]", key);
        return template.opsForList().leftPop(key);
    }

    public Long rightPush(Object key, Object value) {
        logger.debug("Redis rightPush, key [{}] value [{}]", key, value);
        return template.opsForList().rightPush(key, value);
    }

    public Long rightPushAll(Object key, Object... values) {
        logger.debug("Redis rightPushAll, key [{}] values [{}]", key, values);
        return template.opsForList().rightPushAll(key, values);
    }

    public Object rightPop(Object key) {
        logger.debug("Redis rightPop, key [{}]", key);
        return template.opsForList().rightPop(key);
    }
    
    public Long listSize(Object key) {
        logger.debug("Redis listSize, key [{}]", key);
        return template.opsForList().size(key);
    }
    
    public List<Object> range(Object key,long start,long end) {
       	logger.debug("Redis list range, key [{}]", key);
       	return template.opsForList().range(key, start, end);
   	}
   	
    public Long addSet(Object key,Object ... values){
        logger.debug("Redis set, key [{}] values [{}]", key, values);
        return template.opsForSet().add(key, values);
    }
    
    public Set <Object> getSet(Object key){
    	logger.debug("Redis get set, key [{}]", key);
    	return template.opsForSet().members(key);
    }
    
    public Object popSet(Object key) {
    	logger.debug("Redis pop key, key [{}]", key);
    	return template.opsForSet().pop(key);
    }
    
    public Long getSetSize(Object key){
    	logger.debug("Redis Set Size, key [{}]", key);
    	return template.opsForSet().size(key);
    }
    
    
    public boolean isSetMember(Object key, Object member) {
    	return template.opsForSet().isMember(key,member);
    }
    
	
	public void addHash(Object key, Object field, Object value) {
		template.opsForHash().put(key, field, value);
	}

	
	public Object getHash(Object key, Object field) {
		return template.opsForHash().get(key, field);
	}
	
	
	
	public Set <Object> getHashKeys(Object key){
		return template.opsForHash().keys(key);
	}
	
	public Long incr(Object key) {
		return template.opsForValue().increment(key, 1);
	}
	
	public Long getIncr(Object key) {
		String val = template.boundValueOps(key).get(0, -1);
		if(StringUtils.isNotBlank(val)) {
			return Long.parseLong(val);
		}
		return null;
	}
	
	
	public void setKeyExpire(Object key,Object value,Long expireTime,TimeUnit unit){
		template.opsForValue().set(key, value, expireTime, unit);
	}

	
	public List<Object> getHashValues(Object key) {
		return template.opsForHash().values(key);
	}

	
	public Long getExpire(Object key, TimeUnit unit) {
		return template.getExpire(key, unit);
	}

	
	public Long incrBy(Object key,long delta) {
	    if(hasKey(key)){
		return template.opsForValue().increment(key, delta);
	    } else {
		return 0L;
	    }
	}

	
	public Long remvoeKeyFromSet(Object key, Object set) {
	    return template.opsForSet().remove(key, set);
	}

	
	public Map<Object,Object> hashGetAll(Object key) {
		return template.opsForHash().entries(key);
	}

	
	public void addHashAll(Object key, Map<Object, Object> map) {
		template.opsForHash().putAll(key, map);
	}

	
	public void deleteHash(Object key, Object ... hashkeys) {
		template.opsForHash().delete(key, hashkeys);
	}
	
	
	public Long getZSetCount(Object key)
	{
		return template.opsForZSet().size(key);
	}
	
	
	public void addOneToZSet(Object key,Object value,double score)
	{
		template.opsForZSet().add(key, value, score);
	}
	
	
	public void addSetToZSet(Object key,Set<TypedTuple<Object>> setObject)
	{
		template.opsForZSet().add(key, setObject);
	}
	
	
	public void removeOneFromZSet(Object key, Object value)
	{
		template.opsForZSet().remove(key, value);
	}
	
	
	public Set<TypedTuple<Object>> getZRevrangeWithScores(Object key,long start,long end)
	{
		return template.opsForZSet().reverseRangeWithScores(key, start, end);
	}
	
	
	public List<Object> getHashMultiGet(Object key,Collection<Object> hashKeys) {
		return template.opsForHash().multiGet(key, hashKeys);
	}

	
	public List<Object> getSortedObject(Integer pageIndex, Integer pageSize,
			String hashKey, String zsetKey) {
		if(hasKey(hashKey) && hasKey(zsetKey))
		{
			long start = (pageIndex-1)*pageSize;
			long end = pageIndex*pageSize-1;
			Set<TypedTuple<Object>> idZSet = getZRevrangeWithScores
					(zsetKey, start, end);
			List<Object> hashKeys=new LinkedList<Object>();
			for(TypedTuple<Object> idTuple : idZSet){
				hashKeys.add(idTuple.getValue());
			}
			List<Object> list = getHashMultiGet(hashKey, hashKeys);
			return list;
		}
		return null;
	}
	
}


