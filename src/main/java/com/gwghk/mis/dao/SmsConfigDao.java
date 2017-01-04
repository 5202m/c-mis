package com.gwghk.mis.dao;

import java.util.Date;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.gwghk.mis.common.dao.MongoDBBaseDao;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.model.SmsConfig;
import com.mongodb.WriteResult;

/**
 * 短信配置管理<BR>
 * ------------------------------------------<BR>
 * <BR>
 * Copyright (c) 2015<BR>
 * Author : Dick.guo <BR>
 * Date : 2015年10月29日 <BR>
 * Description : <BR>
 * <p>
 *    短信配置管理
 * </p>
 */
@Repository
public class SmsConfigDao extends MongoDBBaseDao {
	
    /**
     * 分页查询短信信息列表
     * @param query
     * @param dCriteria
     * @return
     */
	public Page<SmsConfig> querySmsConfigs(Query query, DetachedCriteria<SmsConfig> dCriteria){
		return this.findPage(SmsConfig.class, query, dCriteria);
	}
	
    /**
     * 按照Id查找短信配置
     * @param smsCfgId
     * @return
     */
	public SmsConfig findSmsCfg(String smsCfgId){
		return this.findById(SmsConfig.class, smsCfgId);
	}
	
	/**
	 * 按照类型查找短信配置
	 * @param type
	 * @param useType
	 * @return
	 */
	public SmsConfig findSmsCfg(String systemCategory, String type, String useType){
		Criteria criteria = Criteria.where("type").is(type);
		criteria.and("useType").is(useType);
		criteria.and("isDeleted").is(1);
		criteria.and("systemCategory").is(systemCategory);
		return this.findOne(SmsConfig.class, new Query(criteria));
	}
	

	/**
	 * 保存
	 * @param smsConfig
	 * @return
	 */
	public boolean save(SmsConfig smsConfig) {
		this.add(smsConfig);
		return true;
	}
	
	/**
	 * 修改
	 * @param 
	 * @return
	 */
	public boolean update(SmsConfig smsConfig) {
		Update loc_update = new Update();
		loc_update.set("updateUser", smsConfig.getUpdateUser());
		loc_update.set("updateIp", smsConfig.getUpdateIp());
		loc_update.set("updateDate", new Date());
		
		loc_update.set("status", smsConfig.getStatus());
		loc_update.set("validTime", smsConfig.getValidTime());
		loc_update.set("cnt", smsConfig.getCnt());
		loc_update.set("cycle", smsConfig.getCycle());
		
		this.update(new Query(Criteria.where("_id").is(smsConfig.getSmsCfgId())), loc_update, SmsConfig.class);
		return true;
	}
	

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public boolean delete(String id) {
		WriteResult wr = this.mongoTemplate.updateMulti(Query.query(Criteria.where("_id").is(id))
					, Update.update("isDeleted", 0), SmsConfig.class);
		return wr != null && wr.getN() > 0;
	}
}
