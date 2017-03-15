package com.gwghk.mis.dao;

import com.gwghk.mis.common.dao.MongoDBBaseDao;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.model.ChatPraise;
import com.mongodb.WriteResult;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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

  /**
   * 更新点赞数
   * @param chatPraise
   */
  public boolean modifyPraiseNum(ChatPraise chatPraise){
    WriteResult wr = this.mongoTemplate.updateMulti(Query.query(Criteria.where("_id").is(chatPraise.getId()))
        , Update.update("praiseNum", chatPraise.getPraiseNum()), ChatPraise.class);
    return wr != null && wr.getN() > 0;
  }


  /**
   *
   * @function:  获取单个订阅数据
   * @param praiseId
   * @return ChatPraise
   * @exception
   * @author:Jade.zhu
   * @since  1.0.0
   */
  public ChatPraise getPraiseById(String praiseId){
    return this.findById(ChatPraise.class, praiseId);
  }

}
