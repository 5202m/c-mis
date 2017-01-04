package com.gwghk.mis.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gwghk.mis.common.model.AjaxJson;
import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.service.MenuService;
import com.gwghk.mis.util.ResourceUtil;

/**
 * 摘要：权限管理
 * @author Gavin.guo
 * @date   2014-10-24
 */
@Scope("prototype")
@Controller
public class AuthorityController extends BaseController{
	private static final Logger logger = LoggerFactory.getLogger(AuthorityController.class);
	
	@Autowired
	private MenuService menuService;
	/**
	 * 功能：权限管理-首页
	 */
	@RequestMapping(value = "/authorityController/index", method = RequestMethod.GET)
	public  String  index(ModelMap map){
		logger.debug(">>start into AuthorityController.index()...");
		return "system/authority/authorityList";
	}
	
	/**
	 * 提取菜单对应功能
	 * @param request
	 * @param map
	 * @return
	 */
    @RequestMapping(value="/authorityController/getFuns",method=RequestMethod.POST)
   	@ResponseBody
    public AjaxJson getFuns(HttpServletRequest request,ModelMap map){
		String menuId = request.getParameter("menuId");
		AjaxJson result = new AjaxJson();
		boolean superFlag = (boolean)request.getSession().getAttribute(WebConstant.SESSION_SUPER_FLAG);
		result.setObj(menuService.getSubFunByMenuId(menuId,superFlag?null:userParam.getRole().getRoleId()));
		return result;
	}
	/**
	 * 功能：菜单管理-加载菜单
	 */
	@RequestMapping(value = "/authorityController/loadMenuTree", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String loadMenuTree(HttpServletRequest request,String roleId){
		boolean superFlag = (boolean)request.getSession().getAttribute(WebConstant.SESSION_SUPER_FLAG);
		return menuService.getMenuRoleTreeJson(superFlag?null:userParam.getRole().getRoleId(),roleId,true,ResourceUtil.getSessionLocale(),false).getMenuJson();
	}
	
	 /**
   	* 功能：权限管理-保存更新
   	*/
    @RequestMapping(value="/authorityController/update",method=RequestMethod.POST)
   	@ResponseBody
    public AjaxJson update(HttpServletRequest request,HttpServletResponse response){
    	AjaxJson j = new AjaxJson();
    	String roleId = request.getParameter("roleId");
    	if(StringUtils.isBlank(roleId)){
    		j.setSuccess(false);
    		j.setMsg("输入参数有误，请检查！");
    		return j;
    	}
    	if(userParam.getRole()!=null && roleId.equals(userParam.getRole().getRoleId())){
    		j.setSuccess(false);
    		j.setMsg("不能编辑自己所属的角色；如需修改，请向该角色的上级角色申请修改！");
    		return j;
    	}
    	String[] menuIdArr=null;
    	String menuIds = request.getParameter("ids");
    	if(StringUtils.isNotBlank(menuIds)){
    		menuIdArr=menuIds.split(",");
    	}
    	logger.info(">>method:update update authority roleId: " + roleId + ",menuIds : "+menuIds + " start...");
    	ApiResult result = menuService.setMenuRole(menuIdArr,roleId);
    	if(result.isOk()){
	    	j.setSuccess(true);
			logger.info("<<update()|update authority success!");
        }else{
        	j.setSuccess(false);
			logger.error("<<update()|update authority fail!"+result.getErrorMsg());
        }
   		return j;
     } 
}
