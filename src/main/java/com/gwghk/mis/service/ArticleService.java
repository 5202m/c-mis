package com.gwghk.mis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.dao.ArticleDao;
import com.gwghk.mis.dao.CategoryDao;
import com.gwghk.mis.enums.ResultCode;
import com.gwghk.mis.model.Article;
import com.gwghk.mis.model.ArticleAuthor;
import com.gwghk.mis.model.ArticleDetail;
import com.gwghk.mis.model.Category;
import com.gwghk.mis.util.BeanUtils;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.StringUtil;

/**
 * 摘要：文章管理服务类
 * @author Alan.wu
 * @date   2015年3月16日
 */
@Service
public class ArticleService{

	@Autowired
	private ArticleDao articleDao;
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private PmApiService pmApiService;

	@Autowired
	private ChatApiService chatApiService;
	/**
	 * 分页查询文章
	 * @param detachedCriteria
	 * @return
	 */
	public Page<Article> getArticlePage(DetachedCriteria<Article> dCriteria, Integer type) {
		Query query=new Query();
		Article article=dCriteria.getSearchModel();
		if(article!=null){
			Criteria criteria=new Criteria();
			criteria.and("valid").is(1);
			String categoryId=article.getCategoryId();
			//栏目筛选
			List<Category> rowList = null;
			ArrayList<String> ids=new ArrayList<String>();
			if(StringUtils.isNotBlank(categoryId)){
				rowList=categoryDao.getChildrenByParentId(categoryId, type);
				ids.add(categoryId);
			}else{
				rowList=categoryDao.getListByType(type);
			}
			if(rowList!=null && rowList.size()>0){
				for(Category row:rowList){
					ids.add(row.getId());
				}
			}
			if(ids.isEmpty() == false){
				criteria.and("categoryId").in(ids.toArray());
			}

			if(StringUtils.isNotBlank(article.getId())){
				criteria.and("_id").is(article.getId());
			}
			if(StringUtils.isNotBlank(article.getPlatform())){
				criteria.and("platform").regex(article.getPlatform().replaceAll(",","|"));
			}
			if(article.getStatus()!=null){
				criteria.and("status").is(article.getStatus());
			}
			if(article.getPublishStartDate()!=null){
				criteria = criteria.and("publishStartDate").gte(article.getPublishStartDate());
			}
			if(article.getPublishEndDate()!=null){
				criteria = criteria.and("publishEndDate").lte(article.getPublishEndDate());
			}
			Criteria  detailCriteria = new Criteria();
			List<ArticleDetail> detailList=article.getDetailList();
			if(detailList!=null&&detailList.size()>0){
				ArticleDetail detail=detailList.get(0);
				if(StringUtils.isNotBlank(detail.getLang())){
					detailCriteria.and("lang").in((Object[])detail.getLang().split(","));
				}
				if(StringUtils.isNotBlank(detail.getTitle())){
					detailCriteria.and("title").regex(StringUtil.toFuzzyMatch(detail.getTitle()));
				}
				if(StringUtils.isNotBlank(detail.getAuthorInfo().getName())){
					detailCriteria.and("authorInfo.name").regex(StringUtil.toFuzzyMatch(detail.getAuthorInfo().getName()));
				}
			}
			criteria.and("detailList").elemMatch(detailCriteria);
			query.addCriteria(criteria);
		}
		Page<Article> page=articleDao.getPage(query,dCriteria);
	    List<Article> articleList=page.getCollection();
	    ArrayList<String> categoryIds=new ArrayList<String>();
	    for(Article row:articleList){
	    	categoryIds.add(row.getCategoryId());
	    }
	    List<Category> categoryList=categoryDao.findByIdArr(categoryIds.toArray());
	    if(categoryList!=null && categoryList.size()>0){
		    //提取栏目父类路径名
		    Category oldCategory=null;
		    for(Article row:articleList){
		    	Optional<Category> optional=categoryList.stream().filter(e->e.getId().equals(row.getCategoryId())).findFirst();
		    	if(optional==null){
		    		continue;
		    	}
		        try{
		        	oldCategory=optional.get();
		        }catch(Exception e){
		        	continue;
		        }
		    	if(oldCategory!=null){
		    		if(StringUtils.isNotBlank(oldCategory.getParentNamePath())){
		    			row.setCategoryNamePath(StringUtil.concatStr(oldCategory.getParentNamePath(),",",oldCategory.getName()));
		    		}else{
		    			row.setCategoryNamePath(oldCategory.getName());
		    		}
		    	}
		        
		    }
	    }
		return page;
	}
	
