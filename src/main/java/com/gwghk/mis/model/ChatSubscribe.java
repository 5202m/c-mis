package com.gwghk.mis.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * 订阅实体类
 * @author Jade.zhu
 * 2016.08.25
 *
 */
public class ChatSubscribe extends BaseModelExt {

	/**
	 * ID
	 */
	@Id
	private String id;//objectid
	/**
	 * 区分直播间
	 */
	@Indexed
	private String groupType;
	
	/**
	 * 房间ID，预留
	 */
	private String groupId;
	
	/**
	 * 订阅服务类型
	 */
	private String type;
	
	/**
	 * 用户ID
	 */
	private String userId;
	
	/**
	 * 分析师
	 */
	private String analyst;
	
	/**
	 * 订阅方式
	 */
	private String noticeType;
	
	/**
	 * 内容，预留
	 */
	private String remarks;
	
	/**
	 * 开始时间
	 */
	private Date startDate;
	
	/**
	 * 结束时间
	 */
	private Date endDate;
	
	/**
	 * 积分ID
	 */
	private String pointsId;
	
	/**
	 * 积分
	 */
	private Integer point;
	
	/**
	 * 是否删除
	 */
	private Integer valid;

	/**
	 * 是否有效
	 */
	private Integer status;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAnalyst() {
		return analyst;
	}

	public void setAnalyst(String analyst) {
		this.analyst = analyst;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getPointsId() {
		return pointsId;
	}

	public void setPointsId(String pointsId) {
		this.pointsId = pointsId;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
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
