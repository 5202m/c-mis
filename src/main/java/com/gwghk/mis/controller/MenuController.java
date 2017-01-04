package com.gwghk.mis.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import com.gwghk.mis.common.model.AjaxJson;
import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.model.BoMenu;
import com.gwghk.mis.model.MenuResult;
import com.gwghk.mis.service.MenuService;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.IPUtil;
import com.gwghk.mis.util.ResourceBundleUtil;
import com.gwghk.mis.util.ResourceUtil;

/**
 * 摘要：菜单管理
 * @author Gavin.guo
 * @date   2014-10-23
 */
@Scope("prototype")
@Controller
public class MenuController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(MenuController.class);
	
	@Autowired
	private MenuService menuService;
	
	/**
	 * 功能：菜单管理-首页
	 */
	@RequestMapping(value = "/menuController/index", method = RequestMethod.GET)
	public  String  index(ModelMap map){
		logger.debug(">>start into MenuController.index()...");
		return "system/menu/menuList";
	}
	
	/**
	 * 功能：菜单管理-加载菜单
	 */
	@RequestMapping(value = "/menuController/loadMenuTree", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String loadMenuTree(){
		return menuService.getMenuTreeJson(ResourceUtil.getSessionLocale());
	}
	
	/**
	 * 功能：登录成功后加载菜单
	 */
	@RequestMapping(value = "/menuController/getMenuRoleTree", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String getMenuRoleTree(HttpServletRequest request){
		HttpSession session=request.getSession();
		Object obj = session.getAttribute(WebConstant.SESSION_MENU_KEY);
		Object superFlag = session.getAttribute(WebConstant.SESSION_SUPER_FLAG);
		MenuResult menuResult=null;
		if(obj != null){
			menuResult = (MenuResult)obj;
		}else{
			boolean isSuper=(superFlag!=null && (boolean)superFlag);
			String roleId=isSuper?null:userParam.getRole().getRoleId();
			menuResult = menuService.getMenuRoleTreeJson(roleId,roleId, false,ResourceUtil.getSessionLocale(),isSuper);
			session.setAttribute(WebConstant.SESSION_MENU_KEY , menuResult);
		}
		return menuResult.getMenuJson();
	}
	
    /**
	 * 功能：菜单管理-新增
	 */
    @RequestMapping(value="/menuController/add", method = RequestMethod.GET)
    public String add(HttpServletRequest request,ModelMap map) throws Exception {
    	String menuId = request.getParameter("menuId");
    	BoMenu menu = menuService.getMenuById(menuId);
    	map.addAttribute("menu",menu);
    	map.put("locale", ResourceUtil.getSessionLocale());
    	return "system/menu/menuAdd";
    }
    
    /**
   	 * 功能：菜单管理-保存新增
   	 */
    @RequestMapping(value="/menuController/create",method=RequestMethod.POST)
   	@ResponseBody
    public AjaxJson create(HttpServletRequest request,BoMenu menu){
    	menu.setCreateUser(userParam.getUserNo());
    	menu.setCreateIp(IPUtil.getClientIP(request));
    	AjaxJson j = new AjaxJson();
    	ApiResult result = menuService.saveMenu(menu, false);
    	if(result != null && result.isOk()){
	    	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功新增菜单："+menu.getNameCN();
	    	addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
    		logger.info("<<method:create()|"+message);
    		j.setObj(result.getReturnObj()[0]);
    		j.setSuccess(true);
    	}else{
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 新增菜单："+menu.getNameCN()+" 失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<<method:create()|"+message+",ErrorMsg:"+result.toString());
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    	}
		return j;
    }
    
    /**
   	* 功能：菜单管理-保存更新
   	*/
    @RequestMapping(value="/menuController/update",method=RequestMethod.POST)
   	@ResponseBody
    public AjaxJson update(HttpServletRequest request,BoMenu menu){
    	AjaxJson j = new AjaxJson();
    	menu.setUpdateUser(userParam.getUserNo());
    	menu.setUpdateIp(IPUtil.getClientIP(request));
    	ApiResult result = menuService.saveMenu(menu, true);
    	if(result != null && result.isOk()){
	    	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功修改菜单："+menu.getNameCN();
	    	addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_UPDATE);
    		logger.info("<<method:update()|"+message);
    		j.setObj(result.getReturnObj()[0]);
    		j.setSuccess(true);
    	}else{
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 修改菜单："+menu.getNameCN()+" 失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_UPDATE);
    		logger.error("<<method:update()|"+message+",ErrorMsg:"+result.toString());
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    	}
   		return j;
     }
    
    /**
  	 * 功能：菜单管理-获取菜单数据
  	 */
    @RequestMapping(value="/menuController/{menuId}/view", method = RequestMethod.GET)
  	@ResponseBody
    public AjaxJson view(@PathVariable String menuId , ModelMap map) throws Exception {
    	AjaxJson j = new AjaxJson();
    	BoMenu menu = menuService.getMenuById(menuId);
    	Map<String,List<BoMenu>> parentMenuMap = new HashMap<String,List<BoMenu>>();
    	j.setObj(menu);
      	j.setAttributes(parentMenuMap);
		j.setSuccess(true);
      	return j;
    }
    
    /**
     * 功能：菜单管理-移动
     */
    @RequestMapping(value="/menuController/move", method = RequestMethod.GET)
    public String move(HttpServletRequest request,ModelMap map){
    	List<BoMenu> menuList = menuService.getMenuList(false);
    	List<BoMenu> result = new ArrayList<BoMenu>();
 		ResourceUtil.formatMenutoTree(menuList, 0, "", result);
    	map.put("menuList", result);
    	map.put("menuId",request.getParameter("menuId"));
    	map.put("locale", ResourceUtil.getSessionLocale());
    	return "/system/menu/menuMove";
    }
    
    /**
     * 功能：菜单管理-保存移动
     */
    @RequestMapping(value="/menuController/saveMove",method=RequestMethod.POST)
    @ResponseBody
    public AjaxJson saveMove(HttpServletRequest request,BoMenu menu){
    	AjaxJson j = new AjaxJson();
    	menu.setUpdateUser(userParam.getUserNo());
    	menu.setUpdateIp(IPUtil.getClientIP(request));
    	ApiResult result = menuService.moveMenu(menu.getParentMenuId(),menu.getMenuId());
    	if(result != null && result.isOk()){
	    	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功移动菜单";
	    	addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_UPDATE);
    		logger.info("<<method:saveMove()|"+message);
    		j.setSuccess(true);
    	}else{
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 移动菜单失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_UPDATE);
    		logger.error("<<method:saveMove()|"+message+",ErrorMsg:"+result.toString());
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    	}
   		return j;
    }
    
    /**
  	* 功能：菜单管理-单条菜单删除
  	*/
    @RequestMapping(value="/menuController/del",method=RequestMethod.POST)
    @ResponseBody
    public AjaxJson del(HttpServletRequest request,HttpServletResponse response){
    	AjaxJson j = new AjaxJson();
    	String menuId = request.getParameter("menuId");
    	String[] menuIds = menuId.contains(",") ? menuId.split(",") : new String[]{menuId};
    	ApiResult result = menuService.deleteMenu(menuIds);
    	if(result != null && result.isOk()){
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除菜单成功";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:del()|"+message);
    		j.setSuccess(true);
    	}else{
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除菜单失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:del()|"+message+",ErrorMsg:"+result.toString());
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    	}
  		return j;
    }
}
