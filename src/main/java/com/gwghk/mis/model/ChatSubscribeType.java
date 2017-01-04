/**   
 * @projectName:pm_mis  
 * @packageName:com.gwghk.mis.model  
 * @className:ChatSubscribeType.java  
 *   
 * @createTime:2016年8月31日-上午11:03:12  
 *  
 *    
 */
package com.gwghk.mis.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**   
 * @description:   订阅配置类
 * @fileName:ChatSubscribeType.java 
 * @createTime:2016年8月31日 上午11:03:12  
 * @author:Jade.zhu
 * @version 1.0.0  
 *    
 */
public class ChatSubscribeType extends BaseModelExt {

	@Id
	private String id;//objectid
	
	private String name;//订阅服务名称
	
	@Indexed
	private String groupType;//直播间
	
	@Indexed
	private String groupId;//房间ID，预留
	
	@Indexed
	private String code;//订阅服务名称代码
	
	private String analysts;//可订阅老师 [{userId, userName, point},{},{}]
	
	private String noticeTypes;//提供的订阅方式 [{type,cname,point},{},{}]
	
	private String noticeCycle;//提供的订阅周期 [{cycle,cname,point},{}]
	
	private Integer valid;
	
	private Integer status;//状态
	
	private Date startDate;//开始时间

	private Date endDate;//结束时间
	
	private String remark;//备注
	
	private Integer sequence;//排序

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAnalysts() {
		return analysts;
	}

	public void setAnalysts(String analysts) {
		this.analysts = analysts;
	}

	public String getNoticeTypes() {
		return noticeTypes;
	}

	public void setNoticeTypes(String noticeTypes) {
		this.noticeTypes = noticeTypes;
	}

	public String getNoticeCycle() {
		return noticeCycle;
	}

	public void setNoticeCycle(String noticeCycle) {
		this.noticeCycle = noticeCycle;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}
