package com.gwghk.mis.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * 摘要：数据字典实体对象(系统管理)
 * @author Gavin.guo
 */
public class BoDict extends BaseModelExt{ 

	@Id
	private String id;
	/**
	 * 字典代码
	 */
	@Indexed
	private String code;

	/**
	 * 是否有效，默认有效
	 */
	private Integer valid;
	
	/**
     * 状态
     */
	private Integer status;

	/**
	 * 简体名称
	 */
	private String nameCN;

	/**
	 * 繁体名称
	 */
	private String nameTW;

	/**
	 * 英文名称
	 */
	private String nameEN;

	/**
	 * 排序
	 */
	private Integer sort;
    
	/**
	 * 用于显示菜单的名称
	 */
	private String name;
	
	/**
	 * 用于显示菜单的格外值
	 */
	private String value;

	/**
	 * 子类字典
	 */
	private List<BoDict> children;
	
	/**
	 * 仅用于显示层的参数，非数据层字段
	 */
	private boolean checked;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode () { 
		return this.code;
	}

	public void setCode (String code) { 
		this.code = code;
	}

	public Integer getValid () { 
		return this.valid;
	}

	public void setValid (Integer valid) { 
		this.valid = valid;
	}

	public String getNameCN () { 
		return this.nameCN;
	}

	public void setNameCN (String nameCN) { 
		this.nameCN = nameCN;
	}

	public String getNameTW () { 
		return this.nameTW;
	}

	public void setNameTW (String nameTW) { 
		this.nameTW = nameTW;
	}

	public String getNameEN () { 
		return this.nameEN;
	}

	public void setNameEN (String nameEN) { 
		this.nameEN = nameEN;
	}

	public Integer getSort () { 
		return this.sort;
	}

	public void setSort (Integer sort) { 
		this.sort = sort;
	}

	public String getName () { 
		return this.name;
	}

	public void setName (String name) { 
		this.name = name;
	}

	public List<BoDict> getChildren() {
		return children;
	}

	public void setChildren(List<BoDict> children) {
		this.children = children;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
}