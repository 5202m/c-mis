package com.gwghk.mis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.dao.MenuDao;
import com.gwghk.mis.dao.RoleDao;
import com.gwghk.mis.dao.UserDao;
import com.gwghk.mis.enums.ResultCode;
import com.gwghk.mis.model.BoRole;
import com.gwghk.mis.model.ChatGroup;
import com.gwghk.mis.util.BeanUtils;

/**
 * 摘要：角色管理相关Service
 * @author Alan.wu
 * @date   2015年2月4日
 */
@Service
public class RoleService{
	
	@Autowired
	private RoleDao roleDao;

	@Autowired
	private MenuDao menuDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ChatGroupService chatGroupService;
	
	@Autowired
	private  HttpServletRequest request;
	
	/**
	 * 角色分页查询
	 * @param dCriteria
	 * @return
	 */
	public Page<BoRole> getRolePage(DetachedCriteria<BoRole> dCriteria) {
		BoRole role=dCriteria.getSearchModel();
		Query query=new Query();
		if(role!=null){
			Criteria criteria=new Criteria();
			criteria.and("valid").is(1);
			if(StringUtils.isNotBlank(role.getRoleNo())){
				criteria.and("roleNo").is(role.getRoleNo());
			}
			criteria.and("systemCategory").is(role.getSystemCategory());
			if(StringUtils.isNotBlank(role.getRoleName())){
				criteria.and("roleName").is(role.getRoleName());
			}
			if(StringUtils.isNotBlank(role.getRoleNoOrName())){
				criteria.orOperator(Criteria.where("roleName").is(role.getRoleNoOrName()),Criteria.where("roleNo").is(role.getRoleNoOrName()));
			}
			query.addCriteria(criteria);
		}
		return roleDao.getRolePage(query, dCriteria);
	}

	/**
	 * 通过角色id找对应记录
	 * @param id
	 * @return
	 */
	public BoRole getByRoleId(String roleId) {
		return roleDao.getByRoleId(roleId);
	}

	/**
	 * 保存角色
	 * @param roleParam
	 * @param b
	 * @return
	 */
	public ApiResult saveRole(BoRole roleParam, boolean isUpdate) {
		ApiResult result=new ApiResult();
		roleParam.setValid(1);
    	if(isUpdate){
    		BoRole role=roleDao.getByRoleId(roleParam.getRoleId());
    		BeanUtils.copyExceptNull(role, roleParam);
    		roleDao.update(role);
    	}else{
    		if(roleDao.getByRoleNo(roleParam.getSystemCategory(),roleParam.getRoleNo())!=null){
    			return result.setCode(ResultCode.Error102);
    		}
    		roleDao.addRole(roleParam);	
    	}
    	return result.setCode(ResultCode.OK);
	}
	
	/**
	 * 删除角色
	 * @param ids
	 * @return
	 */
	public ApiResult deleteRole(String[] roleIds) {
		ApiResult api=new ApiResult();
		if(roleDao.deleteRole(roleIds)){
		    for(String roleId:roleIds){
		    	menuDao.deleteMenuRole(null, roleId);
		    }
			userDao.deleteUserRole(roleIds);
			return api.setCode(ResultCode.OK);
		}else{
			return api.setCode(ResultCode.FAIL);
		}
	}

	/**
     * 获取角色对应的聊天室
     */
    public Map<String,Object> getRoleChatGroupRelation(String systemCategory,String roleId){
    	Map<String,Object> map = new HashMap<String, Object>();
    	List<ChatGroup> relationChatGroupList = roleDao.getByRoleId(roleId).getChatGroupList();   //关联的聊天室
		map.put("relationChatGroupList", relationChatGroupList);
		map.put("unRelationChatGroupList",chatGroupService.getUnRelationRoleChatGroup(systemCategory,relationChatGroupList)); //非关联的聊天室
		return map;
    }
    
    /**
     * 保存角色-聊天室关联信息
     */
    public ApiResult saveRoleChatGroup(String[] selectGroupIdArr,String roleId){
    	ApiResult api = new ApiResult();
    	if(selectGroupIdArr != null && selectGroupIdArr.length > 0){
    		List<ChatGroup> selectGroupList = chatGroupService.findGroupList(selectGroupIdArr);
    		BoRole role = roleDao.getByRoleId(roleId);
    		List<ChatGroup> selectGroupListTmp=new ArrayList<ChatGroup>();
    		ChatGroup cgTmp=null;
    	    for(ChatGroup cg:selectGroupList){
    	    	cgTmp=new ChatGroup();
    	    	cgTmp.setId(cg.getId());
    	    	cgTmp.setGroupType(cg.getGroupType());
    	    	cgTmp.setName(cg.getName());
    	    	selectGroupListTmp.add(cgTmp);
    	    }
    		role.setChatGroupList(selectGroupListTmp);
    		role.setUpdateDate(new Date());
    		roleDao.update(role);
    	}else{
    		BoRole role = roleDao.getByRoleId(roleId);
    		role.setChatGroupList(null);
    		role.setUpdateDate(new Date());
    		roleDao.update(role);
    	}
    	return api.setCode(ResultCode.OK);
    }
	
	/**
	 * 提取所有角色信息
	 * @return
	 */
	public List<BoRole> getRoleList(String systemCategory) {
		Criteria criteria = new Criteria();
		criteria.and("valid").is(1)
				.and("systemCategory").is(systemCategory);
		return roleDao.getRoleList(Query.query(criteria));
	}
	
	/**
	 * 提取所有分析师角色信息
	 * @return
	 */
	public List<BoRole> getAnalystRoleList(String systemCategory) {
		return roleDao.getRoleList(Query.query(Criteria.where("valid").is(1).and("roleNo").regex("analyst").and("systemCategory").is(systemCategory)));
	}
	
	/**
	 * 提取聊天室关联的角色列表
	 * @param groupId
	 * @return
	 */
	public List<BoRole> getRoleListByChatGroup(String groupId) {
		Criteria criteria = Criteria.where("valid").is(1);
		criteria.and("chatGroupList._id").is(groupId);
		Query query=new Query(criteria);
		return roleDao.findList(BoRole.class, query);
	}
}
