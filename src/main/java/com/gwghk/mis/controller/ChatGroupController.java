package com.gwghk.mis.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gwghk.mis.model.*;
import com.gwghk.mis.util.*;
import com.sdk.orm.DataRowSet;
import com.sdk.orm.IRow;
import com.sdk.poi.POIExcelBuilder;
import net.sf.json.JSONObject;

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

import com.alibaba.fastjson.JSONArray;
import com.gwghk.mis.authority.ActionVerification;
import com.gwghk.mis.common.model.AjaxJson;
import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DataGrid;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.common.model.TreeBean;
import com.gwghk.mis.constant.DictConstant;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.service.ChatClientGroupService;
import com.gwghk.mis.service.ChatGroupService;
import com.gwghk.mis.service.MemberService;
import com.gwghk.mis.service.RoleService;
import com.gwghk.mis.service.TokenAccessService;
import com.gwghk.mis.service.UserService;

/**
 * 聊天室房间管理
 * @author Alan.wu
 * @date   2015/04/02
 */
@Scope("prototype")
@Controller
public class ChatGroupController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(ChatGroupController.class);
	
	@Autowired
	private ChatGroupService chatGroupService;

	@Autowired
	private TokenAccessService tokenAccessService;
	
	@Autowired
	private ChatClientGroupService clientGroupService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;

	@Autowired
	private MemberService memberService;

	/**
	 * 设置状态
	 * @param map
	 */
	private void setCommonShow(ModelMap map){
		DictConstant dict=DictConstant.getInstance();
		map.put("groupLevelList", ResourceUtil.getSubDictListByParentCode(dict.DICT_CHAT_GROUP_LEVEL));
		map.put("groupTypeList", ResourceUtil.getSubDictListByParentCode(dict.DICT_CHAT_GROUP_TYPE));
		List<BoDict> dictList=new ArrayList<BoDict>();
		dictList.addAll(ResourceUtil.getSubDictListByParentCode(dict.DICT_USE_STATUS));
		BoDict dictRow=new BoDict();
		dictRow.setName("授权访问");
		dictRow.setNameCN("授权访问");
		dictRow.setId("room_limit_id");
		dictRow.setCode("2");
		dictList.add(dictRow);
    	map.put("statusList",dictList);
    	
	}
	
	/**
	 * 功能：聊天室房间管理-首页
	 */
	@RequestMapping(value = "/chatGroupController/index", method = RequestMethod.GET)
	public  String  index(HttpServletRequest request,ModelMap map){
		setCommonShow(map);
		logger.debug(">>start into chatGroupController.index() and url is /chatGroupController/index.do");
		return "chat/groupList";
	}

	/**
	 * 获取datagrid列表
	 * @param request
	 * @param dataGrid  分页查询参数对象
	 * @param chatGroup   实体查询参数对象
	 * @return Map<String,Object> datagrid需要的数据
	 */
	@RequestMapping(value = "/chatGroupController/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public  Map<String,Object>  datagrid(HttpServletRequest request, DataGrid dataGrid,ChatGroup chatGroup){
		String[] chatRuleIdArr=request.getParameterValues("chatRuleId");
    	if(chatRuleIdArr!=null){
    		chatGroup.setChatRuleIds(StringUtils.join(chatRuleIdArr, ","));
    	}
    	String[] talkStyleArr=request.getParameterValues("talkStyleStr");
    	if(talkStyleArr!=null){
    		chatGroup.setTalkStyle(StringUtils.join(talkStyleArr, ","));
       	}
		 Page<ChatGroup> page = chatGroupService.getChatGroupPage(this.createDetachedCriteria(dataGrid, chatGroup));
		 List<ChatGroup> chatGroupList=page.getCollection();
		 Map<String, Object> result = new HashMap<String, Object>();
		 result.put("total",null == page ? 0  : page.getTotalSize());
	     result.put("rows", null == page ? new ArrayList<ChatGroup>() : chatGroupList);
	     return result;
	}
	
	/**
   	 * 功能：房间
   	 */
    @RequestMapping(value = "/chatGroupController/getGroupTreeList", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
   	@ResponseBody
    public String getGroupTreeList(HttpServletRequest request,ModelMap map) throws Exception {
    	String groupId=request.getParameter("groupId"),groupType=request.getParameter("groupType");
       	List<TreeBean> treeList=new ArrayList<TreeBean>();
       	TreeBean tbean=null;
       	List<ChatGroup> subList=chatGroupService.getChatGroupByTypeList(groupType,"id","name");
       	groupId=StringUtils.isBlank(groupId)?"":(",".concat(groupId).concat(","));
       	for(ChatGroup row:subList){
       		 tbean=new TreeBean();
       		 tbean.setId(row.getId());
       		 tbean.setText(row.getName());
       		 if(groupId.contains(",".concat(row.getId()).concat(","))){
    			tbean.setChecked(true);
    		 }
       		 tbean.setParentId("");
   			 treeList.add(tbean);
       	}
       	return JsonUtil.formatListToTreeJson(treeList,false);
     }
	
	/**
	 * 获取分组列表
	 * @param request
	 * @param map
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/chatGroupController/getGroupList", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
   	@ResponseBody
    public String getGroupList(HttpServletRequest request,ModelMap map) throws Exception {
       	return JSONArray.toJSONString(chatGroupService.getChatGroupList("id","name"));
     }
	
	/**
	 * 功能：聊天室房间管理-新增
	 */
    @RequestMapping(value="/chatGroupController/add", method = RequestMethod.GET)
    @ActionVerification(key="add")
    public String add(ModelMap map) throws Exception {
    	setCommonShow(map);
    	map.addAttribute("chatRuleIds","");
    	map.addAttribute("chatGroup",new ChatGroup());
    	List<BoUser> analystList = userService.getUserListByRole("analyst");
    	//分析师列表
    	map.addAttribute("analystList", analystList);
    	map.addAttribute("serviceList",userService.getUserListByRole("cs"));
    	return "chat/groupSubmit";
    }
    
	/**
	 * 功能：聊天室房间管理-修改
	 */
    @RequestMapping(value="/chatGroupController/{chatGroupId}/edit", method = RequestMethod.GET)
    @ActionVerification(key="edit")
    public String edit(@PathVariable String chatGroupId , ModelMap map) throws Exception {
    	setCommonShow(map);
    	ChatGroup chatGroup=chatGroupService.getChatGroupById(chatGroupId);
    	if(chatGroup!=null && chatGroup.getChatRules()!=null){
    		ArrayList<String> list=new ArrayList<String>();
    		for(ChatGroupRule row:chatGroup.getChatRules()){
				list.add(row.getId());
    		}
    		chatGroup.setChatRuleIds(StringUtils.join(list, ","));
    	}
    	//分析师列表
    	List<BoUser> loc_allAnalysts = userService.getUserListByRole("analyst");
    	String[] loc_authUsers = chatGroup.getAuthUsers();
    	List<BoUser> loc_users = new ArrayList<BoUser>();
    	int lenJ = loc_authUsers == null ? 0 : loc_authUsers.length;
    	BoUser loc_analyst = null;
    	for(int i = 0, lenI = loc_allAnalysts == null ? 0 : loc_allAnalysts.size(); i < lenI; i++){
    		loc_analyst = loc_allAnalysts.get(i);
    		for (int j = 0; j < lenJ; j++) {
				if(loc_authUsers[j].equals(loc_analyst.getUserNo())){
					loc_users.add(loc_analyst);
					break;
				}
			}
    	}
    	map.addAttribute("analystList", loc_users);
    	map.addAttribute("chatGroup",chatGroup);
		map.addAttribute("serviceList",userService.getUserListByRole("cs"));
		return "chat/groupSubmit";
    }

	/**
	 * 功能：聊天室房间管理-跳转用户授权界面
	 */
	@RequestMapping(value = "/chatGroupController/{chatGroupId}/preAuthUser", method = RequestMethod.GET)
	@ActionVerification(key = "assignUser")
	public String preAuthUser(@PathVariable String chatGroupId, ModelMap map) throws Exception {
		ChatGroup chatGroup = chatGroupService.getChatGroupById(chatGroupId);

		List<BoUser> loc_users = userService.getUserList(null);
		List<BoUser> loc_unAuthUsers = new ArrayList<BoUser>();
		List<BoUser> loc_authUsers = new ArrayList<BoUser>();
		if (loc_users != null) {
			int lenI = chatGroup == null || chatGroup.getAuthUsers() == null ? 0 : chatGroup.getAuthUsers().length;
			if (lenI == 0) {
				loc_unAuthUsers = loc_users;
			}
			else {
				String loc_userNo = null;
				LOOP: for (BoUser loc_user : loc_users) {
					loc_userNo = loc_user.getUserNo();
					for (int i = 0; i < lenI; i++) {
						if (loc_userNo.equals(chatGroup.getAuthUsers()[i])) {
							loc_authUsers.add(loc_user);
							continue LOOP;
						}
					}
					loc_unAuthUsers.add(loc_user);
				}
			}
		}
		map.addAttribute("unAuthUserList", loc_unAuthUsers);
		map.addAttribute("authUserList", loc_authUsers);
    	map.addAttribute("chatGroup",chatGroup);
		return "chat/groupUserAuth";
	}
    
	/**
	 * 功能：聊天室房间管理-保存更新
	 * 
	 * @param request
	 * @param response
	 * @param chatGroup
	 * @return
	 */
	@RequestMapping(value = "/chatGroupController/authUser", method = RequestMethod.POST)
	@ResponseBody
	@ActionVerification(key = "assignUser")
	public AjaxJson authUser(HttpServletRequest request, HttpServletResponse response, ChatGroup chatGroup) {
		setBaseInfo(chatGroup, request, true);
		AjaxJson j = new AjaxJson();
		//注意，如果为null则不会修改，对清空授权用户功能的影响
		if(chatGroup.getAuthUsers() == null){
			chatGroup.setAuthUsers(new String[0]);
		}
		ApiResult result = chatGroupService.saveChatGroup(chatGroup, true, false);
		if (result.isOk()) {
			j.setSuccess(true);
			String message = "用户：" + chatGroup.getUpdateUser() + " " + DateUtil.getDateSecondFormat(new Date()) + " 修改聊天室授权用户成功";
			logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_UPDATE, BrowserUtils.checkBrowse(request), IPUtil.getClientIP(request));
			logger.info("<--method:authUser()|" + message);
		}
		else {
			j.setSuccess(false);
			j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
			String message = "用户：" + chatGroup.getUpdateUser() + " " + DateUtil.getDateSecondFormat(new Date()) + " 修改聊天室授权用户失败";
			logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_UPDATE, BrowserUtils.checkBrowse(request), IPUtil.getClientIP(request));
			logger.error("<--method:authUser()|" + message + ",ErrorMsg:" + result.toString());
		}
		return j;
	}

	/**
	 * 功能：聊天室房间管理-保存更新
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/chatGroupController/clearClient", method = RequestMethod.POST)
	@ResponseBody
	@ActionVerification(key = "assignUser")
	public AjaxJson clearClient(HttpServletRequest request, HttpServletResponse response) {
		String groupId = request.getParameter("groupId");
		boolean isAll = "true".equals(request.getParameter("isAll"));
		AjaxJson j = new AjaxJson();
		ApiResult result = chatGroupService.clearClient(groupId, isAll);
		if (result.isOk()) {
			j.setSuccess(true);
			String message = "用户：" + userParam.getUserNo() + " " + DateUtil.getDateSecondFormat(new Date()) + " 清空聊天室授权用户成功";
			logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_UPDATE, BrowserUtils.checkBrowse(request), IPUtil.getClientIP(request));
			logger.info("<--method:authUser()|" + message);
		}
		else {
			j.setSuccess(false);
			j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
			String message = "用户：" + userParam.getUserNo() + " " + DateUtil.getDateSecondFormat(new Date()) + " 清空聊天室授权用户失败";
			logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_UPDATE, BrowserUtils.checkBrowse(request), IPUtil.getClientIP(request));
			logger.error("<--method:authUser()|" + message + ",ErrorMsg:" + result.toString());
		}
		return j;
	}

	/**
   	 * 功能：聊天室房间管理-保存新增
   	 */
    @RequestMapping(value="/chatGroupController/create",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="add")
    public AjaxJson create(HttpServletRequest request,HttpServletResponse response,ChatGroup chatGroup){
    	setBaseInfo(chatGroup,request,false);
    	setCommonChatGroupParam(request,chatGroup);
    	AjaxJson j = new AjaxJson();
    	String[] chatRuleIdArr=request.getParameterValues("chatRuleId");
    	if(chatRuleIdArr!=null){
    		chatGroup.setChatRuleIds(StringUtils.join(chatRuleIdArr, ","));
    	}
    	String[] talkStyleArr=request.getParameterValues("talkStyleStr");
    	if(talkStyleArr!=null){
    		chatGroup.setTalkStyle(StringUtils.join(talkStyleArr, ","));
       	}else{
       		j.setSuccess(false);
       		j.setMsg("聊天方式不能为空！");
       		return j;
       	}
    	String[] chatWhisperRoleStr=request.getParameterValues("chatWhisperRoleStr");
    	if(chatWhisperRoleStr!=null && StringUtils.isNotBlank(chatGroup.getTalkStyle())&& chatGroup.getTalkStyle().contains("1")){
    		chatGroup.setWhisperRoles(StringUtils.join(chatWhisperRoleStr, ","));
       	}else{
       		chatGroup.setWhisperRoles("");
       	}
    	ApiResult result =chatGroupService.saveChatGroup(chatGroup, false, false);
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = "用户：" + chatGroup.getCreateUser() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功新增聊天室房间："+chatGroup.getName();
    		logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.info("<<method:create()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = "用户：" + chatGroup.getCreateUser() + " "+DateUtil.getDateSecondFormat(new Date()) + " 新增聊天室房间："+chatGroup.getName()+" 失败";
    		logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.error("<<method:create()|"+message+",ErrorMsg:"+result.toString());
    	}
		return j;
    }
   
   /**
   	* 功能：聊天室房间管理-保存更新
   	*/
    @RequestMapping(value="/chatGroupController/update",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="edit")
    public AjaxJson update(HttpServletRequest request,HttpServletResponse response,ChatGroup chatGroup){
    	AjaxJson j = new AjaxJson();
    	setBaseInfo(chatGroup,request,true);
    	setCommonChatGroupParam(request,chatGroup);
    	String[] chatRuleIdArr=request.getParameterValues("chatRuleId");
    	if(chatRuleIdArr!=null){
    		chatGroup.setChatRuleIds(StringUtils.join(chatRuleIdArr, ","));
    	}
    	String[] talkStyleArr=request.getParameterValues("talkStyleStr");
    	if(talkStyleArr!=null){
    		chatGroup.setTalkStyle(StringUtils.join(talkStyleArr, ","));
       	}else{
       		j.setSuccess(false);
       		j.setMsg("聊天方式不能为空！");
       		return j;
       	}
    	String[] chatWhisperRoleStr=request.getParameterValues("chatWhisperRoleStr");
    	if(chatWhisperRoleStr!=null && StringUtils.isNotBlank(chatGroup.getTalkStyle())&& chatGroup.getTalkStyle().contains("1")){
    		chatGroup.setWhisperRoles(StringUtils.join(chatWhisperRoleStr, ","));
       	}else{
       		chatGroup.setWhisperRoles("");
       	}
    	ApiResult result =chatGroupService.saveChatGroup(chatGroup, true, true);
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = "用户：" + chatGroup.getUpdateUser() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功修改聊天室房间："+chatGroup.getId();
    		logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_UPDATE
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.info("<--method:update()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = "用户：" + chatGroup.getUpdateUser() + " "+DateUtil.getDateSecondFormat(new Date()) + " 修改聊天室房间："+chatGroup.getId()+" 失败";
    		logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.error("<--method:update()|"+message+",ErrorMsg:"+result.toString());
    	}
   		return j;
     }
    
   /**
  	* 功能：聊天室房间管理-删除
  	*/
    @RequestMapping(value="/chatGroupController/del",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson del(HttpServletRequest request,HttpServletResponse response){
    	BoUser boUser = ResourceUtil.getSessionUser();
    	String delIds = request.getParameter("ids");
    	if(StringUtils.isBlank(delIds)){
    		delIds = request.getParameter("id");
    	}
    	AjaxJson j = new AjaxJson();
    	ApiResult result =chatGroupService.deleteChatGroup(delIds.split(","));
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = "用户：" + boUser.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除聊天室房间成功";
    		logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.info("<<method:batchDel()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = "用户：" + boUser.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除聊天室房间失败";
    		logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.error("<<method:batchDel()|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
    }

    /**
     * 
     * @function:  查看培训班报名客户
     * @param chatGroupId
     * @param map
     * @return
     * @throws Exception String   
     * @exception 
     * @author:Jade.zhu   
     * @since  1.0.0
     */
    @RequestMapping(value = "/chatGroupController/{chatGroupId}/getTrainClient", method = RequestMethod.GET)
	@ActionVerification(key = "bookingClient")
    public String bookingUserList(@PathVariable String chatGroupId , ModelMap map) throws Exception {
    	ChatGroup chatGroup = chatGroupService.getChatGroupById(chatGroupId);
    	List<TraninClient>  TraninClientList = chatGroup.getTraninClient();
		List<TraninClient> authTraninClientList = new ArrayList<TraninClient>();
		List<TraninClient> unAuthTraninClientList = new ArrayList<TraninClient>();
		if(null != TraninClientList){
			for (TraninClient traninClient : TraninClientList) {
				if(traninClient.getIsAuth() == 0){
					unAuthTraninClientList.add(traninClient);
				}else{
					authTraninClientList.add(traninClient);
				}
			}
		}
    	
    	map.put("chatGroup", chatGroup); 	
    	map.put("authTraninClientList", authTraninClientList); 
    	map.put("unAuthTraninClientList", unAuthTraninClientList); 
    	return "chat/groupBookingUser";
    }
    
    /**
     * 修改审批报名客户
     */
    @RequestMapping(value = "/chatGroupController/authTrainClient", method = RequestMethod.POST)
	@ResponseBody
	@ActionVerification(key = "authTrainClient")
	public AjaxJson authTrainRegis(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		ApiResult result;	
		String chatGroupId = request.getParameter("chatGroupId");
		String authTraninClient = request.getParameter("authTranin");
		String unAuthTraninClient = request.getParameter("unAuthTranin");
	   	ChatGroup chatGroup=chatGroupService.getChatGroupById(chatGroupId);
	   	net.sf.json.JSONArray authTraninClientArray = null;
	   	net.sf.json.JSONArray unAuthTraninClientArray = null;
	   	if(null != authTraninClient && !"null".equals(authTraninClient)){
			authTraninClientArray = JSONHelper.toJSONArray(authTraninClient);
		}
	   	if(null != unAuthTraninClient && !"null".equals(unAuthTraninClient)){
	   		unAuthTraninClientArray = JSONHelper.toJSONArray(unAuthTraninClient);
		}
	   	
	   	if(null != authTraninClientArray){
	   		for (int i = 0; i < authTraninClientArray.size(); i++) {
				JSONObject jsonObject = authTraninClientArray.getJSONObject(i);	   
				for (TraninClient traninClient : chatGroup.getTraninClient()) {
					if(traninClient.getClientId().equals(jsonObject.getString("clientId"))){
						traninClient.setIsAuth(1);
					}
				}
		   }
	   	}
	   	
	   	if(null != unAuthTraninClientArray){
	   		for (int i = 0; i < unAuthTraninClientArray.size(); i++) {
				JSONObject jsonObject = unAuthTraninClientArray.getJSONObject(i);	   
				for (TraninClient traninClient : chatGroup.getTraninClient()) {
					if(traninClient.getClientId().equals(jsonObject.getString("clientId"))){
						traninClient.setIsAuth(0);
					}
				}
		   }
	   	}
	   	
		result = chatGroupService.saveChatGroupTranin(chatGroup);
		if (result.isOk()) {
			j.setSuccess(true);
			String message = "用户房间：" + chatGroup.getName() + " " + DateUtil.getDateSecondFormat(new Date()) + " 修改审批报名用户成功";
			logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_UPDATE, BrowserUtils.checkBrowse(request), IPUtil.getClientIP(request));
			logger.info("<--method:authUser()|" + message);
		}
		else {
			j.setSuccess(false);
			j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
			String message = "用户：" + chatGroup.getName()  + " " + DateUtil.getDateSecondFormat(new Date()) + " 修改审批报名用户失败";
			logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT, BrowserUtils.checkBrowse(request), IPUtil.getClientIP(request));
			logger.error("<--method:authUser()|" + message + ",ErrorMsg:" + result.toString());
		}
		return j;
	}
    
    /**
     * 设置直播通用参数
     * @param request
     * @param chatGroup
     */
    private void setCommonChatGroupParam(HttpServletRequest request,ChatGroup chatGroup){
    	String[] clientGroupArr=request.getParameterValues("clientGroupStr");
       	if(clientGroupArr!=null){
       		chatGroup.setClientGroup(StringUtils.join(clientGroupArr, ","));
       	}
    }
   
    /**
     * 跳转到客户导入页面
     * @param chatGroupId
     * @param map
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/chatGroupController/{chatGroupId}/preImportClient", method = RequestMethod.GET)
	@ActionVerification(key = "bookingClient")
    public String preImportClient(@PathVariable String chatGroupId , ModelMap map) throws Exception {
    	map.put("groupId", chatGroupId); 
    	return "chat/groupUserImport";
    }

    /**
     * 跳转到客户导入页面
     * @param map
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/chatGroupController/importClient", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson importClient(HttpServletRequest request, ModelMap map) throws Exception {
    	BoUser boUser = ResourceUtil.getSessionUser();
    	AjaxJson result = new AjaxJson();
		String groupId = request.getParameter("groupId");
		String mobiles = request.getParameter("mobiles");
		ApiResult apiResult = chatGroupService.importClient(groupId, mobiles);
		if (apiResult.isOk()) {
			result.setSuccess(true);
			result.setObj(apiResult.getReturnObj());
			String message = "用户：" + boUser.getUserNo() + " " + DateUtil.getDateSecondFormat(new Date()) + " 导入用户成功";
			logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_UPDATE, BrowserUtils.checkBrowse(request), IPUtil.getClientIP(request));
			logger.info("<--method:importClient()|" + message);
		}
		else {
			result.setSuccess(false);
			result.setMsg(apiResult.getErrorMsg());
			String message = "用户：" + boUser.getUserNo()  + " " + DateUtil.getDateSecondFormat(new Date()) + " 导入用户失败";
			logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT, BrowserUtils.checkBrowse(request), IPUtil.getClientIP(request));
			logger.error("<--method:importClient()|" + message + ",ErrorMsg:" + result.toString());
		}
    	return result;
    }
	@RequestMapping(value = "/chatGroupController/{chatGroupId}/exportUnAuthClient", method = RequestMethod.GET)
	public void exportUnAuthClient(HttpServletRequest request, HttpServletResponse response,@PathVariable  String chatGroupId){
		try{
			POIExcelBuilder builder = new POIExcelBuilder(new File(request.getServletContext().getRealPath(WebConstant.ROOM_TRAIN_CLINET_DATA)));
			//
			ChatGroup chatGroup = chatGroupService.getChatGroupById(chatGroupId);
			List<TraninClient>  TraninClientList = chatGroup.getTraninClient();
			Map<String, TraninClient> traninClientMap = new HashMap<String, TraninClient>();
			//获取未授权用户id列表
			if(null != TraninClientList){
				for (TraninClient traninClient : TraninClientList) {
					traninClientMap.put(traninClient.getClientId(), traninClient);
				}
			}
			String[] ids = traninClientMap.keySet().toArray(new String[0]);

			//根据用户id，对应组别查询
			List<Member> userList = memberService.getMemberByUserIdGroupType(ids,chatGroup.getGroupType());
			DataRowSet dataSet = new DataRowSet();
			for(int i = 0;i<userList.size();i++){
				try{
					Member member = userList.get(i);
					//因已对组别过滤 所以直接取第一条
					ChatUserGroup chatUserGroup = member.getLoginPlatform().getChatUserGroup().get(0);
					IRow row = dataSet.append();
					row.set("nickName",chatUserGroup.getNickname());
					row.set("mobilePhone",member.getMobilePhone());
					row.set("accountNo",chatUserGroup.getAccountNo());
					TraninClient traninClient = traninClientMap.get(chatUserGroup.getUserId());
					if(traninClient != null){
						row.set("isAuth", traninClient.getIsAuth() == 1 ? "通过" : "未通过");
						row.set("dateTime", traninClient.getDateTime() == null ? "" : DateUtil.formatDate(traninClient.getDateTime(), "yyyy-MM-dd HH:mm:ss"));
					}
				}catch (Exception e){
					logger.error("<--method:exportUnAuthClient()|" + e + ",ErrorMsg:" + e.toString());
				}
			}
			builder.put("dataSet",dataSet);
			builder.parse();
			ExcelUtil.wrapExcelExportResponse("报名用户", request, response);
			builder.write(response.getOutputStream());
		}catch (Exception e){
			logService.addLog(e.toString(), WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT, BrowserUtils.checkBrowse(request), IPUtil.getClientIP(request));
			logger.error("<--method:exportUnAuthClient()|" + e + ",ErrorMsg:" + e.toString());
		}

	}
}
