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
import com.gwghk.mis.dao.MemberDao;
import com.gwghk.mis.enums.ResultCode;
import com.gwghk.mis.model.ChatRoom;
import com.gwghk.mis.model.ChatUserGroup;
import com.gwghk.mis.model.Member;
import com.gwghk.mis.util.BeanUtils;
import com.gwghk.mis.util.StringUtil;

/**
 * 摘要：会员 Service实现
 * @author Gavin.guo
 * @date   2015年3月16日
 */
@Service
public class MemberService{
	
	@Autowired
	private MemberDao memberDao;

	/**
	 * 功能：会员分页查询
	 */
	public Page<Member> getMemberPage(DetachedCriteria<Member> dCriteria) {
		Member member = dCriteria.getSearchModel();
		Query query=new Query();
		if(member != null){
			Criteria criteria = new Criteria();
			criteria.and("systemCategory").is(member.getSystemCategory());
			if(StringUtils.isNotBlank(member.getMobilePhone())){
				criteria.and("mobilePhone").regex(StringUtil.toFuzzyMatch(member.getMobilePhone()));
			}
			if(member.getStatus()!=null){
				criteria.and("status").is(member.getStatus());
			}
			criteria.and("valid").is(1);
			query.addCriteria(criteria);
		}
		return memberDao.getMemberPage(query,dCriteria);
	}

	/**
	 * 功能：根据Id-->获取会员
	 */
	public Member getByMemberId(String memberId){
		return memberDao.getByMemberId(memberId);
	}
	
	/**
	 * 功能：根据mobilePhone-->获取会员
	 */
	public Member getByMobilePhone(String systemCategory,String mobilePhone){
		return memberDao.getByMemberMobilePhone(systemCategory,mobilePhone);
	}


	/**
	 * 按照指定的用户编号查询用户列表
	 * @param userNos
	 * @return
	 */
	public List<Member> getMemberListByMobiles(String systemCategory,String[] mobiles, String groupType)
	{
		Query query=new Query();
		Criteria criteria = Criteria.where("valid").is(1);
		criteria.and("systemCategory").is(systemCategory);
		if(mobiles != null && mobiles.length > 0)
		{
			criteria.and("mobilePhone").in((Object[])mobiles);
		}
		criteria.and("loginPlatform.chatUserGroup._id").is(groupType);
		query.addCriteria(criteria);
		return memberDao.findListInclude(Member.class, query, "mobilePhone", "loginPlatform.chatUserGroup.$");
	}
	
	/**
	 * 保存:修改或者新增，如果memberId为null,为新增，否则为修改
	 * @param member
	 * @return
	 */
	public boolean save(Member member){
		return memberDao.save(member);
	}
	
	/**
	 * 功能：获取后台会员
	 */
	public List<Member> getBackMember(){
		return memberDao.getBackMember();
	}
	

	/**
	 * 功能：保存会员
	 */
	public ApiResult saveMember(Member memberParam, boolean isUpdate) {
		ApiResult result=new ApiResult();
    	if(isUpdate){
    		Member member = memberDao.getByMemberId(memberParam.getMemberId());
    		BeanUtils.copyExceptNull(member, memberParam);
    		memberDao.update(member);
    	}else{
    		if(memberDao.getByMemberMobilePhone(memberParam.getSystemCategory(),memberParam.getMobilePhone())!=null){
    			return result.setCode(ResultCode.Error102);
    		}
    		memberDao.addMember(memberParam);
    	}
    	return result.setCode(ResultCode.OK);
	}
	
	/**
	 * 功能：重置密码
	 */
	public ApiResult resetPmAppPassword(Member memberParam){
		Member member = memberDao.getByMemberId(memberParam.getMemberId());
		//member.getLoginPlatform().getPmApp().setPwd(MD5.getMd5(memberParam.getLoginPlatform().getPmApp().getPwd()));
		memberDao.update(member);
		return new ApiResult().setCode(ResultCode.OK);
	}
	
	/**
	 * 功能：删除会员
	 */
	public ApiResult deleteMember(String[] MemberIds) {
		ApiResult api=new ApiResult();
		if(memberDao.deleteMember(MemberIds)){
			return api.setCode(ResultCode.OK);
		}else{
			return api.setCode(ResultCode.FAIL);
		}
	}

	/**
	 * 功能：设置禁言
	 */
	public ApiResult saveUserGag(String groupType,String memberId,String groupId,String gagDate,String gagTips,String remark){
		boolean isOk=memberDao.setUserGag(groupType,memberId, groupId, gagDate, gagTips, remark);
		return new ApiResult().setCode(isOk?ResultCode.OK:ResultCode.FAIL);
	}
	
	
	/**
	 * 设置用户为vip或价值用户、用户解绑、用户级别设置
	 * @param memberId
	 * @param groupId
	 * @param type
	 * @param value
	 * @param remark
	 * @param clientGroup
	 * @param accountNo
	 * @return
	 */
	public ApiResult saveUserSetting(String memberId,String groupId,String type,boolean value,String remark, String clientGroup, String accountNo){
		boolean isOk=memberDao.updateUserSetting(memberId, groupId,type,value,remark, clientGroup, accountNo);
		return new ApiResult().setCode(isOk?ResultCode.OK:ResultCode.FAIL);
	}
	
	/**
	 * 修改用户昵称
	 * @param mobile
	 * @param groupType
	 * @param nickname
	 * @return
	 */
	public ApiResult modifyName(String systemCategory,String mobile,String groupType,String nickname){
		return memberDao.modifyName(systemCategory,mobile,groupType,nickname);
	}
	
