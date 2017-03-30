package com.gwghk.mis.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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

import com.alibaba.fastjson.JSONArray;
import com.gwghk.mis.authority.ActionVerification;
import com.gwghk.mis.common.model.AjaxJson;
import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DataGrid;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.model.BoRole;
import com.gwghk.mis.model.BoUser;
import com.gwghk.mis.service.ChatShowTradeService;
import com.gwghk.mis.service.RoleService;
import com.gwghk.mis.service.UserService;
import com.gwghk.mis.util.BrowserUtils;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.IPUtil;
import com.gwghk.mis.util.PropertiesUtil;
import com.gwghk.mis.util.ResourceBundleUtil;
import com.gwghk.mis.util.ResourceUtil;

/**
 * 摘要：用户管理
 * @author Gavin.guo
 * @date   2014-10-14
 */
@Scope("prototype")
@Controller
public class UserController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private ChatShowTradeService chatShowTradeService;
	
	/**
	 * 功能：用户管理-首页
	 */
	@RequestMapping(value = "/userController/index", method = RequestMethod.GET)
	public  String  index(HttpServletRequest request,ModelMap map, String opType){
		logger.debug(">>start into UserController.index() and url is /userController/index.do");
		map.addAttribute("roleList",this.getAcceptRoles(opType));
		map.addAttribute("opType", opType);
		return "system/user/userList";
	}

	/**
	 * 获取datagrid列表
	 * @param request
	 * @param dataGrid  分页查询参数对象
	 * @param mngUser   实体查询参数对象
	 * @return Map<String,Object> datagrid需要的数据
	 */
	@RequestMapping(value = "/userController/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public  Map<String,Object>  datagrid(HttpServletRequest request, DataGrid dataGrid,BoUser mngUser, String opType){
		 String roleId=request.getParameter("roleId");
		 String[] roleIds = null; 
		 if(StringUtils.isNotEmpty(roleId)){
			 roleIds = new String[]{roleId};
		 }else if("analyst".equals(opType)){
			 List<BoRole> analystRoles = this.getAcceptRoles(opType);
			 if(analystRoles != null){
				 int len = analystRoles.size();
				 roleIds = new String[len];
				 for (int i = 0; i < len; i++) {
					 roleIds[i] = analystRoles.get(i).getRoleId();
				 }
			 }
		 }
		 
		 Page<BoUser> page = userService.getUserPage(this.createDetachedCriteria(dataGrid, mngUser), roleIds);
		 Map<String, Object> result = new HashMap<String, Object>();
		 result.put("total",null == page ? 0  : page.getTotalSize());
	     result.put("rows", null == page ? new ArrayList<BoUser>() : page.getCollection());
	     return result;
	}
	
	/**
	 * 功能：用户管理-新增
	 */
    @RequestMapping(value="/userController/add", method = RequestMethod.GET)
    @ActionVerification(key="add")
    public String add(ModelMap map, String opType) throws Exception {
    	map.addAttribute("roleList",this.getAcceptRoles(opType));
    	map.addAttribute("filePath",PropertiesUtil.getInstance().getProperty("pmfilesDomain"));
    	Pattern pattern = Pattern.compile("^13800138.*$", Pattern.CASE_INSENSITIVE);
    	map.addAttribute("telephone", this.getNextPhone(pattern));
    	return "system/user/userAdd";
    }
    
    /**
	 * 功能：用户管理-查看
	 */
    @RequestMapping(value="/userController/{userId}/view", method = RequestMethod.GET)
    @ActionVerification(key="view")
    public String view(@PathVariable String userId , ModelMap map) throws Exception {
    	BoUser user=userService.getUserById(userId);
    	map.addAttribute("filePath",PropertiesUtil.getInstance().getProperty("pmfilesDomain"));
    	map.addAttribute("mngUser",user);
		return "system/user/userView";
    }
	
	/**
	 * 功能：用户管理-修改
	 */
    @ActionVerification(key="edit")
    @RequestMapping(value="/userController/{userId}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable String userId , ModelMap map, String opType) throws Exception {
    	BoUser user=userService.getUserById(userId);
    	map.addAttribute("mngUser",user);
		map.addAttribute("roleList", this.getAcceptRoles(opType));
		map.addAttribute("filePath",PropertiesUtil.getInstance().getProperty("pmfilesDomain"));
		return "system/user/userEdit";
    }
    
    /**
   	 * 功能：用户管理-保存新增
   	 */
    @RequestMapping(value="/userController/create",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="add")
    public AjaxJson create(HttpServletRequest request,BoUser user){
    	BoUser userParam = ResourceUtil.getSessionUser();
    	user.setCreateUser(userParam.getUserNo());
    	user.setCreateIp(IPUtil.getClientIP(request));
    	AjaxJson j = new AjaxJson();
    	ApiResult result =userService.saveUser(user, false);
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功新增用户："+user.getUserNo();
    		logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.info("<<method:create()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 新增用户："+user.getUserNo()+" 失败";
    		logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.error("<<method:create()|"+message+",ErrorMsg:"+result.toString());
    	}
		return j;
    }
       
   /**
   	* 功能：用户管理-保存更新
   	*/
    @RequestMapping(value="/userController/update",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="edit")
    public AjaxJson update(HttpServletRequest request,BoUser user){
    	BoUser userParam = ResourceUtil.getSessionUser();
    	user.setUpdateUser(userParam.getUserNo());
    	user.setUpdateIp(IPUtil.getClientIP(request));
    	AjaxJson j = new AjaxJson();
    	ApiResult result =userService.saveUser(user, true);
    	if(result.isOk()){
    		chatShowTradeService.asyncTradeByBoUser(user);
    		j.setSuccess(true);
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功修改用户："+user.getUserNo();
    		logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_UPDATE
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.info("<--method:update()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 修改用户："+user.getUserNo()+" 失败";
    		logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.error("<--method:update()|"+message+",ErrorMsg:"+result.toString());
    	}
   		return j;
     }
    
    /**
	 * 功能：用户管理-重设密码
	 */
    @ActionVerification(key="resetPwd")
    @RequestMapping(value="/userController/resetPwd", method = RequestMethod.GET)
    public String resetPwd(HttpServletRequest request,ModelMap map) throws Exception{
    	String userId = request.getParameter("id");
    	map.addAttribute("userId", userId);
    	return  "system/user/resetPwd";
    }
    
    /**
  	* 功能：用户管理-保存重设密码
  	*/
    @RequestMapping(value="/userController/saveResetPwd",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="resetPwd")
    public AjaxJson saveResetPwd(HttpServletRequest request){
    	String id = request.getParameter("id"),newPwd = request.getParameter("newPwd");
    	AjaxJson j = new AjaxJson();
    	ApiResult result = userService.saveResetPwd(id,newPwd);
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 重置密码成功!";
    		logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_UPDATE
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.info("<<method:saveResetPwd()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 重置密码失败!";
    		logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_UPDATE
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.error("<<method:saveResetPwd()|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
    }
    
   /**
  	* 功能：用户管理-批量删除
  	*/
    @RequestMapping(value="/userController/batchDel",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson batchDel(HttpServletRequest request){
    	BoUser userParam = ResourceUtil.getSessionUser();
    	String delIds = request.getParameter("ids");
    	AjaxJson j = new AjaxJson();
    	ApiResult result =userService.deleteUser(delIds.contains(",")?delIds.split(","):new String[]{delIds});
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 批量删除用户成功";
    		logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.info("<<method:batchDel()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 批量删除用户失败";
    		logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.error("<<method:batchDel()|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
    }
    
    /**
  	* 功能：用户管理-单条记录删除
  	*/
    @RequestMapping(value="/userController/oneDel",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson oneDel(HttpServletRequest request,HttpServletResponse response){
    	BoUser userParam = ResourceUtil.getSessionUser();
    	String delId = request.getParameter("id");
    	AjaxJson j = new AjaxJson();
    	ApiResult result = userService.deleteUser(new String[]{delId});
    	if(result.isOk()){
          	j.setSuccess(true);
          	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除用户成功";
          	logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL
          					 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.info("<<method:oneDel()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除用户失败";
    		logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.error("<<method:oneDel()|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
    }
    
    /**
     * 获取角色列表
     * @param opType
     * @return
     */
    private List<BoRole> getAcceptRoles(String opType){
    	if("analyst".equals(opType)){
    		return roleService.getAnalystRoleList();
    	}else{
    		return roleService.getRoleList();
    	}
    }
    
    @RequestMapping(value = "/userController/getAnalystList", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
   	@ResponseBody
    public String getAnalystList(HttpServletRequest request,ModelMap map) throws Exception {
    	List<BoUser> allAnalysts = userService.getUserListByRole("analyst");
    	String hasOther=request.getParameter("hasOther");
			String apiUrl = PropertiesUtil.getInstance().getProperty("pmApiUrl");
    	if(StringUtils.isNotBlank(hasOther) && !apiUrl.contains("hx9999.com")){
    		String path=PropertiesUtil.getInstance().getProperty("pmfilesDomain")+"/upload/pic/header/chat/201508";
        	String[] nameArr={"梁育诗","罗恩•威廉","黃湛铭","赵相宾","周游","刘敏","陈杭霞","金道研究院"};
        	BoUser user=null;
        	for(int i=0;i<nameArr.length;i++){
        		user=new BoUser();
        		user.setAvatar(path+(i==1?"/headimg51_2.jpg":"/headimg61_"+(i+1)+".jpg"));
        		user.setUserNo("gw_analyst_" + i);
        		user.setUserName(nameArr[i]);
        	    user.setPosition("金道研究院");
        		allAnalysts.add(user);
        	}
    	}
       	return JSONArray.toJSONString(allAnalysts);
     }
    
    /**
     * 
     * @function:  获取下一个手机号
     * @param pattern 正则
     * @return String   返回下一个手机号
     * @exception 
     * @author:jade.zhu   
     * @since  1.0.0
     */
    private String getNextPhone(Pattern pattern){
    	return userService.getNextUserPhone(pattern);
    }
}
