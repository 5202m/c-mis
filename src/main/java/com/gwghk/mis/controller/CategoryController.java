package com.gwghk.mis.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gwghk.mis.authority.ActionVerification;
import com.gwghk.mis.common.model.AjaxJson;
import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.TreeBean;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.model.Category;
import com.gwghk.mis.service.CategoryService;
import com.gwghk.mis.util.BrowserUtils;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.IPUtil;
import com.gwghk.mis.util.JsonUtil;
import com.gwghk.mis.util.ResourceBundleUtil;

/**
 * 栏目管理
 * @author Alan.wu
 * @date 2015/3/17
 */
@Scope("prototype")
@Controller
public class CategoryController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
	
	@Autowired
	private CategoryService categoryService;
	/**
	 * 功能：栏目管理-首页
	 */
	@RequestMapping(value = "/categoryController/index", method = RequestMethod.GET)
	public  String  index(){
		logger.debug(">>start into categoryController.index()...");
		return "category/categoryList";
	}
	
	/**
	 * 功能：栏目管理-加载一级栏目列表
	 */
	@RequestMapping(value = "/categoryController/treeGrid", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public  String  treeGrid(HttpServletRequest request){
		String code = request.getParameter("code"),name = request.getParameter("name"),status= request.getParameter("status");
		List<Category> typeList = categoryService.getCategoryList(name,code,status);
		return JSONArray.fromObject(formatTreeList(typeList)).toString();
	}
	
	/**
	 * 格式成树形列表
	 * @param nodeListTmp
	 * @return
	 */
	private List<Category> formatTreeList(List<Category> nodeListTmp){
    	List<Category> nodeList = new ArrayList<Category>(); 
    	String parentId="";
    	for(Category outNode : nodeListTmp){
    	    boolean flag = false;  
    	    for(Category inNode : nodeListTmp){ 
    	    	parentId=outNode.getParentId();
    	        if(StringUtils.isNotBlank(parentId) && inNode.getId().equals(parentId)){  
    	            flag = true; 
    	            if(inNode.getChildren()== null){  
    	                inNode.setChildren(new ArrayList<Category>());  
    	            }
    	            inNode.getChildren().add(outNode); 
    	            break;  
    	        }  
    	    }
    	    if(!flag){
    	        nodeList.add(outNode);   
    	    }  
    	}  
    	return nodeList;
	}
	
	
	/**
	 * 功能：栏目管理-新增
	 */
    @RequestMapping(value="/categoryController/view", method = RequestMethod.GET)  
    @ActionVerification(key="edit")
    public String view(HttpServletRequest request,ModelMap map) throws Exception {
    	String id = request.getParameter("id");
    	String parentId=request.getParameter("parentId");
    	if(StringUtils.isNotBlank(id)){
    		map.addAttribute("category",categoryService.getCategoryById(id));
    	}
    	map.addAttribute("parentId",parentId);
    	return "category/categoryView";
    }

    /**
	 * 功能：栏目管理-新增
	 */
    @RequestMapping(value = "/categoryController/getCategoryTree", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
	@ResponseBody
    public String getCategoryTree(HttpServletRequest request,@Param("type")Integer type) throws Exception {
    	List<Category> typeList = categoryService.getCategoryList(type);
    	List<TreeBean> treeList=new ArrayList<TreeBean>();
    	TreeBean tbean=null;
    	for(Category row:typeList){
    		 tbean=new TreeBean();
    		 tbean.setId(row.getId());
    		 tbean.setText(row.getName());
    		 tbean.setParentId(row.getParentId());
			 treeList.add(tbean);
    	}
    	return JsonUtil.formatListToTreeJson(treeList,false);
    }

    
    /**
   	 * 功能：栏目管理-保存新增
   	 */
    @RequestMapping(value="/categoryController/create",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="add")
    public AjaxJson create(HttpServletRequest request,Category category){
    	category.setCreateUser(userParam.getUserNo());
    	category.setCreateIp(IPUtil.getClientIP(request));
        AjaxJson j = new AjaxJson();
        String parentId = request.getParameter("parentId");
        ApiResult result = categoryService.saveCategory(parentId,category, false);
    	if(result.isOk()){
	    	j.setSuccess(true);
	    	j.setObj(result.getReturnObj()[0]);
	    	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功新增栏目："+category.getName();
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
    		logger.info("<<create()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(result.getErrorMsg());
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 新增栏目："+category.getName()+" 失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<<create()|"+message+",ErrorMsg:"+result.toString());
    	}
		return j;
    }
    
   /**
   	* 功能：栏目管理-保存更新
   	*/
    @RequestMapping(value="/categoryController/update",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="edit")
    public AjaxJson update(HttpServletRequest request,Category category){
    	category.setUpdateUser(userParam.getUserNo());
    	category.setUpdateIp(IPUtil.getClientIP(request));
    	AjaxJson j = new AjaxJson();
    	String parentId = request.getParameter("parentId");
        ApiResult result =categoryService.saveCategory(parentId,category, true);
    	if(result.isOk()){
	    	j.setObj(result.getReturnObj()[0]);
	    	j.setSuccess(true);
	    	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功修改栏目："+category.getName();
	    	addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_UPDATE);
    		logger.info("<<method:update()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 修改栏目："+category.getName()+" 失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<<method:update()|"+message+",ErrorMsg:"+result.toString());
    	}
   		return j;
    }
    
   /**
  	* 功能：栏目管理-删除
  	*/
    @RequestMapping(value="/categoryController/del",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson del(HttpServletRequest request){
    	String delId = request.getParameter("id");
    	AjaxJson j = new AjaxJson();
    	ApiResult result=categoryService.deleteCategory(delId);
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除栏目成功";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:oneDel()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除栏目失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:oneDel()|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
    }
    
}
