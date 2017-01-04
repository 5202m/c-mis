package com.gwghk.mis.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 摘要：积分流水
 * @author Dick.guo
 * @date   2016年09月02日
 */
@Document
public class ChatPointsJournal extends BaseModelExt{
	
	/**
	 * 主键
	 */
	@Id
	private String journalId;

	/**
	 * 积分项
	 */
	private String item;
	
	/**
	 * 积分前值
	 */
	private Long before;
	
	/**
	 * 积分变化
	 */
	private Integer change;
	
	/**
	 * 积分后值
	 */
	private Long after;
	
	/**
	 * 操作用户
	 */
	private String opUser;
	
	/**
	 * 时间
	 */
	private Date date;
	
	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 标记
	 */
	private String tag;

	/**
	 * 是否删除(0：未删除 1：删除)
	 */
	private Integer isDeleted;

	/**
	 * @return the journalId
	 */
	public String getJournalId() {
		return journalId;
	}

	/**
	 * @param journalId the journalId to set
	 */
	public void setJournalId(String journalId) {
		this.journalId = journalId;
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
	 * @return the before
	 */
	public Long getBefore() {
		return before;
	}

	/**
	 * @param before the before to set
	 */
	public void setBefore(Long before) {
		this.before = before;
	}

	/**
	 * @return the change
	 */
	public Integer getChange() {
		return change;
	}

	/**
	 * @param change the change to set
	 */
	public void setChange(Integer change) {
		this.change = change;
	}

	/**
	 * @return the after
	 */
	public Long getAfter() {
		return after;
	}

	/**
	 * @param after the after to set
	 */
	public void setAfter(Long after) {
		this.after = after;
	}

	/**
	 * @return the opUser
	 */
	public String getOpUser() {
		return opUser;
	}

	/**
	 * @param opUser the opUser to set
	 */
	public void setOpUser(String opUser) {
		this.opUser = opUser;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
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

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
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
}
