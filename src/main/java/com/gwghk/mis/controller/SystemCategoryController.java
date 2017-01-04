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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.gwghk.mis.common.model.AjaxJson;
import com.gwghk.mis.common.model.DataGrid;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.common.model.TreeBean;
import com.gwghk.mis.model.BoRole;
import com.gwghk.mis.model.BoSystemCategory;
import com.gwghk.mis.service.SystemCategoryService;
import com.gwghk.mis.util.JsonUtil;


@Scope("prototype")
@Controller
public class SystemCategoryController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(SystemCategoryController.class);

    @Autowired
    private SystemCategoryService systemCategoryService;

    @RequestMapping("/systemCategoryController/index")
    public String indexPage(){
        return "system/category/category";
    }

    @RequestMapping("/systemCategoryController/list")
    @ResponseBody
    public Map<String,Object> listPage(DataGrid dataGrid, BoSystemCategory systemCategory){
        Page<BoSystemCategory> page = systemCategoryService.list(this.createDetachedCriteria(dataGrid, systemCategory));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total",null == page ? 0  : page.getTotalSize());
        result.put("rows", null == page ? new ArrayList<BoRole>() : page.getCollection());
        return result;
    }
    @RequestMapping("systemCategoryController/edit")
    public ModelAndView editPage(String id){
        ModelAndView view = new ModelAndView("system/category/categoryEdit");
        if(StringUtils.isNotBlank(id)){
            //有id 修改 查询原始数据
            BoSystemCategory systemCategory = systemCategoryService.getSystemCategory(id);
            view.addObject("systemCategory",systemCategory);
        }
        return view;
    }
    @RequestMapping(value = "/systemCategoryController/edit",method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson edit(HttpServletRequest request,BoSystemCategory systemCategory){
		AjaxJson json = new AjaxJson();
		if(StringUtils.isBlank(systemCategory.getCode())||StringUtils.isBlank(systemCategory.getName())){
			 json.setMsg("非法参数");
	         json.setSuccess(false);
	         return json;
		}
		try{
	        if(systemCategory.getId() == null){
	            systemCategory.setCreateUser(userParam.getUserNo());
	            systemCategory.setCreateDate(new Date());
	        }
	        systemCategoryService.save(systemCategory);
	        json.setSuccess(true);
	     }catch (Exception e){
	        json.setMsg(e.getMessage());
	        json.setSuccess(false);
	        logger.error("<<method:edit()|ErrorMsg:",e.getMessage());
	    }
	    return json;
    }

    @RequestMapping(value = "/systemCategoryController/delete")
    @ResponseBody
    public AjaxJson delete(String id){
        AjaxJson json = new AjaxJson();
        try{
            systemCategoryService.delete(id);
            json.setSuccess(true);
        }catch (Exception e){
            json.setMsg("删除失败");
            json.setSuccess(false);
        }
        return json;
    }
    
	/**
   	 * 功能：房间
   	 */
    @RequestMapping(value = "/systemCategoryController/getCategoryTreeList", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
   	@ResponseBody
    public String getCategoryTreeList(HttpServletRequest request,ModelMap map) throws Exception {
    	String categoryId=request.getParameter("categoryId");
       	List<TreeBean> treeList=new ArrayList<TreeBean>();
       	TreeBean tbean=null;
       	List<BoSystemCategory> lst=systemCategoryService.list();
       	categoryId=StringUtils.isBlank(categoryId)?"":(",".concat(categoryId).concat(","));
       	for(BoSystemCategory row:lst){
       		 tbean=new TreeBean();
       		 tbean.setId(row.getCode());
       		 tbean.setText(row.getName());
       		 if(categoryId.contains(",".concat(row.getCode()).concat(","))){
    			tbean.setChecked(true);
    		 }
       		 tbean.setParentId("");
   			 treeList.add(tbean);
       	}
       	return JsonUtil.formatListToTreeJson(treeList,false);
     }
}
