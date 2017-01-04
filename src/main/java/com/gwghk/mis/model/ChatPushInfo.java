package com.gwghk.mis.model;

import org.springframework.data.mongodb.core.mapping.Document;


/** 聊天室信息推送实体类
 * @author Alan.wu
 * @date   2015年7月10日
 */
@Document
public class ChatPushInfo extends BaseModelExt {
	/**
	 * 信息id
	 */
	private String id;
	
	private String title;
	/**
	 * 信息内容
	 */
	private String content;
	
	/**
	 * 推送方式： 0 预定义、1、即时执行
	 */
	private Integer pushType;
	
	/**
	 * 推送位置：0 任务栏 、1 私聊框、2、页面提示
	 */
	private Integer position;
	
	/**
	 * 房间类型
	 */
	private String groupType;
	
	/**
	 * 客户类型
	 */
	private String[] clientGroup;
	
	/**
	 * 房间
	 */
	private String[] roomIds;
	
	/**
	 * 推送总次数
	 */
	private Integer pushTimes;
	
	/**
	 * 在线分钟数
	 */
	private Float onlineMin;
	
	/**
	 * 间隔分钟数
	 */
	private Float intervalMin;
	
	/**
	 * 推送有效时间  保存格式{beginDate:yyyy-MM-dd,endDate:yyyy-MM-dd,weekTime:[{week:0..6,beginTime:'HH:mm:ss',endTime:'HH:mm:ss'}]}
	 */
	private String pushDate;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 状态，启用1，禁用 0
	 */
	private Integer status;
	
	/**
	 * 推送内容回复后是否继续重复通知
	 * 0表示回复后不再重复，1表示回复后继续提示
	 */
	private Integer replyRepeat;
	
	/**
	 * 是否有效
	 */
	private Integer valid;
	
	/**
	 * 跳转链接
	 */
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getPushType() {
		return pushType;
	}

	public void setPushType(Integer pushType) {
		this.pushType = pushType;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String[] getClientGroup() {
		return clientGroup;
	}

	public void setClientGroup(String[] clientGroup) {
		this.clientGroup = clientGroup;
	}

	public String[] getRoomIds() {
		return roomIds;
	}

	public void setRoomIds(String[] roomIds) {
		this.roomIds = roomIds;
	}

	public Integer getPushTimes() {
		return pushTimes;
	}

	public void setPushTimes(Integer pushTimes) {
		this.pushTimes = pushTimes;
	}

	public Float getOnlineMin() {
		return onlineMin;
	}

	public void setOnlineMin(Float onlineMin) {
		this.onlineMin = onlineMin;
	}
	
	public Float getIntervalMin()
	{
		return intervalMin;
	}

	public void setIntervalMin(Float intervalMin)
	{
		this.intervalMin = intervalMin;
	}

	public String getPushDate() {
		return pushDate;
	}

	public void setPushDate(String pushDate) {
		this.pushDate = pushDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getReplyRepeat() {
		return replyRepeat;
	}

	public void setReplyRepeat(Integer replyRepeat) {
		this.replyRepeat = replyRepeat;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
