package com.gwghk.mis.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.dao.SmsInfoDao;
import com.gwghk.mis.enums.ResultCode;
import com.gwghk.mis.model.SmsConfig;
import com.gwghk.mis.model.SmsInfo;
import com.gwghk.mis.util.DateUtil;

/**
 * 短信管理<BR>
 * ------------------------------------------<BR>
 * <BR>
 * Copyright (c) 2015<BR>
 * Author : Dick.guo <BR>
 * Date : 2015年10月28日 <BR>
 * Description : <BR>
 * <p>
 *     短信信息管理
 * </p>
 */
@Service
public class SmsInfoService {

	@Autowired
	private SmsInfoDao smsInfoDao;
	
	@Autowired
	private PmApiService pmApiService;
	
	@Autowired
	private SmsConfigService smsConfigService;
	
	

	/**
	 * 短信信息列表查询
	 * 
	 * @param dCriteria
	 * @return
	 */
	public Page<SmsInfo> getSmsInfos(DetachedCriteria<SmsInfo> dCriteria) {
		SmsInfo smsInfo = dCriteria.getSearchModel();
		Query query = new Query();
		Criteria criteria = new Criteria();
		if (smsInfo != null) {
			criteria.and("systemCategory").is(smsInfo.getSystemCategory());
			boolean hasStartTime = StringUtils.isNotBlank(smsInfo.getSendStart()); 
			if(hasStartTime){
				criteria = criteria.and("sendTime").gte(DateUtil.parseDateSecondFormat(smsInfo.getSendStart()));
			}
			if(StringUtils.isNotBlank(smsInfo.getSendEnd())){
				if(hasStartTime){
					criteria.lte(DateUtil.parseDateSecondFormat(smsInfo.getSendEnd()));
				}else{
					criteria.and("sendTime").lte(DateUtil.parseDateSecondFormat(smsInfo.getSendEnd()));
				}
			}
			if (StringUtils.isNotBlank(smsInfo.getMobilePhone())) {
				criteria.and("mobilePhone").is(smsInfo.getMobilePhone());
			}
			if (StringUtils.isNotBlank(smsInfo.getType())) {
				criteria.and("type").is(smsInfo.getType());
			}
			if (StringUtils.isNotBlank(smsInfo.getUseType())) {
				criteria.and("useType").is(smsInfo.getUseType());
			}
			if (smsInfo.getStatus() != null) {
				criteria.and("status").is(smsInfo.getStatus());
			}
			
			query.addCriteria(criteria);
		}
		return smsInfoDao.querySmsInfos(query, dCriteria);
	}
	
	/**
	 * 重新发送
	 * @param smsId
	 * @return
	 */
	public ApiResult resend(String smsId){
		ApiResult result = new ApiResult();
		boolean isOk = pmApiService.resend(smsId);
		result.setCode(isOk ? ResultCode.OK : ResultCode.FAIL);
		return result;
	}
	
	/**
	 * 短信计数
	 * @param mobile
	 * @param type
	 * @param useType
	 * @param startDate
	 * @return
	 */
	public int smsCount(String systemCategory,String mobile, String type, String useType, Date startDate){
		Criteria criteria = Criteria.where("cntFlag").is(1);
		criteria.and("mobilePhone").is(mobile);
		criteria.and("type").is(type);
		criteria.and("useType").is(useType);
		criteria.and("sendTime").gte(startDate);
		criteria.and("systemCategory").is(systemCategory);
		return smsInfoDao.count(SmsInfo.class, new Query(criteria)).intValue();
	}
	
	/**
	 * 获取短信信息
	 * @param smsId
	 * @return
	 */
	public Map<String, Object> getSmsInfoMap(String systemCategory,String smsId){
		Map<String, Object> loc_result = new HashMap<String, Object>();
		SmsInfo loc_sms = smsInfoDao.findSmsInfo(smsId);
		loc_result.put("mobilePhone", loc_sms.getMobilePhone());
		loc_result.put("type", loc_sms.getType());
		loc_result.put("useType", loc_sms.getUseType());
		loc_result.put("deviceKey", loc_sms.getDeviceKey());
		SmsConfig loc_smsConfig = smsConfigService.findByType(systemCategory,loc_sms.getType(), loc_sms.getUseType());
		if(loc_smsConfig != null && new Integer(1).equals(loc_smsConfig.getStatus())){
			loc_result.put("cnt", loc_smsConfig.getCnt());
			loc_result.put("cycle", loc_smsConfig.getCycle());
			Date loc_start = DateUtil.getStartDateOfCycle(new Date(), loc_smsConfig.getCycle());
			int loc_cnt = this.smsCount(systemCategory,loc_sms.getMobilePhone(), loc_sms.getType(), loc_sms.getUseType(), loc_start);
			loc_result.put("cntUsed", loc_cnt);
			loc_result.put("resetStart", DateUtil.formatDate(loc_start, DateUtil.FORMAT_YYYYDDMMHHMMSS));
			Date loc_end = DateUtil.getEndDateOfCycle(new Date(), loc_smsConfig.getCycle());
			loc_result.put("resetEnd", DateUtil.formatDate(loc_end, "YYYY-MM-dd HH:mm:ss"));
		}
		return loc_result;
	}
	
	/**
	 * 设置计数标记
	 * @param mobilePhone
	 * @param type
	 * @param useType
	 * @param deviceKey
	 * @param startDate
	 */
	public ApiResult setCntFlag(String systemCategory,String mobilePhone, String type, String useType, String deviceKey, String startDate){
		ApiResult result = new ApiResult();
		if(StringUtils.isBlank(mobilePhone) || StringUtils.isBlank(type) || StringUtils.isBlank(useType) || StringUtils.isBlank(startDate)){
			result.setCode(ResultCode.Error103);
		}
		else{
			Date loc_startDate = DateUtil.parseDateFormat(startDate, DateUtil.FORMAT_YYYYDDMMHHMMSS);
			Criteria criteria = Criteria.where("cntFlag").is(1);
			criteria.and("type").is(type);
			criteria.and("useType").is(useType);
			criteria.and("systemCategory").is(systemCategory);
			criteria.and("sendTime").gte(loc_startDate);
			if (StringUtils.isNotBlank(deviceKey)) {
				criteria.orOperator(Criteria.where("mobilePhone").is(mobilePhone), Criteria.where("deviceKey").is(deviceKey));
			}else{
				criteria.and("mobilePhone").is(mobilePhone);
			}
			smsInfoDao.setCntFlag(new Query(criteria));
			result.setCode(ResultCode.OK);
		}
		return result;
	}
}
