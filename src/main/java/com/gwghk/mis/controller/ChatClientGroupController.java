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
import com.gwghk.mis.model.ChatClientGroup;
import com.gwghk.mis.service.ChatClientGroupService;
import com.gwghk.mis.service.ChatGroupService;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.JsonUtil;
import com.gwghk.mis.util.ResourceBundleUtil;
import com.gwghk.mis.util.ResourceUtil;

/**
 * 聊天室客户组别管理
 * @author Alan.wu
 * @date   2015/04/02
 */
@Scope("prototype")
@Controller
public class ChatClientGroupController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(ChatClientGroupController.class);
	
	@Autowired
	private ChatClientGroupService chatClientGroupService;
	
	@Autowired
	private ChatGroupService chatGroupService;
	/**
	 * 设置组别
	 * @param map
	 */
	private void setCommonShow(ModelMap map){
    	DictConstant dict=DictConstant.getInstance();
    	map.put("chatGroupList", chatGroupService.getChatGroupList(this.getSystemFlag(),"id","name", "groupType"));
		map.put("groupTypeList", ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_TYPE));
	}
	
	   /**
   	 * 功能：客户组别
   	 */
    @RequestMapping(value = "/chatClientGroupController/getClientGroupList", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
   	@ResponseBody
    public String getClientGroupList(HttpServletRequest request,ModelMap map) throws Exception {
    	String groupType=request.getParameter("groupType");
    	String clientGroup=request.getParameter("clientGroup");
    	String filter=request.getParameter("filter");
       	List<TreeBean> treeList=new ArrayList<TreeBean>();
       	TreeBean tbean=null;
       	List<ChatClientGroup> subList=chatClientGroupService.getClientGroupList(groupType,getSystemFlag());
       	clientGroup=StringUtils.isBlank(clientGroup)?"":(",".concat(clientGroup).concat(","));
       	for(ChatClientGroup row:subList){
       		 if(StringUtils.isNotBlank(filter) && (","+filter+",").contains(","+row.getClientGroupId()+",")){
       			 continue;
       		 }
       		 tbean=new TreeBean();
       		 tbean.setId(row.getClientGroupId());
       		 tbean.setText(row.getName());
       		 if(clientGroup.contains(",".concat(row.getClientGroupId()).concat(","))){
    			tbean.setChecked(true);
    		 }
       		 tbean.setParentId("");
   			 treeList.add(tbean);
       	}
       	return JsonUtil.formatListToTreeJson(treeList,false);
     }
	
	/**
	 * 功能：聊天室组别管理-首页
	 */
	@RequestMapping(value = "/chatClientGroupController/index", method = RequestMethod.GET)
	public  String  index(HttpServletRequest request,ModelMap map){
		setCommonShow(map);
		logger.debug(">>start into chatClientGroupController.index() and url is /chatClientGroupController/index.do");
		return "chat/rooms/clientGroupList";
	}

	/**
	 * 获取datagrid列表
	 * @param request
	 * @param dataGrid  分页查询参数对象
	 * @param chatGroup   实体查询参数对象
	 * @return Map<String,Object> datagrid需要的数据
	 */
	@RequestMapping(value = "/chatClientGroupController/dataGrid", method = RequestMethod.GET)
	@ResponseBody
	public  Map<String,Object>  datagrid(HttpServletRequest request, DataGrid dataGrid,ChatClientGroup clientGroup){
		 Page<ChatClientGroup> page = chatClientGroupService.getClientGroupPage(this.createDetachedCriteria(dataGrid, clientGroup));
		 List<ChatClientGroup> chatGroupList=page.getCollection();
		 Map<String, Object> result = new HashMap<String, Object>();
		 result.put("total",null == page ? 0  : page.getTotalSize());
	     result.put("rows", null == page ? new ArrayList<ChatClientGroup>() : chatGroupList);
	     return result;
	}
	
	
	/**
	 * 功能：聊天室组别管理-新增
	 */
    @RequestMapping(value="/chatClientGroupController/add", method = RequestMethod.GET)
    @ActionVerification(key="add")
    public String add(ModelMap map) throws Exception {
    	setCommonShow(map);
    	map.addAttribute("clientGroup",new ChatClientGroup());
    	return "chat/rooms/clientGroupSubmit";
    }
    
	/**
	 * 功能：聊天室组别管理-修改
	 */
    @RequestMapping(value="/chatClientGroupController/{clientGroupId}/edit", method = RequestMethod.GET)
    @ActionVerification(key="edit")
    public String edit(@PathVariable String clientGroupId , ModelMap map) throws Exception {
    	setCommonShow(map);
    	map.addAttribute("clientGroup",chatClientGroupService.getById(clientGroupId));
		return "chat/rooms/clientGroupSubmit";
    }
    
	  /**
   	 * 功能：聊天室组别管理-保存新增
   	 */
    @RequestMapping(value="/chatClientGroupController/create",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="add")
    public AjaxJson create(HttpServletRequest request,HttpServletResponse response,ChatClientGroup clientGroup){
    	setBaseInfo(clientGroup,request,false);
    	AjaxJson j = new AjaxJson();
    	ApiResult result =chatClientGroupService.saveClientGroup(clientGroup, false);
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = "用户：" + clientGroup.getCreateUser() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功新增客户组别："+clientGroup.getName();
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT
    						 );
    		logger.info("<<method:create()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = "用户：" + clientGroup.getCreateUser() + " "+DateUtil.getDateSecondFormat(new Date()) + " 新增客户组别："+clientGroup.getName()+" 失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT
    						 );
    		logger.error("<<method:create()|"+message+",ErrorMsg:"+result.toString());
    	}
		return j;
    }
       
   /**
   	* 功能：聊天室组别管理-保存更新
   	*/
    @RequestMapping(value="/chatClientGroupController/update",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="edit")
    public AjaxJson update(HttpServletRequest request,HttpServletResponse response,ChatClientGroup clientGroup){
    	setBaseInfo(clientGroup,request,true);
    	AjaxJson j = new AjaxJson();
    	ApiResult result =chatClientGroupService.saveClientGroup(clientGroup, true);
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = "用户：" + clientGroup.getUpdateUser() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功修改客户组别："+clientGroup.getId();
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_UPDATE
    						 );
    		logger.info("<--method:update()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = "用户：" + clientGroup.getUpdateUser() + " "+DateUtil.getDateSecondFormat(new Date()) + " 修改客户组别："+clientGroup.getId()+" 失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT
    						 );
    		logger.error("<--method:update()|"+message+",ErrorMsg:"+result.toString());
    	}
   		return j;
     }
    
   /**
  	* 功能：聊天室组别管理-删除
  	*/
    @RequestMapping(value="/chatClientGroupController/del",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson del(HttpServletRequest request,HttpServletResponse response){
    	String delIds = request.getParameter("ids");
    	if(StringUtils.isBlank(delIds)){
    		delIds = request.getParameter("id");
    	}
    	AjaxJson j = new AjaxJson();
    	ApiResult result =chatClientGroupService.deleteClientGroup(delIds.split(","));
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = "用户：" + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除客户组别成功";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL
    						 );
    		logger.info("<<method:batchDel()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = "用户：" + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除客户组别失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL
    						 );
    		logger.error("<<method:batchDel()|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
    }
}
