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
import com.gwghk.mis.model.ChatPoints;
import com.gwghk.mis.model.SmsInfo;
import com.gwghk.mis.service.ChatGroupService;
import com.gwghk.mis.service.ChatPointsService;
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
public class ChatPointsController extends BaseController{

	private static final Logger logger = LoggerFactory.getLogger(ChatPointsController.class);

	@Autowired
	private ChatPointsService chatPointsService;
	
	@Autowired
	private ChatGroupService chatGroupService;
	
	/**
	 * 功能：积分配置管理-首页
	 */
	@RequestMapping(value = "/chatPointsInfo/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, ModelMap map) {
		DictConstant dict=DictConstant.getInstance();
		map.put("type", ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_POINTS_TYPE));
		map.put("item", ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_POINTS_ITEM));
		map.put("groupList", ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_TYPE));
		logger.debug("-->start into ChatPointsController.index() and url is /ChatPointsController/index.do");
		return "points/chatPointsInfoList";
	}

	/**
	 * 功能：获取dataGrid列表
	 * 
	 * @param request
	 * @param dataGrid
	 *            分页查询参数对象
	 * @param chatPoints
	 *            积分配置实体查询参数对象
	 * @return Map<String,Object> dataGrid需要的数据
	 */
	@RequestMapping(value = "/chatPointsInfo/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> datagrid(HttpServletRequest request, DataGrid dataGrid, ChatPoints chatPoints) {
		setSystemFlag(chatPoints);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", request.getParameter("type"));
		String param = request.getParameter("pointsStart");
		if(StringUtils.isNotBlank(param)){
			params.put("pointsStart", Long.parseLong(param));
		}
		param = request.getParameter("pointsEnd");
		if(StringUtils.isNotBlank(param)){
			params.put("pointsEnd", Long.parseLong(param));
		}
		param = request.getParameter("timeStart");
		if(StringUtils.isNotBlank(param)){
			params.put("timeStart", DateUtil.parseDateSecondFormat(param));
		}
		param = request.getParameter("timeEnd");
		if(StringUtils.isNotBlank(param)){
			params.put("timeEnd", DateUtil.parseDateSecondFormat(param));
		}
		
		Page<ChatPoints> page = chatPointsService.getChatPoints(this.createDetachedCriteria(dataGrid, chatPoints), params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", null == page ? 0 : page.getTotalSize());
		result.put("rows", null == page ? new ArrayList<SmsInfo>() : page.getCollection());
		return result;
	}
	
	/**
	 * 跳转到添加页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/chatPointsInfo/preAdd",method=RequestMethod.GET)
	@ActionVerification(key="add")
	public String preAdd(HttpServletRequest request){
		return "points/chatPointsInfoAdd";
	}

	/**
	 * 保存
	 * @param request
	 * @param chatPoints
	 * @return
	 */
	@RequestMapping(value="/chatPointsInfo/save",method=RequestMethod.POST)
   	@ResponseBody
    public AjaxJson save(HttpServletRequest request, ChatPoints chatPoints){
        AjaxJson j = new AjaxJson();
        ApiResult result = null;
        
        result = chatPointsService.add(chatPoints, userParam.getUserNo(), IPUtil.getClientIP(request));
    	if(result.isOk()){
	    	j.setSuccess(true);
	    	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 保存积分成功：["+chatPoints.getGroupType()+"-"+chatPoints.getUserId()+"]!";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
    		logger.info("<<save()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(StringUtils.isBlank(result.getErrorMsg()) ? ResourceBundleUtil.getByMessage(result.getCode()) : result.getErrorMsg());
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 保存积分失败：["+chatPoints.getGroupType()+"-"+chatPoints.getUserId()+"]!";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<<save()|"+message+",ErrorMsg:"+result.toString());
    	}
		return j;
    }
	
	/**
	 * 删除
	 * @param request
	 * @param chatPoints
	 * @return
	 */
	@RequestMapping(value="/chatPointsInfo/delete",method=RequestMethod.POST)
	@ActionVerification(key="delete")
	@ResponseBody
	public AjaxJson delete(HttpServletRequest request){
		String pointsCfgId = request.getParameter("id");
		AjaxJson j = new AjaxJson();
		ApiResult result = chatPointsService.delete(pointsCfgId);
		if(result.isOk()){
			j.setSuccess(true);
			String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除积分成功：" + pointsCfgId + "!";
			addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
			logger.info("<<delete()|"+message);
		}else{
			j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
			String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除积分失败：" + pointsCfgId + "!";
			addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
			logger.error("<<delete()|"+message+",ErrorMsg:"+result.toString());
		}
		return j;
	}
	
	/**
	 * 积分流水明细
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/chatPointsInfo/view",method=RequestMethod.POST)
	@ActionVerification(key="view")
	@ResponseBody
	public ChatPoints view(HttpServletRequest request){
		String pointsId = request.getParameter("pointsId");
		if(StringUtils.isBlank(pointsId)){
			return null;
		}
		ChatPoints result = chatPointsService.findById(pointsId);
		return result;
	}
	
	/**
	 * 冲正
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/chatPointsInfo/reversal",method=RequestMethod.POST)
	@ActionVerification(key="edit")
	@ResponseBody
	public AjaxJson reversal(HttpServletRequest request){
		String pointsId = request.getParameter("pointsId");
		String journalId = request.getParameter("journalId");
		AjaxJson result = new AjaxJson();
		ApiResult apiResult = chatPointsService.reversal(pointsId, journalId, userParam.getUserNo(), IPUtil.getClientIP(request));
    	if(apiResult.isOk()){
    		result.setSuccess(true);
	    	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 积分冲正成功：["+pointsId+"-"+journalId+"]!";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
    		logger.info("<<save()|"+message);
    	}else{
    		result.setSuccess(false);
    		result.setMsg(StringUtils.isBlank(apiResult.getErrorMsg()) ? ResourceBundleUtil.getByMessage(apiResult.getCode()) : apiResult.getErrorMsg());
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 积分冲正失败：["+pointsId+"-"+journalId+"]!";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<<save()|"+message+",ErrorMsg:"+result.toString());
    	}
		return result;
	}
	
}
