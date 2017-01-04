package com.gwghk.mis.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 摘要：短信配置
 * @author Dick.guo
 * @date   2015年10月29日
 */
@Document
public class SmsConfig extends BaseModelExt{

	/**
	 * 主键
	 */
	@Id
	private String smsCfgId;

	/**
	 * 信息类型 NORMAL-普通、AUTH_CODE-验证码
	 */
	private String type;
	
	/**
	 * 应用点
	 */
	private String useType;
	
	/**
	 * 计数周期（H-时、D-天、W-周、M-月、Y-年）
	 */
	private String cycle;
	
	/**
	 * 允许发送次数
	 */
	private Integer cnt;
	
	/**
	 * 验证码有效时间
	 */
	private Long validTime;
	
	/**
	 * 是否有效 1-有效、0-无效
	 */
	private Integer status;

	/**
	 * 是否删除(0：删除 1：未删除)
	 */
	private Integer isDeleted;

	/**
	 * @return the smsCfgId
	 */
	public String getSmsCfgId() {
		return smsCfgId;
	}

	/**
	 * @param smsCfgId the smsCfgId to set
	 */
	public void setSmsCfgId(String smsCfgId) {
		this.smsCfgId = smsCfgId;
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
	 * @return the useType
	 */
	public String getUseType() {
		return useType;
	}

	/**
	 * @param useType the useType to set
	 */
	public void setUseType(String useType) {
		this.useType = useType;
	}

	/**
	 * @return the cycle
	 */
	public String getCycle() {
		return cycle;
	}

	/**
	 * @param cycle the cycle to set
	 */
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	/**
	 * @return the cnt
	 */
	public Integer getCnt() {
		return cnt;
	}

	/**
	 * @param cnt the cnt to set
	 */
	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}
	
	/**
	 * @return the validTime
	 */
	public Long getValidTime() {
		return validTime;
	}

	/**
	 * @param validTime the validTime to set
	 */
	public void setValidTime(Long validTime) {
		this.validTime = validTime;
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
}
