package com.gwghk.mis.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.gwghk.mis.common.dao.MongoDBBaseDao;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.enums.IdSeq;
import com.gwghk.mis.model.BoRole;
import com.gwghk.mis.model.ChatGroup;
import com.mongodb.WriteResult;

/**
 * 角色DAO类
 * @author alan.wu
 * @date 2015/2/5
 */
@Repository
public class RoleDao extends MongoDBBaseDao{
	/**
	 * 根据用户id提取对应角色信息
	 * @param userId
	 * @return
	 */
	public List<BoRole> getRoleListByUserId(String userId){
		return null;
	}
	
	/**
	 * 提取角色信息
	 * @param roleId
	 * @return
	 */
	public BoRole getByRoleId(String roleId){
		return this.findById(BoRole.class, roleId);
	}


	/**
	 * 删除角色
	 * @param roleIds
	 * @return
	 */
	public boolean deleteRole(Object[] roleIds){
		WriteResult wr=this.mongoTemplate.updateMulti(Query.query(Criteria.where("roleId").in(roleIds)), Update.update("valid", 0), BoRole.class);
		return wr!=null&&wr.getN()>0;
	}
	
	/**
	 * 删除聊天室组  删除角色组信息
	 */
	public boolean deleteRoleChatGroup(String[] chatGroupIds){
		boolean isSuccess = false;
		for(String id : chatGroupIds){
			ChatGroup chatGroup = new ChatGroup();
			chatGroup.setId(id);
			WriteResult wr = this.mongoTemplate.updateMulti(new Query(Criteria.where("chatGroupList.id").is(id))
						   ,new Update().pull("chatGroupList",chatGroup), BoRole.class);
			isSuccess = (wr!=null&&wr.getN()>0);
		}
    	return isSuccess;
	}
	
	/**
	 * 修改聊天室组 更新角色组信息
	 */
	public boolean updateRoleChatGroup(ChatGroup chatGroup){
		chatGroup.setUpdateDate(new Date());
		chatGroup.setValid(1);
		WriteResult wr = this.mongoTemplate.updateMulti(new Query(Criteria.where("chatGroupList.id").is(chatGroup.getId()))
					  , new Update().set("chatGroupList.$", chatGroup), BoRole.class);
		return wr!=null&&wr.getN()>0;
	}

	/**
	 * 提取所有角色信息
	 * @param query
	 * @return
	 */
	public List<BoRole> getRoleList(Query query){
		return this.findList(BoRole.class, query);
	}
	
	/**
	 * 新增角色信息
	 * @param role
	 * @return
	 * @throws Exception 
	 */
    public boolean addRole(BoRole role){
    	role.setRoleId(this.getNextSeqId(IdSeq.Role));
    	role.setValid(1);
		this.add(role);
		return true;
	}
    
    /**
	 * 分页查询
	 * @param query
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<BoRole> getRolePage(Query query,DetachedCriteria<BoRole> dCriteria){
		return this.findPage(BoRole.class, query, dCriteria);
	}

	/**
	 * 通过角色编号找记录
	 * @param roleNo
	 * @return
	 */
	public BoRole getByRoleNo(String systemCategory,String roleNo) {
		return this.findOne(BoRole.class,Query.query(Criteria.where("roleNo").is(roleNo).and("systemCategory").is(systemCategory)));
	}

	/****
	 * 根据id查询角色
	 * @param ids
	 * @return
	 */
	public List<BoRole> findByIds(String[] ids){
		Query query = new Query(Criteria.where("roleId").in((Object[])ids));
		return this.findList(BoRole.class,query);
	}
}
