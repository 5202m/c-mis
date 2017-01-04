package com.gwghk.mis.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.dao.SmsConfigDao;
import com.gwghk.mis.enums.ResultCode;
import com.gwghk.mis.model.SmsConfig;

/**
 * 短信配置管理<BR>
 * ------------------------------------------<BR>
 * <BR>
 * Copyright (c) 2015<BR>
 * Author : Dick.guo <BR>
 * Date : 2015年10月29日 <BR>
 * Description : <BR>
 * <p>
 *     短信配置管理
 * </p>
 */
@Service
public class SmsConfigService {

	@Autowired
	private SmsConfigDao smsConfigDao;
	
	/**
	 * 短信配置列表查询
	 * 
	 * @param dCriteria
	 * @return
	 */
	public Page<SmsConfig> getSmsConfigs(DetachedCriteria<SmsConfig> dCriteria) {
		SmsConfig smsConfig = dCriteria.getSearchModel();
		Query query = new Query();
		Criteria criteria = Criteria.where("isDeleted").is(1);
		if (smsConfig != null) {
			criteria.and("systemCategory").is(smsConfig.getSystemCategory());
			if (StringUtils.isNotBlank(smsConfig.getType())) {
				criteria.and("type").is(smsConfig.getType());
			}
			if (StringUtils.isNotBlank(smsConfig.getUseType())) {
				criteria.and("useType").is(smsConfig.getUseType());
			}
			if (smsConfig.getStatus() != null) {
				criteria.and("status").is(smsConfig.getStatus());
			}
		}
		query.addCriteria(criteria);
		return smsConfigDao.querySmsConfigs(query, dCriteria);
	}
	
	/**
	 * 根据ID查找
	 * @param smsCfgId
	 * @return
	 */
	public SmsConfig findById(String smsCfgId){
		return smsConfigDao.findSmsCfg(smsCfgId);
	}
	
	/**
	 * 根据类型查找
	 * @param type
	 * @param useType
	 * @return
	 */
	public SmsConfig findByType(String systemCategory,String type, String useType){
		return smsConfigDao.findSmsCfg(systemCategory,type, useType);
	}
	
	/**
	 * 新增保存
	 * @param smsConfig
	 * @return
	 */
	public ApiResult add(SmsConfig smsConfig) {
		ApiResult result=new ApiResult();
		if(this.findByType(smsConfig.getSystemCategory(),smsConfig.getType(), smsConfig.getUseType())!=null){
			return result.setCode(ResultCode.Error102);
		}
		smsConfig.setIsDeleted(1);
		boolean isOk = smsConfigDao.save(smsConfig);
    	return result.setCode(isOk ? ResultCode.OK : ResultCode.FAIL);
	}
	
	/**
	 * 更新保存
	 * @param smsConfig
	 * @return
	 */
	public ApiResult update(SmsConfig smsConfig) {
		ApiResult result=new ApiResult();
		boolean isOk = smsConfigDao.update(smsConfig);
		return result.setCode(isOk ? ResultCode.OK : ResultCode.FAIL);
	}
	
	/**
	 * 删除
	 * @param smsCfgId
	 * @return
	 */
	public ApiResult delete(String smsCfgId) {
		ApiResult result=new ApiResult();
		boolean isOk = smsConfigDao.delete(smsCfgId);
		return result.setCode(isOk ? ResultCode.OK : ResultCode.FAIL);
	}
}
