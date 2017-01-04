package com.gwghk.mis.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * 用户通用实体类
 * @author Alan.wu
 * @date 2016/11/18
 */
public class BaseUser extends BaseModel{
	/**
	 * 用户Id
	 */
	@Id
	protected String userId;
	
    /**
     * 用户账号
     */
	@Indexed
	protected String userNo;

	/**
     * 密码
     */
	protected String password;

	/**
     * 姓名
     */
	protected String userName;
	
	/**
     * 登录ip
     */
	protected String loginIp;

	/**
     * 登陆次数
     */
	protected Integer loginTimes;

	/**
     * 登陆时间
     */
	protected Date loginDate;
	
	/**
	 * 备注
	 */
	protected String remark;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
    
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Integer getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
