package com.gwghk.mis.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 摘要：积分配置
 * @author Dick.guo
 * @date   2016年09月02日
 */
@Document
public class ChatPointsConfig extends BaseModelExt{
	
	/**
	 * 主键
	 */
	@Id
	private String cfgId;
	
	/**
	 * 房间组别
	 */
	private String groupType;
	
	/**
	 * 房间编号
	 */
	private String groupId;
	
	/**
	 * 客户组别
	 */
	private String[] clientGroup;
	
	/**
	 * 类别
	 */
	private String type;
	
	/**
	 * 项目
	 */
	private String item;
	
	/**
	 * 值
	 */
	private Integer val;
	
	/**
	 * 上限计量单位：分/天、次、分
	 */
	private String limitUnit;
	
	/**
	 * 上限值
	 */
	private String limitVal;
	
	/**
	 * 上限参数
	 */
	private String limitArg;
	
	/**
	 * 提示语
	 */
	private String tips;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 状态
	 */
	private Integer status;

	/**
	 * 是否删除(0：未删除 1：删除)
	 */
	private Integer isDeleted;

	/**
	 * @return the cfgId
	 */
	public String getCfgId() {
		return cfgId;
	}

	/**
	 * @param cfgId the cfgId to set
	 */
	public void setCfgId(String cfgId) {
		this.cfgId = cfgId;
	}
	
	/**
	 * @return the groupType
	 */
	public String getGroupType() {
		return groupType;
	}

	/**
	 * @param groupType the groupType to set
	 */
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the item
	 */
	public String getItem() {
		return item;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(String item) {
		this.item = item;
	}

	/**
	 * @return the val
	 */
	public Integer getVal() {
		return val;
	}

	/**
	 * @param val the val to set
	 */
	public void setVal(Integer val) {
		this.val = val;
	}

	/**
	 * @return the limitUnit
	 */
	public String getLimitUnit() {
		return limitUnit;
	}

	/**
	 * @param limitUnit the limitUnit to set
	 */
	public void setLimitUnit(String limitUnit) {
		this.limitUnit = limitUnit;
	}

	/**
	 * @return the limitVal
	 */
	public String getLimitVal() {
		return limitVal;
	}

	/**
	 * @param limitVal the limitVal to set
	 */
	public void setLimitVal(String limitVal) {
		this.limitVal = limitVal;
	}

	/**
	 * @return the limitArg
	 */
	public String getLimitArg() {
		return limitArg;
	}

	/**
	 * @param limitArg the limitArg to set
	 */
	public void setLimitArg(String limitArg) {
		this.limitArg = limitArg;
	}
	
	/**
	 * @return the tips
	 */
	public String getTips() {
		return tips;
	}

	/**
	 * @param tips the tips to set
	 */
	public void setTips(String tips) {
		this.tips = tips;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the isDeleted
	 */
	public Integer getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String[] getClientGroup() {
		return clientGroup;
	}

	public void setClientGroup(String[] clientGroup) {
		this.clientGroup = clientGroup;
	}
}
