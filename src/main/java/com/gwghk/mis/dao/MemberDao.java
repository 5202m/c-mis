package com.gwghk.mis.dao;

import com.gwghk.mis.util.JSONHelper;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.gwghk.mis.common.dao.MongoDBBaseDao;
import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.enums.ResultCode;
import com.gwghk.mis.model.ChatRoom;
import com.gwghk.mis.model.ChatUserGroup;
import com.gwghk.mis.model.Member;
import com.gwghk.mis.util.DateUtil;
import com.mongodb.WriteResult;

/**
 * 摘要：会员DAO实现
 * @author Gavin.guo
 * @date   2015年3月16日
 */
@Repository
public class MemberDao extends MongoDBBaseDao{
	
	/**
	 * 功能：分页查询会员列表
	 */
	public Page<Member> getMemberPage(Query query,DetachedCriteria<Member> dCriteria){
		return super.findPage(Member.class, query, dCriteria);
	}
	
	/**
	 * 功能：根据Id-->获取会员
	 */
	public Member getByMemberId(String MemberId){
		return this.findById(Member.class, MemberId);
	}
	
	/**
	 * 功能：根据mobilePhone-->获取会员
	 */
	public Member getByMemberMobilePhone(String systemCategory,String mobilePhone){
		return this.findOne(Member.class,Query.query(
				   new Criteria().andOperator(Criteria.where("mobilePhone").is(mobilePhone).and("systemCategory").is(systemCategory),Criteria.where("valid").is(1))));
	}
	
	/**
	 * 功能：获取后台会员(该会员只用于后台)
	 */
	public List<Member> getBackMember(){
		return this.findList(Member.class, Query.query(
			   new Criteria().andOperator(Criteria.where("loginPlatform.financePlatForm.isBack").is(1)
			   ,Criteria.where("loginPlatform.financePlatForm.isDeleted").is(1)
			   ,Criteria.where("valid").is(1))));
	}
	
	/**
	 * 功能：新增会员
	 */
    public boolean addMember(Member member){
    	member.setValid(1);
		this.add(member);
		return true;
	}
	
	/**
	 * 保存:修改或者新增，如果memberId为null,为新增，否则为修改
	 * @param member
	 * @return
	 */
	public boolean save(Member member) {
		if(member.getMemberId() == null){
			this.add(member);
		}else{
			this.update(member);
		}
		return true;
	}
    
    /**
	 * 功能：删除会员
	 */
	public boolean deleteMember(Object[] MemberIds){
		WriteResult wr = this.mongoTemplate.updateMulti(Query.query(Criteria.where("memberId").in(MemberIds))
					   , Update.update("valid", 0), Member.class);
		return wr!=null && wr.getN()>0;
	}
	
    /**
     * 更新用户设置，包括设置用户为价值用户或vip用户, 用户解绑
     * @param memberId
     * @param groupId
     * @param type 类型：1为价值用户，2为vip用户, 3为用户级别, unbind为用户解绑
     * @param isTrue
     * @return
     */
	public boolean updateUserSetting(String memberId,String groupType,String type,Boolean isTrue,String remark, String clientGroup, String accountNo){
		Update update=new Update();
		if("1".equals(type)){
			update.set("loginPlatform.chatUserGroup.$.valueUser", isTrue);
			update.set("loginPlatform.chatUserGroup.$.valueUserRemark", remark);
		}else if("2".equals(type)){
			update.set("loginPlatform.chatUserGroup.$.vipUser", isTrue);
			update.set("loginPlatform.chatUserGroup.$.vipUserRemark", remark);
		}else if("3".equals(type)){
			update.set("loginPlatform.chatUserGroup.$.clientGroup", clientGroup);
			update.set("loginPlatform.chatUserGroup.$.accountNo", accountNo);
		}else if("unbind".equals(type)){
			ChatUserGroup chatUserGroup = new ChatUserGroup();
			chatUserGroup.setId(groupType);
			update.pull("loginPlatform.chatUserGroup", chatUserGroup);
		}else{
			return false;
		}
		
		update.set("updateDate",new Date());
		WriteResult wr = this.mongoTemplate.updateFirst(Query.query(new Criteria().andOperator(Criteria.where("memberId").is(memberId),Criteria.where("loginPlatform.chatUserGroup.id").is(groupType))), update, Member.class);
		return wr!=null && wr.getN()>0;
	}
	
	/**
	 * 修改用户昵称
	 * @param mobile
	 * @param groupType
	 * @param nickname
	 * @return
	 */
	public ApiResult modifyName(String systemCategory,String mobile,String groupType,String nickname){
		ApiResult result=new ApiResult();
		Criteria mCri=new Criteria();
		mCri.and("mobilePhone").ne(mobile).and("valid").is(1).and("systemCategory").is(systemCategory);
		mCri.and("loginPlatform.chatUserGroup").elemMatch(new Criteria().and("id").is(groupType).and("nickname").is(nickname).and("userType").is(0));
		if(this.mongoTemplate.count(Query.query(mCri), Member.class)>0){
			return result.setErrorMsg("该昵称已被占用！");
		}
		Criteria searchObj=new Criteria();
		searchObj.and("mobilePhone").is(mobile).and("valid").is(1).and("systemCategory").is(systemCategory);
		searchObj.and("loginPlatform.chatUserGroup").elemMatch(new Criteria().and("id").is(groupType).and("userType").is(0));
		Update update=new Update();
		update.set("loginPlatform.chatUserGroup.$.nickname", nickname);
		update.set("updateDate",new Date());
		WriteResult wr = this.mongoTemplate.updateFirst(Query.query(searchObj), update, Member.class);
		return result.setCode((wr!=null && wr.getN()>0)?ResultCode.OK:ResultCode.FAIL);
	}
	
