package com.gwghk.mis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.dao.ChatPointsDao;
import com.gwghk.mis.enums.ResultCode;
import com.gwghk.mis.model.ChatPoints;
import com.gwghk.mis.model.ChatPointsJournal;

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
public class ChatPointsService {

	@Autowired
	private ChatPointsDao chatPointsDao;
	
	/**
	 * 积分配置列表查询
	 * 
	 * @param dCriteria
	 * @param params
	 * @return
	 */
	public Page<ChatPoints> getChatPoints(DetachedCriteria<ChatPoints> dCriteria, Map<String, Object> params, boolean isExport) {
		ChatPoints chatPoints = dCriteria.getSearchModel();
		Query query = new Query();
		Criteria criteria = Criteria.where("isDeleted").is(0);
		if (chatPoints != null) {
			criteria.and("systemCategory").is(chatPoints.getSystemCategory());
			if (StringUtils.isNotBlank(chatPoints.getGroupType())) {
				criteria.and("groupType").is(chatPoints.getGroupType());
			}
			if (StringUtils.isNotBlank(chatPoints.getUserId())) {
				criteria.and("userId").is(chatPoints.getUserId());
			}
			Object pointsStart = params.get("pointsStart");
			Object pointsEnd = params.get("pointsEnd");
			if(pointsStart != null || pointsEnd != null){
				criteria = criteria.and("points");
				if(pointsStart != null){
					criteria.gte(pointsStart);
				}
				if(pointsEnd != null){
					criteria.lte(pointsEnd);
				}
			}
			
			Criteria journalCriteria = Criteria.where("isDeleted").is(0);

				List<ChatPointsJournal> journals = chatPoints.getJournal();
				if (journals != null && journals.size() == 1) {
					ChatPointsJournal journal = journals.get(0);
					Object type = params.get("type");
					if (StringUtils.isNotBlank(journal.getItem())) {
						journalCriteria.and("item").is(journal.getItem());
					} else if (type != null && "".equals(type) == false) {
						journalCriteria.and("item").regex(type + ".*");
					}
				}
				Object timeStart = params.get("timeStart");
				Object timeEnd = params.get("timeEnd");
				if (timeStart != null || timeEnd != null) {
					journalCriteria = journalCriteria.and("date");
					if (timeStart != null) {
						journalCriteria.gte(timeStart);
					}
					if (timeEnd != null) {
						journalCriteria.lte(timeEnd);
					}
				}
				criteria.and("journal").elemMatch(journalCriteria);

		}
		Field field = query.fields();
		field.include("_id");
		field.include("groupType");
		field.include("userId");
		field.include("pointsGlobal");
		field.include("points");
		if(!isExport) {
			field.include("journal.$");
		}else{
			field.include("journal");
		}
		
		query.addCriteria(criteria);
		
		return chatPointsDao.queryPoints(query, dCriteria);
	}
	
	/**
	 * 根据ID查找
	 * @param pointsId
	 * @return
	 */
	public ChatPoints findById(String pointsId){
		return chatPointsDao.findPoints(pointsId);
	}
	
	/**
	 * 根据ID查找
	 * @param groupType
	 * @param userId
	 * @return
	 */
	public ChatPoints findByUserId(String systemCategory,String groupType, String userId){
		Query query = new Query(Criteria.where("isDeleted").is(0).and("systemCategory").is(systemCategory).and("groupType").is(groupType).and("userId").is(userId));
		return chatPointsDao.findOne(ChatPoints.class, query);
	}
	
	/**
	 * 新增保存
	 * @param chatPoints
	 * @return
	 */
	public ApiResult add(ChatPoints chatPoints, String updater, String updateIp) {
		ApiResult result=new ApiResult();
		List<ChatPointsJournal> journals = chatPoints.getJournal();
		ChatPointsJournal journal = null;
		if(journals != null && journals.size() > 0){
			journal = journals.get(0);
		}
		if(journals.size() > 1){
			chatPoints.setJournal(journals.subList(0, 1));
		}
		if(journal == null || journal.getChange() == null){
			result.setCode(ResultCode.FAIL);
			result.setErrorMsg("积分不能为空！");
			return result;
		}
		Long pointsTmp = journal.getChange().longValue();
		
		ChatPoints pointsDb = null;
		if(StringUtils.isNotBlank(chatPoints.getId())){
			pointsDb = this.findById(chatPoints.getId());
			if(pointsDb == null){
				result.setCode(ResultCode.FAIL);
				result.setErrorMsg("积分信息不存在！");
				return result;
			}
		}else if(StringUtils.isBlank(chatPoints.getGroupType()) || StringUtils.isBlank(chatPoints.getUserId())){
			result.setCode(ResultCode.FAIL);
			result.setErrorMsg("信息不完整！");
			return result;
		}else{
			pointsDb = this.findByUserId(chatPoints.getSystemCategory(),chatPoints.getGroupType(), chatPoints.getUserId());
		}

		if(pointsDb == null){
			result = this.addJournal(true, pointsTmp > 0, chatPoints, journal, updater, new Date(), updateIp);
		}else{
			result = this.addJournal(false, pointsTmp > 0, pointsDb, journal, updater, new Date(), updateIp);
		}
    	return result;
	}
	
