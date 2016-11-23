package com.gwghk.mis.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.gwghk.mis.model.ZxFinanceDataComment;
import org.apache.commons.lang3.StringUtils;
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
import com.gwghk.mis.model.ZxFinanceData;
import com.gwghk.mis.model.ZxFinanceDataCfg;
import com.gwghk.mis.service.ZxFinanceDataService;
import com.gwghk.mis.util.BrowserUtils;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.IPUtil;
import com.gwghk.mis.util.ResourceBundleUtil;

/**
 * 摘要：财经日历
 * @author Dick.guo
 * @date   2016-03-22
 */
@Scope("prototype")
@Controller
public class ZxFinanceDataController extends BaseController{

	private static final Logger logger = LoggerFactory.getLogger(ZxFinanceDataController.class);

	@Autowired
	private ZxFinanceDataService dataService;
	
	/**
	 * 功能：财经日历-首页（财经日历）
	 */
	@RequestMapping(value = "/zxDataController/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, ModelMap map) {
		logger.debug("-->start into ZxFinanceDataController.index() and url is /zxDataController/index.do");
		return "infoManage/zxDataList";
	}

	/**
	 * 功能：获取dataGrid列表（财经日历）
	 * 
	 * @param request
	 * @param dataGrid
	 *            分页查询参数对象
	 * @param data
	 *            实体查询参数对象
	 */
	@RequestMapping(value = "/zxDataController/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> datagrid(HttpServletRequest request, DataGrid dataGrid, ZxFinanceData data) {
		DetachedCriteria<ZxFinanceData> detachedCriteria = this.createDetachedCriteria(dataGrid, data);
		if(detachedCriteria.getOrderbyMap() == null || detachedCriteria.getOrderbyMap().isEmpty()){
			LinkedHashMap<String, SortDirection> orderMap = new LinkedHashMap<String, SortDirection>();
			orderMap.put("date", SortDirection.DESC);
			orderMap.put("time", SortDirection.DESC);
			detachedCriteria.setOrderbyMap(orderMap); 
		}
		Page<ZxFinanceData> page = dataService.getDatas(detachedCriteria);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", null == page ? 0 : page.getTotalSize());
		result.put("rows", null == page ? new ArrayList<ZxFinanceData>() : page.getCollection());
		return result;
	}
	
