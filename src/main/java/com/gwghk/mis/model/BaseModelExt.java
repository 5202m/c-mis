package com.gwghk.mis.model;

/**
 * 摘要：基础实体扩展类
 * @author alan.wu
 * @date   2016-11-22
 */
public abstract class BaseModelExt extends BaseModel{ 
	//所属系统
	protected String systemCategory;

	public String getSystemCategory() {
		return systemCategory;
	}

	public void setSystemCategory(String systemCategory) {
		this.systemCategory = systemCategory;
	}
}