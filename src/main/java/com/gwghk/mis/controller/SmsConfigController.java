package com.gwghk.mis.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.constant.DictConstant;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.model.SmsConfig;
import com.gwghk.mis.model.SmsInfo;
import com.gwghk.mis.service.SmsConfigService;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.IPUtil;
import com.gwghk.mis.util.ResourceBundleUtil;
import com.gwghk.mis.util.ResourceUtil;

/**
 * 摘要：发送短信
 * @author Gavin.guo
 * @date   2015-07-10
 */
@Scope("prototype")
@Controller
public class SmsConfigController extends BaseController{

	private static final Logger logger = LoggerFactory.getLogger(SmsConfigController.class);

	@Autowired
	private SmsConfigService smsConfigService;
	
	/**
	 * 功能：短信配置管理-首页
	 */
	@RequestMapping(value = "/smsConfig/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, ModelMap map) {
		DictConstant dict=DictConstant.getInstance();
		map.put("smsUseTypes", ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_SMS_USE_TYPE));
		map.put("status", ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_USE_STATUS));
		logger.debug("-->start into SmsConfigController.index() and url is /SmsConfigController/index.do");
		return "sms/smsConfig/smsConfigList";
	}

	/**
	 * 功能：获取dataGrid列表
	 * 
	 * @param request
	 * @param dataGrid
	 *            分页查询参数对象
	 * @param smsConfig
	 *            短信配置实体查询参数对象
	 * @return Map<String,Object> dataGrid需要的数据
	 */
	@RequestMapping(value = "/smsConfig/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> datagrid(HttpServletRequest request, DataGrid dataGrid, SmsConfig smsConfig) {
		Page<SmsConfig> page = smsConfigService.getSmsConfigs(this.createDetachedCriteria(dataGrid, smsConfig));
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", null == page ? 0 : page.getTotalSize());
		result.put("rows", null == page ? new ArrayList<SmsInfo>() : page.getCollection());
		return result;
	}
	
	/**
	 * 跳转到修改页面
	 * @param request
	 * @param smsCfgId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/smsConfig/preEdit",method=RequestMethod.GET)
	@ActionVerification(key="edit")
    public String preEdit(HttpServletRequest request,@Param("smsCfgId")String smsCfgId, ModelMap map){
        SmsConfig smsConfig = smsConfigService.findById(smsCfgId);
        map.put("smsConfig", smsConfig);
        return "sms/smsConfig/smsConfigEdit";
    }
	
	/**
	 * 跳转到添加页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/smsConfig/preAdd",method=RequestMethod.GET)
	@ActionVerification(key="add")
	public String preAdd(HttpServletRequest request){
		return "sms/smsConfig/smsConfigAdd";
	}

	/**
	 * 保存
	 * @param request
	 * @param smsConfig
	 * @return
	 */
	@RequestMapping(value="/smsConfig/save",method=RequestMethod.POST)
   	@ResponseBody
    public AjaxJson save(HttpServletRequest request, SmsConfig smsConfig){
        AjaxJson j = new AjaxJson();
        ApiResult result = null;
        Date currDate = new Date();
        smsConfig.setCreateUser(userParam.getUserNo());
        smsConfig.setCreateDate(currDate);
        smsConfig.setCreateIp(IPUtil.getClientIP(request));
        smsConfig.setUpdateUser(smsConfig.getCreateUser());
        smsConfig.setUpdateDate(currDate);
        smsConfig.setUpdateIp(smsConfig.getCreateIp());
        
        if(StringUtils.isNotBlank(smsConfig.getSmsCfgId())){
        	result = smsConfigService.update(smsConfig);
        }else{
        	result = smsConfigService.add(smsConfig);
        }
    	if(result.isOk()){
	    	j.setSuccess(true);
	    	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 保存短信配置成功：[" + smsConfig.getType() + "-" + smsConfig.getUseType() + "]!";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
    		logger.info("<<save()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 保存短信配置失败：[" + smsConfig.getType() + "-" + smsConfig.getUseType() + "]!";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<<save()|"+message+",ErrorMsg:"+result.toString());
    	}
		return j;
    }
	
	/**
	 * 删除
	 * @param request
	 * @param smsConfig
	 * @return
	 */
	@RequestMapping(value="/smsConfig/delete",method=RequestMethod.POST)
	@ActionVerification(key="delete")
	@ResponseBody
	public AjaxJson delete(HttpServletRequest request){
		String smsCfgId = request.getParameter("id");
		AjaxJson j = new AjaxJson();
		ApiResult result = smsConfigService.delete(smsCfgId);
		if(result.isOk()){
			j.setSuccess(true);
			String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除短信配置成功：" + smsCfgId + "!";
			addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
			logger.info("<<delete()|"+message);
		}else{
			j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
			String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除短信配置失败：" + smsCfgId + "!";
			addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
			logger.error("<<delete()|"+message+",ErrorMsg:"+result.toString());
		}
		return j;
	}
}