	/**
	 * 跳转到修改页面（财经日历）
	 * @param request
	 * @param dataId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/zxDataController/preEdit",method=RequestMethod.GET)
	@ActionVerification(key="edit")
    public String preEdit(HttpServletRequest request,@Param("dataId")String dataId, ModelMap map){
		ZxFinanceData data = dataService.findById(dataId);
        map.put("zxFinanceData", data);
        return "infoManage/zxDataEdit";
    }

	/**
	 * 保存（财经日历）
	 * @param request
	 * @param data
	 * @return
	 */
	@RequestMapping(value="/zxDataController/save",method=RequestMethod.POST)
   	@ResponseBody
    public AjaxJson save(HttpServletRequest request, ZxFinanceData data){
        AjaxJson j = new AjaxJson();
        ApiResult result = null;
        Date currDate = new Date();
        data.setUpdateUser(userParam.getUserNo());
        data.setUpdateDate(currDate);
        data.setUpdateIp(IPUtil.getClientIP(request));
        
        result = dataService.update(data);
    	if(result.isOk()){
	    	j.setSuccess(true);
	    	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 保存财经日历成功：[" + data.getDataId() + "-" + data.getDescription() + "-" + data.getDataType() + "]!";
    		logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.info("<<save()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 保存财经日历失败：[" + data.getDataId() + "-" + data.getDescription() + "-" + data.getDataType() + "]!";
    		logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.error("<<save()|"+message+",ErrorMsg:"+result.toString());
    	}
		return j;
    }
	
	/**
	 * 删除（财经日历）
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/zxDataController/delete",method=RequestMethod.POST)
	@ActionVerification(key="delete")
	@ResponseBody
	public AjaxJson delete(HttpServletRequest request){
		String dataId = request.getParameter("id");
		AjaxJson j = new AjaxJson();
		ApiResult result = dataService.delete(dataId);
		if(result.isOk()){
			j.setSuccess(true);
			String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除财经日历成功：" + dataId + "!";
			logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
			logger.info("<<delete()|"+message);
		}else{
			j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
			String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除财经日历失败：" + dataId + "!";
			logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
			logger.error("<<delete()|"+message+",ErrorMsg:"+result.toString());
		}
		return j;
	}
	

	/**
	 * 功能：财经日历-首页（财经日历配置）
	 */
	@RequestMapping(value = "/zxDataController/cfgIndex", method = RequestMethod.GET)
	public String cfgIndex(HttpServletRequest request, ModelMap map) {
		logger.debug("-->start into ZxFinanceDataController.cfgIndex() and url is /zxDataController/cfgIndex.do");
		return "infoManage/zxDataCfgList";
	}

	/**
	 * 功能：获取dataGrid列表（财经日历配置）
	 * 
	 * @param request
	 * @param dataGrid
	 *            分页查询参数对象
	 * @param config
	 *            实体查询参数对象
	 */
	@RequestMapping(value = "/zxDataController/cfgDatagrid", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> cfgDatagrid(HttpServletRequest request, DataGrid dataGrid, ZxFinanceDataCfg config) {
		Page<ZxFinanceDataCfg> page = dataService.getDataCfgs(this.createDetachedCriteria(dataGrid, config));
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", null == page ? 0 : page.getTotalSize());
		result.put("rows", null == page ? new ArrayList<ZxFinanceData>() : page.getCollection());
		return result;
	}
	
	/**
	 * 跳转到修改页面（财经日历配置）
	 * @param request
	 * @param basicIndexId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/zxDataController/cfgPreEdit",method=RequestMethod.GET)
	@ActionVerification(key="edit")
    public String cfgPreEdit(HttpServletRequest request,@Param("basicIndexId")String basicIndexId, ModelMap map){
		ZxFinanceDataCfg data = dataService.findCfgById(basicIndexId);
        map.put("zxFinanceDataCfg", data);
        return "infoManage/zxDataCfgEdit";
    }

	/**
	 * 保存
	 * @param request
	 * @param data
	 * @return
	 */
	@RequestMapping(value="/zxDataController/cfgSave",method=RequestMethod.POST)
   	@ResponseBody
    public AjaxJson cfgSave(HttpServletRequest request, ZxFinanceDataCfg config){
        AjaxJson j = new AjaxJson();
        ApiResult result = null;
        Date currDate = new Date();
        config.setUpdateUser(userParam.getUserNo());
        config.setUpdateDate(currDate);
        config.setUpdateIp(IPUtil.getClientIP(request));
        
        result = dataService.updateCfg(config);
    	if(result.isOk()){
	    	j.setSuccess(true);
	    	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 保存财经日历配置成功：[" + config.getBasicIndexId() + "-" + config.getDataType() + "-" + config.getImportanceLevel() + "-" + config.getDescription() + "]!";
    		logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.info("<<save()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 保存财经日历配置失败：[" + config.getBasicIndexId() + "]!";
    		logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.error("<<save()|"+message+",ErrorMsg:"+result.toString());
    	}
		return j;
    }
	
	/**
	 * 删除
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/zxDataController/cfgDelete",method=RequestMethod.POST)
	@ActionVerification(key="delete")
	@ResponseBody
	public AjaxJson cfgDelete(HttpServletRequest request){
		String dataId = request.getParameter("id");
		AjaxJson j = new AjaxJson();
		ApiResult result = dataService.deleteCfg(dataId);
		if(result.isOk()){
			j.setSuccess(true);
			String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除财经日历配置成功：" + dataId + "!";
			logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
			logger.info("<<delete()|"+message);
		}else{
			j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
			String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除财经日历配置失败：" + dataId + "!";
			logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
			logger.error("<<delete()|"+message+",ErrorMsg:"+result.toString());
		}
		return j;
	}

	/**
	 * 跳转到点评页面（财经日历）
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/zxDataController/review",method=RequestMethod.POST,produces = "plain/text; charset=UTF-8")
	@ActionVerification(key="review")
	@ResponseBody
	public String preReview(HttpServletRequest request, ModelMap map){
		String dataId = request.getParameter("dataId");
		if(StringUtils.isBlank(dataId)){
			return null;
		}
		ZxFinanceData data = dataService.findById(dataId);
		map.put("user", userParam);
		map.put("data", data);
		return JSONArray.toJSONString(map);
	}

	/**
	 * 保存（财经日历）
	 * @param request
	 * @param data
	 * @return
	 */
	@RequestMapping(value="/zxDataController/saveReview",method=RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveComment(HttpServletRequest request, ZxFinanceData data){
		AjaxJson j = new AjaxJson();
		ApiResult result = null;
		String dataId = request.getParameter("dataId");
		String commentId = request.getParameter("id");
		String comment = request.getParameter("comment");
		String avatar = request.getParameter("avatar");
		String userId = request.getParameter("userId");
		String userName = request.getParameter("name");
		Date currDate = new Date();
		data.setDataId(dataId);
		data.setUpdateUser(userParam.getUserNo());
		data.setUpdateDate(currDate);
		data.setUpdateIp(IPUtil.getClientIP(request));
		ZxFinanceDataComment comments = new ZxFinanceDataComment();
		if(StringUtils.isNotBlank(commentId)) {
			comments.setId(commentId);
			comments.setUpdateDate(currDate);
			comments.setUpdateIp(data.getUpdateIp());
			comments.setUpdateUser(userParam.getUserName());
		}else{
			comments.setAvatar(avatar);
			comments.setUserId(userId);
			comments.setUserName(userName);
			comments.setCreateIp(data.getUpdateIp());
			comments.setCreateDate(currDate);
			comments.setCreateUser(userParam.getUserName());
		}
		comments.setValid(1);
		comments.setComment(comment);

		result = dataService.saveComments(data, comments);
		if(result.isOk()){
			j.setSuccess(true);
			String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 保存财经日历点评成功：[" + data.getDataId() + "-" + data.getDescription() + "-" + data.getDataType() + "]!";
			logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
			logger.info("<<save()|"+message);
		}else{
			j.setSuccess(false);
			j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
			String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 保存财经日历点评失败：[" + data.getDataId() + "-" + data.getDescription() + "-" + data.getDataType() + "]!";
			logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
			logger.error("<<save()|"+message+",ErrorMsg:"+result.toString());
		}
		return j;
	}

	/**
	 * 删除（财经日历）
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/zxDataController/delReview",method=RequestMethod.POST)
	@ActionVerification(key="delete")
	@ResponseBody
	public AjaxJson delReview(HttpServletRequest request){
		String dataId = request.getParameter("dataId");
		String id = request.getParameter("id");
		AjaxJson j = new AjaxJson();
		ApiResult result = dataService.delReview(dataId, id);
		if(result.isOk()){
			j.setSuccess(true);
			String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除财经日历点评成功：" + dataId + "!";
			logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
			logger.info("<<delete()|"+message);
		}else{
			j.setSuccess(false);
			j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
			String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除财经日历点评失败：" + dataId + "!";
			logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
			logger.error("<<delete()|"+message+",ErrorMsg:"+result.toString());
		}
		return j;
	}

}
