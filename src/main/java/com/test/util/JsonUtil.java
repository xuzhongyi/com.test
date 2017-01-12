package com.test.util;

import net.sf.json.JSONObject;

public class JsonUtil {

	public static String getStringByObject(Object object){
		JSONObject fromObject = JSONObject.fromObject(object);
		return fromObject.toString();
	}
	
}
