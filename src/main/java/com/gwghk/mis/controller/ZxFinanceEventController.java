package com.gwghk.mis.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gwghk.mis.authority.ActionVerification;
import com.gwghk.mis.common.model.AjaxJson;
import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DataGrid;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.enums.SortDirection;
import com.gwghk.mis.model.ZxFinanceEvent;
import com.gwghk.mis.service.ZxFinanceEventService;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.IPUtil;
import com.gwghk.mis.util.ResourceBundleUtil;

/**
 * 摘要：财经大事
 * @author Dick.guo
 * @date   2016-03-23
 */
@Scope("prototype")
@Controller
public class ZxFinanceEventController extends BaseController{

	private static final Logger logger = LoggerFactory.getLogger(ZxFinanceEventController.class);

	@Autowired
	private ZxFinanceEventService eventService;
	
	/**
	 * 功能：财经日历-首页
	 */
	@RequestMapping(value = "/zxEventController/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, ModelMap map) {
		logger.debug("-->start into ZxFinanceEventController.index() and url is /zxEventController/index.do");
		return "infoManage/zxEventList";
	}

	/**
	 * 功能：获取dataGrid列表
	 * 
	 * @param request
	 * @param dataGrid
	 *            分页查询参数对象
	 * @param event
	 *            短信配置实体查询参数对象
	 * @return Map<String,Object> dataGrid需要的数据
	 */
	@RequestMapping(value = "/zxEventController/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> datagrid(HttpServletRequest request, DataGrid dataGrid, ZxFinanceEvent event) {
		DetachedCriteria<ZxFinanceEvent> detachedCriteria = this.createDetachedCriteria(dataGrid, event);
		if(detachedCriteria.getOrderbyMap() == null || detachedCriteria.getOrderbyMap().isEmpty()){
			LinkedHashMap<String, SortDirection> orderMap = new LinkedHashMap<String, SortDirection>();
			orderMap.put("date", SortDirection.DESC);
			orderMap.put("time", SortDirection.DESC);
			detachedCriteria.setOrderbyMap(orderMap); 
		}
		Page<ZxFinanceEvent> page = eventService.getEvents(detachedCriteria);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", null == page ? 0 : page.getTotalSize());
		result.put("rows", null == page ? new ArrayList<ZxFinanceEvent>() : page.getCollection());
		return result;
	}
	
	/**
	 * 跳转到修改页面
	 * @param request
	 * @param eventId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/zxEventController/preEdit",method=RequestMethod.GET)
	@ActionVerification(key="edit")
    public String preEdit(HttpServletRequest request,@Param("eventId")String eventId, ModelMap map){
		ZxFinanceEvent event = eventService.findById(eventId);
        map.put("zxFinanceEvent", event);
        return "infoManage/zxEventEdit";
    }

	/**
	 * 保存
	 * @param request
	 * @param event
	 * @return
	 */
	@RequestMapping(value="/zxEventController/save",method=RequestMethod.POST)
   	@ResponseBody
    public AjaxJson save(HttpServletRequest request, ZxFinanceEvent event){
        AjaxJson j = new AjaxJson();
        ApiResult result = null;
        Date currDate = new Date();
        event.setUpdateUser(userParam.getUserNo());
        event.setUpdateDate(currDate);
        event.setUpdateIp(IPUtil.getClientIP(request));
        
        result = eventService.update(event);
    	if(result.isOk()){
	    	j.setSuccess(true);
	    	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 保存财经日历成功：[" + event.getEventId() + "-" + event.getDataType() + "]!";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
    		logger.info("<<save()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 保存财经日历失败：[" + event.getEventId() + "-" + event.getDataType() + "]!";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<<save()|"+message+",ErrorMsg:"+result.toString());
    	}
		return j;
    }
	
	/**
	 * 删除
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/zxEventController/delete",method=RequestMethod.POST)
	@ActionVerification(key="delete")
	@ResponseBody
	public AjaxJson delete(HttpServletRequest request){
		String eventId = request.getParameter("id");
		AjaxJson j = new AjaxJson();
		ApiResult result = eventService.delete(eventId);
		if(result.isOk()){
			j.setSuccess(true);
			String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除财经日历成功：" + eventId + "!";
			addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
			logger.info("<<delete()|"+message);
		}else{
			j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
			String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除财经日历失败：" + eventId + "!";
			addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
			logger.error("<<delete()|"+message+",ErrorMsg:"+result.toString());
		}
		return j;
	}
}
