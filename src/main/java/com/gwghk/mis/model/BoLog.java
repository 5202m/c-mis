package com.gwghk.mis.model;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 摘要：日志对象
 * @author Gavin.guo
 * @date   2015年3月13日
 */
@Document
public class BoLog extends BaseModelExt{

	@Id
	private String id;

	/**
	 * 用户编号
	 */
	private String userNo;

	/**
	 * 日志级别
	 */
	private Integer logLevel;

	/**
	 * 操作类型
	 */
	private String operateType;

	/**
	 * 操作时间
	 */
	private Date operateDate;

	/**
	 * 操作内容
	 */
	private String logContent;

	/**
	 * 用户浏览器类型
	 */
	private String broswer;

	/**
	 * 日志级别
	 */
	private Integer valid;
	
	/**
	 * 操作开始时间(用于查询)
	 */
	@Transient
	private Date operStartDate;
	
	/**
	 * 操作结束时间(用于查询)
	 */
	@Transient
	private Date operEndDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public Integer getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(Integer logLevel) {
		this.logLevel = logLevel;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public String getLogContent() {
		return logContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

	public String getBroswer() {
		return broswer;
	}

	public void setBroswer(String broswer) {
		this.broswer = broswer;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	public Date getOperStartDate() {
		return operStartDate;
	}

	public void setOperStartDate(Date operStartDate) {
		this.operStartDate = operStartDate;
	}

	public Date getOperEndDate() {
		return operEndDate;
	}

	public void setOperEndDate(Date operEndDate) {
		this.operEndDate = operEndDate;
	}
}