	/**
	 * 冲正
	 * @param pointsId
	 * @param journalId
	 * @param updater
	 * @param updateIp
	 * @return
	 */
	public ApiResult reversal(String pointsId, String journalId, String updater, String updateIp){
		ApiResult result=new ApiResult();
		if(StringUtils.isBlank(pointsId) || StringUtils.isBlank(journalId)){
			result.setCode(ResultCode.FAIL);
			result.setErrorMsg("信息不完整！");
			return result;
		}
		ChatPoints pointsDb = this.findById(pointsId);
		if(pointsDb == null){
			result.setCode(ResultCode.FAIL);
			result.setErrorMsg("积分信息不存在！");
			return result;
		}
		List<ChatPointsJournal> journals = pointsDb.getJournal();
		ChatPointsJournal journal = null;
		for(int i = 0, lenI = journals == null ? 0 : journals.size(); i < lenI; i++){
			if(journalId.equals(journals.get(i).getJournalId())){
				journal = journals.get(i);
				break;
			}
		}
		if(journal == null){
			result.setCode(ResultCode.FAIL);
			result.setErrorMsg("积分流水不存在！");
			return result;
		}
		ChatPointsJournal newJournal = new ChatPointsJournal();
		newJournal.setChange(-journal.getChange());
		newJournal.setItem(journal.getItem());
		newJournal.setRemark("冲正：" + journal.getJournalId());
		result = this.addJournal(false, true, pointsDb, newJournal, updater, new Date(), updateIp);
		return result;
	}
	
	/**
	 * 更新客户积分信息
	 * @param isNew 是否新的积分信息
	 * @param changeGlobal 是否影响总积分（普通-积分变化为正时影响总积分， 冲正-直接影响）
	 * @param points 积分信息（isNew=true是一个全新的积分信息， 否则是数据库积分信息的完整对象）
	 * @param journal 待增加的积分流水
	 * @param updater
	 * @param updateDate
	 * @param updateIp
	 * @return
	 */
	private ApiResult addJournal(boolean isNew, boolean changeGlobal, ChatPoints points, ChatPointsJournal journal, String updater, Date updateDate, String updateIp){
		ApiResult result = new ApiResult();
		journal.setJournalId(new ObjectId().toString());
		journal.setDate(updateDate);
		journal.setOpUser(updater);
		journal.setIsDeleted(0);
		
		Long pointsTmp = journal.getChange().longValue();
		
		if(isNew){
			//新加
			if(pointsTmp < 0){
				result.setCode(ResultCode.FAIL);
				result.setErrorMsg("客户积分不足！");
				return result;
			}
			points.setCreateDate(updateDate);
			points.setCreateUser(updater);
			points.setCreateIp(updateIp);
			points.setUpdateDate(updateDate);
			points.setUpdateUser(updater);
			points.setUpdateIp(updateIp);
			points.setIsDeleted(0);
			
			points.setPointsGlobal(pointsTmp);
			points.setPoints(pointsTmp);
			points.setRemark(journal.getRemark());
			journal.setBefore(0L);
			journal.setAfter(pointsTmp);
			List<ChatPointsJournal> journalsNew = new ArrayList<ChatPointsJournal>();
			journalsNew.add(journal);
			points.setJournal(journalsNew);
			chatPointsDao.save(points);
		}else{
			//追加流水
			if(pointsTmp + points.getPoints() < 0){
				result.setCode(ResultCode.FAIL);
				result.setErrorMsg("客户积分不足！");
				return result;
			}
			if (changeGlobal) {
				points.setPointsGlobal(points.getPointsGlobal() + pointsTmp);
			}
			points.setUpdateDate(updateDate);
			points.setUpdateUser(updater);
			points.setUpdateIp(updateIp);
			
			journal.setBefore(points.getPoints());
			points.setPoints(points.getPoints() + pointsTmp);
			journal.setAfter(points.getPoints());
			if(points.getJournal() == null){
				points.setJournal(new ArrayList<ChatPointsJournal>());
			}
			points.getJournal().add(journal);
			chatPointsDao.update(points);
		}
    	return result.setCode(ResultCode.OK);
	}
	
	/**
	 * 删除
	 * @param pointsId
	 * @return
	 */
	public ApiResult delete(String pointsId) {
		ApiResult result=new ApiResult();
		boolean isOk = chatPointsDao.delete(pointsId);
		return result.setCode(isOk ? ResultCode.OK : ResultCode.FAIL);
	}
}
