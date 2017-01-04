package com.gwghk.mis.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gwghk.mis.authority.ActionVerification;
import com.gwghk.mis.common.model.AjaxJson;
import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DataGrid;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.enums.AttachmentType;
import com.gwghk.mis.model.Attachment;
import com.gwghk.mis.service.AttachmentService;
import com.gwghk.mis.util.BrowserUtils;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.IPUtil;
import com.gwghk.mis.util.ResourceBundleUtil;

/**
 * 摘要：附件管理
 * @author Alan.wu
 * @date  2015/3/25
 */
@Scope("prototype")
@Controller
public class AttachmentController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(AttachmentController.class);
	
	@Autowired
	private AttachmentService attachmentService;
	
	/**
	 * 功能：附件管理-首页
	 */
	@RequestMapping(value = "/attachmentController/index", method = RequestMethod.GET)
	public  String  index(HttpServletRequest request,ModelMap map){
		Map<String,String> typeMap=new LinkedHashMap<String,String>();
    	for(AttachmentType type:AttachmentType.values()){
    		typeMap.put(type.getCode(), type.getText());
    	}
    	map.put("typeMap",typeMap);
		map.put("createDateStr", DateUtil.getDateMonthFormat(new Date()));
		return "attachment/attachmentList";
	}

	/**
	 * 获取datagrid列表
	 * @param request
	 * @param dataGrid  分页查询参数对象
	 * @param mngattachment   实体查询参数对象
	 */
	@RequestMapping(value = "/attachmentController/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public  Map<String, Object>  datagrid(HttpServletRequest request, DataGrid dataGrid,Attachment attachment){
		 if(attachment==null){
			 attachment=new Attachment();
		 }
		 String createDateStr=request.getParameter("createDateStr");
	     if(StringUtils.isNotBlank(createDateStr)){
	    	 attachment.setCreateDate(DateUtil.parseDateMonthFormat(createDateStr));
	     }
	     List<Attachment> list = attachmentService.getAttachmentList(this.createDetachedCriteria(dataGrid, attachment));
		 Map<String, Object> result = new HashMap<String, Object>();
		 result.put("total",null == list ? 0  : list.size());
	     result.put("rows", null == list ? new ArrayList<Attachment>() : list);
	     return result;
	}
	
	/**
	 * 功能：附件管理-新增
	 */
    @RequestMapping(value="/attachmentController/add", method = RequestMethod.GET)
    @ActionVerification(key="add")
    public String add(ModelMap map) throws Exception {
    	return "attachment/attachmentAdd";
    }
    
    /**
  	* 功能：附件管理-记录删除
  	*/
    @RequestMapping(value="/attachmentController/del",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson del(HttpServletRequest request,HttpServletResponse response){
    	String paths=request.getParameter("id");
    	if(StringUtils.isBlank(paths)){
    		paths = request.getParameter("ids");
    	}
    	AjaxJson j = new AjaxJson();
    	ApiResult result = attachmentService.deleteAttachment(paths.split(","));
    	if(result.isOk()){
          	j.setSuccess(true);
          	String message = " 附件: " + paths + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除附件成功";
          	addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:oneDel()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 附件: " + paths+ " "+DateUtil.getDateSecondFormat(new Date()) + " 删除附件失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:oneDel()|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
    }
    
}
