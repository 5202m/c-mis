package com.gwghk.mis.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 聊天室组别规则实体类
 * @author Alan.wu
 * @date   2015年4月1日
 */
@Document
public class ChatGroupRule extends BaseModelExt{
	/**
	 * 规则id
	 */
	@Id
	private String id;
		
	/**
	 * 规则类别
	 */
	@Indexed
	private String type;
	
	/**
	 * 规则名称
	 */
	private String name;

	/**
     * 使用规则前的值
     */
	private String beforeRuleVal;
	
	/**
     * 使用规则后的值
     */
	private String afterRuleVal;
	
	/**
     * 生效时间，保存格式{beginDate:yyyy-MM-dd,endDate:yyyy-MM-dd,weekTime:[{week:0..6,beginTime:'HH:mm:ss',endTime:'HH:mm:ss'}]}
     */
	private String periodDate;
	
	/**
     * 执行规则后的提示语
     */
	private String afterRuleTips;
	
	/**
     * 是否删除
     */
	private Integer valid;
	
	/**
	 * 客户组（对应的客户组,多个逗号分隔）
	 */
	private String clientGroup;
	

	public String getClientGroup() {
		return clientGroup;
	}

	public void setClientGroup(String clientGroup) {
		this.clientGroup = clientGroup;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBeforeRuleVal() {
		return beforeRuleVal;
	}

	public void setBeforeRuleVal(String beforeRuleVal) {
		this.beforeRuleVal = beforeRuleVal;
	}

	public String getAfterRuleVal() {
		return afterRuleVal;
	}

	public void setAfterRuleVal(String afterRuleVal) {
		this.afterRuleVal = afterRuleVal;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	public String getPeriodDate() {
		return periodDate;
	}

	public void setPeriodDate(String periodDate) {
		this.periodDate = periodDate;
	}

	public String getAfterRuleTips() {
		return afterRuleTips;
	}

	public void setAfterRuleTips(String afterRuleTips) {
		this.afterRuleTips = afterRuleTips;
	}
	
}
