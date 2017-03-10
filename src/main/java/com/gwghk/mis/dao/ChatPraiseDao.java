package com.gwghk.mis.dao;

import com.gwghk.mis.common.dao.MongoDBBaseDao;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.model.ChatPraise;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * 点赞Dao类
 * Created by Jade.zhu on 2017/3/9.
 */
@Repository
public class ChatPraiseDao extends MongoDBBaseDao {

  /**
   * 分页查询
   * @param query
   * @param dCriteria
   * @return
   */
  public Page<ChatPraise> getPraisePage(Query query, DetachedCriteria<ChatPraise> dCriteria){
    return super.findPage(ChatPraise.class, query, dCriteria);
  }

}
