package com.gwghk.mis.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 摘要：积分
 * @author Dick.guo
 * @date   2016年09月02日
 */
@Document
public class ChatPoints extends BaseModelExt{
	
	/**
	 * 主键
	 */
	@Id
	private String id;
	
	/**
	 * 房间组别
	 */
	private String groupType;
	
	/**
	 * 用户编号
	 */
	private String userId;
	
	/**
	 * 用户总积分
	 */
	private Long pointsGlobal;
	
	/**
	 * 用户积分
	 */
	private Long points;
	
	/**
	 * 用户积分流水
	 */
	private List<ChatPointsJournal> journal;
	
	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 是否删除(0：未删除 1：删除)
	 */
	private Integer isDeleted;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the pointsGlobal
	 */
	public Long getPointsGlobal() {
		return pointsGlobal;
	}

	/**
	 * @param pointsGlobal the pointsGlobal to set
	 */
	public void setPointsGlobal(Long pointsGlobal) {
		this.pointsGlobal = pointsGlobal;
	}

	/**
	 * @return the points
	 */
	public Long getPoints() {
		return points;
	}

	/**
	 * @param points the points to set
	 */
	public void setPoints(Long points) {
		this.points = points;
	}

	/**
	 * @return the journal
	 */
	public List<ChatPointsJournal> getJournal() {
		return journal;
	}

	/**
	 * @param journal the journal to set
	 */
	public void setJournal(List<ChatPointsJournal> journal) {
		this.journal = journal;
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