	/**
	 * 通过id找对应文章记录
	 * @param articleId
	 * @return
	 */
	public Article getArticleById(String articleId) {
		return articleDao.findById(Article.class, articleId);
	}

	/**
	 * 新增文章记录
	 * @param article
	 * @param syncArticle
	 * @return
	 */
	public ApiResult addArticle(Article article, boolean isSendSubscribe) {
		ApiResult result=new ApiResult();
		try {
			article.setValid(1);
			article.setPraise(0);
			articleDao.addArticle(article);
			//chat socket通知
			if("class_note".equals(article.getCategoryId()) ||
					"trade_strategy_article".equals(article.getCategoryId())){
				chatApiService.noticeArticle(article, "C");
			}
			//api 订阅通知
			if(isSendSubscribe && "class_note".equals(article.getCategoryId())){
				pmApiService.subscribeArticle(article.getId());
			}
		} catch (Exception e) {
			return result.setCode(ResultCode.FAIL);
		}
		return result.setCode(ResultCode.OK);
	}
	
	/**
	 * 更新文章记录
	 * @param articleParam
	 * @param b
	 * @return
	 */
	public ApiResult updateArticle(Article articleParam) {
		Article article=articleDao.findById(Article.class, articleParam.getId());
		BeanUtils.copyExceptNull(article, articleParam);
		article.setDetailList(articleParam.getDetailList());
		try {
			article.setValid(1);
			articleDao.update(article);
			if("class_note".equals(article.getCategoryId()) ||
					"trade_strategy_article".equals(article.getCategoryId())){
				chatApiService.noticeArticle(article, "U");
			}
		} catch (Exception e) {
			return new ApiResult().setCode(ResultCode.FAIL);
		}
		return new ApiResult().setCode(ResultCode.OK);
	}

	/**
	 * 删除文章记录
	 * @param ids
	 * @return
	 */
	public ApiResult deleteArticle(String[] ids) {
		return new ApiResult().setCode(articleDao.softDelete(Article.class, ids)?ResultCode.OK:ResultCode.FAIL);
	}
	
	/**
	 * 设置文章状态
	 * @param ids
	 * @param status
	 * @return
	 */
	public ApiResult setArticleStatus(String[] ids,String status) {
		return new ApiResult().setCode(articleDao.batchSetFieldVal(Article.class, ids,"status",status)?ResultCode.OK:ResultCode.FAIL);
	}

