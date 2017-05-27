package com.gwghk.mis.service;

import com.gwghk.mis.util.APIAuth;
import com.gwghk.mis.util.APIToken;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gwghk.mis.enums.ApiDir;
import com.gwghk.mis.model.TokenAccess;
import com.gwghk.mis.util.HttpClientUtils;
import com.gwghk.mis.util.PropertiesUtil;

/**
 * Node API请求服务类
 * @author Alan.wu
 * @date  2015年4月1日
 */
@Service
public class PmApiService{
	private static final Logger logger = LoggerFactory.getLogger(PmApiService.class);
	/**
	 * 格式请求url
	 * @return
	 */
	private String formatUrl(ApiDir apiDir,String actionName){
		return String.format("%s/%s/%s",PropertiesUtil.getInstance().getProperty("pmApiUrl"),apiDir.getValue(),actionName);
	}

	/**
	 * 根据token获取headerValues
	 * @param systemCategory
	 * @return
	 */
	private Map<String, String> getHeaderValues(String systemCategory){
		String appId = PropertiesUtil.getInstance().getProperty(systemCategory+"AppId");
		String appSecret = PropertiesUtil.getInstance().getProperty(systemCategory+"AppSecret");
		APIAuth apiAuth = new APIAuth(appId, appSecret);
		String token = apiAuth.getToken();
		Map<String, String> result = new HashMap<String, String>();
		result.put("apptoken", token);
		result.put("appsecret", appSecret);
		return result;
	}

	/**
	 * 设置tokenAcccess
	 * @param 
	 */
    public boolean setTokenAccess(TokenAccess tokenAceess,Boolean isUpdate){
    	 Map<String, String> paramMap=new HashMap<String, String>();
    	 paramMap.put("tokenAccessId", isUpdate?tokenAceess.getTokenAccessId():"");
    	 paramMap.put("appId", tokenAceess.getAppId());
    	 paramMap.put("appSecret", tokenAceess.getAppSecret());
    	 paramMap.put("platform", tokenAceess.getPlatform());
    	 paramMap.put("expires", tokenAceess.getExpires());
    	 paramMap.put("valid", "1");
    	 paramMap.put("status", "1");
    	 paramMap.put("remark", tokenAceess.getRemark());
    	 paramMap.put("createUser",tokenAceess.getCreateUser());
    	 paramMap.put("createIp",tokenAceess.getCreateIp());
    	 paramMap.put("createDate",tokenAceess.getCreateDate());
       	 paramMap.put("updateUser",tokenAceess.getUpdateUser());
    	 paramMap.put("updateIp",tokenAceess.getUpdateIp());
    	 paramMap.put("updateDate",tokenAceess.getUpdateDate());
         try {
			String str=HttpClientUtils.httpPostString(formatUrl(ApiDir.token,"setTokenAccess"),paramMap);
			if(StringUtils.isNotBlank(str)){
				JSONObject obj=JSON.parseObject(str);
				return obj.getBoolean("isOK");
			}else{
				return false;
			}
		} catch (Exception e) {
			logger.error("setTokenAccess fail!", e);
			return false;
		}
    }
	 
    /**
	 * 提取tokenAcccessList
	 * @param 
	 */
    public List<TokenAccess> getTokenAccessList(TokenAccess tokenAceess){
    	 Map<String, String> paramMap=new HashMap<String, String>();
    	 paramMap.put("appId", tokenAceess.getAppId());
    	 paramMap.put("appSecret", tokenAceess.getAppSecret());
    	 paramMap.put("platform", tokenAceess.getPlatform());
         try {
			String str=HttpClientUtils.httpGetString(formatUrl(ApiDir.token,"getTokenAccessList"),paramMap);
			if(StringUtils.isNotBlank(str)){
				return JSONArray.parseArray(str, TokenAccess.class);
			}else{
				return null;
			}
		} catch (Exception e) {
			logger.error("getTokenAccessList fail!", e);
			return null;
		}
    }
    
      /**
       * 提取getTokenAccessByPlatform
       * @param platform
       */
	   public TokenAccess getTokenAccessByPlatform(String platform){
	   	 Map<String, String> paramMap=new HashMap<String, String>();
	   	 paramMap.put("platform", platform);
	     try {
			String str=HttpClientUtils.httpGetString(formatUrl(ApiDir.token,"getTokenAccessByPlatform"),paramMap);
			if(StringUtils.isNotBlank(str)){
				TokenAccess obj=JSON.parseObject(str, TokenAccess.class);
				return obj;
			}else{
				return null;
			}
		} catch (Exception e) {
			logger.error("getTokenAccessByPlatform fail!", e);
			return null;
		}
	   }
	   
