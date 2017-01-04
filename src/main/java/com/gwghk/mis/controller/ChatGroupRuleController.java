package com.gwghk.mis.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.gwghk.mis.common.model.TreeBean;
import com.gwghk.mis.constant.DictConstant;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.model.BoDict;
import com.gwghk.mis.model.BoUser;
import com.gwghk.mis.model.ChatGroupRule;
import com.gwghk.mis.service.ChatGroupRuleService;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.JsonUtil;
import com.gwghk.mis.util.ResourceBundleUtil;
import com.gwghk.mis.util.ResourceUtil;

/**
 * 聊天室规则管理
 * @author Alan.wu
 * @date   2015/04/02
 */
@Scope("prototype")
@Controller
public class ChatGroupRuleController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(ChatGroupRuleController.class);
	@Autowired
	private ChatGroupRuleService chatGroupRuleService;

	/**
	 * 设置规则类别（从数据字典中加入）
	 * @param map
	 */
	private void setDictGroupRule(ModelMap map){
		DictConstant dict=DictConstant.getInstance();
		List<BoDict> dictList=ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_RULE);
    	map.put("dictConstant", dict);
    	map.put("dictList", dictList);
	}
	
	/**
	 * 功能：聊天室规则管理-首页
	 */
	@RequestMapping(value = "/chatGroupRuleController/index", method = RequestMethod.GET)
	public  String  index(HttpServletRequest request,ModelMap map){
		setDictGroupRule(map);
		logger.debug(">>start into chatGroupRuleController.index() and url is /chatGroupRuleController/index.do");
		return "chat/rooms/groupRuleList";
	}

	/**
	 * 获取datagrid列表
	 * @param request
	 * @param dataGrid  分页查询参数对象
	 * @param chatGroupRule   实体查询参数对象
	 * @return Map<String,Object> datagrid需要的数据
	 */
	@RequestMapping(value = "/chatGroupRuleController/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public  Map<String,Object>  datagrid(HttpServletRequest request, DataGrid dataGrid,ChatGroupRule chatGroupRule){
		 Page<ChatGroupRule> page = chatGroupRuleService.getChatGroupRulePage(this.createDetachedCriteria(dataGrid, chatGroupRule));
		 Map<String, Object> result = new HashMap<String, Object>();
		 result.put("total",null == page ? 0  : page.getTotalSize());
	     result.put("rows", null == page ? new ArrayList<ChatGroupRule>() : page.getCollection());
	     return result;
	}
	

	/**
	 * 获取规则列表，构造多选组合框
	 * @param request
	 * @param map
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/chatGroupRuleController/getGroupRuleCombox", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
   	@ResponseBody
    public String getGroupRuleCombox(HttpServletRequest request,ModelMap map) throws Exception {
		DictConstant dict=DictConstant.getInstance();
		List<BoDict> dictList=ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_RULE);
		Map<String, BoDict> chatGroupRuleType = new HashMap<String, BoDict>();
		for (BoDict boDict : dictList) {
			chatGroupRuleType.put(boDict.getCode(), boDict);
		}
       	List<TreeBean> treeList=new ArrayList<TreeBean>();
       	TreeBean tbean=null;
       	List<ChatGroupRule> list=chatGroupRuleService.getChatGroupRuleList(getSystemFlag(),"id","name","type");
       	for(ChatGroupRule row:list){
       		 tbean=new TreeBean();
       		 tbean.setId(row.getId());
       		 tbean.setText(row.getName() + "【" + chatGroupRuleType.get(row.getType()).getNameCN() + "】");
       		 tbean.setParentId("");
   			 treeList.add(tbean);
       	}
       	return JsonUtil.formatListToTreeJson(treeList,false);
     }
	
	
	/**
	 * 功能：聊天室规则管理-新增
	 */
    @RequestMapping(value="/chatGroupRuleController/add", method = RequestMethod.GET)
    @ActionVerification(key="add")
    public String add(ModelMap map) throws Exception {
    	setDictGroupRule(map);
    	map.addAttribute("chatGroupRule",new ChatGroupRule());
    	return "chat/rooms/groupRuleSubmit";
    }
    
	/**
	 * 功能：聊天室规则管理-修改
	 */
    @RequestMapping(value="/chatGroupRuleController/{chatGroupRuleId}/edit", method = RequestMethod.GET)
    @ActionVerification(key="edit")
    public String edit(@PathVariable String chatGroupRuleId , ModelMap map) throws Exception {
    	ChatGroupRule chatGroupRule=chatGroupRuleService.getChatGroupRuleById(chatGroupRuleId);
    	setDictGroupRule(map);
    	map.addAttribute("chatGroupRule",chatGroupRule);
		return "chat/rooms/groupRuleSubmit";
    }
    
    /**
   	 * 功能：聊天室规则管理-保存新增
   	 */
    @RequestMapping(value="/chatGroupRuleController/create",method=RequestMethod.POST)
   	@ResponseBody
    public AjaxJson create(HttpServletRequest request,HttpServletResponse response,ChatGroupRule chatGroupRule){
    	setBaseInfo(chatGroupRule,request,false);
    	setCommonChatGroupRuleParam(request, chatGroupRule);
    	AjaxJson j = new AjaxJson();
    	ApiResult result =chatGroupRuleService.saveChatGroupRule(chatGroupRule, false);
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = "用户：" + chatGroupRule.getCreateUser() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功新增聊天室规则："+chatGroupRule.getName();
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
    		logger.info("<<method:create()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = "用户：" + chatGroupRule.getCreateUser() + " "+DateUtil.getDateSecondFormat(new Date()) + " 新增聊天室规则："+chatGroupRule.getName()+" 失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<<method:create()|"+message+",ErrorMsg:"+result.toString());
    	}
		return j;
    }
       
   /**
   	* 功能：聊天室规则管理-保存更新
   	*/
    @RequestMapping(value="/chatGroupRuleController/update",method=RequestMethod.POST)
   	@ResponseBody
    public AjaxJson update(HttpServletRequest request,HttpServletResponse response,ChatGroupRule chatGroupRule){
    	setBaseInfo(chatGroupRule,request,true);
    	setCommonChatGroupRuleParam(request, chatGroupRule);
    	AjaxJson j = new AjaxJson();
    	ApiResult result =chatGroupRuleService.saveChatGroupRule(chatGroupRule, true);
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = "用户：" + chatGroupRule.getUpdateUser() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功修改聊天室规则："+chatGroupRule.getId();
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_UPDATE);
    		logger.info("<--method:update()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = "用户：" + chatGroupRule.getUpdateUser() + " "+DateUtil.getDateSecondFormat(new Date()) + " 修改聊天室规则："+chatGroupRule.getId()+" 失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<--method:update()|"+message+",ErrorMsg:"+result.toString());
    	}
   		return j;
     }
    
   /**
  	* 功能：聊天室规则管理-删除
  	*/
    @RequestMapping(value="/chatGroupRuleController/del",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson del(HttpServletRequest request,HttpServletResponse response){
    	BoUser boUser = ResourceUtil.getSessionUser();
    	String delIds = request.getParameter("ids");
    	if(StringUtils.isBlank(delIds)){
    		delIds = request.getParameter("id");
    	}
    	AjaxJson j = new AjaxJson();
    	ApiResult result =chatGroupRuleService.deleteChatGroupRule(delIds.split(","));
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = "用户：" + boUser.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除聊天室规则成功";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:batchDel()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = "用户：" + boUser.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除聊天室规则失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:batchDel()|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
    }
    
    /**
     * 设置规则通用参数
     * @param request
     * @param chatGroup
     */
    private void setCommonChatGroupRuleParam(HttpServletRequest request, ChatGroupRule chatGroupRule){
    	String[] clientGroupArr=request.getParameterValues("clientGroupStr");
       	if(clientGroupArr!=null){
       		chatGroupRule.setClientGroup(StringUtils.join(clientGroupArr, ","));
       	}else{
       		chatGroupRule.setClientGroup("");
       	}
    }
}
