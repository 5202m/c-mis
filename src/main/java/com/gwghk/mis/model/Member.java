package com.gwghk.mis.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 摘要：会员对象
 * @author Gavin.guo
 * @date   2015年3月16日
 */
@Document
public class Member extends BaseModelExt{
	
	/**
	 * 会员Id
	 */
	@Id
	private String memberId;
	
	/**
	 * 手机号
	 */
	@Indexed
	private String mobilePhone;
	
	/**
     * 是否删除
     */
	private Integer valid;
	
	
	/**
	 * 用户状态(0:禁用 1：启用)
	 */
	private Integer status;
	
	/**
	 * 登录平台信息
	 */
	private LoginPlatform loginPlatform;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public LoginPlatform getLoginPlatform() {
		return loginPlatform;
	}

	public void setLoginPlatform(LoginPlatform loginPlatform) {
		this.loginPlatform = loginPlatform;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
