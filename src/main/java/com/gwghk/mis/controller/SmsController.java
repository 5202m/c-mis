package com.gwghk.mis.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.constant.DictConstant;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.model.SmsInfo;
import com.gwghk.mis.service.PmApiService;
import com.gwghk.mis.service.SmsInfoService;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.ResourceBundleUtil;
import com.gwghk.mis.util.ResourceUtil;

/**
 * 摘要：发送短信
 * @author Gavin.guo
 * @date   2015-07-10
 */
@Scope("prototype")
@Controller
public class SmsController extends BaseController{

	private static final Logger logger = LoggerFactory.getLogger(SmsController.class);

	@Autowired
	private SmsInfoService smsInfoService;

	@Autowired
	private PmApiService pmApiService;
	
	/**
	 * 功能：短信信息管理-首页
	 */
	@RequestMapping(value = "/sms/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, ModelMap map) {
		DictConstant dict=DictConstant.getInstance();
		map.put("smsUseTypes", ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_SMS_USE_TYPE));
		logger.debug("-->start into SmsInfoController.index() and url is /smsInfoController/index.do");
		return "sms/smsInfo/smsInfoList";
	}

	/**
	 * 功能：获取dataGrid列表
	 * 
	 * @param request
	 * @param dataGrid
	 *            分页查询参数对象
	 * @param smsInfo
	 *            短信实体查询参数对象
	 * @return Map<String,Object> dataGrid需要的数据
	 */
	@RequestMapping(value = "/sms/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> datagrid(HttpServletRequest request, DataGrid dataGrid, SmsInfo smsInfo) {
		Page<SmsInfo> page = smsInfoService.getSmsInfos(this.createDetachedCriteria(dataGrid, smsInfo));
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", null == page ? 0 : page.getTotalSize());
		result.put("rows", null == page ? new ArrayList<SmsInfo>() : page.getCollection());
		return result;
	}
	
	/**
	 * 重新发送短信
	 * @param smsId
	 * @return
	 */
	@RequestMapping(value="/sms/resend",method=RequestMethod.POST)
	@ActionVerification(key="redo")
   	@ResponseBody
    public AjaxJson resend(HttpServletRequest request,@Param("smsId")String smsId){
        AjaxJson j = new AjaxJson();
        ApiResult result = smsInfoService.resend(smsId);
    	if(result.isOk()){
	    	j.setSuccess(true);
	    	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 重发短信成功smsId=" + smsId + "!";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
    		logger.info("<<resend()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 重发短信失败smsId=" + smsId + "!";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<<resend()|"+message+",ErrorMsg:"+result.toString());
    	}
		return j;
    }
	
	/**
	 * 跳转到重置计数器页面
	 * @param request
	 * @param smsId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/sms/preReset",method=RequestMethod.GET)
	@ActionVerification(key="reply")
    public String preEdit(HttpServletRequest request,@Param("smsId")String smsId, ModelMap map){
		map.putAll(smsInfoService.getSmsInfoMap(getSystemFlag(),smsId));
        return "sms/smsInfo/smsResetCnt";
    }
	

	/**
	 * 重新发送短信
	 * @param smsId
	 * @return
	 */
	@RequestMapping(value="/sms/reset",method=RequestMethod.POST)
   	@ResponseBody
    public AjaxJson reset(HttpServletRequest request){
        AjaxJson j = new AjaxJson();
        String mobile = request.getParameter("mobilePhone");
        String type = request.getParameter("type");
        String useType = request.getParameter("useType");
        String deviceKey = request.getParameter("deviceKey");
        String startDate = request.getParameter("resetStart");
        ApiResult result = smsInfoService.setCntFlag(getSystemFlag(),mobile, type, useType, deviceKey, startDate);
    	if(result.isOk()){
	    	j.setSuccess(true);
	    	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 重置短信计数器成功：" + mobile + "!";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
    		logger.info("<<resend()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 重置短信计数器失败：" + mobile + "!";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<<resend()|"+message+",ErrorMsg:"+result.toString());
    	}
		return j;
    }
}
