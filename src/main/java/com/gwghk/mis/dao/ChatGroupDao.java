package com.gwghk.mis.dao;

import com.gwghk.mis.model.TraninClient;
import com.gwghk.mis.util.JSONHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.gwghk.mis.common.dao.MongoDBBaseDao;
import com.gwghk.mis.model.ChatGroup;
import com.gwghk.mis.model.ChatGroupRule;
import com.mongodb.WriteResult;

/**
 * 聊天室组别DAO
 * @author Alan.wu
 * @date  2015年4月1日
 */
@Repository
public class ChatGroupDao extends MongoDBBaseDao{

	/**
	 * 修改组的内容关联规则
	 */
	public boolean updateGroupRule(ChatGroupRule rule){
		rule.setUpdateDate(new Date());
		rule.setValid(1);
		WriteResult wr=this.mongoTemplate.updateMulti(new Query(Criteria.where("chatRules.id").is(rule.getId())), new Update().set("chatRules.$", rule), ChatGroup.class);
		return wr!=null&&wr.getN()>0;
	}
	
	/**
	 * 删除组的内容关联规则
	 */
	public boolean deleteGroupRule(String[] ids){
	    boolean isSuccess=false;
	    ChatGroupRule rule=null;
		for(String id:ids){
			rule=new ChatGroupRule();
			rule.setId(id);
			WriteResult wr=this.mongoTemplate.updateMulti(new Query(Criteria.where("chatRules.id").is(id)), new Update().pull("chatRules",rule), ChatGroup.class);
			isSuccess=(wr!=null&&wr.getN()>0);
		}
    	return isSuccess;
	}
	
	
	/**
	 * 功能：查询组列表
	 */
	public List<ChatGroup> findGroupList(String systemCategory){
		return this.findList(ChatGroup.class, new Query(Criteria.where("valid").is(1).and("systemCategory").is(systemCategory)));
	}
	
	/**
	 * 功能： 根据组Id数组 --> 查询组列表
	 */
	public List<ChatGroup> findGroupList(Object[] groupIdArr){
		return this.findList(ChatGroup.class, new Query(Criteria.where("id").in(groupIdArr)));
	}
	
	/**
	 * 功能： 根据GroupType--> 查询组列表
	 */
	public List<ChatGroup> findByGroupType(String systemCategory,String groupType){
		return this.findList(ChatGroup.class, new Query(Criteria.where("groupType").is(groupType).and("systemCategory").is(systemCategory)));
	}
	/**
	 * 功能： 根据组Id集合 --> 查询组列表
	 */
	public List<ChatGroup> findGroupList(List<String> groupIdArr){
		return this.findList(ChatGroup.class, new Query(Criteria.where("id").in(groupIdArr)));
	}
	
	/**
	 * 查询规则所在房间
	 * @param ruleId
	 */
	public List<String> getRoomIdByRuleId(String systemCategory,String ruleId){
		if(StringUtils.isNotBlank(ruleId)){
			List<ChatGroup> groupList=this.findList(ChatGroup.class, new Query(Criteria.where("chatRules.id").is(ruleId).and("systemCategory").is(systemCategory)));
			List<String> strList=new ArrayList<String>();
			for(ChatGroup g: groupList){
				strList.add(g.getId());
			}
			return strList;
		}else{
			return null;
		}
	}
}
