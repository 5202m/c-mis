package com.gwghk.mis.service;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.dao.ChatGroupDao;
import com.gwghk.mis.dao.ChatGroupRuleDao;
import com.gwghk.mis.dao.RoleDao;
import com.gwghk.mis.enums.IdSeq;
import com.gwghk.mis.enums.ResultCode;
import com.gwghk.mis.model.BoUser;
import com.gwghk.mis.model.ChatGroup;
import com.gwghk.mis.model.ChatGroupRule;
import com.gwghk.mis.model.ChatUserGroup;
import com.gwghk.mis.model.Member;
import com.gwghk.mis.model.TraninClient;
import com.gwghk.mis.util.BeanUtils;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.StringUtil;

/**
 * 聊天室组别管理服务类
 * @author Alan.wu
 * @date  2015年4月1日
 */
@Service
public class ChatGroupService{

	@Autowired
	private ChatGroupDao chatGroupDao;
	
	@Autowired
	private ChatGroupRuleDao chatGroupRuleDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MemberService memberService;

	@Autowired
	private ChatApiService chatApiService;

	/**
	 * 通过id找对应记录
	 * @param chatGroupId
	 * @return
	 */
	public ChatGroup getChatGroupById(String chatGroupId) {
		return chatGroupDao.findById(ChatGroup.class,chatGroupId);
	}

	/**
	 * 保存房间
	 * @param chatGroupParam
	 * @param isUpdate
	 * @param isUpdateDefaultAnalyst  是否修改默认分析师,同时会修改聊天规则
	 * @return
	 */
	public ApiResult saveChatGroup(ChatGroup chatGroupParam, boolean isUpdate, boolean isUpdateDefaultAnalyst) {
		ApiResult result=new ApiResult();
		chatGroupParam.setValid(1);
    	if(isUpdate){
    		if(StringUtils.isBlank(chatGroupParam.getId())){
    			return result.setCode(ResultCode.Error103);
    		}
    		ChatGroup group=getChatGroupById(chatGroupParam.getId());
    		if(group==null){
    			return result.setCode(ResultCode.Error104);
    		}
    		//有效-->无效、在开放时间-->不在开放时间的情况，需要通知API leaveRoom
    		if((new Integer(1).equals(group.getStatus()) && new Integer(0).equals(chatGroupParam.getStatus()))
    				|| (DateUtil.dateTimeWeekCheck(group.getOpenDate(), true) && DateUtil.dateTimeWeekCheck(chatGroupParam.getOpenDate(), true) == false)){
    			chatApiService.leaveRoom(chatGroupParam.getId(),"");
    		}
    		BeanUtils.copyExceptNull(group, chatGroupParam);
    		if(isUpdateDefaultAnalyst){
    			//修改默认分析师
        		BoUser analyst = null;
        		if(chatGroupParam.getDefaultAnalyst() != null){
        			String analystId = chatGroupParam.getDefaultAnalyst().getUserId();
        			if(StringUtils.isNotBlank(analystId)){
        				BoUser analystTmp = userService.getUserById(analystId);
        				if(analystTmp != null){
        					analyst = new BoUser();
        					analyst.setUserId(analystTmp.getUserId());
        					analyst.setUserNo(analystTmp.getUserNo());
        					analyst.setUserName(analystTmp.getUserName());
        					analyst.setPosition(analystTmp.getPosition());
        					analyst.setAvatar(analystTmp.getAvatar());
        				}
        			}
        		}
    			group.setDefaultAnalyst(analyst);
    			setGroupRule(group);
    		}
    		roleDao.updateRoleChatGroup(group);
    		chatGroupDao.update(group);
    	}else{
    		if(StringUtils.isNotBlank(chatGroupParam.getId())){
    			return result.setCode(ResultCode.Error103);
    		}
    		setGroupRule(chatGroupParam);
    		chatGroupParam.setId(chatGroupParam.getGroupType()+"_"+chatGroupDao.getIncSeq(IdSeq.ChatGroup));
    		chatGroupDao.add(chatGroupParam);	
    	}
    	return result.setCode(ResultCode.OK);
	}

	/**
	 * 设置规则
	 * @param group
	 */
	private void setGroupRule(ChatGroup group){
		if(StringUtils.isNotBlank(group.getChatRuleIds())){
			List<ChatGroupRule> ruleList=chatGroupRuleDao.getByIdArr(group.getChatRuleIds().split(","));
			group.setChatRules(ruleList);
		}else{
			group.setChatRules(new ArrayList<ChatGroupRule>());
		}
		//不保存id
		group.setChatRuleIds(null);
	}
	
