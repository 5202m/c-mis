package com.gwghk.mis.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 摘要：公共树形对象
 * @author Gavin
 * @date   2014-10-28
 */
public class TreeVo{

	/**主键*/
	private String  id;
	
	/**父节点code*/
	private String  parentCode;

	/**菜单编码*/
	private String code;
	
	/**菜单名称*/
	private String name;
	
	private String nameCN;
	
	private String nameTW;
	
	private String nameEN;
	
	/**菜单状态*/
	private String state;
	
	/**排序*/
	private Integer sort;
	
	/**菜单类型： 分数字字典 1、数据字典分类2 两种  */
	private String type;
	
	private String flag;
	/**
	 * 状态(0：禁用 1：启用)
	 */
	private Integer status;
	
	private String value;
	
	/**子菜单*/
	private List<TreeVo>  children = new ArrayList<TreeVo>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<TreeVo> getChildren() {
		return children;
	}

	public void setChildren(List<TreeVo> children) {
		this.children = children;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getNameCN() {
		return nameCN;
	}

	public void setNameCN(String nameCN) {
		this.nameCN = nameCN;
	}

	public String getNameTW() {
		return nameTW;
	}

	public void setNameTW(String nameTW) {
		this.nameTW = nameTW;
	}

	public String getNameEN() {
		return nameEN;
	}

	public void setNameEN(String nameEN) {
		this.nameEN = nameEN;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}