	   /**
	    * 提取getTokenAccessById
	    * @param tokenAccessId
	   */
	   public TokenAccess getTokenAccessById(String tokenAccessId){
	   	 Map<String, String> paramMap=new HashMap<String, String>();
	   	 paramMap.put("tokenAccessId", tokenAccessId);
	     try {
			String str=HttpClientUtils.httpGetString(formatUrl(ApiDir.token,"getTokenAccessById"),paramMap);
			if(StringUtils.isNotBlank(str)){
				TokenAccess obj=JSON.parseObject(str, TokenAccess.class);
				return obj;
			}else{
				return null;
			}
		} catch (Exception e) {
			logger.error("getTokenAccessById fail!", e);
			return null;
		}
	   }
	   
     /**
  	 * 删除TokenAccess
  	 * @param tokenAccessIds=>appId_appSecret
  	 */
      public boolean deleteTokenAccess(String tokenAccessIds){
      	 Map<String, String> paramMap=new HashMap<String, String>();
      	 paramMap.put("ids",tokenAccessIds);
         try {
  			String str=HttpClientUtils.httpPostString(formatUrl(ApiDir.token,"deleteTokenAccess"),paramMap);
  			if(StringUtils.isNotBlank(str)){
  				JSONObject obj=JSON.parseObject(str);
				return obj.getBoolean("isOK");
  			}else{
  				return false;
  			}
  		} catch (Exception e) {
  			logger.error("delTokenAccess fail!", e);
  			return false;
  		}
	}

	/**
	 * 发送短信
	 * 
	 * @param type
	 * @param useType
	 * @param mobilePhone
	 * @param content
	 * @param ip
	 * @param validTime
	 * @return
	 */
	public boolean sendMsg(String type, String useType, String mobilePhone, String content, String ip, long validTime, String systemCategory) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("mobile", mobilePhone);
		paramMap.put("type", type);
		paramMap.put("useType", useType);
		paramMap.put("content", content);
		paramMap.put("deviceKey", ip);
		paramMap.put("validTime", String.valueOf(validTime));
		Map<String, String> headerValues = getHeaderValues(systemCategory);
		try {
			String str = HttpClientUtils.httpPostString(formatUrl(ApiDir.sms, "send"), paramMap, headerValues, "UTF-8");
			logger.info("<<API-sendMsg() : " + str);
			if (StringUtils.isNotBlank(str)) {
				JSONObject obj = JSON.parseObject(str);
				return obj.getIntValue("result") == 0;
			}
			else {
				return false;
			}
		}
		catch (Exception e) {
			logger.error("短信发送失败!", e);
			return false;
		}
	}
	
	/**
	 * 短信重新
	 * @param smsId
	 * @return
	 */
	public boolean resend(String smsId, String systemCategory){
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("smsId", smsId);
		Map<String, String> headerValues = getHeaderValues(systemCategory);
		try {
			String str = HttpClientUtils.httpPostString(formatUrl(ApiDir.sms, "resend"), paramMap, headerValues, "UTF-8");
			logger.info("<<API-resend() : " + str);
			if (StringUtils.isNotBlank(str)) {
				JSONObject obj = JSON.parseObject(str);
				return obj.getIntValue("result") == 0;
			}
			else {
				return false;
			}
		}
		catch (Exception e) {
			logger.error("重发短信失败!", e);
			return false;
		}
	}
	/**
     * 离开房间
     * @param platform 房间ID,如果存在多个，中间用逗号分隔
     * @param dateStr
     * @param lang
     * @return
     */
    public String getBroadStrateList(String platform, String dateStr,String lang){
    	Map<String, String> paramMap=new HashMap<String, String>();
    	paramMap.put("platform", platform);
    	paramMap.put("dateStr", dateStr);
    	paramMap.put("lang", lang);
    	try {
    		return HttpClientUtils.httpGetString(formatUrl(ApiDir.common,"getBroadStrateList"),paramMap);
    	} catch (Exception e) {
    		return null;
    	}
    }

	
	/**
	 * 订阅通知（文档）
	 * @param articleId
	 * @return
	 */
	public boolean subscribeArticle(String articleId, String systemCategory){
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("type", "ARTICLE");
		paramMap.put("dataId", articleId);
		Map<String, String> headerValues = getHeaderValues(systemCategory);
		try {
			String str = HttpClientUtils.httpPostString(formatUrl(ApiDir.subscribe, "notice"), paramMap, headerValues, "UTF-8");
			logger.info("<<API-subscribeArticle() : " + str);
			if (StringUtils.isNotBlank(str)) {
				JSONObject obj = JSON.parseObject(str);
				return obj.getBooleanValue("data");
			} else {
				return false;
			}
		}
		catch (Exception e) {
			logger.error("发送订阅通知失败!", e);
			return false;
		}
	}
} 
