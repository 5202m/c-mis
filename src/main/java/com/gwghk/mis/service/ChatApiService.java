package com.gwghk.mis.service;

import com.gwghk.mis.util.APIAuth;
import com.gwghk.mis.util.APIToken;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.enums.ResultCode;
import com.gwghk.mis.model.Article;
import com.gwghk.mis.util.HttpClientUtils;
import com.gwghk.mis.util.PropertiesUtil;

/**
 * 聊天室API请求服务类
 * @author Alan.wu
 * @date  2015年4月1日
 */
@Service
public class ChatApiService{
	//private static final Logger logger = LoggerFactory.getLogger(ChatApiService.class);
	/**
	 * 格式请求url
	 * @return
	 */
	private String formatUrl(String actionName){
		return String.format("%s/api/chat/%s",PropertiesUtil.getInstance().getProperty("chatUrl"),actionName);
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
	 * 通知移除聊天内容
	 * @param msgIds
	 * @param groupId
	 * @param systemCategory
	 */
    public boolean removeMsg(String msgIds, String groupId, String systemCategory){
    	 Map<String, String> paramMap=new HashMap<String, String>();
    	 paramMap.put("msgIds", msgIds);
    	 paramMap.put("groupId", groupId);
			Map<String, String> headerValues = getHeaderValues(systemCategory);
         try {
			String str=HttpClientUtils.httpPostString(formatUrl("removeMsg"), paramMap, headerValues, "UTF-8");
			if(StringUtils.isNotBlank(str)){
				JSONObject obj=JSON.parseObject(str);
				return obj.getBoolean("isOK");
			}else{
				return false;
			}
		} catch (Exception e) {
			return false;
		}
    }
    
    /**
	 * 通知审核聊天内容
	 * @param approvalUserNo
	 * @param publishTimeArr
	 * @param fUserIdArr
	 * @param status
	 * @param groupId
	 * @param systemCategory
	 */
    public ApiResult approvalMsg(String approvalUserNo,String publishTimeArr,String fUserIdArr,String status,String groupId, String systemCategory){
    	 Map<String, String> paramMap=new HashMap<String, String>();
    	 paramMap.put("publishTimeArr", publishTimeArr);
    	 paramMap.put("fUserIdArr", fUserIdArr);
    	 paramMap.put("status", status);
    	 paramMap.put("groupId", groupId);
    	 paramMap.put("approvalUserNo", approvalUserNo);
			Map<String, String> headerValues = getHeaderValues(systemCategory);
    	 ApiResult api=new ApiResult();
         try {
			String str=HttpClientUtils.httpPostString(formatUrl("approvalMsg"),paramMap, headerValues, "UTF-8");
			if(StringUtils.isNotBlank(str)){
				JSONObject obj=JSON.parseObject(str);
				return api.setCode(obj.getBoolean("isOK")?ResultCode.OK:ResultCode.FAIL).setErrorMsg(obj.getString("error"));
			}else{
				return api.setCode(ResultCode.FAIL);
			}
		} catch (Exception e) {
			return api.setCode(ResultCode.FAIL).setErrorMsg(e.getMessage());
		}
    }
    
    /**
     * 离开房间
     * @param groupIds 房间ID,如果存在多个，中间用逗号分隔
     * @param groupIds
     * @param userIds
		 * @param systemCategory
     * @return
     */
    public ApiResult leaveRoom(String groupIds,String userIds, String systemCategory){
    	Map<String, String> paramMap=new HashMap<String, String>();
    	paramMap.put("groupIds", groupIds);
    	paramMap.put("userIds", userIds);
			Map<String, String> headerValues = getHeaderValues(systemCategory);
    	ApiResult api=new ApiResult();
    	try {
    		String str=HttpClientUtils.httpPostString(formatUrl("leaveRoom"),paramMap, headerValues, "UTF-8");
    		if(StringUtils.isNotBlank(str)){
    			JSONObject obj=JSON.parseObject(str);
    			return api.setCode(obj.getBoolean("isOK")?ResultCode.OK:ResultCode.FAIL).setErrorMsg(obj.getString("error"));
    		}else{
    			return api.setCode(ResultCode.FAIL);
    		}
    	} catch (Exception e) {
    		return api.setCode(ResultCode.FAIL).setErrorMsg(e.getMessage());
    	}
    }
    /**
     * 新增或修改字幕通知
     * @param infoStr
		 * @param isValid
		 * @param systemCategory
     * @return
     */
	public boolean submitPushInfo(String infoStr,boolean isValid, String systemCategory){
	  	 Map<String, String> paramMap=new HashMap<String, String>();
	  	 paramMap.put("infoStr", infoStr);
	  	 paramMap.put("isValid", isValid?"true":"false");
		Map<String, String> headerValues = getHeaderValues(systemCategory);
	       try {
			String str=HttpClientUtils.httpPostString(formatUrl("submitPushInfo"),paramMap, headerValues, "UTF-8");
			if(StringUtils.isNotBlank(str)){
				JSONObject obj=JSON.parseObject(str);
				return obj.getBoolean("isOK");
			}else{
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 删除字幕通知
	 * @param position
	 * @param roomIds
	 * @param ids
	 * @param systemCategory
	 * @return
	 */
    public boolean removePushInfo(int position,String roomIds , String ids, String systemCategory){
     	 Map<String, String> paramMap=new HashMap<String, String>();
     	 paramMap.put("position", String.valueOf(position));
     	 paramMap.put("ids", ids);
     	 paramMap.put("roomIds", roomIds);
			Map<String, String> headerValues = getHeaderValues(systemCategory);
          try {
  			String str=HttpClientUtils.httpPostString(formatUrl("removePushInfo"),paramMap, headerValues, "UTF-8");
  			if(StringUtils.isNotBlank(str)){
  				JSONObject obj=JSON.parseObject(str);
  				return obj.getBoolean("isOK");
  			}else{
  				return false;
  			}
  		} catch (Exception e) {
  			return false;
  		}
    }
    
    /**
     * socket通知客户端
     * @param article
     * @param opType
		 * @param systemCategory
     * @return
     */
    public boolean noticeArticle(Article article,String opType, String systemCategory){
    	Map<String, String> paramMap=new HashMap<String, String>();
    	paramMap.put("article", JSON.toJSONString(article));
	  	paramMap.put("opType", opType);
			Map<String, String> headerValues = getHeaderValues(systemCategory);
		try {
			String str = HttpClientUtils.httpPostString(formatUrl("noticeArticle"), paramMap, headerValues, "UTF-8");
			if (StringUtils.isNotBlank(str)) {
				JSONObject obj = JSON.parseObject(str);
				return obj.getBoolean("isOK");
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
    }
    
    /**
     * socket通知客户端,晒单审核完成后，通知到客户端
     * @param tradeInfo
		 * @param systemCategory
     * @return
     */
    public boolean showTradeNotice(String tradeInfo, String systemCategory){
    	Map<String, String> paramMap=new HashMap<String, String>();
    	paramMap.put("tradeInfo", tradeInfo);
			Map<String, String> headerValues = getHeaderValues(systemCategory);
		try {
			String url = formatUrl("showTradeNotice");
			String str = HttpClientUtils.httpPostString(url, paramMap, headerValues, "UTF-8");
			if (StringUtils.isNotBlank(str)) {
				JSONObject obj = JSON.parseObject(str);
				return obj.getBoolean("isOK");
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
    }
    
    /**
     * 新增或修改规则通知
     * @param roomIds
	 * @param ruleInfo
		 * @param systemCategory
     * @return
     */
    public boolean modifyRuleNotice(String roomIds,String ruleInfo, String systemCategory){
    	Map<String, String> paramMap=new HashMap<String, String>();
    	paramMap.put("roomIds", roomIds);
    	paramMap.put("ruleInfo", ruleInfo);
			Map<String, String> headerValues = getHeaderValues(systemCategory);
		try {
			String url = formatUrl("modifyRuleNotice");
			String str = HttpClientUtils.httpPostString(url, paramMap, headerValues, "UTF-8");
			if (StringUtils.isNotBlank(str)) {
				JSONObject obj = JSON.parseObject(str);
				return obj.getBoolean("isOK");
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
    }

	/**
	 * 财经日历点评
	 * @param financeData
	 * @param reviewData
	 * @param systemCategory
	 * @return
	 */
	public boolean zxFinanceReviewNotice(String financeData, String reviewData, String systemCategory){
		Map<String, String> paramMap=new HashMap<String, String>();
		paramMap.put("fData", financeData);
		paramMap.put("reviewData", reviewData);
		Map<String, String> headerValues = getHeaderValues(systemCategory);
		try {
			String url = String.format("%s/notice/%s",PropertiesUtil.getInstance().getProperty("chatApiUrl"),"zxFinanceReviewNotice");//formatUrl("zxFinanceReviewNotice");
			String str = HttpClientUtils.httpPostString(url, paramMap, headerValues, "UTF-8");
			System.out.println(str);
			if (StringUtils.isNotBlank(str)) {
				JSONObject obj = JSON.parseObject(str);
				return obj.getBoolean("isOK");
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
} 
