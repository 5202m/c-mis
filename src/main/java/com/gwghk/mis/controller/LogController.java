package com.gwghk.mis.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gwghk.mis.common.model.DataGrid;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.model.BoLog;
import com.gwghk.mis.service.LogService;
import com.gwghk.mis.util.DateUtil;

/**
 * 摘要：日志管理
 * @author Gavin.guo
 * @date   2014-10-27
 */
@Scope("prototype")
@Controller
public class LogController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(LogController.class);
	
	@Autowired
	private LogService logService;
	
	/**
	 * 功能：首页
	 */
	@RequestMapping(value = "/sysLogController/index", method = RequestMethod.GET)
	public  String  index(ModelMap map){
		logger.debug("-->start into sysLogController.index()...");
		return "system/syslog/syslogList";
	}

	/**
	 * 功能：获取日志dataGrid列表
	 * @param request
	 * @param dataGrid  			分页查询参数对象
	 * @param log  					日志实体查询参数对象
	 * @return Map<String,Object>   dataGrid需要的数据
	 */
	@RequestMapping(value = "/sysLogController/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public  Map<String,Object>  datagrid(HttpServletRequest request, DataGrid dataGrid,BoLog log){
		String startDate = request.getParameter("startDate"),endDate = request.getParameter("endDate");
		log.setOperStartDate(DateUtil.parseDateSecondFormat(startDate));
		log.setOperEndDate(DateUtil.parseDateSecondFormat(endDate));
		Page<BoLog> page = logService.getLogPage(this.createDetachedCriteria(dataGrid, log));
		Map<String, Object> result = new HashMap<String, Object>(); 
		result.put("total",null == page ? 0  : page.getTotalSize());
	    result.put("rows", null == page ? new ArrayList<BoLog>() : page.getCollection());
	    return result;
	}
	
}
