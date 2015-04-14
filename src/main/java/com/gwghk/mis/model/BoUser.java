package com.gwghk.mis.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 摘要：用户实体对象(系统管理)
 * @author Gavin.guo
 * @date   2015年2月4日
 */
@Document
public class BoUser extends BaseModel{ 
	/**
	 * 用户Id
	 */
	@Id
	private String userId;
	
    /**
     * 用户账号
     */
	@Indexed
	private String userNo;

	/**
     * 密码
     */
	private String password;

	/**
     * 姓名
     */
	private String userName;

	/**
     * 邮件地址
     */
	private String email;

	/**
     * 手机
     */
	private String telephone;

	/**
     * 职位
     */
	private String position;

	/**
     * 登录ip
     */
	private String loginIp;

	/**
     * 登陆次数
     */
	private Integer loginTimes;

	/**
     * 登陆时间
     */
	private Date loginDate;

	/**
     * 是否删除
     */
	private Integer valid;

	/**
     * 状态
     */
	private Integer status;

	/**
     * 备注
     */
	private String remark;

	private BoRole role;

	/**
	 * 用户名或用户号，用于权限类别中用户的查询
	 */
    private String userNoOrName;
    
	public String getUserNoOrName() {
		return userNoOrName;
	}

	public void setUserNoOrName(String userNoOrName) {
		this.userNoOrName = userNoOrName;
	}

	public String getUserId () { 
		return this.userId;
	}

	public void setUserId (String userId) { 
		this.userId = userId;
	}

	public String getUserNo () { 
		return this.userNo;
	}

	public void setUserNo (String userNo) { 
		this.userNo = userNo;
	}

	public String getPassword () { 
		return this.password;
	}

	public void setPassword (String password) { 
		this.password = password;
	}

	public String getUserName () { 
		return this.userName;
	}

	public void setUserName (String userName) { 
		this.userName = userName;
	}

	public String getEmail () { 
		return this.email;
	}

	public void setEmail (String email) { 
		this.email = email;
	}

	public String getTelephone () { 
		return this.telephone;
	}

	public void setTelephone (String telephone) { 
		this.telephone = telephone;
	}

	public String getPosition () { 
		return this.position;
	}

	public void setPosition (String position) { 
		this.position = position;
	}

	public String getLoginIp () { 
		return this.loginIp;
	}

	public void setLoginIp (String loginIp) { 
		this.loginIp = loginIp;
	}

	public Integer getLoginTimes () { 
		return this.loginTimes;
	}

	public void setLoginTimes (Integer loginTimes) { 
		this.loginTimes = loginTimes;
	}

	public Date getLoginDate () { 
		return this.loginDate;
	}

	public void setLoginDate (Date loginDate) { 
		this.loginDate = loginDate;
	}

	public Integer getValid () { 
		return this.valid;
	}

	public void setValid (Integer valid) { 
		this.valid = valid;
	}

	public Integer getStatus () { 
		return this.status;
	}

	public void setStatus (Integer status) { 
		this.status = status;
	}

	public String getRemark () { 
		return this.remark;
	}

	public void setRemark (String remark) { 
		this.remark = remark;
	}

	public BoRole getRole() {
		return role;
	}

	public void setRole(BoRole role) {
		this.role = role;
	}
	
}