	/**
	 * 功能：设置组别
	 */
	public ApiResult saveSetToken(ChatGroup group){
		ApiResult result = new ApiResult();
		ChatGroup g = getChatGroupById(group.getId());
		if(g != null){
			g.setTokenAccessId(group.getTokenAccessId());
			chatGroupDao.update(g);
		}
		return result.setCode(ResultCode.OK);
	}
		
	/**
	 * 删除组
	 * @param ids
	 * @return
	 */
	public ApiResult deleteChatGroup(String[] ids) {
		ApiResult api=new ApiResult();
    	boolean isSuccess = chatGroupDao.softDelete(ChatGroup.class,ids);
    	if(isSuccess){
    		roleDao.deleteRoleChatGroup(ids);
    		chatApiService.leaveRoom(StringUtils.join(ids, ","),"");
    	}
    	return api.setCode(isSuccess?ResultCode.OK:ResultCode.FAIL);
	}

	/**
	 * 分页查询
	 * @param dCriteria
	 * @return
	 */
	public Page<ChatGroup> getChatGroupPage(
			DetachedCriteria<ChatGroup> dCriteria) {
		Criteria criter=new Criteria();
		criter.and("valid").is(1);
		ChatGroup model=dCriteria.getSearchModel();
		if(model!=null){
			criter.and("systemCategory").is(model.getSystemCategory());
			if(StringUtils.isNotBlank(model.getGroupType())){
				criter.and("groupType").is(model.getGroupType());
			}
			if(StringUtils.isNotBlank(model.getId())){
				criter.and("id").regex(StringUtil.toFuzzyMatch(model.getId()));
			}
			if(StringUtils.isNotBlank(model.getName())){
				criter.and("name").regex(StringUtil.toFuzzyMatch(model.getName()));
			}
			if(StringUtils.isNotBlank(model.getChatRuleIds())){
				criter.and("chatRules.id").regex(model.getChatRuleIds().replaceAll(",","|"));
			}
			if(model.getStatus()!=null){
				criter.and("status").is(model.getStatus());
			}
			if(StringUtils.isNotBlank(model.getTalkStyle())){
				criter.and("talkStyle").regex(model.getTalkStyle().replaceAll(",","|"));
			}
		}
		return chatGroupDao.findPage(ChatGroup.class, Query.query(criter), dCriteria);
	}

	/**
	 * 提供没有关联角色的聊天室组列表
	 */
	public List<ChatGroup> getUnRelationRoleChatGroup(String systemCategory,List<ChatGroup> relationRoleChatGroupList){
		List<ChatGroup> allChatGroupList = chatGroupDao.findGroupList(systemCategory);
		List<String> allChatGroupIdList = new ArrayList<String>();
		if(allChatGroupList != null && allChatGroupList.size() > 0){
			for(ChatGroup ac : allChatGroupList){
				allChatGroupIdList.add(ac.getId());
			}
		}
		List<String> relationRoleChatGroupIdList = new ArrayList<String>();
		if(relationRoleChatGroupList != null && relationRoleChatGroupList.size() > 0 ){
			for(ChatGroup rc : relationRoleChatGroupList){
				relationRoleChatGroupIdList.add(rc.getId());
			}
		}
		allChatGroupIdList.removeAll(relationRoleChatGroupIdList);
		return chatGroupDao.findGroupList(allChatGroupIdList);
	}
	
	/**
	 * 功能： 根据组Id集合 --> 查询组列表
	 */
	public List<ChatGroup> findGroupList(Object[] groupIdArr){
		 return chatGroupDao.findGroupList(groupIdArr);
	}
	
	/**
	 * 功能： 根据groupType --> 查询组列表
	 */
	public List<ChatGroup> findByGroupType(String systemCategory,String groupType){
		 return chatGroupDao.findByGroupType(systemCategory,groupType);
	}
	
