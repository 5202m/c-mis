package com.gwghk.mis.service;


import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.dao.ChatShowTradeDao;
import com.gwghk.mis.enums.ResultCode;
import com.gwghk.mis.model.BoUser;
import com.gwghk.mis.model.ChatShowTrade;
import com.gwghk.mis.util.BeanUtils;
import com.gwghk.mis.util.JSONHelper;

/**
 * 晒单管理服务类
 * 
 * @author Henry.cao
 * @date 2016年6月22日
 */
@Service
public class ChatShowTradeService {

  @Autowired
  private ChatShowTradeDao chatShowTradeDao;

  @Autowired
  private UserService userService;

  @Autowired
  private ChatApiService chatApiService;

  /**
   * 新赠和更新
   * 
   * @param showTrade
   * @param isUpdate
   * @return
   */
  public ApiResult saveTrade(ChatShowTrade showTrade, boolean isUpdate) {
    ApiResult result = new ApiResult();
    /**
     * 新赠和修改(更改分析师)
     */
    if (showTrade.getTradeType() == 1) {
      BoUser user = userService.getUserByNo(showTrade.getBoUser().getUserNo());
      if (user == null) {
        return result.setCode(ResultCode.Error104);
      }
      BoUser newUser = new BoUser();
      newUser.setAvatar(user.getAvatar());
      newUser.setUserName(user.getUserName());
      newUser.setUserNo(user.getUserNo());
      newUser.setWechatCode(user.getWechatCode());
      newUser.setWechatCodeImg(user.getWechatCodeImg());
      newUser.setWinRate(user.getWinRate());

      showTrade.setBoUser(newUser);
    }
    showTrade.setValid(1);

    if (isUpdate) {
      ChatShowTrade trade = chatShowTradeDao.getById(showTrade.getId());
      if (trade == null) {
        return result.setCode(ResultCode.Error104);
      }
      BeanUtils.copyExceptNull(trade, showTrade);
      chatShowTradeDao.updateTrade(trade);
      if (showTrade.getStatus() == 1 && showTrade.getTradeType() == 2) {
        List<ChatShowTrade> ChatShowTradeList =
            chatShowTradeDao.findList(ChatShowTrade.class,
                Query.query(Criteria.where("_id").in(showTrade.getId())));
        String tradeInfo = JSONHelper.toJSONString(ChatShowTradeList);
        chatApiService.showTradeNotice(tradeInfo, trade.getSystemCategory());
      }
    } else {
      showTrade.setId(null);
      showTrade.setShowDate(new Date());
      chatShowTradeDao.addTrade(showTrade);
    }
    return result.setCode(ResultCode.OK);
  }

  /**
   * 功能：分页查询晒单数据
   */
  public Page<ChatShowTrade> getShowTradePage(DetachedCriteria<ChatShowTrade> dCriteria) {
    Query query = new Query();
    ChatShowTrade chatShowTrade = dCriteria.getSearchModel();
    Criteria criteria = Criteria.where("valid").is(1);
    if (chatShowTrade != null) {
      criteria.and("systemCategory").is(chatShowTrade.getSystemCategory());
      if (StringUtils.isNotBlank(chatShowTrade.getGroupType())) {
        criteria.and("groupType").is(chatShowTrade.getGroupType());
      }
      if (chatShowTrade.getBoUser() != null
          && StringUtils.isNotBlank(chatShowTrade.getBoUser().getUserNo())) {
        criteria.and("boUser.userNo").is(chatShowTrade.getBoUser().getUserNo());
      }
      if (chatShowTrade.getBoUser() != null
          && StringUtils.isNotBlank(chatShowTrade.getBoUser().getUserName())) {
        criteria.and("boUser.userName").is(chatShowTrade.getBoUser().getUserName());
      }
      if (chatShowTrade.getStatus() != null
          && StringUtils.isNotBlank(String.valueOf(chatShowTrade.getStatus()))) {
        criteria.and("status").is(chatShowTrade.getStatus());
      }
      if (chatShowTrade.getTradeType() != null && chatShowTrade.getTradeType() > 0) {
        criteria.and("tradeType").is(chatShowTrade.getTradeType());
      }
      if(chatShowTrade.getIsAccord() != null && chatShowTrade.getIsAccord() > -1) {
        criteria.and("isAccord").is(chatShowTrade.getIsAccord());
      }
    }
    query.addCriteria(criteria);
    return chatShowTradeDao.getShowTradePage(query, dCriteria);
  }

  /**
   * 删除
   * 
   * @param tradeIds
   * @return
   */
  public ApiResult deleteTrade(String[] tradeIds) {
    ApiResult api = new ApiResult();
    boolean isSuccess = chatShowTradeDao.deleteTrade(tradeIds);
    return api.setCode(isSuccess ? ResultCode.OK : ResultCode.FAIL);
  }

  /**
   * 获取数据
   * 
   * @param tradeId
   * @return
   */
  public ChatShowTrade getTradeById(String tradeId) {
    return chatShowTradeDao.getTradeById(tradeId);
  }

  /**
   * 被动同步更新用户明细
   * 
   * @param user
   * @return
   */
  public ApiResult asyncTradeByBoUser(BoUser user) {
    ApiResult result = new ApiResult();
    return result.setCode(chatShowTradeDao.updateTradeByBoUser(user) ? ResultCode.OK
        : ResultCode.FAIL);
  }

  /**
   * 批量更新状态
   * 
   * @param tradeIds
   * @param status
   * @return
   */
  public ApiResult modifyTradeStatusByIds(String[] tradeIds, int status, String systemCategory) {
    ApiResult api = new ApiResult();
    boolean isSuccess = chatShowTradeDao.modifyTradeStatusByIds(tradeIds, status);
    if (isSuccess && status == 1) {
      List<ChatShowTrade> ChatShowTradeList =
          chatShowTradeDao.findList(ChatShowTrade.class,
              Query.query(Criteria.where("_id").in((Object[]) tradeIds)));
      String tradeInfo = JSONHelper.toJSONString(ChatShowTradeList);
      chatApiService.showTradeNotice(tradeInfo, systemCategory);
    }
    return api.setCode(isSuccess ? ResultCode.OK : ResultCode.FAIL);
  }

	/**
	 * 删除评论
	 * @param sid
	 * @param cid
	 * @return
	 */
	public ApiResult delComment(String sid, String cid){
		ApiResult result=new ApiResult();
		if(chatShowTradeDao.delComment(sid, cid)){
			result.setCode(ResultCode.OK);
		}else{
			result.setCode(ResultCode.FAIL);
		}
		return result;
	}

  /**
   * 批量更新晒单盖楼状态
   *
   * @param tradeIds
   * @param isAccord
   * @return
   */
  public ApiResult modifyTradeIsAccordByIds(String[] tradeIds, int isAccord) {
    ApiResult api = new ApiResult();
    boolean isSuccess = chatShowTradeDao.modifyTradeIsAccordByIds(tradeIds, isAccord);
    return api.setCode(isSuccess ? ResultCode.OK : ResultCode.FAIL);
  }

}
