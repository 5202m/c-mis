package com.gwghk.mis.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.dao.ChatSubscribeDao;
import com.gwghk.mis.enums.ResultCode;
import com.gwghk.mis.model.ChatSubscribe;
import com.gwghk.mis.util.BeanUtils;

/**
 * 
 * @description:  订阅service类 
 * @fileName:ChatSubscribeService.java 
 * @createTime:2016年8月25日 下午2:04:56  
 * @author:Jade.zhu
 * @version 1.0.0  
 *
 */
@Service
public class ChatSubscribeService {
	
	@Autowired
	private ChatSubscribeDao chatSubscribeDao;
	
	/**
	 * 
	 * @function:  保存订阅
	 * @param subscribe
	 * @param isUpdate
	 * @return ApiResult   
	 * @exception 
	 * @author:Jade.zhu
	 * @since  1.0.0
	 */
	public ApiResult saveSubscribe(ChatSubscribe subscribe, boolean isUpdate){
		ApiResult result=new ApiResult();
		if(isUpdate){
			ChatSubscribe chatSubscribe = chatSubscribeDao.getSubscribeById(subscribe.getId());
			if(chatSubscribe == null){
				return result.setCode(ResultCode.Error104);
			}
			BeanUtils.copyExceptNull(chatSubscribe, subscribe);
			chatSubscribeDao.updateSubscribe(chatSubscribe);
		} else {
			subscribe.setId(null);
			chatSubscribeDao.addSubscribe(subscribe);
		}
		return result.setCode(ResultCode.OK);
	}
	
	/**
	 * 
	 * @function:  获取分页数据
	 * @param dCriteria
	 * @return Page<ChatSubscribe>   
	 * @exception 
	 * @author:Jade.zhu
	 * @since  1.0.0
	 */
	public Page<ChatSubscribe> getSubscribePage(DetachedCriteria<ChatSubscribe> dCriteria){
		Query query=new Query();
		ChatSubscribe subscribe = dCriteria.getSearchModel();
		Criteria criteria = Criteria.where("valid").is(1);
		if(subscribe!=null){
			criteria.and("systemCategory").is(subscribe.getSystemCategory());
			if(StringUtils.isNotBlank(subscribe.getType())){
				criteria.and("type").is(subscribe.getType());
			}
			if(StringUtils.isNotBlank(subscribe.getAnalyst())){
				criteria.and("analyst").regex(subscribe.getAnalyst().replaceAll(",","|"));
			}
			if(StringUtils.isNotBlank(subscribe.getUserId())){
				criteria.and("userId").is(subscribe.getUserId());
			}
			if(subscribe.getStatus() != null && StringUtils.isNotBlank(String.valueOf(subscribe.getStatus()))){
				criteria.and("status").is(subscribe.getStatus());
			}
			if(StringUtils.isNotBlank(subscribe.getGroupType())){
				criteria.and("groupType").is(subscribe.getGroupType());
			}
		}
		query.addCriteria(criteria);
		return chatSubscribeDao.getSubscribePage(query, dCriteria);
	}
	
	/**
	 * 
	 * @function:  批量删除数据
	 * @param subscrbeIds
	 * @return ApiResult   
	 * @exception 
	 * @author:Jade.zhu
	 * @since  1.0.0
	 */
	public ApiResult deleteSubscibe(String[] subscribeIds){
		ApiResult api=new ApiResult();
		boolean isSuccess = chatSubscribeDao.deleteSubscribe(subscribeIds);
		return api.setCode(isSuccess?ResultCode.OK:ResultCode.FAIL);
	}
	
	/**
	 * 
	 * @function:  获取单条数据
	 * @param subscribeId
	 * @return ChatSubscribe   
	 * @exception 
	 * @author:Jade.zhu
	 * @since  1.0.0
	 */
	public ChatSubscribe getSubscribeById(String subscribeId){
		return chatSubscribeDao.getSubscribeById(subscribeId);
	}
	
	/**
	 * 批量更新状态
	 * @param subscribeIds
	 * @param status
	 * @return
	 */
	public ApiResult modifySubscribeStatusByIds(String[] subscribeIds, int status){
		ApiResult api=new ApiResult();
    	boolean isSuccess=chatSubscribeDao.modifySubscribeStatusByIds(subscribeIds, status);
    	return api.setCode(isSuccess?ResultCode.OK:ResultCode.FAIL);
	}
}
