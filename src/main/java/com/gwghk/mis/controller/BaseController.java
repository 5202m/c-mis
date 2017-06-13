package com.gwghk.mis.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gwghk.mis.common.model.DataGrid;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.enums.SortDirection;
import com.gwghk.mis.model.BaseModel;
import com.gwghk.mis.model.BaseModelExt;
import com.gwghk.mis.model.BoMenu;
import com.gwghk.mis.model.BoUser;
import com.gwghk.mis.model.MenuResult;
import com.gwghk.mis.service.LogService;
import com.gwghk.mis.util.BrowserUtils;
import com.gwghk.mis.util.IPUtil;
import com.gwghk.mis.util.ResourceUtil;

/**
 * 摘要： 基础controller类
 * @author Gavin.guo
 * @date   2014-10-14
 */
@Scope("prototype")
@Controller
public	class BaseController{
	
	protected BoUser userParam = ResourceUtil.getSessionUser();
	
	@Autowired
	protected LogService  logService;
	
	
	@Autowired
	protected HttpServletRequest request;
	
	/**
	 * 功能：分页查询时构造公共的查询条件
	 */
	protected  <T>	DetachedCriteria<T> createDetachedCriteria(DataGrid dataGrid,T t){
		 DetachedCriteria<T> dCriteria = new DetachedCriteria<T>();
		 dCriteria.setSearchModel(t);
		 dCriteria.setPageNo(dataGrid.getPage());
		 dCriteria.setPageSize(dataGrid.getRows());
		 if(StringUtils.isNotEmpty(dataGrid.getSort())){
			 SortDirection sd = "asc".equalsIgnoreCase(dataGrid.getOrder()) ? SortDirection.ASC : SortDirection.DESC;
			 LinkedHashMap<String,SortDirection> orderMap = new  LinkedHashMap<String,SortDirection>();
			 /**
			  * 混合排序开始
			  */
			 String sortStr = dataGrid.getSort();
			 if(sortStr.contains(",")){
				 String[] sortSubs = sortStr.split(",");
				 for (String sub : sortSubs) {
					 //子排序
					 if(sub.contains("-")){
						 String[] iSubs = sub.split("-");
						 orderMap.put(iSubs[0] , "asc".equalsIgnoreCase(iSubs[1]) ? SortDirection.ASC : SortDirection.DESC);
					 }else{
						 orderMap.put(sub, sd); 
					 }
				 }
			 }else{
				 orderMap.put(sortStr, sd); 
			 }
			 /**
			  * 混合排序结束
			  */
			 dCriteria.setOrderbyMap(orderMap); 
		 }
		 return dCriteria;
	}
	
	/**
	 * 功能：设置基本信息(创建人、创建人IP、更新人、更新人IP)
	 */
	protected  void setBaseInfo(Object obj,HttpServletRequest request,boolean update){
		String userNo = userParam.getUserNo();
		if(obj instanceof BaseModel){
    		BaseModel b =(BaseModel)obj;
    		if(!update){
    			b.setCreateUser(userNo);
        		b.setCreateIp(IPUtil.getClientIP(request));
    		}
    		b.setUpdateUser(userNo);
    		b.setUpdateIp(IPUtil.getClientIP(request));
    	}
		setSystemFlag(obj);
	}
	
	/**
	 * 功能：获取菜单下的功能列表
	 * @param menuId  当前选中菜单Id
	 * @return List<MenuParam>  功能列表
	 */
	protected	List<BoMenu>  getFunMenuList(String menuId){
		MenuResult menuResult = ResourceUtil.getSessionMenu();
		Map<String, List<BoMenu>> funMap= menuResult.getFunMap();
		if(funMap==null){
		    return null;
		}
		List<BoMenu> menuParamList = funMap.get(menuId);
		return menuParamList;
	}
	
	/**
	 * 返回系统标记
	 * @return
	 */
	protected String getSystemFlag(){
		Object superFlag = request.getSession().getAttribute(WebConstant.SESSION_SUPER_FLAG);
		boolean isSuper=(superFlag!=null && (boolean)superFlag);
		String systemCategory=request.getParameter("systemCategory");
		if(isSuper){
			return systemCategory;
		}else{
			if(StringUtils.isNotBlank(systemCategory) && !systemCategory.equals(userParam.getSystemCategory())){
				System.out.println("has error==>systemCategory【"+systemCategory+"】 not eq the userParam.getSystemCategory()【"+userParam.getSystemCategory()+"】");
			}
			return userParam.getSystemCategory();
		}
	}
	
	/**
	 * 设置系统标志
	 * @param obj
	 */
	protected  void setSystemFlag(Object obj){
		if(obj instanceof BaseModelExt){
			BaseModelExt b =(BaseModelExt)obj;
    	    b.setSystemCategory(getSystemFlag());
    	}
	}
	
	/**
	 * 功能：添加日志
	 * @param   logcontent   日志内容
	 * @param   loglevel     日志级别
	 * @param   operatetype  操作类型
	 */
	protected void addLog(String logcontent, Integer loglevel, String operatetype) {
		String systemCategory = StringUtils.isNotBlank(getSystemFlag()) ? getSystemFlag() : userParam.getSystemCategory();
		logService.addLog(systemCategory,logcontent,loglevel,operatetype,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
	}
}