	/**
	 * 提取列表数据
	 * @return
	 */
	public List<ChatGroup> getChatGroupList(String systemCategory, String...selectField) {
		Query query = Query.query(Criteria.where("valid").is(1).and("status").in(1, 2).and("systemCategory").is(systemCategory));
		query.with(new Sort(new Order(Direction.ASC, "groupType"), new Order(Direction.ASC, "level")));
		if(selectField!=null){
			return chatGroupDao.findListInclude(ChatGroup.class, query,selectField);
		}
		return chatGroupDao.findList(ChatGroup.class, query);
	}
	/**
	 * 提取列表数据
	 * @return
	 */
	public List<ChatGroup> getChatGroupByTypeList(String systemCategory,String groupType,String...selectField) {
		Criteria cri=Criteria.where("valid").is(1).and("status").in(1, 2).and("systemCategory").is(systemCategory);
		if(StringUtils.isNotBlank(groupType)){
			cri.and("groupType").is(groupType);
		}
		Query query = Query.query(cri);
		query.with(new Sort(new Order(Direction.ASC, "groupType"), new Order(Direction.ASC, "level")));
		if(selectField!=null){
			return chatGroupDao.findListInclude(ChatGroup.class, query,selectField);
		}
		return chatGroupDao.findList(ChatGroup.class, query);
	}
	/**
	 * 提取列表数据
	 * @return
	 */
	public List<ChatGroup> getChatGroupAllList(String systemCategory,String...selectField) {
		Query query = Query.query(Criteria.where("valid").is(1).and("systemCategory").is(systemCategory));
		query.with(new Sort(new Order(Direction.ASC, "groupType"), new Order(Direction.ASC, "level")));
		if(selectField!=null){
			return chatGroupDao.findListInclude(ChatGroup.class, query,selectField);
		}
		return chatGroupDao.findList(ChatGroup.class, query);
	}
	
	/**
	 * 根据用户编号查询已授权房间列表
	 * @param userId
	 * @param selectField
	 * @return
	 */
	public List<ChatGroup> getChatGroupListByAuthUser(String systemCategory,String userId, String...selectField){
		Criteria criteria = Criteria.where("valid").is(1).and("status").in(1, 2).and("systemCategory").is(systemCategory);
		if(StringUtils.isNotBlank(userId)){
			criteria.and("authUsers").is(userId);
		}
		Query query = Query.query(criteria);
		query.with(new Sort(new Order(Direction.ASC, "groupType"), new Order(Direction.ASC, "level")));
		if(selectField!=null){
			return chatGroupDao.findListInclude(ChatGroup.class, query,selectField);
		}
		return chatGroupDao.findList(ChatGroup.class, query);
	}
	
	public ApiResult saveChatGroupTranin(ChatGroup chatGroupParam) {
		ApiResult result=new ApiResult();
		chatGroupParam.setValid(1);
		if(StringUtils.isBlank(chatGroupParam.getId())){
			return result.setCode(ResultCode.Error103);
		}

		chatGroupDao.update(chatGroupParam);          
    	return result.setCode(ResultCode.OK);
	}
	
	/**
	 * 导入客户
	 * @param groupId
	 * @param mobiles
	 * @return
	 */
	public ApiResult importClient(String systemCategory,String groupId, String mobiles) {
		ChatGroup chatGroup = this.getChatGroupById(groupId);
		String[] mobilephones = mobiles.split(",");

		ApiResult result=new ApiResult();
		if(chatGroup != null && mobilephones != null && mobilephones.length > 0){
			//查询导入的手机号对应对直播间注册信息
			List<Member> members = memberService.getMemberListByMobiles(systemCategory,mobilephones, chatGroup.getGroupType());
			Set<String> importUserIds = new HashSet<String>();
			Set<String> importMobiles = new HashSet<String>();
			List<TraninClient> newTcs = new ArrayList<TraninClient>();
			//将新导入的全部追加到房间授权列表中
			if(members != null){
				ChatUserGroup groupTmp = null;
				TraninClient newTc = null;
				Date now = new Date();
				for (Member member : members) {
					groupTmp = member.getLoginPlatform().getChatUserGroup().get(0);
					if(importUserIds.contains(groupTmp.getUserId()) == false){
						newTc = new TraninClient();
						newTc.setClientId(groupTmp.getUserId());
						newTc.setNickname(groupTmp.getNickname());
						newTc.setIsAuth(1);
						newTc.setDateTime(now);
						newTcs.add(newTc);
						importUserIds.add(groupTmp.getUserId());
						importMobiles.add(member.getMobilePhone());
					}
				}
			}
			//补充原始已经报名的客户信息（以新导入的信息为准）
			List<TraninClient> oldTcs = chatGroup.getTraninClient();
			if(oldTcs != null){
				for (TraninClient oldTc : oldTcs) {
					if(importUserIds.contains(oldTc.getClientId()) == false){
						newTcs.add(oldTc);
					}
				}
			}
			chatGroup.setTraninClient(newTcs);
			//保存房间信息
			chatGroupDao.update(chatGroup);
			
			//筛选未成功导入的手机号
			List<String> importFails = new ArrayList<String>();
			for (String mobilephone : mobilephones) {
				if(importMobiles.contains(mobilephone) == false){
					importFails.add(mobilephone);
				}
			}
			result.setReturnObj(importFails.toArray());
			result.setCode(ResultCode.OK);
		}else{
			result.setErrorMsg("房间或客户手机不存在");
			result.setCode(ResultCode.FAIL);
		}
		return result;
	}

}
