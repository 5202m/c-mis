package com.gwghk.mis.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * 聊天室信息实体类
 * @author Alan.wu
 * @date   2015年3月16日
 */
public class ChatMessage extends BaseModelExt{
	
	/**
	 * 用户id
	 */
	protected String userId;
	
	/**
	 * 用户昵称
	 */
	@Indexed
	protected String nickname;
	
	/**
	 * 用户头像
	 */
	protected String avatar;
	
	//房间大类组
	protected String groupType;
	
	/**
	 * 区分系统用户还是会员，0表示会员，1表示系统用户
	 */
	@Indexed
	protected Integer userType;
	
	/**
	 * 组别Id
	 */
	protected String groupId;
	
	/**
	 * 客户组别
	 */
	protected String clientGroup;
	
	/**
	 * 上线时间
	 */
	protected Date onlineDate;
	
	
	protected String fromPlatform;//平台来源

	/**
	 * 内容Id
	 */
	@Id
	protected String id;
	
	/**
	 * 信息内容
	 */
	protected ChatContent content;
	
	/**
	 * 接收者toUser
	 */
	protected ChatMsgToUser toUser;
	
	/**
	 * 手机号码
	 */
	@Indexed
	protected String mobilePhone;
	
	/**
	 * 用户账号
	 */
	@Indexed
	protected String accountNo;
	
	/**
	 * 需要审核角色编号
	 */
	protected String approvalUserArr;
	
	/**
	 * 审核人编号
	 */
	protected String approvalUserNo;
	/**
	 * 审核状态：0、等待审批，1、通过 ；2、拒绝
	 */
	protected Integer status;
	
	/**
	 * 内容状态：0、无效，1、有效
	 */
	protected Integer valid;
	/**
	 * 发布日期
	 */
	protected String publishTime;
	
	/**
	 * 发布开始日期（用于查询）
	 */
	protected String publishStartDateStr;
	
	/**
	 * 发布结束日期（用于查询）
	 */
	protected String publishEndDateStr;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public String getClientGroup() {
		return clientGroup;
	}

	public void setClientGroup(String clientGroup) {
		this.clientGroup = clientGroup;
	}

	public Date getOnlineDate() {
		return onlineDate;
	}

	public void setOnlineDate(Date onlineDate) {
		this.onlineDate = onlineDate;
	}
	

	public String getFromPlatform() {
		return fromPlatform;
	}

	public void setFromPlatform(String fromPlatform) {
		this.fromPlatform = fromPlatform;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public ChatContent getContent() {
		return content;
	}

	public void setContent(ChatContent content) {
		this.content = content;
	}
	
	public ChatMsgToUser getToUser() {
		return toUser;
	}

	public void setToUser(ChatMsgToUser toUser) {
		this.toUser = toUser;
	}

	public String getPublishStartDateStr() {
		return publishStartDateStr;
	}

	public void setPublishStartDateStr(String publishStartDateStr) {
		this.publishStartDateStr = publishStartDateStr;
	}

	public String getPublishEndDateStr() {
		return publishEndDateStr;
	}

	public void setPublishEndDateStr(String publishEndDateStr) {
		this.publishEndDateStr = publishEndDateStr;
	}

	/**
	 * @return the approvalUserArr
	 */
	public String getApprovalUserArr() {
		return approvalUserArr;
	}

	/**
	 * @param approvalUserArr the approvalUserArr to set
	 */
	public void setApprovalUserArr(String approvalUserArr) {
		this.approvalUserArr = approvalUserArr;
	}

	public String getApprovalUserNo() {
		return approvalUserNo;
	}

	public void setApprovalUserNo(String approvalUserNo) {
		this.approvalUserNo = approvalUserNo;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	
	
}
