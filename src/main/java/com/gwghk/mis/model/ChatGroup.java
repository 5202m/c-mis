package com.gwghk.mis.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 聊天室组别实体类
 * @author Alan.wu
 * @date   2015年4月1日
 */
@Document
public class ChatGroup extends BaseModel{
	
	/**
	 * 组别编码
	 */
	@Id
	private String id;
	
	/**
	 * 组类别
	 */
	private String groupType;
		
	/**
	 * 组别名
	 */
	private String name;
	
	/**
	 * 内容状态：0 、禁用 ；1、启动
	 */
	private Integer status;

	/**
	 * 聊天规则
	 */
	private List<ChatGroupRule> chatRules;
	
	/**
	 * 关联tokenAccess的id
	 */
	private String tokenAccessId;
	
	/**
     * 是否删除
     */
	private Integer valid;
	
	/**
	 * 序列
	 */
	private Integer sequence;
	
	/**
	 * 房间等级
	 */
	private Integer level;

	/**
	 * 聊天方式,多个逗号分隔
	 */
	private String talkStyle;
	
	/**
	 * 直播实体
	 */
	private ChatStudio chatStudio;
	
	/**
	 * 聊天规则id(用于数据传输）
	 */
	private String chatRuleIds;
	
	/**
	 * 聊天室路径(用于页面显示）
	 */
	private String chatUrl;
	
	/**
	 * 开放时间，保存格式{beginDate:yyyy-MM-dd,endDate:yyyy-MM-dd,weekTime:[{week:0..6,beginTime:'HH:mm:ss',endTime:'HH:mm:ss'}]}
	 */
	private String openDate;
	
	/** 房间最大人数 */
	private Integer maxCount;

	/** 授权用户列表 */
	private String[] authUsers;
	
	/** 默认分析师（用户信息） */
	private BoUser defaultAnalyst;
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}
	
	public List<ChatGroupRule> getChatRules() {
		return chatRules;
	}

	public void setChatRules(List<ChatGroupRule> chatRules) {
		this.chatRules = chatRules;
	}

	public String getChatRuleIds() {
		return chatRuleIds;
	}

	public void setChatRuleIds(String chatRuleIds) {
		this.chatRuleIds = chatRuleIds;
	}

	public String getChatUrl() {
		return chatUrl;
	}

	public void setChatUrl(String chatUrl) {
		this.chatUrl = chatUrl;
	}
	
	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	
	public Integer getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}
	
	/**
	 * @return the authUsers
	 */
	public String[] getAuthUsers() {
		return authUsers;
	}

	/**
	 * @param authUsers the authUsers to set
	 */
	public void setAuthUsers(String[] authUsers) {
		this.authUsers = authUsers;
	}

	public BoUser getDefaultAnalyst() {
		return defaultAnalyst;
	}

	public void setDefaultAnalyst(BoUser defaultAnalyst) {
		this.defaultAnalyst = defaultAnalyst;
	}

	public String getTokenAccessId() {
		return tokenAccessId;
	}

	public void setTokenAccessId(String tokenAccessId) {
		this.tokenAccessId = tokenAccessId;
	}

	public ChatStudio getChatStudio() {
		return chatStudio;
	}

	public void setChatStudio(ChatStudio chatStudio) {
		this.chatStudio = chatStudio;
	}
	
	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
	 * @return the talkStyle
	 */
	public String getTalkStyle() {
		return talkStyle;
	}

	/**
	 * @param talkStyle the talkStyle to set
	 */
	public void setTalkStyle(String talkStyle) {
		this.talkStyle = talkStyle;
	}
}
