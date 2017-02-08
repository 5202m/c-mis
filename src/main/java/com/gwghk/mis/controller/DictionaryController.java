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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gwghk.mis.authority.ActionVerification;
import com.gwghk.mis.common.model.AjaxJson;
import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.model.BoDict;
import com.gwghk.mis.model.TreeVo;
import com.gwghk.mis.service.DictService;
import com.gwghk.mis.util.BeanUtils;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.IPUtil;
import com.gwghk.mis.util.ResourceUtil;

/**
 * 摘要：数据字典管理
 * @author Gavin.guo
 * @date   2014-10-27
 */
@Scope("prototype")
@Controller
public class DictionaryController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(DictionaryController.class);
	
	@Autowired
	private DictService dictService;
	/**
	 * 功能：数据字典管理-首页
	 */
	@RequestMapping(value = "/dictionaryController/index", method = RequestMethod.GET)
	public  String  index(ModelMap map){
		logger.debug(">>start into dictionaryController.index()...");
		return "system/dictionary/dictionaryList";
	}
	
	/**
	 * 功能：数据字典管理-加载一级菜单列表
	 * @param dictJsonParam 查询参数
	 */
	@RequestMapping(value = "/dictionaryController/treeGrid", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public  String  treeGrid(HttpServletRequest request){
		String code = request.getParameter("dictionaryCodeS");
		String name = request.getParameter("dictionaryNameS");
		String status = request.getParameter("dictionaryStatusS");
		List<TreeVo>  typeDicList = null;
		List<BoDict> typeList = dictService.getDictList(name,code,status);
		if(typeList != null && typeList.size() > 0){
			typeDicList = new ArrayList<TreeVo>();
			String lang=ResourceUtil.getSessionLocale();
			for(BoDict type  : typeList){
				TreeVo typeGroupDic = new TreeVo();
				typeGroupDic.setId(type.getId());
				typeGroupDic.setParentCode(null);
				typeGroupDic.setCode(type.getCode());
				typeGroupDic.setName(WebConstant.LOCALE_ZH_CN.equals(lang)?type.getNameCN():(WebConstant.LOCALE_ZH_TW.equals(lang)?type.getNameTW():type.getNameEN()));
				typeGroupDic.setNameCN(type.getNameCN());
				typeGroupDic.setNameTW(type.getNameTW());
				typeGroupDic.setNameEN(type.getNameEN());
				typeGroupDic.setValue(type.getValue());
				typeGroupDic.setState("closed");
				typeGroupDic.setType("1");
				typeGroupDic.setSort(type.getSort());
				typeGroupDic.setChildren(new ArrayList<TreeVo>());
				typeGroupDic.setStatus(type.getStatus());
				typeDicList.add(typeGroupDic);
			}
			return JSONArray.fromObject(typeDicList).toString();
		}
		return JSONArray.fromObject(new ArrayList<TreeVo>()).toString();
	}
	
	/**
	 * 功能：数据字典管理-加载子菜单
	 */
	@RequestMapping(value = "/dictionaryController/loadChildTreeGrid", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
	@ResponseBody
	public String loadChildTreeGrid(String typeGroupId){
		List<TreeVo> typeDicList = null;
		BoDict pDict = dictService.getDictByCode(typeGroupId,null);
		if(pDict!=null){
			List<BoDict> typeList = pDict.getChildren();
			if(typeList != null && typeList.size() > 0){
				String lang=ResourceUtil.getSessionLocale();
				typeDicList = new ArrayList<TreeVo>();
				for(BoDict type : typeList){
					TreeVo  typeDic = new  TreeVo();
					typeDic.setId(type.getId());
					typeDic.setParentCode(pDict.getCode());
					typeDic.setCode(type.getCode());
					typeDic.setName(WebConstant.LOCALE_ZH_CN.equals(lang)?type.getNameCN():(WebConstant.LOCALE_ZH_TW.equals(lang)?type.getNameTW():type.getNameEN()));
					typeDic.setNameCN(type.getNameCN());
					typeDic.setNameTW(type.getNameTW());
					typeDic.setNameEN(type.getNameEN());
					typeDic.setFlag(type.getSystemCategory());
					typeDic.setState("colse");
					typeDic.setValue(type.getValue());
					typeDic.setType("2");
					typeDic.setSort(type.getSort());
					typeDic.setStatus(type.getStatus());
					typeDicList.add(typeDic);
				}
			}
			logger.debug(">>method:loadChildTreeGrid child menu list : "+JSONArray.fromObject(typeDicList).toString());
		}
		return pDict!=null&&typeDicList != null ? JSONArray.fromObject(typeDicList).toString() : JSONArray.fromObject(new ArrayList<TreeVo>()).toString();
	}
	
	/**
	 * 功能：数据字典管理-新增
	 */
    @RequestMapping(value="/dictionaryController/add", method = RequestMethod.GET)  
    @ActionVerification(key="add")
    public String add(HttpServletRequest request,ModelMap map) throws Exception {
    	String type = request.getParameter("type");
    	String parentCode = request.getParameter("parentCode");
    	map.addAttribute("type",type);
    	map.addAttribute("parentCode",parentCode);
    	return "system/dictionary/dictionaryAdd";
    }
    
    /**
   	 * 功能：数据字典管理-保存新增
   	 */
    @RequestMapping(value="/dictionaryController/create",method=RequestMethod.POST)
   	@ResponseBody
    public AjaxJson create(HttpServletRequest request,TreeVo dictionaryJsonParam){
    	BoDict dict=new BoDict();
        BeanUtils.copyExceptNull(dict, dictionaryJsonParam);
        dict.setCreateUser(userParam.getUserNo());
        dict.setCreateIp(IPUtil.getClientIP(request));
        String[] categoryArr=request.getParameterValues("systemCategoryStr");
        if(categoryArr!=null && categoryArr.length>0){
        	dict.setSystemCategory(StringUtils.join(categoryArr, ","));
        }
        AjaxJson j = new AjaxJson();
        ApiResult result = "1".equals(dictionaryJsonParam.getType())?dictService.saveParentDict(dict, false)
        		:dictService.saveChildrenDict(dictionaryJsonParam.getParentCode(),dict, false);
    	if(result.isOk()){
	    	j.setSuccess(true);
	    	j.setObj(result.getReturnObj()[0]);
	    	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功新增数据字典："+dict.getNameCN();
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
    		logger.info("<<create()|"+message);
    	}else{
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 新增数据字典："+dict.getNameCN()+" 失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<<create()|"+message+",ErrorMsg:"+result.toString());
    	}
		return j;
    }
    
	/**
	 * 功能：数据字典管理-修改
	 */
    @RequestMapping(value="/dictionaryController/edit", method = RequestMethod.GET)
    @ActionVerification(key="edit")
    public String edit(HttpServletRequest request,ModelMap map) throws Exception {
    	String id = request.getParameter("id");
    	String type=request.getParameter("type");
    	BoDict dictParam =null;
    	TreeVo dictionaryJsonParam = new TreeVo();
    	if("1".equals(type)){
    		dictParam =dictService.getDictById(id);
    	}else{
    		dictParam =dictService.getDictByChildId(id);
    		if(dictParam!=null){
    			dictionaryJsonParam.setParentCode(dictParam.getCode());
    			dictParam=dictParam.getChildren().stream().filter(e->e.getId().equals(id)).findFirst().get();
    		}
    	}
    	BeanUtils.copyExceptNull(dictionaryJsonParam, dictParam);
    	dictionaryJsonParam.setId(dictParam.getId());
    	dictionaryJsonParam.setType(request.getParameter("type"));
    	map.addAttribute("dictionaryJsonParam",dictionaryJsonParam);
    	map.addAttribute("systemCategory",dictParam.getSystemCategory());
		return "system/dictionary/dictionaryEdit";
    }
    
   /**
   	* 功能：数据字典管理-保存更新
   	*/
    @RequestMapping(value="/dictionaryController/update",method=RequestMethod.POST)
   	@ResponseBody
    public AjaxJson update(HttpServletRequest request,BoDict dictParam){
    	dictParam.setUpdateUser(userParam.getUserNo());
    	dictParam.setUpdateIp(IPUtil.getClientIP(request));
    	String type=request.getParameter("type");
    	 String[] categoryArr=request.getParameterValues("systemCategoryStr");
         if(categoryArr!=null && categoryArr.length>0){
        	 dictParam.setSystemCategory(StringUtils.join(categoryArr, ","));
         }
    	AjaxJson j = new AjaxJson();
        ApiResult result = "1".equals(type)?dictService.saveParentDict(dictParam, true)
           		:dictService.saveChildrenDict(request.getParameter("parentCode"),dictParam, true);
    	if(result.isOk()){
	    	j.setObj(result.getReturnObj()[0]);
	    	j.setSuccess(true);
	    	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功修改数据字典："+dictParam.getNameCN();
	    	addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_UPDATE);
    		logger.info("<<method:update()|"+message);
    	}else{
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 修改数据字典："+dictParam.getNameCN()+" 失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<<method:update()|"+message+",ErrorMsg:"+result.toString());
    	}
   		return j;
    }
    
   /**
  	* 功能：数据字典管理-删除
  	*/
    @RequestMapping(value="/dictionaryController/oneDel",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson oneDel(HttpServletRequest request){
    	String delId = request.getParameter("id");
    	String type = request.getParameter("type");
    	AjaxJson j = new AjaxJson();
    	ApiResult result=dictService.deleteDict(delId,"1".equals(type));
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除数据字典成功";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:oneDel()|"+message);
    	}else{
    		j.setSuccess(false);
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除数据字典失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:oneDel()|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
    }
    
}
