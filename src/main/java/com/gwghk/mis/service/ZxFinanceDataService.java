package com.gwghk.mis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.gwghk.mis.model.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
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
import com.gwghk.mis.dao.ZxFinanceDataDao;
import com.gwghk.mis.enums.ResultCode;
import com.gwghk.mis.util.BeanUtils;
import com.gwghk.mis.util.HttpClientUtils;
import com.gwghk.mis.util.PropertiesUtil;
import com.gwghk.mis.util.StringUtil;

/**
 * 财经日历<BR>
 * ------------------------------------------<BR>
 * <BR>
 * Copyright (c) 2016<BR>
 * Author : Dick.guo <BR>
 * Date : 2016年03月18日 <BR>
 * Description : <BR>
 * <p>
 *     财经日历
 * </p>
 */
@Service
public class ZxFinanceDataService {
	private static final Logger logger = Logger.getLogger(ZxFinanceDataService.class);

	@Autowired
	private ZxFinanceDataDao dataDao;

	@Autowired
	private ChatApiService chatApiService;
	
	/**
	 * 列表查询（财经日历）
	 * 
	 * @param dCriteria
	 * @return
	 */
	public Page<ZxFinanceData> getDatas(DetachedCriteria<ZxFinanceData> dCriteria) {
		ZxFinanceData data = dCriteria.getSearchModel();
		Query query = new Query();
		Criteria criteria = new Criteria();
		if (data != null) {
			if (StringUtils.isNotBlank(data.getBasicIndexId())) {
				criteria.and("basicIndexId").is(data.getBasicIndexId());
			}
			if (StringUtils.isNotBlank(data.getName())) {
				criteria.and("name").regex(StringUtil.toFuzzyMatch(data.getName()));
			}
			if (StringUtils.isNotBlank(data.getCountry())) {
				criteria.and("country").regex(StringUtil.toFuzzyMatch(data.getCountry()));
			}
			if (data.getDataType() != null) {
				criteria.and("dataType").is(data.getDataType());
			}
			boolean hasStart = StringUtils.isNotBlank(data.getDateStart()); 
			if(hasStart){
				criteria = criteria.and("date").gte(data.getDateStart());
			}
			if(StringUtils.isNotBlank(data.getDateEnd())){
				if(hasStart){
					criteria.lte(data.getDateEnd());
				}else{
					criteria.and("date").lte(data.getDateEnd());
				}
			}
			if (data.getValid() != null) {
				criteria.and("valid").is(data.getValid());
			}
		}
		query.addCriteria(criteria);
		return dataDao.queryDatas(query, dCriteria);
	}
	
	/**
	 * 按照指标编号查询财经日历数据
	 * @param basicIndexId
	 * @return
	 */
	public List<ZxFinanceData> getDatas(String basicIndexId) {
		Query query = new Query(Criteria.where("basicIndexId").is(basicIndexId));
		return dataDao.findList(ZxFinanceData.class, query);
	}
	
	/**
	 * 查找单个财经日历
	 * @param basicIndexId 类别号
	 * @param period 周期
	 * @param year   年
	 * @return
	 */
	public ZxFinanceData findData(String basicIndexId, String period, int year){
		Criteria criteria = Criteria.where("valid").is(1)
				.and("basicIndexId").is(basicIndexId)
				.and("period").is(period)
				.and("year").is(year);
		return dataDao.findOne(ZxFinanceData.class, new Query(criteria));
	}
	
	/**
	 * 按照ID查询（财经日历）
	 * @param dataId
	 * @return
	 */
	public ZxFinanceData findById(String dataId){
		return dataDao.findById(ZxFinanceData.class, dataId);
	}
	
	/**
	 * 新增保存（财经日历）
	 * @param data
	 * @return
	 */
	public ApiResult add(ZxFinanceData data) {
		ApiResult result=new ApiResult();
		data.setValid(1);
		dataDao.add(data);
    	return result.setCode(ResultCode.OK);
	}
	
	/**
	 * 更新保存（财经日历）
	 * @param data
	 * @return
	 */
	public ApiResult update(ZxFinanceData data) {
		ZxFinanceData src = this.findById(data.getDataId());
		BeanUtils.copyExceptNull(src, data);
		ApiResult result=new ApiResult();
		dataDao.update(src);
		return result.setCode(ResultCode.OK);
	}
	
