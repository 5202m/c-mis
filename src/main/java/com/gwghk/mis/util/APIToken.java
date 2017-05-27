package com.gwghk.mis.util;

/**
 * APIToken 实体类
 * @author Jade.zhu
 * @date 2017/5/25
 */
public class APIToken {
  private String token;

  private Integer expires;

  private Long beginTime;

  private String appId;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Integer getExpires() {
    return expires;
  }

  public void setExpires(Integer expires) {
    this.expires = expires;
  }

  public Long getBeginTime() {
    return beginTime;
  }

  public void setBeginTime(long beginTime) {
    this.beginTime = beginTime;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }
}
