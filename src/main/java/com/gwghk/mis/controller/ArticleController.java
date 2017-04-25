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
import com.gwghk.mis.model.ChatGroup;
import com.gwghk.mis.service.ArticleService;
import com.gwghk.mis.service.CategoryService;
import com.gwghk.mis.service.ChatGroupService;
import com.gwghk.mis.util.BrowserUtils;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.IPUtil;
import com.gwghk.mis.util.JsonUtil;
import com.gwghk.mis.util.PropertiesUtil;
import com.gwghk.mis.util.ResourceBundleUtil;
import com.gwghk.mis.util.ResourceUtil;
import com.gwghk.mis.util.StringUtil;

/**
 * 摘要：文章管理
 * @author Alan.wu
 * @date   2015/3/19
 */
@Scope("prototype")
@Controller
public class ArticleController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ChatGroupService chatGroupService;

	/**
	 * 功能：文章管理-首页
	 */
	@RequestMapping(value = "/articleController/index", method = RequestMethod.GET)
	public  String  index(HttpServletRequest request,ModelMap map){
		DictConstant dict=DictConstant.getInstance();
		Map<String, List<BoDict>> dictMap=ResourceUtil.getDictListByLocale(getSystemFlag(),new String[]{dict.DICT_USE_STATUS,dict.DICT_PLATFORM});
    	map.put("dictConstant", dict);
    	map.put("dictMap", dictMap);
    	map.put("articlePlatformJson",JSONArray.toJSONString(dictMap.get(dict.DICT_PLATFORM)));
		return "article/articleList";
	}

	/**
	 * 获取datagrid列表
	 * @param request
	 * @param dataGrid  分页查询参数对象
	 * @param article   实体查询参数对象
	 * @return Map<String,Object> datagrid需要的数据
	 */
	@RequestMapping(value = "/articleController/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public  Map<String,Object>  datagrid(HttpServletRequest request, DataGrid dataGrid,Article article){
		 String publishStartDateStr=request.getParameter("publishStartDateStr");
    	 String publishEndDateStr=request.getParameter("publishEndDateStr");
    	 article.setPublishStartDate(DateUtil.parseDateFormat(publishStartDateStr));
    	 article.setPublishEndDate(DateUtil.parseDateFormat(publishEndDateStr));
			 article.setSystemCategory(getSystemFlag());
    	 List<ArticleDetail> detailList=new ArrayList<ArticleDetail>();
    	 ArticleDetail detail=new ArticleDetail();
    	 ArticleAuthor author = new ArticleAuthor();
    	 author.setName(request.getParameter("author"));
    	 detail.setTitle(request.getParameter("title"));
    	 detail.setAuthorInfo(author);
    	 detailList.add(detail);
    	 article.setDetailList(detailList);
    	 DetachedCriteria<Article> detachedCriteria = this.createDetachedCriteria(dataGrid, article);
    	 if(detachedCriteria.getOrderbyMap() == null || detachedCriteria.getOrderbyMap().isEmpty()){
    		 LinkedHashMap<String, SortDirection> orderMap = new LinkedHashMap<String, SortDirection>();
 			orderMap.put("sequence", SortDirection.DESC);
 			orderMap.put("publishStartDate", SortDirection.DESC);
 			detachedCriteria.setOrderbyMap(orderMap); 
    	 }
		 Page<Article> page = articleService.getArticlePage(detachedCriteria, 1);
		 Map<String, Object> result = new HashMap<String, Object>();
		 result.put("total",null == page ? 0  : page.getTotalSize());
	     result.put("rows", null == page ? new ArrayList<BoUser>() : page.getCollection());
	     return result;
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
    	map.put("filePath",PropertiesUtil.getInstance().getProperty("pmfilesDomain"));
    }
	
    /**
     * 设置栏目名称
     * @param categoryId
     * @return
     */
    private String getCategoryTxt(String categoryId){
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
    	return namePath.replaceAll(",", "-");
    }
    
    /**
     * 检查输入参数
     * @return
     */
    public AjaxJson checkSubmitParams(Article article){
    	AjaxJson jx=new AjaxJson();
    	if(StringUtils.isBlank(article.getCategoryId())||StringUtils.isBlank(article.getPlatform())||article.getPublishStartDate()==null
    			||article.getPublishEndDate()==null){
    		jx.setSuccess(false);
	    	jx.setMsg("部分参数输入为空值，请检查！");
	    	return jx;
    	}
    	return jx;
    }
    /**
   	 * 功能：文章管理-保存新增
   	 */
    @RequestMapping(value="/articleController/create",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="add")
    public AjaxJson create(HttpServletRequest request,HttpServletResponse response,Article article){
    	BoUser userParam = ResourceUtil.getSessionUser();
			article.setSystemCategory(getSystemFlag());
    	setBaseInfo(article,request,false);
    	AjaxJson j = new AjaxJson();
    	try{
    		setCommonSave(request,article);
    		j=checkSubmitParams(article);
    		if(!j.isSuccess()){
    			return j;
    		}
    		boolean isSendSubscribe = "1".equals(request.getParameter("sendSubscribe"));
        	ApiResult result =articleService.addArticle(article, isSendSubscribe);
        	if(result.isOk()){
        		j.setSuccess(true);
        		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功新增文章："+article.getId();
        		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
        		logger.info("<<method:create()|"+message);
        	}else{
        		j.setSuccess(false);
        		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
        		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 新增文章："+article.getId()+" 失败";
        		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
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
     * @param article
     */
    private void setCommonSave(HttpServletRequest request,Article article){
    	String detaiInfo= request.getParameter("detaiInfo");
    	String publishStartDateStr=request.getParameter("publishStartDateStr");
    	String publishEndDateStr=request.getParameter("publishEndDateStr");
    	article.setPublishStartDate(DateUtil.parseDateFormat(publishStartDateStr));
    	article.setPublishEndDate(DateUtil.parseDateFormat(publishEndDateStr));
        List<ArticleDetail> detailList=JSON.parseArray(JsonUtil.formatJsonStr(detaiInfo), ArticleDetail.class);
        article.setDetailList(detailList);
        String[] platformArr=request.getParameterValues("platformStr");
        if(platformArr!=null){
        	article.setPlatform(StringUtils.join(platformArr, ","));
        }
    }
   /**
   	* 功能：文章管理-保存更新
   	*/
    @RequestMapping(value="/articleController/update",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="edit")
    public AjaxJson update(HttpServletRequest request,HttpServletResponse response,Article article){
    	BoUser userParam = ResourceUtil.getSessionUser();
			article.setSystemCategory(getSystemFlag());
    	setBaseInfo(article,request,true);
    	AjaxJson j = new AjaxJson();
        try{
	    	setCommonSave(request,article);
	    	j=checkSubmitParams(article);
    		if(!j.isSuccess()){
    			return j;
    		}
	    	ApiResult result =articleService.updateArticle(article);
	    	if(result.isOk()){
	    		j.setSuccess(true);
	    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功修改文章："+article.getId();
	    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_UPDATE);
	    		logger.info("<--method:update()|"+message);
	    	}else{
	    		j.setSuccess(false);
	    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
	    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 修改文章："+article.getId()+" 失败";
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
  	* 功能：文章管理-批量删除
  	*/
    @RequestMapping(value="/articleController/del",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson batchDel(HttpServletRequest request,HttpServletResponse response){
    	BoUser userParam = ResourceUtil.getSessionUser();
    	AjaxJson j = new AjaxJson();
    	String delIds = request.getParameter("ids");
    	if(StringUtils.isBlank(delIds)){
    		delIds=request.getParameter("id");
    	}
    	ApiResult result =articleService.deleteArticle(delIds.contains(",")?delIds.split(","):new String[]{delIds});
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除文章成功";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:deleteArticle|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除文章失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:deleteArticle|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
    }
    
    /**
  	* 功能：文章管理-批量删除
  	*/
    @RequestMapping(value="/articleController/setStatus",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="setStatus")
    public AjaxJson setStatus(HttpServletRequest request,HttpServletResponse response){
    	BoUser userParam = ResourceUtil.getSessionUser();
    	AjaxJson j = new AjaxJson();
    	String delIds = request.getParameter("ids"),status=request.getParameter("fieldVal");
    	ApiResult result =articleService.setArticleStatus(delIds.contains(",")?delIds.split(","):new String[]{delIds},status);
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 设置文章状态成功";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:setArticleStatus|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 设置文章状态失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:setArticleStatu|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
    }

	/**
	 * 功能：文章管理-提取策略
	 */
    @RequestMapping(value="/articleController/toTradeStrateGet", method = RequestMethod.GET)
    @ActionVerification(key="getTradeStrate")
    public String toGetTradeStrate(ModelMap map) throws Exception {
      	map.addAttribute("serverDate",DateUtil.getDateDayFormat(new Date()));
    	return "article/tradeStrateGet";
    }
    
    /**
  	* 功能：文章管理-提取策略
  	*/
    @RequestMapping(value="/articleController/getTradeStrate",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="getTradeStrate")
    public AjaxJson tradeStrateGet(HttpServletRequest request,HttpServletResponse response){
    	BoUser userParam = ResourceUtil.getSessionUser();
    	AjaxJson j = new AjaxJson();
    	String dateStr = request.getParameter("dateStr"),
    		   srcPlaform = request.getParameter("srcPlaform"),
    		   lang=request.getParameter("lang"),
    	       titles=request.getParameter("titles");
    	String[] platformArr=request.getParameterValues("platform");
    	if(platformArr==null||StringUtils.isBlank(dateStr)||StringUtils.isBlank(lang)||StringUtils.isBlank(srcPlaform)){
    		j.setMsg("输入参数有误，请检查输入栏位！");
    		j.setSuccess(false);
    		return j;
    	}
    	ApiResult apiResult = articleService.getTradeStrate(srcPlaform,dateStr,lang,titles,StringUtils.join(platformArr, ","),IPUtil.getClientIP(request),userParam.getUserId());
		if(apiResult.isOk()){
			j.setSuccess(true);
    		String message = "用户：" + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 提取交易策略成功";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:tradeStrateGet()|"+message);
		}else{
			j.setSuccess(false);
			j.setMsg(apiResult.getErrorMsg());
    		String message = "用户：" + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 提取交易策略失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:tradeStrateGet()|"+message+",ErrorMsg:"+apiResult.toString());
		}
  		return j;
    }
    
	/**
	 * 格式成树形列表
	 * 
	 * @param dictList
	 * @return
	 */
	private List<ChatGroup> formatTreeList(List<BoDict> dictList)
	{
		List<ChatGroup> nodeList = new ArrayList<ChatGroup>();
		List<ChatGroup> groupList = chatGroupService.getChatGroupList("id", "name", "groupType");
		ChatGroup tbean = null;
		for (BoDict dict : dictList)
		{
			tbean = new ChatGroup();
			tbean.setId(dict.getCode());
			tbean.setName(dict.getNameCN());
			nodeList.add(tbean);
			for (ChatGroup group : groupList)
			{
				if (group.getGroupType().equals(dict.getCode()))
				{
					group.setName(StringUtil.fillChar('　', 1) + group.getName());
					nodeList.add(group);
				}
			}
		}
		return nodeList;
	}
	/**
	 * 功能：文章管理-新增、修改、预览
	 */
	@ActionVerification(key="view")
    @RequestMapping(value="/articleController/articleInfo", method = RequestMethod.GET)
    public String articleInfo(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws Exception {
    	String articleId = request.getParameter("articleId");
    	setCommonShowModel(map);
    	String opType = request.getParameter("opType");
    	map.addAttribute("opType", opType);
    	Article article=articleService.getArticleById(articleId);
    	if("R".equals(opType)){//预览
    		article.setCategoryId(getCategoryTxt(article.getCategoryId()));
        	//平台类型转成中文显示
        	List<BoDict> subList=ResourceUtil.getSubDictListByParentCode(getSystemFlag(),DictConstant.getInstance().DICT_PLATFORM);
        	String platform=article.getPlatform(),articlePlatform="";
        	int size=0;
        	if(StringUtils.isNotBlank(platform) && subList!=null && (size=subList.size())>0){
    	    	platform=",".concat(platform).concat(",");
    	    	BoDict row=null;
    	    	for(int i=0;i<size;i++){
    	    		 row=subList.get(i);
    				 if(platform.contains(",".concat(row.getCode()).concat(","))){
    					 articlePlatform+=(StringUtils.isBlank(articlePlatform)?"":"，")+row.getNameCN();
    				 }
    	   		}
        	}
        	article.setPlatform(articlePlatform);
    	}
    	map.addAttribute("article",JSON.toJSONString(article));
		return "article/articleInfo";
    }
    /**
     * 功能：文章管理-加载文档模板
     */
    @RequestMapping(value="/articleController/loadTemp", method = RequestMethod.GET)
    public String loadTemp(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws Exception {
    	String template = request.getParameter("template");
    	DictConstant dict = DictConstant.getInstance();
    	Map<String, Object> config = new HashMap<String, Object>();
    	Map<String,String> langMap=new LinkedHashMap<String,String>();
    	for(Lang lang:Lang.values()){
    		langMap.put(lang.getCode(), lang.getText());
    	}
    	config.put("langMap", langMap);
    	config.put("dictConstant", dict);
    	config.put("dictMap", ResourceUtil.getDictListByLocale(getSystemFlag(),new String[]{dict.DICT_USE_STATUS}));
    	config.put("filePath",PropertiesUtil.getInstance().getProperty("pmfilesDomain"));
    	
    	String view = null;
    	if("note".equals(template)){
    		config.put("chatGroupList", this.formatTreeList(ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_TYPE)));
    		config.put("pmApiCourseUrl", PropertiesUtil.getInstance().getProperty("pmApiUrl")+"/common/getCourse");
    		view = "article_note";
    	}else if("live".equals(template)){
    		view = "article_live";
    	}else{//normal
    		view = "article_normal";
    	}
    	map.addAttribute("config", JSON.toJSONString(config));
		return "article/" + view;
    }
    
    /**
     * 功能：文章管理-批量删除
     */
    @RequestMapping(value="/articleController/sendSubscribe",method=RequestMethod.POST)
    @ResponseBody
    public AjaxJson sendSubscribe(HttpServletRequest request,HttpServletResponse response){
    	String articleId = request.getParameter("articleId");
    	AjaxJson j = new AjaxJson();
    	if(StringUtils.isBlank(articleId)){
    		j.setSuccess(false);
    		j.setMsg("文章编号不能为空！");
    	}else{
    		articleService.sendSubscribe(articleId);
    		j.setSuccess(true);
    		j.setMsg("发送订阅通知成功！");
    	}
    	return j;
    }
}
