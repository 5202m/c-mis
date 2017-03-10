package com.gwghk.mis.service;

import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.dao.ChatPraiseDao;
import com.gwghk.mis.model.ChatPraise;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * 点赞管理相关类
 * Created by Jade.zhu on 2017/3/9.
 */
@Service
public class ChatPraiseService {

  @Autowired
  private ChatPraiseDao chatPraiseDao;

  /**
   * 分页查询点赞数据
   * @param dCriteria
   * @return
   */
  public Page<ChatPraise> getPraisePage(DetachedCriteria<ChatPraise> dCriteria){
    Query query=new Query();
    ChatPraise chatPraise = dCriteria.getSearchModel();
    Criteria criteria = Criteria.where("praiseType").is("user");
    if(chatPraise!=null){
      if(StringUtils.isNotBlank(chatPraise.getPraiseId())){
        criteria.and("praiseId").in(chatPraise.getPraiseId().split(","));
      }
      if(StringUtils.isNotBlank(chatPraise.getFromPlatform())){
        criteria.and("fromPlatform").is(chatPraise.getFromPlatform());
      }
    }
    query.addCriteria(criteria);
    return chatPraiseDao.getPraisePage(query, dCriteria);
  }
}
