package com.gwghk.mis.model;

import org.springframework.data.mongodb.core.mapping.Document;


/** 聊天室客户组实体类
 * @author Alan.wu
 * @date   2015年7月10日
 */
@Document
public class ChatClientGroup extends BaseModelExt {
	/**
	 * 主键id
	 */
	private String id;
	
	/**
	 * 客户组id
	 */
	private String clientGroupId;
	
	/**
	 * 类别
	 */
	private String groupType;
	
	/**
	 * 客户组名称
	 */
	private String name;
	/**
	 * 默认房间组id
	 */
	private String defChatGroupId;
	
	/**
	 * 总人数
	 */
	private Integer clientNum;
	
	/**
	 * 排序序列
	 */
	private Integer sequence;
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 权限描述
	 */
	private String authorityDes;

	/**
	 * 是否有效
	 */
	private Integer valid;
	
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

	public String getDefChatGroupId() {
		return defChatGroupId;
	}

	public void setDefChatGroupId(String defChatGroupId) {
		this.defChatGroupId = defChatGroupId;
	}

	public Integer getClientNum() {
		return clientNum;
	}

	public void setClientNum(Integer clientNum) {
		this.clientNum = clientNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getAuthorityDes() {
		return authorityDes;
	}

	public void setAuthorityDes(String authorityDes) {
		this.authorityDes = authorityDes;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	
	public String getClientGroupId() {
		return clientGroupId;
	}

	public void setClientGroupId(String clientGroupId) {
		this.clientGroupId = clientGroupId;
	}
	
}
