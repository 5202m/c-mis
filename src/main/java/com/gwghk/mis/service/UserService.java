package com.gwghk.mis.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.gwghk.mis.authority.SystemServiceLog;
import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.dao.RoleDao;
import com.gwghk.mis.dao.UserDao;
import com.gwghk.mis.enums.ResultCode;
import com.gwghk.mis.model.BoRole;
import com.gwghk.mis.model.BoUser;
import com.gwghk.mis.util.BeanUtils;
import com.gwghk.mis.util.MD5;
import com.gwghk.mis.util.StringUtil;

/**
 * 摘要：用户管理相关Service
 * @author Alan.wu
 * @date   2015年2月4日
 */
@Service
public class UserService{

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;
	
	/**
	 * 功能：登录验证
	 */
	@SystemServiceLog(description = "登录验证")
	public ApiResult login(BoUser userParam){
		ApiResult result = new ApiResult();
		Criteria criatira=new Criteria();
		criatira.andOperator(Criteria.where("userNo").is(userParam.getUserNo()),
				Criteria.where("password").is(MD5.getMd5(WebConstant.MD5_KEY+userParam.getPassword())));
		List<BoUser> userList = userDao.getUserList(new Query(criatira));
		if(userList == null || userList.size() == 0){      //先是否通过用户名和密码的验证
			result.setMsg(ResultCode.Error1006);
		}else{
			BoUser bu = userList.get(0);
			//更新LoginDate、LoginIp、LoginTimes等字段
			bu.setLoginDate(new Date());
			bu.setLoginIp(userParam.getLoginIp());
			bu.setLoginTimes(bu.getLoginTimes() == null ? 1 : (bu.getLoginTimes()+1));
			bu.setUpdateIp(userParam.getUpdateIp());
			bu.setUpdateUser(userParam.getUpdateUser());
			userDao.update(bu);
			result.setReturnObj(new Object[]{bu});
		}
		return result;
	}
	
	/**
	 * 功能：分页查询用户数据
	 */
	public Page<BoUser> getUserPage(DetachedCriteria<BoUser> dCriteria){
		Query query=new Query();
		BoUser boUser=dCriteria.getSearchModel();
		if(boUser!=null){
			Criteria criteria=new Criteria();
			criteria.and("valid").is(1);
			if(StringUtils.isNotBlank(boUser.getUserNo())){
				criteria.and("userNo").regex(StringUtil.toFuzzyMatch(boUser.getUserNo()));
			}
			if(StringUtils.isNotBlank(boUser.getUserName())){
				criteria.and("userName").regex(StringUtil.toFuzzyMatch(boUser.getUserName()));
			}
			if(boUser.getStatus()!=null){
				criteria.and("status").is(boUser.getStatus());
			}
			if(StringUtils.isNotBlank(boUser.getRole().getRoleId())){
				criteria.and("role.roleId").is(boUser.getRole().getRoleId());
			}
			query.addCriteria(criteria);
		}
		return userDao.getUserPage(query,dCriteria);
	}
	
	/**
	 * 功能：分页查询用户数据
	 */
	public List<BoUser> getUserList(DetachedCriteria<BoUser> dCriteria){
		Query query=new Query();
		BoUser boUser=dCriteria.getSearchModel();
		if(boUser!=null){
			Criteria criteria=new Criteria();
			if(StringUtils.isNotBlank(boUser.getUserNoOrName())){
				criteria.orOperator(Criteria.where("userNo").is(boUser.getUserNoOrName()),Criteria.where("userName").is(boUser.getUserNoOrName()));
			}
			query.addCriteria(criteria);
		}
		return userDao.getUserList(query);
	}
	
	/**
	 * 新增用户信息
	 * @param userParam
	 * @param boolean isUpdate 是否更新，true为更新，false为插入
	 * @return
	 * @throws Exception 
	 */
    public ApiResult saveUser(BoUser userParam,boolean isUpdate){
    	userParam.setValid(1);
    	BoRole boRole=userParam.getRole();
    	String roleId=null;
    	ApiResult result=new ApiResult();
    	if(boRole!=null){
    		roleId=boRole.getRoleId();
    		if(StringUtils.isNotBlank(roleId)){
    			boRole=roleDao.getByRoleId(roleId);
        	}
    	}
    	if(isUpdate){
    		BoUser user=userDao.getByUserId(userParam.getUserId());
    		if(user==null){
    			return result.setCode(ResultCode.Error104);
    		}
    		BeanUtils.copyExceptNull(user, userParam);
    		if(boRole!=null){
    			user.setRole(boRole);
    		}
    		userDao.update(user);
    	}else{
    		if(userDao.getByUserNo(userParam.getUserNo())!=null){
    			return result.setCode(ResultCode.Error102);
    		}
    		userParam.setPassword(MD5.getMd5(WebConstant.MD5_KEY+WebConstant.DEFAULT_PWD));
    		if(boRole!=null){
    			userParam.setRole(boRole);
    		}
    		userDao.addUser(userParam);	
    	}
    	return result.setCode(ResultCode.OK);
    }
	
	/**
	 * 删除用户信息
	 * @param userIds
	 * @return
	 */
    public ApiResult deleteUser(String[] userIds){
    	ApiResult api=new ApiResult();
    	boolean isSuccess=userDao.deleteUser(userIds);
    	return api.setCode(isSuccess?ResultCode.OK:ResultCode.FAIL);
    }
    
    /**
	 * 取用户角色的关系信息
	 * @param roleId
	 * @return
	 */
	public Map<String,Object> getUserRoleRelation(String roleId){
		Map<String,Object> map=new HashMap<String, Object>();
		//查询关联用户
		map.put("relation", userDao.getRelationList(roleId));
		//查询非关联用户
		map.put("unRelation",userDao.getUnRelationList());
		return map;
	}

	/**
	 * 用户角色分配
	 * @param userIds
	 * @param roleId
	 * @return
	 */
	public ApiResult setUserRole(String[] userIds, String roleId) {
		ApiResult apiResult=new ApiResult();
		if(userIds==null){
			return apiResult.setCode(userDao.deleteUserRole(new String[]{roleId})?ResultCode.OK:ResultCode.FAIL);
		}else{
			boolean result=userDao.addUserRole(userIds, roleId);//对输入的用户赋予角色权限
			return apiResult.setCode(result?ResultCode.OK:ResultCode.FAIL);
		}
	}

	/**
	 * 通过userId查用户信息
	 * @param userId
	 * @return
	 */
	public BoUser getUserById(String userId) {
		return userDao.getByUserId(userId);
	}
	
	/**
	 * 功能：保存修改的密码
	 * @param userNo  用户no
	 * @param oldPwd  旧密码
	 * @param newPwd  新密码
	 */
	public ApiResult saveChangePwd(String userNo,String oldPwd,String newPwd){
		ApiResult result = new ApiResult();
		List<BoUser> userList = userDao.getUserList(new Query(new Criteria()
							  .andOperator(Criteria.where("userNo").is(userNo),Criteria.where("password")
							  .is(MD5.getMd5(WebConstant.MD5_KEY+oldPwd)))));
		if(null == userList || userList.size() == 0){
			return result.setCode(ResultCode.Error1010);
		}else{
			BoUser bu = userList.get(0);
			bu.setPassword(MD5.getMd5(WebConstant.MD5_KEY+newPwd));
			userDao.update(bu);
			return result.setCode(ResultCode.OK);
		}
	}
}
