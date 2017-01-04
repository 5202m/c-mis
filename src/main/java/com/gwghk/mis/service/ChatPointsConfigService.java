package com.gwghk.mis.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.dao.ChatPointsConfigDao;
import com.gwghk.mis.enums.ResultCode;
import com.gwghk.mis.model.ChatPointsConfig;
import com.gwghk.mis.util.BeanUtils;

/**
 * 积分配置管理<BR>
 * ------------------------------------------<BR>
 * <BR>
 * Copyright (c) 2015<BR>
 * Author : Dick.guo <BR>
 * Date : 2015年10月29日 <BR>
 * Description : <BR>
 * <p>
 *     积分配置管理
 * </p>
 */
@Service
public class ChatPointsConfigService {

	@Autowired
	private ChatPointsConfigDao chatPointsConfigDao;
	
	/**
	 * 积分配置列表查询
	 * 
	 * @param dCriteria
	 * @return
	 */
	public Page<ChatPointsConfig> getChatPointsConfigs(DetachedCriteria<ChatPointsConfig> dCriteria) {
		ChatPointsConfig chatPointsConfig = dCriteria.getSearchModel();
		Query query = new Query();
		Criteria criteria = Criteria.where("isDeleted").is(0);
		if (chatPointsConfig != null) {
			criteria.and("systemCategory").is(chatPointsConfig.getSystemCategory());
			if (StringUtils.isNotBlank(chatPointsConfig.getGroupType())) {
				criteria.and("groupType").is(chatPointsConfig.getGroupType());
			}
			if (StringUtils.isNotBlank(chatPointsConfig.getGroupId())) {
				criteria.and("groupId").is(chatPointsConfig.getGroupId());
			}
			if (StringUtils.isNotBlank(chatPointsConfig.getType())) {
				criteria.and("type").is(chatPointsConfig.getType());
			}
			if (StringUtils.isNotBlank(chatPointsConfig.getItem())) {
				criteria.and("item").is(chatPointsConfig.getItem());
			}
			if (chatPointsConfig.getStatus() != null) {
				criteria.and("status").is(chatPointsConfig.getStatus());
			}
			if (chatPointsConfig.getVal() != null) {
				criteria.and("val").is(chatPointsConfig.getVal());
			}
		}
		query.addCriteria(criteria);
		return chatPointsConfigDao.queryPointsConfigs(query, dCriteria);
	}
	
	/**
	 * 根据ID查找
	 * @param pointsCfgId
	 * @return
	 */
	public ChatPointsConfig findById(String pointsCfgId){
		return chatPointsConfigDao.findPointsConfig(pointsCfgId);
	}
	
	/**
	 * 新增保存
	 * @param chatPointsConfig
	 * @return
	 */
	public ApiResult add(ChatPointsConfig chatPointsConfig) {
		ApiResult result=new ApiResult();
		chatPointsConfig.setIsDeleted(0);
		boolean isOk = chatPointsConfigDao.save(chatPointsConfig);
    	return result.setCode(isOk ? ResultCode.OK : ResultCode.FAIL);
	}
	
	/**
	 * 更新保存
	 * @param chatPointsConfig
	 * @return
	 */
	public ApiResult update(ChatPointsConfig chatPointsConfig) {
		ChatPointsConfig chatPointsConfigDb = this.findById(chatPointsConfig.getCfgId());
		BeanUtils.copyExceptNull(chatPointsConfigDb, chatPointsConfig);
		chatPointsConfigDao.update(chatPointsConfigDb);
		return new ApiResult().setCode(ResultCode.OK);
	}
	
	/**
	 * 删除
	 * @param pointsCfgId
	 * @return
	 */
	public ApiResult delete(String pointsCfgId) {
		ApiResult result=new ApiResult();
		boolean isOk = chatPointsConfigDao.delete(pointsCfgId);
		return result.setCode(isOk ? ResultCode.OK : ResultCode.FAIL);
	}
}