	/**
	 * 用新的财经日历配置信息更新所有相关财经日历数据
	 * @param datas
	 * @param config
	 * @return
	 */
	public ApiResult update(List<ZxFinanceData> datas, ZxFinanceDataCfg config){
		if (datas != null && !datas.isEmpty())
		{
			String description = null;
			for (ZxFinanceData data : datas)
			{
				description = config.getDescription();
				description = description.replaceAll(",", "_U_U_U,") + "_U_U_U";
				data.setDescription(description);
				data.setDataType(config.getDataType());
				data.setImportanceLevel(config.getImportanceLevel());
				data.setValid(config.getValid());
				data.setDescription(this.getDescription(data));
				data.setUpdateDate(config.getUpdateDate());
				dataDao.update(data);
			}
		}
		ApiResult result=new ApiResult();
		return result.setCode(ResultCode.OK);
	}
	
	/**
	 * 删除（财经日历）
	 * @param dataId
	 * @return
	 */
	public ApiResult delete(String dataId) {
		ApiResult result=new ApiResult();
		boolean isOk = dataDao.delete(dataId);
		return result.setCode(isOk ? ResultCode.OK : ResultCode.FAIL);
	}
	
	/**
	 * 删除（财经日历）
	 * @param basicIndexId
	 * @return
	 */
	public ApiResult deleteByBasicIndexId(String basicIndexId) {
		ApiResult result=new ApiResult();
		boolean isOk = dataDao.deleteByBasicIndexId(basicIndexId);
		return result.setCode(isOk ? ResultCode.OK : ResultCode.FAIL);
	}
	
	/**
	 * 列表查询（财经日历配置）
	 * 
	 * @param dCriteria
	 * @return
	 */
	public Page<ZxFinanceDataCfg> getDataCfgs(DetachedCriteria<ZxFinanceDataCfg> dCriteria) {
		ZxFinanceDataCfg dataCfg = dCriteria.getSearchModel();
		Query query = new Query();
		Criteria criteria = new Criteria();
		if (dataCfg != null) {
			if (StringUtils.isNotBlank(dataCfg.getBasicIndexId())) {
				criteria.and("basicIndexId").is(dataCfg.getBasicIndexId());
			}
			if (StringUtils.isNotBlank(dataCfg.getName())) {
				criteria.and("name").regex(StringUtil.toFuzzyMatch(dataCfg.getName()));
			}
			if (StringUtils.isNotBlank(dataCfg.getCountry())) {
				criteria.and("country").regex(StringUtil.toFuzzyMatch(dataCfg.getCountry()));
			}
			if (dataCfg.getDataType() != null) {
				criteria.and("dataType").is(dataCfg.getDataType());
			}
			if (dataCfg.getImportanceLevel() != null) {
				criteria.and("importanceLevel").is(dataCfg.getImportanceLevel());
			}
			if (dataCfg.getValid() != null) {
				criteria.and("valid").is(dataCfg.getValid());
			}
			if (new Integer(1).equals(dataCfg.getSetFlag())) {
				criteria.and("updateUser").ne(null);
			}else if(new Integer(0).equals(dataCfg.getSetFlag())) {
				criteria.and("updateUser").is(null);
			}
		}
		query.addCriteria(criteria);
		return dataDao.findPage(ZxFinanceDataCfg.class, query, dCriteria);
	}
	
	/**
	 * 按照ID查询（财经日历配置）
	 * @param basicIndexId
	 * @return
	 */
	public ZxFinanceDataCfg findCfgById(String basicIndexId){
		return dataDao.findById(ZxFinanceDataCfg.class, basicIndexId);
	}
	
	/**
	 * 更新保存（财经日历配置）
	 * @param config
	 * @return
	 */
	public ApiResult updateCfg(ZxFinanceDataCfg config) {
		ZxFinanceDataCfg src = this.findCfgById(config.getBasicIndexId());
		BeanUtils.copyExceptNull(src, config);
		dataDao.update(src);
		//修改所有相关财经日历，将指标编号为cfgId的财经日历数据同步更新
		List<ZxFinanceData> datas = this.getDatas(src.getBasicIndexId());
		return this.update(datas, src);
	}
	
	/**
	 * 删除（财经日历配置）
	 * @param cfgId
	 * @return
	 */
	public ApiResult deleteCfg(String cfgId) {
		ApiResult result=new ApiResult();
		boolean isOk = dataDao.deleteCfg(cfgId);
		//修改所有相关财经日历，将指标编号为cfgId的财经日历数据置为无效
		this.deleteByBasicIndexId(cfgId);
		return result.setCode(isOk ? ResultCode.OK : ResultCode.FAIL);
	}

