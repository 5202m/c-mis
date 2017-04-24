package com.gwghk.mis.common.model;

import java.util.List;

import com.gwghk.mis.enums.SortDirection;
import com.gwghk.mis.util.ContextHolderUtils;
import com.gwghk.mis.util.ResourceUtil;

/**
 * 摘要：easyui的datagrid向后台传递参数使用的model
 * @author Gavin.guo
 */
public class DataGrid {
	
	/**当前页*/
	private int page = 1;
	
	/**每页显示记录数*/
	private int rows = 10;
	
	/**排序字段名*/
	private String sort = null;
	
	/**排序 desc或asc*/
	private String order = SortDirection.ASC.getValue();
	
	/**字段*/
	private String field;
	
	/**树形数据表文本字段*/
	private String treefield;
	
	/**结果集*/
	private List<?> results;
	
	/**总记录数*/
	private int total;
	
	/**合计列*/
	private String footer;
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getField() {
		return field;
	}

	public List<?> getResults() {
		return results;
	}

	public void setResults(List<?> results) {
		this.results = results;
	}

	public void setField(String field) {
		this.field = field;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		if(ContextHolderUtils.getRequest()!=null && ResourceUtil.getParameter("rows")!=null){
			return rows;
		}
		return 100000;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
	public String getTreefield() {
		return treefield;
	}

	public void setTreefield(String treefield) {
		this.treefield = treefield;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}
	
}
