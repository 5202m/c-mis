/**   
 * @projectName:pm_mis  
 * @packageName:com.gwghk.mis.service  
 * @className:ChatSubscribeTypeService.java  
 *   
 * @createTime:2016年8月31日-下午1:26:00  
 *  
 *    
 */
package com.gwghk.mis.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.dao.ChatSubscribeTypeDao;
import com.gwghk.mis.enums.ResultCode;
import com.gwghk.mis.model.ChatSubscribeType;
import com.gwghk.mis.util.BeanUtils;

/**   
 * @description:   订阅配置Service类
 * @fileName:ChatSubscribeTypeService.java 
 * @createTime:2016年8月31日 下午1:26:00  
 * @author:Jade.zhu
 * @version 1.0.0  
 *    
 */
@Service
public class ChatSubscribeTypeService {

	@Autowired
	private ChatSubscribeTypeDao chatSubscribeTypeDao;
	
	/**
	 * 
	 * @function:  保存更新订阅配置
	 * @param subscribeType
	 * @param isUpdate
	 * @return ApiResult   
	 * @exception 
	 * @author:Jade.zhu
	 * @since  1.0.0
	 */
	public ApiResult saveSubscribeType(ChatSubscribeType subscribeType, boolean isUpdate){
		ApiResult result=new ApiResult();
		if(subscribeType == null){
			return result.setCode(ResultCode.Error104);
		}
		if(isUpdate){
			ChatSubscribeType chatSubscribeType = chatSubscribeTypeDao.getSubscribeTypeById(subscribeType.getId());
			BeanUtils.copyExceptNull(chatSubscribeType, subscribeType);
			chatSubscribeTypeDao.updateSubscribeType(chatSubscribeType);
		} else {
			subscribeType.setId(null);
			chatSubscribeTypeDao.addSubscribe(subscribeType);
		}
		return result.setCode(ResultCode.OK);
	}
	
	/**
	 * 
	 * @function:  获取订阅配置列表
	 * @param dCriteria
	 * @return Page<ChatSubscribeType>   
	 * @exception 
	 * @author:Jade.zhu
	 * @since  1.0.0
	 */
	public Page<ChatSubscribeType> getSubscribeTypePage(DetachedCriteria<ChatSubscribeType> dCriteria){
		Query query=new Query();
		ChatSubscribeType subscribeType = dCriteria.getSearchModel();
		Criteria criteria = Criteria.where("valid").is(1);
		if(subscribeType!=null){
			criteria.and("systemCategory").is(subscribeType.getSystemCategory());
			if(StringUtils.isNotBlank(subscribeType.getGroupType())){
				criteria.and("groupType").is(subscribeType.getGroupType());
			}
			if(StringUtils.isNotBlank(subscribeType.getAnalysts())){
				criteria.and("analysts").regex(subscribeType.getAnalysts().replaceAll(",","|"));
			}
			if(StringUtils.isNotBlank(subscribeType.getName())){
				criteria.and("name").is(subscribeType.getName());
			}
			if(subscribeType.getStatus() != null && StringUtils.isNotBlank(String.valueOf(subscribeType.getStatus()))){
				criteria.and("status").is(subscribeType.getStatus());
			}
		}
		query.addCriteria(criteria);
		return chatSubscribeTypeDao.getSubscribeTypePage(query, dCriteria);
	}
	
	/**
	 * 
	 * @function:  删除订阅配置
	 * @param subscribeTypeIds
	 * @return ApiResult   
	 * @exception 
	 * @author:Jade.zhu
	 * @since  1.0.0
	 */
	public ApiResult deleteSubscibeType(String[] subscribeTypeIds){
		ApiResult api=new ApiResult();
		boolean isSuccess = chatSubscribeTypeDao.deleteSubscribeType(subscribeTypeIds);
		return api.setCode(isSuccess?ResultCode.OK:ResultCode.FAIL);
	}
	
	/**
	 * 
	 * @function:  获取单条订阅配置
	 * @param subscribeTypeId
	 * @return ChatSubscribeType   
	 * @exception 
	 * @author:Jade.zhu
	 * @since  1.0.0
	 */
	public ChatSubscribeType getSubscribeTypeById(String subscribeTypeId){
		return chatSubscribeTypeDao.getSubscribeTypeById(subscribeTypeId);
	}
	
	/**
	 * 
	 * @function:  更新订阅配置状态
	 * @param subscribeTypeIds
	 * @param status
	 * @return ApiResult   
	 * @exception 
	 * @author:Jade.zhu
	 * @since  1.0.0
	 */
	public ApiResult modifySubscribeTypeStatusByIds(String[] subscribeTypeIds, int status){
		ApiResult api=new ApiResult();
    	boolean isSuccess=chatSubscribeTypeDao.modifySubscribeTypeStatusByIds(subscribeTypeIds, status);
    	return api.setCode(isSuccess?ResultCode.OK:ResultCode.FAIL);
	}
	
	/**
	 * 
	 * @function:  查找所有订阅服务类型
	 * @return List<ChatSubscribeType>   
	 * @exception 
	 * @author:Jade.zhu
	 * @since  1.0.0
	 */
	public List<ChatSubscribeType> getSubscribeType(String systemCategory){
		Query query=new Query();
		Criteria criteria = Criteria.where("valid").is(1);
		criteria.and("status").is(1).and("systemCategory").is(systemCategory);
		query.addCriteria(criteria);
		return chatSubscribeTypeDao.getSubscribeType(query);
	}
}