	/**
	 * 格式请求url
	 * 财经日历-/IndexEventApi
	 * 财经日历详情-/IndexEventDetailApi
	 * @param contextPath
	 * @return
	 */
	private String formatUrl(String contextPath){
		return PropertiesUtil.getInstance().getProperty("fxgoldApiUrl") + contextPath;
	}

	/**
	 * 从fxgold获取财经日历数据
	 * 
	 * @param date
	 *            2015-11-09
	 */
	public List<ZxFinanceDataApi> getDataFromFxGold(String date)
	{
		List<ZxFinanceDataApi> result = null;
		Map<String, String> paramMap = new HashMap<String, String>();
		if (StringUtils.isBlank(date))
		{
			logger.warn("IndexEventApi error: date is empty!");
			return result;
		}
		paramMap.put("date", date);
		try
		{
			String str = HttpClientUtils.httpGetString(this.formatUrl("/IndexEventApi"), paramMap);
			if (StringUtils.isNotBlank(str))
			{
				JSONObject obj = JSON.parseObject(str);
				result = JSONArray.parseArray(obj.getString("data"), ZxFinanceDataApi.class);
			}
		}
		catch (Exception e)
		{
			logger.warn("IndexEventApi error：" + e);
		}
		return result;
	}

	/**
	 * 从fxgold获取财经日历详情数据
	 * 
	 * @param basicIndexId
	 *            20
	 */
	public ZxFinanceDetailApi getDetailFromFxGold(String basicIndexId)
	{
		ZxFinanceDetailApi result = null;
		Map<String, String> paramMap = new HashMap<String, String>();
		if (StringUtils.isBlank(basicIndexId))
		{
			logger.warn("IndexEventDetailApi error: basicIndexId is empty!");
			return result;
		}
		paramMap.put("basicIndexId", basicIndexId);
		try
		{
			String str = HttpClientUtils.httpGetString(this.formatUrl("/IndexEventDetailApi"), paramMap);
			if (StringUtils.isNotBlank(str))
			{
				JSONObject obj = JSON.parseObject(str);
				List<ZxFinanceDetailApi> detailTemp = JSONArray.parseArray(obj.getString("data"), ZxFinanceDetailApi.class);
				result = detailTemp == null || detailTemp.isEmpty() ? null : detailTemp.get(0);
			}
		}
		catch (Exception e)
		{
			logger.warn("IndexEventDetailApi error：" + e);
		}
		return result;
	}