	/**
	 * 通过pm_api接口提取交易策略
	 * @param platform
	 * @param dateStr
	 * @param langs
	 * @param titles
	 * @param usedByPlatforms 用于那些平台
	 * @return
	 */
	public ApiResult getTradeStrate(String platform, String dateStr,
			String lang, String titles,String usedByPlatforms,String ip,String createUser) {
		String str=pmApiService.getBroadStrateList(platform,dateStr,lang);
		ApiResult api=new ApiResult();
		if(StringUtils.isBlank(str)){
			return api.setErrorMsg("没有可提取的交易策略！");
		}
		api.setErrorMsg("提取交易策略失败,请联系管理员！");
		JSONObject strObj=JSON.parseObject(str);
		if(!strObj.containsKey("broadcasts")){
			return api.setErrorMsg("没有可提取的交易策略！");
		}
		JSONObject bdObj=(JSONObject)strObj.get("broadcasts");
		if(0==bdObj.getIntValue("result")){
			if(!bdObj.containsKey("broadcastList")){
				return api.setErrorMsg("没有可提取的交易策略！");
			}
			Object bdList=bdObj.get("broadcastList");
			JSONArray arr=new JSONArray();
			if(bdList instanceof  JSONArray){
				arr=(JSONArray)bdList;
			}else{
				arr.add(bdList);
			}
			JSONObject jo=null;
			Article article=null;
			ArticleDetail articleDetail=null;
			List<ArticleDetail> detailList=null;
			ArticleAuthor author = null;
			String publishDateStr="",title="",content="",remark="";
			int label=0,dbHasRecord=0,hasTitleIndex=0;
			Long count=0L;
			boolean hasRecord=false;
			String[] titleArr=null;
			Date publishStartDate=null;
			for(Object obj:arr){
				jo=(JSONObject)obj;
				title=jo.getString("title");
				if(StringUtils.isNotBlank(titles)){
					if(titleArr==null){
						titleArr=StringUtils.split(titles,"|");
					}
					for(String titleStr:titleArr){
						if(title.equals(titleStr)){
							hasRecord=true;
							break;
						}
					}
					if(!hasRecord){
						hasTitleIndex++;
						continue; 
					}
				}
				publishDateStr=jo.getString("regtime");
				count=articleDao.count(Article.class, Query.query(new Criteria().and("valid").is(1).and("srcId").is(jo.getIntValue("id")).and("categoryId").is("trade_strategy_article")));
				if(count>0){
					dbHasRecord++;
					continue;
				}
				content=jo.getString("content");
				article=new Article();
				article.setCategoryId("trade_strategy_article");
				article.setPlatform(usedByPlatforms);
				article.setCreateDate(new Date());
				article.setSrcId(jo.getIntValue("id"));
				publishStartDate=DateUtil.parseDateSecondFormat(publishDateStr);
				article.setPublishStartDate(publishStartDate);
				article.setPublishEndDate(DateUtil.getNextDay(publishStartDate));
				article.setCreateIp(ip);
				article.setCreateUser(createUser);
				article.setSequence(0);
				articleDetail=new ArticleDetail();
				articleDetail.setLang(lang);
				label=jo.getIntValue("label");
				if(1==label){
					articleDetail.setTag("黄金");
				}else if(2==label){
					articleDetail.setTag("白银");
				}else if(3==label){
					articleDetail.setTag("美元指数");
				}else{
					articleDetail.setTag("");
				}
				remark=StringUtil.html2Text(content);
				articleDetail.setTitle(title);
				author = new ArticleAuthor();
				author.setName(jo.getString("expertname"));
				author.setAvatar(jo.getString("expertpic"));
				author.setPosition(jo.getString("expertposition"));
				author.setUserId(jo.getString("expertuserid"));
				//articleDetail.setAuthor(jo.getString("expertname")+(StringUtils.isBlank(jo.getString("expertpic"))?"":";"+jo.getString("expertpic")));
				articleDetail.setAuthorInfo(author);
				articleDetail.setContent(content);
				articleDetail.setRemark(remark.length()>50?remark.substring(0, 50):remark);
				detailList=new ArrayList<ArticleDetail>();
				detailList.add(articleDetail);
				article.setDetailList(detailList);
				ApiResult result=this.addArticle(article, false);
				if(result.isOk()){
					api.setCode(ResultCode.OK);
				}
			}
			if(dbHasRecord==arr.size()){
				return api.setErrorMsg("文档已经存在相同的记录，无需再提取！");
			}
			if(hasTitleIndex==arr.size()){
				return api.setErrorMsg("没有可提取的交易策略！");
			}
		}else{
			return api.setErrorMsg("没有可提取的交易策略！");
		}
		return api;
	}
	
	/**
	 * 发送订阅通知
	 * @param articleId
	 * @return
	 */
	public boolean sendSubscribe(String articleId){
		return pmApiService.subscribeArticle(articleId);
	}

	/**
	 * 获取课程信息
	 * @param flag
	 * @param groupType
	 * @param groupId
	 * @return
	 */
	public String getCourse(String flag, String groupType, String groupId){
		return pmApiService.getCourse(flag, groupType, groupId);
	}
}
