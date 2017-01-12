package com.test.util;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.springframework.beans.propertyeditors.URLEditor;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * 发送短信的方法
 * @author xzy
 *
 */
public class AliSSM {

    public static void SSMUtil(String accessKey,String accessSecret,String signName,String templateCode,String code,String phone) throws ClientException, UnsupportedEncodingException{      
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKey, accessSecret);
         DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Sms",  "sms.aliyuncs.com");
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendSmsRequest request = new SingleSendSmsRequest();
        try {
        	signName = URLEncoder.encode(signName, "iso-8859-1");
        	signName = URLDecoder.decode(signName, "utf-8");
            request.setSignName(signName);
            request.setTemplateCode(templateCode);
            request.setParamString("{'mobileNo':'"+code+"'}");
            request.setRecNum(phone);
            client.getAcsResponse(request);
        } catch (ServerException e) {
            e.printStackTrace();
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws ClientException{
    	/*SSMUtil();*/
    }
    
    
}
