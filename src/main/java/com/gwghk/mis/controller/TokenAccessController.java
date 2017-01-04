package com.gwghk.mis.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.model.TokenAccess;
import com.gwghk.mis.service.TokenAccessService;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.IPUtil;
import com.gwghk.mis.util.ResourceBundleUtil;

/**
 * 摘要：token设置controller
 * @author Gavin.guo
 * @date   2015-05-11
 */
@Scope("prototype")
@Controller
public class TokenAccessController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(TokenAccessController.class);
	
	@Autowired
	private TokenAccessService tokenAccessService;
	
	/**
	 * 功能：token设置-首页
	 */
	@RequestMapping(value = "/tokenAccessController/index", method = RequestMethod.GET)
	public  String  index(HttpServletRequest request,ModelMap map){
		logger.debug(">>start into tokenAccessController.index() and url is /tokenAccessController/index.do");
		return "tokenaccess/tokenaccessList";
	}

	/**
	 * 获取dataGrid列表
	 * @param request
	 * @param dataGrid  	分页查询参数对象
	 * @param tokenAccess   实体查询参数对象
	 * @return Map<String,Object> dataGrid需要的数据
	 */
	@RequestMapping(value = "/tokenAccessController/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public  Map<String,Object>  appDatagrid(HttpServletRequest request, DataGrid dataGrid,TokenAccess tokenAccess){
		Page<TokenAccess> page = tokenAccessService.getTokenAccessPage(this.createDetachedCriteria(dataGrid,tokenAccess));
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total",null == page ? 0  : page.getTotalSize());
	    result.put("rows", null == page ? new ArrayList<TokenAccess>() : page.getCollection());
	    return result;
	}
	
	/**
	 * 功能：token设置-新增
	 */
    @RequestMapping(value="/tokenAccessController/add", method = RequestMethod.GET)
    @ActionVerification(key="add")
    public String add(ModelMap map) throws Exception {
    	return "tokenaccess/tokenaccessAdd";
    }
	
	/**
	 * 功能：token设置-修改
	 */
    @RequestMapping(value="/tokenAccessController/edit", method = RequestMethod.GET)
    @ActionVerification(key="edit")
    public String edit(HttpServletRequest request , ModelMap map,TokenAccess tokenAccess) throws Exception {
    	map.addAttribute("tokenAccess",tokenAccessService.getById(tokenAccess.getTokenAccessId()));
		return "tokenaccess/tokenaccessEdit";
    }
    
    /**
   	 * 功能：token设置-保存新增
   	 */
    @RequestMapping(value="/tokenAccessController/create",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="add")
    public AjaxJson create(HttpServletRequest request,TokenAccess tokenAccess){
    	tokenAccess.setCreateUser(userParam.getUserNo());
    	tokenAccess.setCreateIp(IPUtil.getClientIP(request));
    	AjaxJson j = new AjaxJson();
    	ApiResult result = tokenAccessService.saveTokenAccess(tokenAccess,false);
    	if(result.isOk()){
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) 
    					   + " 成功新增token："+tokenAccess.getAppId();
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
    		logger.info("<<method:create()|"+message);
    		j.setSuccess(true);
    	}else{
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 新增token："
    					  + tokenAccess.getAppId()+" 失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<<method:create()|"+message+",ErrorMsg:"+result.toString());
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    	}
		return j;
    }
       
   /**
   	* 功能：token设置-保存更新
   	*/
    @RequestMapping(value="/tokenAccessController/update",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="edit")
    public AjaxJson update(HttpServletRequest request,TokenAccess tokenAccess){
    	tokenAccess.setUpdateUser(userParam.getUserNo());
    	tokenAccess.setUpdateIp(IPUtil.getClientIP(request));
    	AjaxJson j = new AjaxJson();
    	ApiResult result  = tokenAccessService.saveTokenAccess(tokenAccess, true);
    	if(result.isOk()){
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功修改token："
    					   +tokenAccess.getAppId();
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_UPDATE);
    		logger.info("<--method:update()|"+message);
    		j.setSuccess(true);
    	}else{
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 修改token："
    					   +tokenAccess.getAppId()+" 失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<--method:update()|"+message+",ErrorMsg:"+result.toString());
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    	}
   		return j;
     }
    
   /**
  	* 功能：token设置-批量删除
  	*/
    @RequestMapping(value="/tokenAccessController/batchDel",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson batchDel(HttpServletRequest request){
    	String delIds = request.getParameter("ids");
    	AjaxJson j = new AjaxJson();
    	ApiResult result = tokenAccessService.deleteTokenAccess(delIds);
    	if(result.isOk()){
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 批量删除token成功";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:batchDel()|"+message);
    		j.setSuccess(true);
    	}else{
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 批量删除token失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:batchDel()|"+message+",ErrorMsg:"+result.toString());
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    	}
  		return j;
    }
    
    /**
  	* 功能：token设置-单条记录删除
  	*/
    @RequestMapping(value="/tokenAccessController/oneDel",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson oneDel(HttpServletRequest request){
    	String delId = request.getParameter("id");
    	AjaxJson j = new AjaxJson();
    	ApiResult result = tokenAccessService.deleteTokenAccess(delId);
    	if(result.isOk()){
          	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除token成功";
          	addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:oneDel()|"+message);
    		j.setSuccess(true);
    	}else{
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除token失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:oneDel()|"+message+",ErrorMsg:"+result.toString());
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    	}
  		return j;
    }
}