	/**
	 * 从fxgold获取数据并更新到本地数据库
	 * 
	 * @param dates
	 * @return
	 */
	public ApiResult importDataFromFxGold(String... dates)
	{
		ApiResult result = new ApiResult();
		try
		{
			List<ZxFinanceDataApi> apiDatas = new ArrayList<ZxFinanceDataApi>();
			List<ZxFinanceDataApi> apiDatasTmp = null;
			for (String date : dates)
			{
				apiDatasTmp = this.getDataFromFxGold(date);
				if (apiDatasTmp != null && !apiDatasTmp.isEmpty())
				{
					apiDatas.addAll(apiDatasTmp);
				}
			}
			ZxFinanceDetailApi apiDetail = null;
			ZxFinanceData data = null;
			List<ZxFinanceData> newDatas = new ArrayList<ZxFinanceData>();
			Date currDate = new Date();
			Map<String, ZxFinanceDetailApi> detailsCache = new HashMap<String, ZxFinanceDetailApi>();
			for (ZxFinanceDataApi apiData : apiDatas)
			{
				if(detailsCache.containsKey(apiData.getBasicIndexId())){
					apiDetail = detailsCache.get(apiData.getBasicIndexId());
				}else{
					apiDetail = this.getDetailFromFxGold(apiData.getBasicIndexId());
					detailsCache.put(apiData.getBasicIndexId(), apiDetail);
				}
				data = this.findData(apiData.getBasicIndexId(), apiData.getPeriod(), apiData.getYear());
				if (data == null)
				{
					data = ZxFinanceData.refresh(new ZxFinanceData(), apiData, apiDetail);
					data.setCreateDate(currDate);
					data.setUpdateDate(currDate);
					newDatas.add(data);
				}
				else
				{
					data = ZxFinanceData.refresh(data, apiData, apiDetail);
					//数据更新的直接用现有数据更新描述，不需要查询配置信息，因为配置更新的时候会更新所有数据
					data.setDescription(this.getDescription(data)); 
					data.setUpdateDate(currDate);
					dataDao.update(data);
				}
			}
			if (!newDatas.isEmpty())
			{
				//新的数据需要查询配置，如果配置不存在，新加一条，如果存在，用配置数据更新描述数据
				Map<String, ZxFinanceDataCfg> configs = this.getDataConfigs(newDatas);
				List<ZxFinanceDataCfg> newConfigs = new ArrayList<ZxFinanceDataCfg>();
				ZxFinanceDataCfg configTmp = null;
				String description = null;
				for (ZxFinanceData dataTmp : newDatas)
				{
					configTmp = configs.get(dataTmp.getBasicIndexId());
					if (configTmp != null)
					{
						dataTmp.setImportanceLevel(configTmp.getImportanceLevel());
						description = configTmp.getDescription();
						description = description.replaceAll(",", "_U_U_U,") + "_U_U_U";
						dataTmp.setDescription(description);
						dataTmp.setDescription(this.getDescription(dataTmp));
						dataTmp.setValid(configTmp.getValid());
						dataTmp.setDataType(configTmp.getDataType());
					}else{
						dataTmp.setImportanceLevel(this.getDefImportanceLevel(dataTmp)); //默认重要等级
						dataTmp.setDataType(0); //默认数据类型
						dataTmp.setValid(1); //默认有效性
						dataTmp.setDescription(this.getDescription(dataTmp));
						
						//不存在配置，自动新增一个默认配置
						configTmp = new ZxFinanceDataCfg();
						configTmp.setBasicIndexId(dataTmp.getBasicIndexId());
						configTmp.setCountry(dataTmp.getCountry());
						configTmp.setCreateDate(currDate);
						configTmp.setDataType(dataTmp.getDataType());
						configTmp.setDescription("WH_ZX"); //默认是外汇正向
						configTmp.setImportanceLevel(dataTmp.getImportanceLevel());
						configTmp.setName(dataTmp.getName());
						configTmp.setUpdateDate(currDate);
						configTmp.setValid(dataTmp.getValid());
						newConfigs.add(configTmp);
						configs.put(configTmp.getBasicIndexId(), configTmp);
					}
				}
				dataDao.batchAdd(newDatas);
				if (!newConfigs.isEmpty())
				{
					dataDao.batchAdd(newConfigs);
				}
			}
			result.setCode(ResultCode.OK);
		}
		catch (Exception e)
		{
			logger.error("<<importDataFromFxGold()|import data fail！", e);
			result.setCode(ResultCode.FAIL);
			result.setErrorMsg(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 按照财经日历列表，查询配置MAP
	 * @param datas
	 * @return
	 */
	public Map<String, ZxFinanceDataCfg> getDataConfigs(List<ZxFinanceData> datas){
		Map<String, ZxFinanceDataCfg> loc_result = new HashMap<String, ZxFinanceDataCfg>();
		if (datas == null || datas.isEmpty())
		{
			return loc_result;
		}
		Set<String> basicIndexIds = new HashSet<String>();
		for (int i = 0, lenI = datas.size(); i < lenI; i++)
		{
			basicIndexIds.add(datas.get(i).getBasicIndexId());
		}
		Query query = new Query(Criteria.where("_id").in(basicIndexIds));
		List<ZxFinanceDataCfg> configList = dataDao.findList(ZxFinanceDataCfg.class, query);
		if(configList == null || configList.isEmpty()){
			return loc_result;
		}
		for (ZxFinanceDataCfg dataCfg : configList)
		{
			loc_result.put(dataCfg.getBasicIndexId(), dataCfg);
		}
		return loc_result;
	}
	
	/**
	 * 计算默认的重要级别
	 * @param data
	 * @return
	 */
	private int getDefImportanceLevel(ZxFinanceData data){
		int result = 0;
		if (data.getImportance() != null)
		{
			switch (data.getImportance().intValue())
			{
			case 1:
				result = 1;
				break;
			case 2:
				result = Math.random() >= 0.5 ? 2 : 3;
				break;

			case 3:
				result = Math.random() >= 0.5 ? 4 : 5;
				break;

			default:
				break;
			}
		}
		return result;
	}
	
	/**
	 * 描述：默认WH_ZX_U_U_U
	 * 预期影响： 正向，预期值>前值 利多;预期值<前值 利空;预期值=前值 持平;
	 *         反向，预期值>前值 利空;预期值<前值 利多;预期值=前值 持平;
	 *         前值或预期值无效 未知
	 * 实际影响： 正向，公布值>前值 利多;公布值<前值 利空;公布值=前值 持平;
	 *         反向，公布值>前值 利空;公布值<前值 利多;公布值=前值 持平;
	 *         前值或公布值无效 未知
	 * 影响力度： 前值为0，影响度 = |公布值| * 重要级数
	 *         前值不为0，影响度 = |(公布值-前值)/前值| * 重要级数
	 *         影响度[0,20%）LV1;[20%,50%）LV2;[50%,∞）LV3;
	 *         前值或公布值无效 未知
	 * @param data
	 * @return
	 */
	private String getDescription(ZxFinanceData data){
		String description = data.getDescription();
		if (StringUtils.isEmpty(description))
		{
			description = "WH_ZX_U_U_U";//默认是外汇正向
		}
		
		//计算前值、预期值、公布值
		Pattern numPattern = Pattern.compile("^[+-]?\\d+(\\.\\d+)?$");
		Float predictValue = null;	//预期值
		Float lastValue = null;     //前值
		Float value = null;         //公布值
		String valTemp = null; 
		valTemp = data.getPredictValue() == null ? "" : data.getPredictValue();
		valTemp = valTemp.replaceAll("[^0-9\\-\\.]", "");
		if (numPattern.matcher(valTemp).matches())
		{
			predictValue = new Float(valTemp);
		}
		valTemp = data.getLastValue() == null ? "" : data.getLastValue();
		valTemp = valTemp.replaceAll("[^0-9\\-\\.]", "");
		if (numPattern.matcher(valTemp).matches())
		{
			lastValue = new Float(valTemp);
		}
		valTemp = data.getValue() == null ? "" : data.getValue();
		valTemp = valTemp.replaceAll("[^0-9\\-\\.]", "");
		if (numPattern.matcher(valTemp).matches())
		{
			value = new Float(valTemp);
		}
		
		//计算预期影响、实际影响、影响力度
		int comp = 0;
		boolean isZX = false;
		String[] srcArr = description.split(",");
		int lenI = srcArr.length;
		String[] destArr = new String[lenI];
		String[] descs = null;
		for(int i = 0; i < lenI; i++){
			description = srcArr[i];
			descs = description.split("_");
			isZX = "ZX".equals(descs[1]);
			if (lastValue == null)
			{
				descs[2] = "U";
				descs[3] = "U";
				descs[4] = "U";
			}else {
				if (predictValue == null)
				{
					descs[2] = "U";
				}else{
					comp = predictValue.compareTo(lastValue);
					if (comp == 0)
					{
						descs[2] = "FLAT";
					}else if((comp > 0 && isZX) || (comp < 0 && !isZX)){
						descs[2] = "GOOD";
					}else{
						descs[2] = "BAD";
					}
				}
				if (value == null)
				{
					descs[3] = "U";
					descs[4] = "U";
				}else{
					comp = value.compareTo(lastValue);
					if (comp == 0)
					{
						descs[3] = "FLAT";
					}else if((comp > 0 && isZX) || (comp < 0 && !isZX)){
						descs[3] = "GOOD";
					}else{
						descs[3] = "BAD";
					}
					//影响力度
					float rate = lastValue == 0 ? value : ((value - lastValue) / lastValue);
					rate = Math.abs(rate) * data.getImportanceLevel();
					if(rate < 0.2){
						descs[4] = "LV1";
					}else if(rate < 0.5){
						descs[4] = "LV2";
					}else{
						descs[4] = "LV3";
					}
				}
			}
			destArr[i] = StringUtils.join(descs, "_");
		}
		return StringUtils.join(destArr, ",");
	}


	/**
	 * 更新保存点评（财经日历）
	 * @param data
	 * @return
	 */
	public ApiResult saveComments(ZxFinanceData data, ZxFinanceDataComment comment) {
		ApiResult result=new ApiResult();
		if(dataDao.saveComment(data, comment)){
			ZxFinanceData zxData = dataDao.findById(ZxFinanceData.class, data.getDataId());
			chatApiService.zxFinanceReviewNotice(JSON.toJSONString(zxData),JSON.toJSONString(comment));
			result.setCode(ResultCode.OK);
		}else{
			result.setCode(ResultCode.FAIL);
		}
		return result;
	}

	/**
	 * 删除点评(财经日历)
	 * @param dataId
	 * @param id
	 * @return
	 */
	public ApiResult delReview(String dataId, String id){
		ApiResult result=new ApiResult();
		if(dataDao.delReview(dataId, id)){
			result.setCode(ResultCode.OK);
		}else{
			result.setCode(ResultCode.FAIL);
		}
		return result;
	}

}
