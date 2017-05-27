package com.gwghk.mis.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * APIAuth通用类
 * @author Jade.zhu
 * @date 2017/5/24
 */
public class APIAuth {

  private static final Logger logger = LoggerFactory.getLogger(APIAuth.class);

  private static final long millisecondsPerHour = 3600 * 1000;

  private static final int expiredGap = 10000;

  private static APIToken apiToken = null;

  private String appId;

  private String appSecret;

  /**
   * 格式请求url
   * @return
   */
  private String formatUrl(String actionName){
    return String.format("%s/api/token/%s",PropertiesUtil.getInstance().getProperty("chatUrl"),actionName);
  }

  public APIAuth(String appId, String appSecret){
    //this.apiToken = null;
    this.appId = appId;
    this.appSecret = appSecret;
  }

  /**
   * 校验token是否在有效时间内
   * @param ltvToken
   * @return
   */
  public boolean isLocalTokenValid(APIToken ltvToken){
    long currentTime = System.currentTimeMillis();
    long expiredTime = ltvToken.getBeginTime() - 0 + ltvToken.getExpires() * this.millisecondsPerHour;
    boolean isValid = (currentTime + this.expiredGap) <= expiredTime;
    if(!isValid){
      logger.info("local token invalid, currentTime:" + currentTime + " expiredTime: " + expiredTime);
      logger.debug(JSONHelper.toJSONString(ltvToken) + "currentTime:" + new Date(currentTime).toLocaleString() + " expiredTime: " + new Date(expiredTime).toLocaleString());
    }
    return isValid;
  }

  /**
   * 校验token是否有效
   * @return
   */
  public boolean verifyToken(){
    boolean flag = false;
    if(this.apiToken == null){
      flag = false;
    }else {
      if (this.isLocalTokenValid(this.apiToken)) { //Token 离过期至少还有一段的时间，因此不需要发请求去api验证token。
        flag = true;
      } else {
        flag = false;
      }
    }
    return flag;
  }

  /**
   * 获取新token
   * @return
   */
  public APIToken getNewToken(){
    Map<String, String> paramMap=new HashMap<String, String>();
    paramMap.put("appId", this.appId);
    paramMap.put("appSecret", this.appSecret);
    String url = formatUrl("getToken");
    logger.info("Fetching new token from >> " + url);
    try {
      String str = HttpClientUtils.httpPostString(url, paramMap);
      if (StringUtils.isNotBlank(str)) {
        logger.info("New token fetched >> " + str);
        JSONObject obj = JSON.parseObject(str);
        apiToken = new APIToken();
        apiToken.setAppId(obj.getString("appId"));
        apiToken.setBeginTime(obj.getLong("beginTime"));
        apiToken.setExpires(obj.getInteger("expires"));
        apiToken.setToken(obj.getString("token"));
      } else {
        apiToken = null;
      }
    } catch (IOException e) {
      logger.error("getNewToken faild >> " + e.getMessage());
      apiToken = null;
    }
    return apiToken;
  }

  /**
   * 返回当前token
   * @return
   */
  public String getToken(){
    String token = "";
    boolean isVerify = this.verifyToken();
    if(isVerify){
      token = apiToken.getToken();
    } else {
      apiToken = this.getNewToken();
      if(apiToken != null){
        token = apiToken.getToken();
      }
    }
    return token;
  }
}
