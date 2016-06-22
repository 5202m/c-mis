package com.gwghk.mis.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.gwghk.mis.common.dao.MongoDBBaseDao;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.model.BoUser;
import com.gwghk.mis.model.ChatGroup;
import com.gwghk.mis.model.ChatShowTrade;
import com.gwghk.mis.model.TokenAccess;
import com.gwghk.mis.util.StringUtil;
import com.mongodb.WriteResult;

/**
 * 用户DAO类
 * @author henry.cao
 * @date 2016/6/22
 */
@Repository
public class ChatShowTradeDao extends MongoDBBaseDao{

    public boolean addTrade(ChatShowTrade chatShowTrade){
		this.add(chatShowTrade);
		return true;
	}
    /**
	 * 查询列表
	 * @return
	 */
	public Page<ChatShowTrade> getShowTradePage(Query query,DetachedCriteria<ChatShowTrade> dCriteria){
		return super.findPage(ChatShowTrade.class, query, dCriteria);
	}
	public boolean deleteTrade(Object[] tradeIds){
		
		WriteResult wr = this.mongoTemplate.updateMulti(Query.query(Criteria.where("_id").in(tradeIds))
				   , Update.update("valid", 0), ChatShowTrade.class);
		return wr != null && wr.getN() > 0;
	}
	public ChatShowTrade getTradeById(String tradeId){
		return this.findById(ChatShowTrade.class, tradeId);
	}
	public void updateTrade(ChatShowTrade chatShowTrade){
		Query query = new Query();
	    query.addCriteria(Criteria.where("id").is(chatShowTrade.getId()));
	    this.update(query, chatShowTrade);
	}
	/**
	 * 同步用户数据
	 * @param boUser
	 * @return
	 */
	public boolean updateTradeByBoUser(BoUser boUser){
	    WriteResult wr=this.mongoTemplate.updateMulti(new Query(Criteria.where("boUser.userNo").is(boUser.getUserNo())), new Update()
	    		.set("boUser.avatar", boUser.getAvatar())
	    		.set("boUser.userNo", boUser.getUserNo())
	    		.set("boUser.userName", boUser.getUserName())
	    		.set("boUser.wechatCode", boUser.getWechatCode())
	    		.set("boUser.winRate", boUser.getWinRate()),ChatShowTrade.class);
	    
		return wr!=null&&wr.getN()>0;
	}
}
