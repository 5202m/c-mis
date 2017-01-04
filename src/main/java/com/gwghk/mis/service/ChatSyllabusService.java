package com.gwghk.mis.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.dao.ChatSyllabusDao;
import com.gwghk.mis.enums.ResultCode;
import com.gwghk.mis.model.BoUser;
import com.gwghk.mis.model.ChatGroup;
import com.gwghk.mis.model.ChatSyllabus;

/**
 * 聊天室课程安排服务类
 * 
 * @author Dick.guo
 * @date 2015年4月1日
 */
@Service
public class ChatSyllabusService
{
	@Autowired
	private ChatSyllabusDao chatSyllabusDao;
	
	@Autowired
	private ChatGroupService chatGroupService;	
	
	@Autowired
	private UserService userService;
	

	/**
	 * 查询课表列表
	 * 
	 * @param dCriteria
	 * @return
	 */
	public Page<ChatSyllabus> getSyllabuses(DetachedCriteria<ChatSyllabus> dCriteria) {
		ChatSyllabus syllabus = dCriteria.getSearchModel();
		Query query = new Query();
		Criteria criteria = Criteria.where("isDeleted").is(0);
		if (syllabus != null) {
			criteria.and("systemCategory").is(syllabus.getSystemCategory());
			if (StringUtils.isNotBlank(syllabus.getGroupType())) {
				criteria.and("groupType").is(syllabus.getGroupType());
			}
			if (StringUtils.isNotBlank(syllabus.getGroupId())) {
				criteria.and("groupId").is(syllabus.getGroupId());
			}
		}
		query.addCriteria(criteria);
		return chatSyllabusDao.findPage(ChatSyllabus.class, query, dCriteria);
	}
	
	/**
	 * 通过id找对应课程表记录
	 * 
	 * @param id
	 * @return
	 */
	public ChatSyllabus getChatSyllabus(String id)
	{
		return chatSyllabusDao.findById(ChatSyllabus.class, id);
	}
	
	/**
	 * 获取授权分析师
	 * @param chatGroupType
	 * @param chatGroupId
	 * @return
	 */
	public List<BoUser> getAuthUsers(String systemCategory,String chatGroupType, String chatGroupId)
	{
		Set<String> loc_userNos = null;
		String[] loc_userIdArr = null;
		if(StringUtils.isNotBlank(chatGroupId))
		{
			ChatGroup chatGroup = chatGroupService.getChatGroupById(chatGroupId);
			if(chatGroup != null)
			{
				loc_userIdArr = chatGroup.getAuthUsers();
				if(loc_userIdArr != null && loc_userIdArr.length != 0)
				{
					loc_userNos = new HashSet<String>(Arrays.asList(loc_userIdArr));
				}
			}
		}
		else
		{
			List<ChatGroup> chatGroups = chatGroupService.findByGroupType(systemCategory,chatGroupType);
			loc_userNos = new HashSet<String>();
			for(int i = 0, lenI = (chatGroups == null) ? 0 : chatGroups.size(); i < lenI; i++)
			{
				loc_userIdArr = chatGroups.get(i).getAuthUsers();
				if(loc_userIdArr != null && loc_userIdArr.length != 0)
				{
					loc_userNos.addAll(Arrays.asList(loc_userIdArr));
				}
			}
		}
		
		
		List<BoUser> loc_authUsers = null;
		if(loc_userNos != null && loc_userNos.isEmpty() == false)
		{
			loc_authUsers = userService.getUserListByNo(loc_userNos.toArray(new String[0]));
			for(int i = (loc_authUsers == null) ? -1 : loc_authUsers.size() - 1; i >= 0; i--)
			{
				if(loc_authUsers.get(i) == null 
						|| loc_authUsers.get(i).getRole() == null 
						|| loc_authUsers.get(i).getRole().getRoleNo() == null
						||loc_authUsers.get(i).getRole().getRoleNo().startsWith("analyst") == false)
				{
					loc_authUsers.remove(i);
				}
			}
		}
		return loc_authUsers;
	}

	/**
	 * 保存课程表
	 * 
	 * @param ChatSyllabus
	 * @return
	 */
	public ApiResult saveChatSyllabus(ChatSyllabus syllabus) {
		ApiResult loc_result=new ApiResult();
		if (StringUtils.isBlank(syllabus.getId()))
		{
			//没有相关课程表，直接添加
			syllabus.setId(null);
			syllabus.setIsDeleted(0);
			chatSyllabusDao.add(syllabus);
		}
		else
		{

			ChatSyllabus loc_syllabus = this.getChatSyllabus(syllabus.getId());
			//已经存在课程表，直接更新
			loc_syllabus.setStudioLink(syllabus.getStudioLink());
			loc_syllabus.setCourses(syllabus.getCourses());
			loc_syllabus.setPublishStart(syllabus.getPublishStart());
			loc_syllabus.setPublishEnd(syllabus.getPublishEnd());
			loc_syllabus.setUpdateUser(syllabus.getUpdateUser());
			loc_syllabus.setUpdateIp(syllabus.getUpdateIp());
			loc_syllabus.setUpdateDate(syllabus.getUpdateDate());
			chatSyllabusDao.update(loc_syllabus);
		}
    	return loc_result.setCode(ResultCode.OK);
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public ApiResult delete(String id) {
		ApiResult result=new ApiResult();
		boolean isOk = chatSyllabusDao.delete(id);
		return result.setCode(isOk ? ResultCode.OK : ResultCode.FAIL);
	}
}
