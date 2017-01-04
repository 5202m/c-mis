package com.gwghk.mis.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 晒单
 * 
 * @author Henry.cao
 * @date 2016年06月20日
 */
@Document
public class ChatShowTrade extends BaseModelExt
{
	@Id
	private String id;//ObjectId
	@Indexed
	private String groupType;
	@Indexed
	private String groupId;
	@Indexed
	private BoUser boUser; //分析师
	private Date showDate; //晒单时间
	private String tradeImg; //晒单图片
	private String profit; //获利
	private String remark;
	private Integer valid;
	private String title;//标题
	private Integer tradeType;//类别：1 分析师晒单，2 客户晒单
	private Integer status;//状态：0 待审核， 1 审核通过， -1 审核不通过
	private Integer praise;//点赞
	
	public Integer getPraise() {
		return praise;
	}
	public void setPraise(Integer praise) {
		this.praise = praise;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getTradeType() {
		return tradeType;
	}
	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
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
	public BoUser getBoUser() {
		return boUser;
	}
	public void setBoUser(BoUser boUser) {
		this.boUser = boUser;
	}
	public Date getShowDate() {
		return showDate;
	}
	public void setShowDate(Date showDate) {
		this.showDate = showDate;
	}
	public String getTradeImg() {
		return tradeImg;
	}
	public void setTradeImg(String tradeImg) {
		this.tradeImg = tradeImg;
	}
	public String getProfit() {
		return profit;
	}
	public void setProfit(String profit) {
		this.profit = profit;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
}
