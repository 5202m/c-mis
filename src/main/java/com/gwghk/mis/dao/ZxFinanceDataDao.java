package com.gwghk.mis.dao;

import com.gwghk.mis.model.ZxFinanceDataComment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.gwghk.mis.common.dao.MongoDBBaseDao;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.model.ZxFinanceData;
import com.gwghk.mis.model.ZxFinanceDataCfg;
import com.mongodb.WriteResult;
import org.springframework.util.StringUtils;

/**
 * 财经日历DAO<BR>
 * ------------------------------------------<BR>
 * <BR>
 * Copyright (c) 2016<BR>
 * Author : Dick.guo <BR>
 * Date : 2016年03月18日 <BR>
 * Description : <BR>
 * <p>
 *    财经日历DAO
 * </p>
 */
@Repository
public class ZxFinanceDataDao extends MongoDBBaseDao {
	
    /**
     * 分页查询财经日历列表
     * @param query
     * @param dCriteria
     * @return
     */
	public Page<ZxFinanceData> queryDatas(Query query, DetachedCriteria<ZxFinanceData> dCriteria){
		return this.findPage(ZxFinanceData.class, query, dCriteria);
	}

	/**
	 * 删除（财经日历）
	 * @param id
	 * @return
	 */
	public boolean delete(String id) {
		WriteResult wr = this.mongoTemplate.updateMulti(Query.query(Criteria.where("_id").is(id))
					, Update.update("valid", 0), ZxFinanceData.class);
		return wr != null && wr.getN() > 0;
	}
	
	/**
	 * 删除（财经日历）
	 * @param basicIndexId
	 * @return
	 */
	public boolean deleteByBasicIndexId(String basicIndexId) {
		WriteResult wr = this.mongoTemplate.updateMulti(Query.query(Criteria.where("basicIndexId").is(basicIndexId))
				, Update.update("valid", 0), ZxFinanceData.class);
		return wr != null && wr.getN() > 0;
	}
	
	/**
	 * 删除（财经日历配置）
	 * @param id
	 * @return
	 */
	public boolean deleteCfg(String id) {
		WriteResult wr = this.mongoTemplate.updateMulti(Query.query(Criteria.where("_id").is(id))
				, Update.update("valid", 0), ZxFinanceDataCfg.class);
		return wr != null && wr.getN() > 0;
	}

	/**
	 * 添加/修改点评
	 * @param data
	 * @param comment
	 * @return
	 */
	public boolean saveComment(ZxFinanceData data, ZxFinanceDataComment comment){
		WriteResult wr=null;
		if(StringUtils.isEmpty(comment.getId())){
			comment.setId(new ObjectId().toString());
			wr = this.mongoTemplate.updateMulti(new Query(Criteria.where("_id").is(data.getDataId())),new Update()
					.push("comments", comment)
					, ZxFinanceData.class);
		} else {
			wr = this.mongoTemplate.updateMulti(new Query(Criteria.where("_id").is(data.getDataId()).and("comments._id").is(new ObjectId(comment.getId()))),new Update()
							.set("comments.$.comment",comment.getComment())
							.set("comments.$.avatar",comment.getAvatar())
							.set("comments.$.userName",comment.getUserName())
							.set("comments.$.userId",comment.getUserId())
							.set("comments.$.updateDate", comment.getUpdateDate())
							.set("comments.$.updateIp", comment.getUpdateIp())
							.set("comments.$.updateUser", comment.getUpdateUser())
					, ZxFinanceData.class);
		}
		return wr!=null&&wr.getN()>0;
	}

	/**
	 * 删除点评（财经日历）
	 * @param dataId
	 * @param id
	 * @return
	 */
	public boolean delReview(String dataId, String id) {
		WriteResult wr = this.mongoTemplate.updateMulti(Query.query(Criteria.where("_id").is(dataId).and("comments._id").is(new ObjectId(id)))
				, new Update().set("comments.$.valid", 0), ZxFinanceData.class);
		return wr != null && wr.getN() > 0;
	}

}
