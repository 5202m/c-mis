package com.gwghk.mis.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.gwghk.mis.common.dao.MongoDBBaseDao;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.enums.IdSeq;
import com.gwghk.mis.model.Article;

/**
 * 管理DAO类
 * @author alan.wu
 * @date 2015/2/5
 */
@Repository
public class ArticleDao extends MongoDBBaseDao{
	
	/**
	 * 分页查询
	 * @param query
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<Article> getPage(Query query,DetachedCriteria<Article> dCriteria){
		return super.findPage(Article.class, query, dCriteria);
	}

	/**
	 * 删除文章记录
	 * @param ids
	 * @return
	 */
	public boolean deleteArticle(Object[] ids) {
		List<Article> list=this.findList(Article.class,Query.query(Criteria.where("id").in(ids)));
		if(list!=null&&list.size()>0){
			for(Article article:list){
				this.remove(article);
			}
		}
		return true;
	}
	
	 /**
	 * 新增文章记录
	 * @param Article
	 * @return
	 * @throws Exception 
	 */
	public void addArticle(Article article) throws Exception{
		article.setId(this.getIncSeq(IdSeq.Article).toString());
		article.setStatus(1);
		this.add(article);
	}
}
