package com.gwghk.mis.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.gwghk.mis.model.ChatGroup;
import com.gwghk.mis.model.ChatPushInfo;
import com.gwghk.mis.service.ChatClientGroupService;
import com.gwghk.mis.service.ChatGroupService;
import com.gwghk.mis.service.ChatPushInfoService;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.ResourceBundleUtil;
import com.gwghk.mis.util.ResourceUtil;

/**
 * 聊天室组别管理
 * @author Alan.wu
 * @date   2015/04/02
 */
@Scope("prototype")
@Controller
public class ChatPushInfoController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(ChatPushInfoController.class);
	
	@Autowired
	private ChatGroupService chatGroupService;

	@Autowired
	private ChatClientGroupService clientGroupService;
	
	@Autowired
	private ChatPushInfoService chatPushInfoService;
	

	/**
	 * 设置状态
	 * @param map
	 */
	private void setCommonShow(ModelMap map){
		DictConstant dict=DictConstant.getInstance();
		map.put("groupTypeList", ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_TYPE));
    	map.put("statusList", ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_USE_STATUS));
	}
	
	/**
	 * 功能：聊天室组别管理-首页
	 */
	@RequestMapping(value = "/chatPushInfoController/index", method = RequestMethod.GET)
	public  String  index(HttpServletRequest request,ModelMap map){
		setCommonShow(map);
		logger.debug(">>start into chatPushInfoController.index() and url is /chatPushInfoController/index.do");
		return "chat/push/pushInfoList";
	}

	/**
	 * 获取datagrid列表
	 * @param request
	 * @param dataGrid  分页查询参数对象
	 * @param chatGroup   实体查询参数对象
	 * @return Map<String,Object> datagrid需要的数据
	 */
	@RequestMapping(value = "/chatPushInfoController/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public  Map<String,Object>  datagrid(HttpServletRequest request, DataGrid dataGrid,ChatPushInfo pushInfo){
		 setSystemFlag(pushInfo);
		 Page<ChatPushInfo> page = chatPushInfoService.getPage(this.createDetachedCriteria(dataGrid, pushInfo));
		 Map<String, Object> result = new HashMap<String, Object>();
		 result.put("total",null == page ? 0  : page.getTotalSize());
	     result.put("rows", null == page ? new ArrayList<ChatGroup>() : page.getCollection());
	     return result;
	}

	/**
	 * 功能：聊天室组别管理-新增
	 */
    @RequestMapping(value="/chatPushInfoController/add", method = RequestMethod.GET)
    @ActionVerification(key="add")
    public String add(ModelMap map) throws Exception {
    	setCommonShow(map);
    	map.addAttribute("chatPushInfo",new ChatPushInfo());
    	return "chat/push/pushInfoSubmit";
    }
    
	/**
	 * 功能：聊天室组别管理-修改
	 */
    @RequestMapping(value="/chatPushInfoController/{chatPushInfoId}/edit", method = RequestMethod.GET)
    @ActionVerification(key="edit")
    public String edit(@PathVariable String chatPushInfoId , ModelMap map) throws Exception {
    	setCommonShow(map);
    	ChatPushInfo pushInfo=chatPushInfoService.getById(chatPushInfoId);
    	map.addAttribute("roomIdStr",StringUtils.join(pushInfo.getRoomIds(), ","));
    	map.addAttribute("clientGroupStr",StringUtils.join(pushInfo.getClientGroup(), ","));
    	map.addAttribute("chatPushInfo",pushInfo);
		return "chat/push/pushInfoSubmit";
    }
    
	  /**
   	 * 功能：聊天室组别管理-保存新增
   	 */
    @RequestMapping(value="/chatPushInfoController/create",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="add")
    public AjaxJson create(HttpServletRequest request,HttpServletResponse response,ChatPushInfo chatPushInfo){
    	setBaseInfo(chatPushInfo,request,false);
    	AjaxJson j = new AjaxJson();
    	chatPushInfo.setId(null);
    	ApiResult result =chatPushInfoService.save(chatPushInfo, false);
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = "用户：" + chatPushInfo.getCreateUser() + " "+DateUtil.getDateSecondFormat(new Date()) + " 新增推送信息成功";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
    		logger.info("<<method:create()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = "用户：" + chatPushInfo.getCreateUser() + " "+DateUtil.getDateSecondFormat(new Date()) + " 新增推送信息失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<<method:create()|"+message+",ErrorMsg:"+result.toString());
    	}
		return j;
    }
   
   /**
   	* 功能：聊天室组别管理-保存更新
   	*/
    @RequestMapping(value="/chatPushInfoController/update",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="edit")
    public AjaxJson update(HttpServletRequest request,HttpServletResponse response,ChatPushInfo chatPushInfo){
    	AjaxJson j = new AjaxJson();
    	setBaseInfo(chatPushInfo,request,true);
    	ApiResult result =chatPushInfoService.save(chatPushInfo, true);
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = "用户：" + chatPushInfo.getUpdateUser() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功修改推送信息："+chatPushInfo.getId();
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_UPDATE);
    		logger.info("<--method:update()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = "用户：" + chatPushInfo.getUpdateUser() + " "+DateUtil.getDateSecondFormat(new Date()) + " 修改推送信息失败："+chatPushInfo.getId();
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<--method:update()|"+message+",ErrorMsg:"+result.toString());
    	}
   		return j;
     }
    
   /**
  	* 功能：聊天室组别管理-删除
  	*/
    @RequestMapping(value="/chatPushInfoController/del",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson del(HttpServletRequest request,HttpServletResponse response){
    	String delIds = request.getParameter("ids");
    	if(StringUtils.isBlank(delIds)){
    		delIds = request.getParameter("id");
    	}
    	AjaxJson j = new AjaxJson();
    	ApiResult result =chatPushInfoService.delete(delIds.split(","));
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = "用户：" + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除推送信息成功";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:batchDel()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = "用户：" + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除推送信息失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:batchDel()|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
    }
    
}
