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
     * 是否删除 1-未删除  0-已删除
     */
	private Integer valid;

	/**
     * 状态 1-无效 0-有效
     */
	private Integer status;

	/**
     * 简介
     */
	private String introduction;
	
	/**
	 * 简介图片
	 */
	private String introductionImg;
	
	/**
	 * 备注
	 */
	private String remark;

	private BoRole role;

	private String avatar;
	/**
	 * 用户名或用户号，用于权限类别中用户的查询
	 */
    private String userNoOrName;
    /**
     * 微信号
     */
    private String wechatCode; 
    /**
     * 微信二维码
     */
    private String wechatCodeImg;
    
	/**
     * 胜率
     */
    private String winRate;
    
    /**
     * 月收益
     */
    private String earningsM;
    
    /**
     * 简介图片跳转链接
     */
    private String introductionImgLink;
    
    /**
     * 分析师标签
     */
    private String tag;

	/**
	 * 直播地址
	 */
	private String liveLinks;
    
    public String getIntroductionImgLink() {
		return introductionImgLink;
	}

	public void setIntroductionImgLink(String introductionImgLink) {
		this.introductionImgLink = introductionImgLink;
	}

	public String getWechatCodeImg() {
		return wechatCodeImg;
	}

	public void setWechatCodeImg(String wechatCodeImg) {
		this.wechatCodeImg = wechatCodeImg;
	}
	public String getWechatCode() {
		return wechatCode;
	}
	public void setWechatCode(String wechatCode) {
		this.wechatCode = wechatCode;
	}

	public String getWinRate() {
		return winRate;
	}

	public void setWinRate(String winRate) {
		this.winRate = winRate;
	}
	
	public String getEarningsM() {
		return earningsM;
	}

	public void setEarningsM(String earningsM) {
		this.earningsM = earningsM;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

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
	
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getIntroductionImg() {
		return introductionImg;
	}

	public void setIntroductionImg(String introductionImg) {
		this.introductionImg = introductionImg;
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

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}


	public String getLiveLinks() {
		return liveLinks;
	}

	public void setLiveLinks(String liveLinks) {
		this.liveLinks = liveLinks;
	}

}