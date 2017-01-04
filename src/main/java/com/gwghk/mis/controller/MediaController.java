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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gwghk.mis.authority.ActionVerification;
import com.gwghk.mis.common.model.AjaxJson;
import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DataGrid;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.constant.DictConstant;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.enums.Lang;
import com.gwghk.mis.enums.SortDirection;
import com.gwghk.mis.model.Article;
import com.gwghk.mis.model.ArticleAuthor;
import com.gwghk.mis.model.ArticleDetail;
import com.gwghk.mis.model.BoDict;
import com.gwghk.mis.model.BoUser;
import com.gwghk.mis.model.Category;
import com.gwghk.mis.service.ArticleService;
import com.gwghk.mis.service.CategoryService;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.JSONHelper;
import com.gwghk.mis.util.JsonUtil;
import com.gwghk.mis.util.ResourceBundleUtil;
import com.gwghk.mis.util.ResourceUtil;

/**
 * 摘要：媒体管理(包括广告、视频）
 * @author Alan.wu
 * @date   2015/6/29
 */
@Scope("prototype")
@Controller
public class MediaController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(MediaController.class);
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private CategoryService categoryService;
	/**
	 * 功能：媒体管理-首页
	 */
	@RequestMapping(value = "/mediaController/index", method = RequestMethod.GET)
	public  String  index(HttpServletRequest request,ModelMap map){
		DictConstant dict=DictConstant.getInstance();
		Map<String, List<BoDict>> dictMap=ResourceUtil.getDictListByLocale(getSystemFlag(),new String[]{dict.DICT_USE_STATUS,dict.DICT_PLATFORM});
    	map.put("dictConstant", dict);
    	map.put("dictMap", dictMap);
    	map.put("mediaPlatformJson",JSONArray.toJSONString(dictMap.get(dict.DICT_PLATFORM)));
		return "media/mediaList";
	}

	/**
	 * 获取datagrid列表
	 * @param request
	 * @param dataGrid  分页查询参数对象
	 * @param media   实体查询参数对象
	 * @return Map<String,Object> datagrid需要的数据
	 */
	@RequestMapping(value = "/mediaController/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public  Map<String,Object>  datagrid(HttpServletRequest request, DataGrid dataGrid,Article media){
		 String publishStartDateStr=request.getParameter("publishStartDateStr");
    	 String publishEndDateStr=request.getParameter("publishEndDateStr");
    	 media.setPublishStartDate(DateUtil.parseDateFormat(publishStartDateStr));
    	 media.setPublishEndDate(DateUtil.parseDateFormat(publishEndDateStr));
    	 List<ArticleDetail> detailList=new ArrayList<ArticleDetail>();
    	 ArticleDetail detail=new ArticleDetail();
    	 ArticleAuthor author = new ArticleAuthor();
    	 author.setName("");
    	 detail.setTitle(request.getParameter("title"));
    	 detail.setAuthorInfo(author);
    	 detailList.add(detail);
    	 media.setDetailList(detailList);
    	 DetachedCriteria<Article> detachedCriteria = this.createDetachedCriteria(dataGrid, media);
    	 if(detachedCriteria.getOrderbyMap() == null || detachedCriteria.getOrderbyMap().isEmpty()){
			LinkedHashMap<String, SortDirection> orderMap = new LinkedHashMap<String, SortDirection>();
			orderMap.put("sequence", SortDirection.DESC);
			orderMap.put("publishStartDate", SortDirection.DESC);
			detachedCriteria.setOrderbyMap(orderMap); 
    	 }
		 Page<Article> page = articleService.getArticlePage(detachedCriteria, 2);
		 Map<String, Object> result = new HashMap<String, Object>();
		 result.put("total",null == page ? 0  : page.getTotalSize());
	     result.put("rows", null == page ? new ArrayList<BoUser>() : page.getCollection());
	     return result;
	}
	
	/**
	 * 功能：媒体管理-新增
	 */
    @RequestMapping(value="/mediaController/add", method = RequestMethod.GET)
    @ActionVerification(key="add")
    public String add(ModelMap map) throws Exception {
    	setCommonShowModel(map);
    	return "media/mediaAdd";
    }
    
    /**
     * 设置通用的输出
     * @param map
     */
    private void setCommonShowModel(ModelMap map){
    	Map<String,String> langMap=new LinkedHashMap<String,String>();
    	for(Lang lang:Lang.values()){
    		langMap.put(lang.getCode(), lang.getText());
    	}
    	map.put("langMap", langMap);
    	DictConstant dict=DictConstant.getInstance();
    	map.put("dictConstant", dict);
    	map.put("dictMap", ResourceUtil.getDictListByLocale(getSystemFlag(),new String[]{dict.DICT_USE_STATUS}));
    }
    
    /**
	 * 功能：媒体管理-查看
	 */
    @RequestMapping(value="/mediaController/{mediaId}/view", method = RequestMethod.GET)
    @ActionVerification(key="view")
    public String view(@PathVariable String mediaId , ModelMap map) throws Exception {
    	setCommonShowModel(map);
    	Article media=articleService.getArticleById(mediaId);
    	setCategoryTxt(media.getCategoryId(),map);
    	//平台类型转成中文显示
    	List<BoDict> subList=ResourceUtil.getSubDictListByParentCode(getSystemFlag(),DictConstant.getInstance().DICT_PLATFORM);
    	String platform=media.getPlatform(),MediaPlatform="";
    	int size=0;
    	if(StringUtils.isNotBlank(platform) && subList!=null && (size=subList.size())>0){
	    	platform=",".concat(platform).concat(",");
	    	BoDict row=null;
	    	for(int i=0;i<size;i++){
	    		 row=subList.get(i);
				 if(platform.contains(",".concat(row.getCode()).concat(","))){
					 MediaPlatform+=(StringUtils.isBlank(MediaPlatform)?"":"，")+row.getNameCN();
				 }
	   		}
    	}
    	map.addAttribute("media",media);
    	map.addAttribute("mediaPlatform",MediaPlatform);
		return "media/mediaView";
    }
	
    /**
     * 设置栏目名称
     * @param categoryId
     * @param map
     * @return
     */
    private void setCategoryTxt(String categoryId, ModelMap map){
    	Category category=categoryService.getCategoryById(categoryId);
    	String namePath="";
    	if(category!=null){
    		namePath=category.getParentNamePath();
    		if(StringUtils.isNotBlank(namePath)){
    			namePath+="-"+category.getName();
    		}else{
    			namePath=category.getName();
    		}
    	}
    	map.addAttribute("categoryTxt",namePath.replaceAll(",", "-"));
    }
	/**
	 * 功能：媒体管理-修改
	 */
    @ActionVerification(key="edit")
    @RequestMapping(value="/mediaController/{mediaId}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable String mediaId, ModelMap map) throws Exception {
    	setCommonShowModel(map);
    	Article media=articleService.getArticleById(mediaId);
    	map.addAttribute("media",media);
		return "media/mediaEdit";
    }
    
    /**
     * 检查输入参数
     * @return
     */
    public AjaxJson checkSubmitParams(Article media){
    	AjaxJson jx=new AjaxJson();
    	if(StringUtils.isBlank(media.getCategoryId())||StringUtils.isBlank(media.getPlatform())||media.getPublishStartDate()==null
    			||media.getPublishEndDate()==null){
    		jx.setSuccess(false);
	    	jx.setMsg("部分参数输入为空值，请检查！");
	    	return jx;
    	}
    	return jx;
    }
    /**
   	 * 功能：媒体管理-保存新增
   	 */
    @RequestMapping(value="/mediaController/create",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="add")
    public AjaxJson create(HttpServletRequest request,HttpServletResponse response,Article media){
    	BoUser userParam = ResourceUtil.getSessionUser();
    	setBaseInfo(media,request,false);
    	AjaxJson j = new AjaxJson();
    	try{
    		setCommonSave(request,media);
    		j=checkSubmitParams(media);
    		if(!j.isSuccess()){
    			return j;
    		}
    		logger.info(JSONHelper.bean2json(media));
        	ApiResult result =articleService.addArticle(media, false);
        	if(result.isOk()){
        		j.setSuccess(true);
        		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功新增媒体："+media.getId();
        		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
        		logger.info("<<method:create()|"+message);
        	}else{
        		j.setSuccess(false);
        		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
        		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 新增媒体："+media.getId()+" 失败";
        		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT
        						 );
        		logger.error("<<method:create()|"+message+",ErrorMsg:"+result.toString());
        	}
	    }catch(Exception e){
	    	j.setSuccess(false);
	    	j.setMsg("操作失败！");
	    	logger.error("<--Exception:create()",e);
		}
		return j;
    }
    
    /**
     * 设置通用的保存数据
     * @param request
     * @param media
     */
    private void setCommonSave(HttpServletRequest request,Article media){
    	String detaiInfo= request.getParameter("detaiInfo");
    	String publishStartDateStr=request.getParameter("publishStartDateStr");
    	String publishEndDateStr=request.getParameter("publishEndDateStr");
    	media.setPublishStartDate(DateUtil.parseDateFormat(publishStartDateStr));
    	media.setPublishEndDate(DateUtil.parseDateFormat(publishEndDateStr));
        List<ArticleDetail> detailList=JSON.parseArray(JsonUtil.formatJsonStr(detaiInfo), ArticleDetail.class);
        media.setDetailList(detailList);
        String[] platformArr=request.getParameterValues("platformStr");
        if(platformArr!=null){
        	media.setPlatform(StringUtils.join(platformArr, ","));
        }
    }
   /**
   	* 功能：媒体管理-保存更新
   	*/
    @RequestMapping(value="/mediaController/update",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="edit")
    public AjaxJson update(HttpServletRequest request,HttpServletResponse response,Article media){
    	BoUser userParam = ResourceUtil.getSessionUser();
    	setBaseInfo(media,request,true);
    	AjaxJson j = new AjaxJson();
        try{
	    	setCommonSave(request,media);
	    	j=checkSubmitParams(media);
    		if(!j.isSuccess()){
    			return j;
    		}
	    	ApiResult result =articleService.updateArticle(media);
	    	if(result.isOk()){
	    		j.setSuccess(true);
	    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功修改媒体："+media.getId();
	    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_UPDATE);
	    		logger.info("<--method:update()|"+message);
	    	}else{
	    		j.setSuccess(false);
	    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
	    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 修改媒体："+media.getId()+" 失败";
	    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
	    		logger.error("<--method:update()|"+message+",ErrorMsg:"+result.toString());
	    	}
        }catch(Exception e){
        	j.setSuccess(false);
        	j.setMsg("操作失败！");
        	logger.error("<--Exception:update()",e);
    	}
   		return j;
     }
    
   /**
  	* 功能：媒体管理-批量删除
  	*/
    @RequestMapping(value="/mediaController/del",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson batchDel(HttpServletRequest request,HttpServletResponse response){
    	AjaxJson j = new AjaxJson();
    	String delIds = request.getParameter("ids");
    	if(StringUtils.isBlank(delIds)){
    		delIds=request.getParameter("id");
    	}
    	ApiResult result =articleService.deleteArticle(delIds.contains(",")?delIds.split(","):new String[]{delIds});
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除媒体成功";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:deleteMedia|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除媒体失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:deleteMedia|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
    }
}
