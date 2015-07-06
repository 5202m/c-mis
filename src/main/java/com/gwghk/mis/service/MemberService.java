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
import com.gwghk.mis.model.ChatUserGroup;
import com.gwghk.mis.model.Member;
import com.gwghk.mis.util.BeanUtils;
import com.gwghk.mis.util.DateUtil;
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
	public Member getByMobilePhone(String mobilePhone){
		return memberDao.getByMemberMobilePhone(mobilePhone);
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
    		if(memberDao.getByMemberMobilePhone(memberParam.getMobilePhone())!=null){
    			return result.setCode(ResultCode.Error102);
    		}
    	/*	LoginPlatform lp=new LoginPlatform();
    		FinanceApp pmApp=new FinanceApp();
    		pmApp.setPwd(MD5.getMd5(PropertiesUtil.getInstance().getProperty("defaultPwd")));//密码默认设置123456
    		lp.setPmApp(pmApp);
    		memberParam.setLoginPlatform(lp);*/
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
	public ApiResult saveUserGag(String memberId,String groupId,String gagStartDateF,String gagEndDateE,String gagTips){
		Member member = memberDao.getByMemberId(memberId);
		List<ChatUserGroup> userGroupList = member.getLoginPlatform().getChatUserGroup();
		if(userGroupList != null && userGroupList.size() > 0){
			for(ChatUserGroup cg : userGroupList){
				if(groupId.equals(cg.getId())){
					cg.setGagStartDate(DateUtil.parseDateSecondFormat(gagStartDateF));
					cg.setGagEndDate(DateUtil.parseDateSecondFormat(gagEndDateE));
					cg.setGagTips(gagTips);
					break;
				}
			}
		}
		memberDao.update(member);
		return new ApiResult().setCode(ResultCode.OK);
	}
	
	
	/**
	 * 设置用户为vip或价值用户
	 * @param memberId
	 * @param groupId
	 * @param type
	 * @param value
	 * @return
	 */
	public ApiResult saveUserSetting(String memberId,String groupId,String type,boolean value){
		boolean isOk=memberDao.updateUserSetting(memberId, groupId,type,value);
		return new ApiResult().setCode(isOk?ResultCode.OK:ResultCode.FAIL);
	}
	
	/**
	 * 分页查询聊天室用户组信息
	 * @param dCriteria
	 * @return
	 */
	public Page<Member> getChatUserPage(DetachedCriteria<Member> dCriteria,Date onlineStartDate,Date onlineEndDate) {
		Member member = dCriteria.getSearchModel();
		Query query=new Query();
		ChatUserGroup userGroup=member.getLoginPlatform().getChatUserGroup().get(0);
		if(userGroup != null){
			Criteria criteria = new Criteria();
			if(StringUtils.isNotBlank(userGroup.getAccountNo())){
				criteria.and("loginPlatform.chatUserGroup.accountNo").regex(StringUtil.toFuzzyMatch(userGroup.getAccountNo()));
			}
			if(StringUtils.isNotBlank(member.getMobilePhone())){
				criteria.and("mobilePhone").regex(StringUtil.toFuzzyMatch(member.getMobilePhone()));
			}
			if(StringUtils.isNotBlank(userGroup.getNickname())){
				criteria.and("loginPlatform.chatUserGroup.nickname").regex(StringUtil.toFuzzyMatch(userGroup.getNickname()));
			}
			if(StringUtils.isNotBlank(userGroup.getId())){
				criteria.and("loginPlatform.chatUserGroup.id").is(userGroup.getId());
			}
			if(userGroup.getOnlineStatus()!=null){
				criteria.and("loginPlatform.chatUserGroup.onlineStatus").is(userGroup.getOnlineStatus());
			}
			if(onlineStartDate!=null){
				criteria = criteria.and("loginPlatform.chatUserGroup.onlineDate").gte(onlineStartDate);
			}
			if(onlineEndDate != null){
				if(onlineStartDate != null){
					criteria.lte(onlineEndDate);
				}else{
					criteria.and("loginPlatform.chatUserGroup.onlineDate").lte(onlineEndDate);
				}
			}
			query.addCriteria(criteria);
		}
		return memberDao.findPageInclude(Member.class, query, dCriteria,"loginPlatform.chatUserGroup.$","mobilePhone");
	}
	
	
}