	/**
	 * 设置用户禁言
	 * @param groupType
	 * @param memberId
	 * @param groupId
	 * @param gagDate
	 * @param tip
	 * @param remark
	 * @return
	 */
	public boolean setUserGag(String groupType,String memberId,String groupId,String gagDate,String tip,String remark){
		 Member member = this.getByMemberId(memberId);
    	 List<ChatUserGroup> userGroupList = member.getLoginPlatform().getChatUserGroup();
    	 //groupId为空时，针对用户组别设置禁言。
    	 boolean isSet4Group = StringUtils.isEmpty(groupId);
    	 //当前是否禁言,暂时是考虑当前的禁言
    	 boolean isAddGagTimes = DateUtil.dateTimeWeekCheck(gagDate, true);
 		 if(userGroupList != null && userGroupList.size() > 0){
 			for(ChatUserGroup cg : userGroupList){
 				
 				if(cg.getId().equals(groupType)){
 					if(isSet4Group){
 						// 如果当前禁言并且禁言时间有变动则+1
 						if(isAddGagTimes && StringUtils.isNotBlank(gagDate) && (StringUtils.isBlank(cg.getGagDate()) || !cg.getGagDate().equals(gagDate))){
 							cg.setGagTimes(((cg.getGagTimes() == null) ? 0 : cg.getGagTimes()) + 1);
 						}
						cg.setGagTips(tip);
						cg.setGagRemark(remark);
						cg.setGagDate(gagDate);
 					}else{
 						List<ChatRoom> roomList=cg.getRooms();
 						for(ChatRoom room:roomList){
 							if(room.getId().equals(groupId)){
 								// 如果当前禁言并且禁言时间有变动则+1
 								if(isAddGagTimes && StringUtils.isNotBlank(gagDate) && (StringUtils.isBlank(room.getGagDate()) || !room.getGagDate().equals(gagDate))){
 									cg.setGagTimes(((cg.getGagTimes() == null) ? 0 : cg.getGagTimes()) + 1);
 								}
 								room.setGagDate(gagDate);
 								room.setGagTips(tip);
 								room.setGagRemark(remark);
 								break;
 							}
 						}
 					}
 					break;
 				}
 			}
 		 }
 		 member.setUpdateDate(new Date());
 		 this.update(member);
 		 return true;
	}
	
	/**
	 * 功能：通过id与memberId查询记录
	 */
	public Member getByIdAndGroupId(String memberId,String groupId){
		return this.findOne(Member.class,Query.query(new Criteria().andOperator(Criteria.where("memberId").is(memberId),
						   Criteria.where("valid").is(1),Criteria.where("loginPlatform.chatUserGroup.id").is(groupId))));
	}

  /**
   * 根据昵称查询用户
   * @param systemCategory
   * @param nickName
   * @return
   */
	public Member getMemberByNickeName(String systemCategory, String nickName){
		return this.findOne(Member.class, Query.query(new Criteria().andOperator(Criteria.where("loginPlatform.chatUserGroup.nickname").is(nickName),
				Criteria.where("systemCategory").is(systemCategory))));
	}

	/**
	 * 批量更新用户设置，包括设置用户为价值用户或vip用户, 用户解绑
	 * @param mobiles
	 * @param groupType
	 * @param type 类型：1为价值用户，2为vip用户, 3为用户级别, unbind为用户解绑
	 * @param isTrue
	 * @return
	 */
	public boolean updateBacthUserSetting(Object[] mobiles,String groupType,String type,Boolean isTrue,String remark, String clientGroup){
    boolean isSuccess=false;
    Update update=new Update();
		if("1".equals(type)){
			update.set("loginPlatform.chatUserGroup.$.valueUser", isTrue);
			update.set("loginPlatform.chatUserGroup.$.valueUserRemark", remark);
		}else if("2".equals(type)){
			update.set("loginPlatform.chatUserGroup.$.vipUser", isTrue);
			update.set("loginPlatform.chatUserGroup.$.vipUserRemark", remark);
		}else if("3".equals(type)){
			update.set("loginPlatform.chatUserGroup.$.clientGroup", clientGroup);
			//update.set("loginPlatform.chatUserGroup.$.accountNo", accountNo);
		}else if("unbind".equals(type)){
			ChatUserGroup chatUserGroup = new ChatUserGroup();
			chatUserGroup.setId(groupType);
			update.pull("loginPlatform.chatUserGroup", chatUserGroup);
		}else{
			return false;
		}

		update.set("updateDate",new Date());
    //for(String memberId : memberIds) {
      WriteResult wr = this.mongoTemplate.updateMulti(Query.query(new Criteria().andOperator(Criteria.where("mobilePhone").in(mobiles), Criteria.where("loginPlatform.chatUserGroup._id").is(groupType))), update, Member.class);
      isSuccess = (wr != null && wr.getN() > 0);
    //}
		return isSuccess;
	}
}