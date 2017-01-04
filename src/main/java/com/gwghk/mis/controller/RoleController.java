package com.gwghk.mis.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.constant.DictConstant;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.model.BoDict;
import com.gwghk.mis.model.BoRole;
import com.gwghk.mis.model.BoUser;
import com.gwghk.mis.service.RoleService;
import com.gwghk.mis.service.UserService;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.IPUtil;
import com.gwghk.mis.util.ResourceBundleUtil;
import com.gwghk.mis.util.ResourceUtil;

/**
 * 摘要：角色管理
 * @author Gavin.guo
 * @date   2014-10-21
 */
@Scope("prototype")
@Controller
public class RoleController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 功能：角色管理-首页
	 */
	@RequestMapping(value = "/roleController/index", method = RequestMethod.GET)
	public  String  index(){
		logger.debug("-->start into roleController.index() and url is /roleController/index.do");
		return "system/role/roleList";
	}

	/**
	 * 功能：获取dataGrid列表
	 * @param request
	 * @param dataGrid  分页查询参数对象
	 * @param mngRole   角色实体查询参数对象
	 * @return Map<String,Object> dataGrid需要的数据
	 */
	@RequestMapping(value = "/roleController/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public  Map<String,Object>  datagrid(HttpServletRequest request, DataGrid dataGrid,BoRole br){
		Page<BoRole> page = roleService.getRolePage(this.createDetachedCriteria(dataGrid, br));
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total",null == page ? 0  : page.getTotalSize());
	    result.put("rows", null == page ? new ArrayList<BoRole>() : page.getCollection());
	    return result;
	}
	
	/**
	 * 功能：角色管理-新增
	 */
    @RequestMapping(value="/roleController/add", method = RequestMethod.GET)  
    @ActionVerification(key="add")
    public String add(ModelMap map) throws Exception {
    	DictConstant dict=DictConstant.getInstance();
		map.put("roleTypeList", ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_ROLE_TYPE));
    	return "system/role/roleAdd";
    }
    
    /**
	 * 功能：角色管理-查看
	 */
    @RequestMapping(value="/roleController/{id}/view", method = RequestMethod.GET)
    public String view(@PathVariable String id , ModelMap map) throws Exception {
    	map.addAttribute("mngRole",roleService.getByRoleId(id));
		return "system/role/roleView";
    }
	
	/**
	 * 功能：角色管理-修改
	 */
    @RequestMapping(value="/roleController/{id}/edit", method = RequestMethod.GET)
    @ActionVerification(key="edit")
    public String edit(@PathVariable String id , ModelMap map) throws Exception {
    	map.addAttribute("mngRole",roleService.getByRoleId(id));
		return "system/role/roleEdit";
    }
    
    /**
   	 * 功能：角色管理-保存新增
   	 */
    @RequestMapping(value="/roleController/create",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="add")
    public AjaxJson create(HttpServletRequest request,BoRole role){
    	role.setCreateUser(userParam.getUserNo());
    	role.setCreateIp(IPUtil.getClientIP(request));
    	AjaxJson j = new AjaxJson();
    	String roleType=request.getParameter("roleType");
    	if(StringUtils.isBlank(roleType)){
    		j.setSuccess(false);
    		j.setMsg("请选择角色类别！");
    		return j;
    	}
    	List<BoDict> dict=ResourceUtil.getSubDictListByParentCode(getSystemFlag(),DictConstant.getInstance().DICT_ROLE_TYPE);
    	if(dict==null||dict.stream().noneMatch(e->e.getCode().equals(roleType))){
    		j.setSuccess(false);
    		j.setMsg("角色类别有误，请联系系统管理员！");
    		return j;
    	}
    	role.setRoleNo(roleType+"_"+role.getRoleNo());
    	ApiResult result =  roleService.saveRole(role, false);
    	if(result != null && result.isOk()){
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功新增角色："+role.getRoleName();
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
    		logger.info("<<method:create()|"+message);
    		j.setSuccess(true);
    	}else{
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 新增角色："+role.getRoleName()+" 失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<<method:create()|"+message+",ErrorMsg:"+result.toString());
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    	}
		return j;
    }
       
   /**
   	* 功能：角色管理-保存更新
   	*/
    @RequestMapping(value="/roleController/update",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="edit")
    public AjaxJson update(HttpServletRequest request,BoRole role){
    	AjaxJson j = new AjaxJson();
    	ApiResult result = roleService.saveRole(role, true);
    	if(result.isOk()){
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功修改角色："+role.getRoleName();
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_UPDATE);
    		logger.info("<<method:update()|"+message);
    		j.setSuccess(true);
    	}else{
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 修改角色："+role.getRoleName()+" 失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_UPDATE);
    		logger.error("<<method:update()|"+message+",ErrorMsg:"+result.toString());
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    	}
   		return j;
     }
    
   /**
  	* 功能：角色管理-批量删除
  	*/
    @RequestMapping(value="/roleController/batchDel",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson batchDel(HttpServletRequest request){
    	AjaxJson j = new AjaxJson();
    	String delIds = request.getParameter("ids");
    	String[] ids = delIds.contains(",")? delIds.split(","):new String[]{delIds};
    	ApiResult result = roleService.deleteRole(ids);
    	if(result.isOk()){
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 批量删除角色成功";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:batchDel()|"+message);
    		j.setSuccess(true);
    	}else{
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 批量删除角色失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:batchDel()|"+message+",ErrorMsg:"+result.toString());
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    	}
  		return j;
    }
    
    /**
  	* 功能：角色管理-单条记录删除
  	*/
    @RequestMapping(value="/roleController/oneDel",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson oneDel(HttpServletRequest request){
    	String delId = request.getParameter("id");
    	AjaxJson j = new AjaxJson();
    	ApiResult result = roleService.deleteRole(new String[]{delId});
    	if(result.isOk()){
         	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除角色成功";
         	addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:oneDel()|"+message);
    		j.setSuccess(true);
    	}else{
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除角色失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:oneDel()|"+message+",ErrorMsg:"+result.toString());
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    	}
  		return j;
    }
    
    /**
	 * 功能：角色管理-分配人员
	 */
    @RequestMapping(value="/roleController/assignUser", method = RequestMethod.GET) 
    @ActionVerification(key="assignUser")
    public String assignUser(HttpServletRequest request,ModelMap map) throws Exception {
    	String roleId=request.getParameter("roleId");
    	Map<String,Object> dataMap = userService.getUserRoleRelation(roleId);
    	map.addAttribute("selectedUserList",dataMap.get("relation"));
    	map.addAttribute("unselectedUserList",dataMap.get("unRelation"));
    	map.addAttribute("roleId",roleId);
    	return "system/role/selectUserList";
    }
    
    /**
   	* 功能：角色管理-查询未分配的人员列表
   	*/
    @RequestMapping(value="/roleController/searchUnAssignUser",method=RequestMethod.POST)
   	@ResponseBody
    public AjaxJson searchUnAssignUser(HttpServletRequest request){
    	String userNoOrName = request.getParameter("userNoOrName");
    	Map<String,Object> userMap = new HashMap<String,Object>();
    	BoUser user = new BoUser();
    	user.setUserNoOrName(userNoOrName);
    	DetachedCriteria<BoUser> dCriteria = new DetachedCriteria<BoUser>();
    	dCriteria.setSearchModel(user);
    	userMap.put("userList", userService.getUserList(getSystemFlag(),dCriteria));
    	AjaxJson j = new AjaxJson();
    	j.setSuccess(true);
    	j.setAttributes(userMap);
   		return j;
    }
    
    /**
   	* 功能：角色管理-保存更新分配人员
   	*/
    @RequestMapping(value="/roleController/updateAssignUser",method=RequestMethod.POST)
   	@ResponseBody
   	@ActionVerification(key="assignUser")
    public AjaxJson updateAssignUser(HttpServletRequest request){
    	String roleId = request.getParameter("roleId");
    	String userIds = request.getParameter("userIds");
    	AjaxJson j = new AjaxJson();
    	String[] userIdArr=null;
    	if(StringUtils.isNotBlank(userIds)){
    		userIdArr=userIds.split(",");
    	}
    	ApiResult result =userService.setUserRole(userIdArr,roleId);
    	if(result.isOk()){
    		logger.info("<<method:updateAssignUser()|update assign user success!");
    		j.setSuccess(true);
    	}else{
    		logger.error("<<method:updateAssignUser()|update assign user fail,ErrorMsg:"+result.toString());
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    	}
   		return j;
     }
}
