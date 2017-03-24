package com.gwghk.mis.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
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
import com.gwghk.mis.model.BaseUser;
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
	public ApiResult login(BaseUser userParam,boolean isSuper){
		ApiResult result = new ApiResult();
		Criteria criatira=Criteria.where("status").is(0);
		criatira.and("valid").is(1);
		criatira.and("userNo").is(userParam.getUserNo());
		criatira.and("password").is(MD5.getMd5(WebConstant.MD5_KEY+userParam.getPassword()));
		BaseUser bu = userDao.getBaseUserRow(new Query(criatira),isSuper);
		if(bu == null){      //先是否通过用户名和密码的验证
			result.setMsg(ResultCode.Error1006);
		}else{
			//更新LoginDate、LoginIp、LoginTimes等字段
			bu.setLoginDate(new Date());
			bu.setLoginIp(userParam.getLoginIp());
			bu.setLoginTimes(bu.getLoginTimes() == null ? 1 : (bu.getLoginTimes()+1));
			bu.setUpdateIp(userParam.getUpdateIp());
			bu.setUpdateUser(userParam.getUpdateUser());
			userDao.modifyLoginInfo(bu,isSuper);
			result.setReturnObj(new Object[]{bu});
		}
		return result;
	}

	
	/**
	 * 功能：分页查询用户数据
	 */
	public Page<BoUser> getUserPage(DetachedCriteria<BoUser> dCriteria, String[] roleIds){
		Query query=new Query();
		BoUser boUser=dCriteria.getSearchModel();
		Criteria criteria = Criteria.where("valid").is(1);
		if(boUser!=null){
			if(StringUtils.isNotBlank(boUser.getUserNo())){
				criteria.and("userNo").regex(StringUtil.toFuzzyMatch(boUser.getUserNo()));
			}
			if(StringUtils.isNotBlank(boUser.getUserName())){
				criteria.and("userName").regex(StringUtil.toFuzzyMatch(boUser.getUserName()));
			}
			if(boUser.getStatus()!=null){
				criteria.and("status").is(boUser.getStatus());
			}
			if(ArrayUtils.isNotEmpty(roleIds)){
				if(roleIds.length == 1){
					criteria.and("role.roleId").is(roleIds[0]);
				}else{
					criteria.and("role.roleId").in((Object[])roleIds);
				}
			}
			if(StringUtils.isNotBlank(boUser.getSystemCategory())){
				criteria.and("systemCategory").is(boUser.getSystemCategory());
			}
		}
		query.addCriteria(criteria);
		return userDao.getUserPage(query,dCriteria);
	}
	
	/**
	 * 功能：分页查询用户数据
	 */
	public List<BoUser> getUserList(String systemCategory,DetachedCriteria<BoUser> dCriteria){
		Query query=new Query();
		Criteria criteria = Criteria.where("valid").is(1).and("status").is(0).and("systemCategory").is(systemCategory);
		BoUser boUser=dCriteria == null ? null : dCriteria.getSearchModel();
		if(boUser!=null){
			if(StringUtils.isNotBlank(boUser.getUserNoOrName())){
				criteria.orOperator(Criteria.where("userNo").is(boUser.getUserNoOrName()),Criteria.where("userName").is(boUser.getUserNoOrName()));
			}
		}
		query.addCriteria(criteria);
		return userDao.getUserList(query);
	}
	
	/**
	 * 按照指定的用户编号查询用户列表
	 * @param userNos
	 * @return
	 */
	public List<BoUser> getUserListByNo(String... userNos)
	{
		Query query=new Query();
		Criteria criteria = Criteria.where("valid").is(1);
		if(userNos != null && userNos.length > 0)
		{
			criteria.and("userNo").in((Object[])userNos);
		}
		query.addCriteria(criteria);
		
		return userDao.getUserList(query);
	}
	
	/**
	 * 按照角色模糊查询用户列表
	 * @param roleNo
	 * @return
	 */
	public List<BoUser> getUserListByRole(String systemCategory,String roleNo){
		Query query=new Query();
		Criteria criteria = Criteria.where("valid").is(1).and("systemCategory").is(systemCategory);
		if(StringUtils.isNotBlank(roleNo)){
			criteria.and("role.roleNo").regex(StringUtil.toFuzzyMatch(roleNo));
		}
		query.addCriteria(criteria);
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
    			BoRole newBoRole = new BoRole();
    			if(boRole != null){
    				newBoRole.setRoleId(boRole.getRoleId());
    				newBoRole.setRoleNo(boRole.getRoleNo());
    				newBoRole.setRoleName(boRole.getRoleName());
    				newBoRole.setSystemCategory(boRole.getSystemCategory());
    				boRole = newBoRole;
    			}
        	}
    	}
    	if(isUpdate){
    		BoUser user=userDao.getByUserId(userParam.getUserId());
    		if(user==null){
    			return result.setCode(ResultCode.Error104);
    		}
    		if(userParam.getUpdateUser().equals(userParam.getUserNo())){
    			if(roleId!=null && !roleId.equals(user.getRole().getRoleId())){
    				return result.setCode(ResultCode.Error105);
    			}
    		}
    		if(userDao.isExsitUserNo(userParam.getUserId(),userParam.getUserNo()) 
    			  || userDao.isExsitPhone(userParam.getUserId(),userParam.getTelephone())){
    			return result.setCode(ResultCode.Error102);
    		}
    		BeanUtils.copyExceptNull(user, userParam);
    		if(boRole!=null){
    			user.setRole(boRole);
    		}
    		userDao.update(user);
    	}else{
    		if(userDao.getByUserNo(userParam.getUserNo())!=null || userDao.getByPhone(userParam.getTelephone())!=null){
    			return result.setCode(ResultCode.Error102);
    		}
    		userParam.setPassword(MD5.getMd5(WebConstant.MD5_KEY+userParam.getPassword()));
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
	 */
	public BoUser getUserById(String userId) {
		return userDao.getByUserId(userId);
	}

	/*****
	 * 通过userId查用户信息
	 * @param userId
	 * @param systemCategory 当前登录系统，返回对应系统权限
	 * @return
	 */
	public BoUser getUserById(String userId,String systemCategory) {
		BoUser user = userDao.getByUserId(userId);
		return user;
	}


	/**
	 * 通过userNo查用户信息
	 */
	public BoUser getUserByNo(String userNo) {
		return userDao.getByUserNo(userNo);
	}
	
	/**
	 * 通过userNo查用户信息
	 */
	public BaseUser getBaseUserByNo(String userNo,boolean isSuper) {
		return userDao.getBaseUserRow(Query.query(Criteria.where("userNo").is(userNo)),isSuper);
	}
	/**
	 * 功能：重置密码
	 * @param userId 用户ID
	 * @param newPwd 新密码
	 */
    public ApiResult saveResetPwd(String userId,String newPwd){
    	ApiResult result = new ApiResult();
    	BoUser user = userDao.getByUserId(userId);
    	if(user != null){
    		user.setPassword(MD5.getMd5(WebConstant.MD5_KEY+newPwd));
    		userDao.update(user);
    		return result.setCode(ResultCode.OK);
    	}else{
    		return result.setCode(ResultCode.Error1010);
    	}
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
	
	/**
	 * 
	 * @function:  获取下一个手机号
	 * @param pattern 正则
	 * @return String   返回已处理好的下一个手机号，默认返回 13800138000
	 * @exception 
	 * @author:jade.zhu   
	 * @since  1.0.0
	 */
	public String getNextUserPhone(Pattern pattern){
		String nextPhone = "13800138000";
		BoUser boUser = userDao.getNextUserPhone(pattern);
		if(boUser!=null){
			nextPhone = Long.toString((Long.valueOf(boUser.getTelephone()) + 1));
		}
		return nextPhone;
	}

	/**
	 * 功能：重置密码
	 * @param userId 用户ID
	 * @param liveLink 直播地址
	 */
	public ApiResult saveLiveLinks(String userId, String liveLink){
		ApiResult result = new ApiResult();
		BoUser user = userDao.getByUserId(userId);
		if(user != null){
			user.setLiveLinks(liveLink);
			userDao.update(user);
			return result.setCode(ResultCode.OK);
		}else{
			return result.setCode(ResultCode.Error1010);
		}
	}
}
