package com.gwghk.mis.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 摘要：短信信息
 * @author Dick.guo
 * @date   2015年10月28日
 */
@Document
public class SmsInfo extends BaseModelExt{

	/**
	 * 主键
	 */
	@Id
	private String smsId;

	/**
	 * 信息类型 NORMAL-普通、AUTH_CODE-验证码
	 */
	private String type;
	
	/**
	 * 应用点
	 */
	private String useType;
	
	/**
	 * 手机号
	 */
	@Indexed
	private String mobilePhone;
	
	/**
	 * 设备关键字，保存ip或者MAC地址，用于次数限制的设备唯一标识。
	 */
	private String deviceKey;
	
	/**
	 * 短信内容
	 */
	private String content;
	
	/**
	 * 发送状态：0-未发送 1-发送成功 2-发送失败 3-已使用(针对于短信验证码) 4-已失效
	 */
	private Integer status;
	
	/**
	 * 计数标志：1-有效 0-无效
	 * 只汇总cntFlag=1的短信记录作为判断是否达到计数总数
	 */
	private Integer cntFlag;
	
	/**
	 * 发送时间
	 */
	private Date sendTime;
	
	/**
	 * 有效期至
	 */
	private Date validUntil;
	
	/**
	 * 使用时间
	 */
	private Date useTime;
	
	/**
	 * 发送起始时间
	 */
	private String sendStart;
	
	/**
	 * 发送结束时间
	 */
	private String sendEnd;

	/**
	 * @return the smsId
	 */
	public String getSmsId() {
		return smsId;
	}

	/**
	 * @param smsId the smsId to set
	 */
	public void setSmsId(String smsId) {
		this.smsId = smsId;
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
	 * @return the mobilePhone
	 */
	public String getMobilePhone() {
		return mobilePhone;
	}

	/**
	 * @param mobilePhone the mobilePhone to set
	 */
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	/**
	 * @return the deviceKey
	 */
	public String getDeviceKey() {
		return deviceKey;
	}

	/**
	 * @param deviceKey the deviceKey to set
	 */
	public void setDeviceKey(String deviceKey) {
		this.deviceKey = deviceKey;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
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
	 * @return the cntFlag
	 */
	public Integer getCntFlag() {
		return cntFlag;
	}

	/**
	 * @param cntFlag the cntFlag to set
	 */
	public void setCntFlag(Integer cntFlag) {
		this.cntFlag = cntFlag;
	}

	/**
	 * @return the sendTime
	 */
	public Date getSendTime() {
		return sendTime;
	}

	/**
	 * @param sendTime the sendTime to set
	 */
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * @return the validUntil
	 */
	public Date getValidUntil() {
		return validUntil;
	}

	/**
	 * @param validUntil the validUntil to set
	 */
	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

	/**
	 * @return the useTime
	 */
	public Date getUseTime() {
		return useTime;
	}

	/**
	 * @param useTime the useTime to set
	 */
	public void setUseTime(Date useTime) {
		this.useTime = useTime;
	}

	/**
	 * @return the sendStart
	 */
	public String getSendStart() {
		return sendStart;
	}

	/**
	 * @param sendStart the sendStart to set
	 */
	public void setSendStart(String sendStart) {
		this.sendStart = sendStart;
	}

	/**
	 * @return the sendEnd
	 */
	public String getSendEnd() {
		return sendEnd;
	}

	/**
	 * @param sendEnd the sendEnd to set
	 */
	public void setSendEnd(String sendEnd) {
		this.sendEnd = sendEnd;
	}
}