	/**
	 * 分页查询聊天室用户组信息
	 * @param dCriteria
	 * @param onlineStartDate
	 * @param onlineEndDate
	 * @param createDateStart
	 * @param createDateEnd
	 * @return
	 */
	public Page<Member> getChatUserPage(DetachedCriteria<Member> dCriteria,Date onlineStartDate,Date onlineEndDate,Date createDateStart,Date createDateEnd) {
		Member member = dCriteria.getSearchModel();
		Query query=new Query();
		ChatUserGroup userGroup=member.getLoginPlatform().getChatUserGroup().get(0);
		if(userGroup != null){
			Criteria criteria = new Criteria();
			Criteria userGroupCriteria = new Criteria();
			Criteria roomCriteria = new Criteria();
			boolean roomFlag = false;
			if(StringUtils.isNotBlank(member.getMobilePhone())){
				criteria.and("mobilePhone").regex(StringUtil.toFuzzyMatch(member.getMobilePhone()));
			}
			
			if(createDateStart!=null){
				userGroupCriteria = userGroupCriteria.and("createDate").gte(createDateStart);
			}
			if(createDateEnd != null){
				if(createDateStart != null){
					userGroupCriteria.lte(createDateEnd);
				}else{
					userGroupCriteria.and("createDate").lte(createDateEnd);
				}
			}
			
			if(StringUtils.isNotBlank(userGroup.getAccountNo())){
				userGroupCriteria.and("accountNo").regex(StringUtil.toFuzzyMatch(userGroup.getAccountNo()));
			}
			if(StringUtils.isNotBlank(userGroup.getNickname())){
				userGroupCriteria.and("nickname").regex(StringUtil.toFuzzyMatch(userGroup.getNickname()));
			}
			if(StringUtils.isNotBlank(userGroup.getId())){
				if(userGroup.getId().indexOf(",")!=-1){
					userGroupCriteria.and("id").is(userGroup.getId().replace(",", ""));
				}else{
					roomCriteria.and("id").is(userGroup.getId());
					roomFlag = true;
				}
			}
			if(userGroup.getValueUser() != null){
				userGroupCriteria.and("valueUser").is(userGroup.getValueUser());
			}
			if(StringUtils.isNotBlank(userGroup.getClientGroup())){
				if("vip".equals(userGroup.getClientGroup())){
					userGroupCriteria.and("vipUser").is(true);
				}else{
					userGroupCriteria.and("vipUser").is(false);
					userGroupCriteria.and("clientGroup").is(userGroup.getClientGroup());
				}
			}
			if(userGroup.getGagStatus() != null){
				if(userGroup.getGagStatus()){
					userGroupCriteria.and("gagDate").nin(new Object[]{null, ""});
				}else{
					userGroupCriteria.and("gagDate").in(new Object[]{null, ""});
				}
			}
			List<ChatRoom> roomList=userGroup.getRooms();
			if(roomList!=null){
				ChatRoom room=roomList.get(0);
				if(room.getOnlineStatus()!=null){
					roomCriteria.and("onlineStatus").is(room.getOnlineStatus());
					roomFlag = true;
				}
				if(onlineStartDate!=null){
					roomCriteria = roomCriteria.and("onlineDate").gte(onlineStartDate);
					roomFlag = true;
				}
				if(onlineEndDate != null){
					if(onlineStartDate != null){
						roomCriteria.lte(onlineEndDate);
						roomFlag = true;
					}else{
						roomCriteria.and("onlineDate").lte(onlineEndDate);
						roomFlag = true;
					}
				}
				if(room.getGagStatus() != null){
					if(room.getGagStatus()){
						roomCriteria.and("gagDate").nin(new Object[]{null, ""});
						roomFlag = true;
					}else{
						roomCriteria.and("gagDate").in(new Object[]{null, ""});
						roomFlag = true;
					}
				}
			}

			if(roomFlag){
				userGroupCriteria.and("rooms").elemMatch(roomCriteria);
			}
			userGroupCriteria.and("userType").is(0);
			criteria.and("loginPlatform.chatUserGroup").elemMatch(userGroupCriteria);
			query.addCriteria(criteria);
		}
		query.addCriteria(Criteria.where("systemCategory").is(member.getSystemCategory()));
		return memberDao.findPageInclude(Member.class, query, dCriteria,"loginPlatform.chatUserGroup.$","mobilePhone","updateDate");
	}
	
	/**
	 * 
	 * @function:  根据用户ID获取用户信息
	 * @param userIds
	 * @return List<Member>   
	 * @exception 
	 * @author:Jade.zhu   
	 * @since  1.0.0
	 */
	public List<Member> getMemberByUserId(String systemCategory,String groupType,String[] userIds){
		return memberDao.findListInclude(Member.class, Query.query(
				   new Criteria().andOperator(Criteria.where("loginPlatform.chatUserGroup.userId").in((Object[])userIds)
				   ,Criteria.where("valid").is(1),Criteria.where("systemCategory").is(systemCategory))), "loginPlatform.chatUserGroup.$");
	}

	/*****
	 * 根据用户id列表，所属组返回用户信息
	 * @param userIds
	 * @param groupType
	 * @return
	 */
	public List<Member> getMemberByUserIdGroupType(String systemCategory,String groupType,String[] userIds){
		Criteria criteria = new Criteria();
		Criteria userGroupCriteria = new Criteria();
		userGroupCriteria.and("userId").in((Object[])userIds).and("_id").is(groupType);
		criteria.and("systemCategory").is(systemCategory).and("valid").is(1).and("loginPlatform.chatUserGroup").elemMatch(userGroupCriteria);
		Query query =new Query();
		query.addCriteria(criteria);
		return memberDao.findListInclude(Member.class, query, "loginPlatform.chatUserGroup.$","mobilePhone");
	}
}
