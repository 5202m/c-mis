package com.gwghk.mis.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gwghk.mis.authority.ActionVerification;
import com.gwghk.mis.common.model.AjaxJson;
import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DataGrid;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.common.model.TreeBean;
import com.gwghk.mis.constant.DictConstant;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.model.BoDict;
import com.gwghk.mis.model.BoUser;
import com.gwghk.mis.model.ChatGroup;
import com.gwghk.mis.model.ChatSyllabus;
import com.gwghk.mis.service.ChatGroupService;
import com.gwghk.mis.service.ChatSyllabusService;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.IPUtil;
import com.gwghk.mis.util.JsonUtil;
import com.gwghk.mis.util.ResourceBundleUtil;
import com.gwghk.mis.util.ResourceUtil;

/**
 * 聊天室课程表控制类
 * 
 * @author Dick.guo
 * @date 2015年12月24日
 */
@Scope("prototype")
@Controller
public class ChatSyllabusController extends BaseController
{

	private static final Logger logger = LoggerFactory.getLogger(ChatSyllabusController.class);

	@Autowired
	private ChatGroupService chatGroupService;
	
	@Autowired
	private ChatSyllabusService syllabusService;

	/**
	 * 格式成树形列表
	 * @param dictList
	 * @return
	 */
	private List<ChatGroup> formatTreeList(List<BoDict> dictList){
    	List<ChatGroup> nodeList = new ArrayList<ChatGroup>(); 
    	List<ChatGroup> groupList=chatGroupService.getChatGroupAllList(getSystemFlag(),"id","name","groupType");
    	ChatGroup tbean=null;
    	for(BoDict dict:dictList){
    		tbean=new ChatGroup();
    		tbean.setName(dict.getNameCN());
    		tbean.setGroupType(dict.getCode());
    		nodeList.add(tbean);
    		for(ChatGroup group:groupList){
    			if(group.getGroupType().equals(dict.getCode())){
    				nodeList.add(group);
    			}
    		}
    	}
    	return nodeList;
	}
	
	/**
	 * 功能：聊天室规则管理-首页
	 */
	@RequestMapping(value = "/chatSyllabusController/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, ModelMap map)
	{
		DictConstant dict=DictConstant.getInstance();
    	map.put("chatGroupList",this.formatTreeList(ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_TYPE)));
    	
		logger.debug(">>start into chatSyllabusController.index() and url is /chatSyllabusController/index.do");
		return "chat/syllabus/syllabus";
	}

	/**
	 * 功能：获取dataGrid列表
	 * 
	 * @param request
	 * @param dataGrid
	 *            分页查询参数对象
	 * @param syllabus
	 * @return Map<String,Object> dataGrid需要的数据
	 */
	@RequestMapping(value = "/chatSyllabusController/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> datagrid(HttpServletRequest request, DataGrid dataGrid, ChatSyllabus syllabus) {
		Page<ChatSyllabus> page = syllabusService.getSyllabuses(this.createDetachedCriteria(dataGrid, syllabus));
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", null == page ? 0 : page.getTotalSize());
		result.put("rows", null == page ? new ArrayList<ChatSyllabus>() : page.getCollection());
		return result;
	}

	/**
	 * 获取课程表数据
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/chatSyllabusController/view", method = RequestMethod.GET)
	@ResponseBody
	public ChatSyllabus view(HttpServletRequest request, @Param("id")String id)
	{
		ChatSyllabus loc_syllabus = syllabusService.getChatSyllabus(id);
		return loc_syllabus;
	}
	
	/**
	 * 功能：课程表修改
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/chatSyllabusController/preEdit", method = RequestMethod.GET)
	@ActionVerification(key = "edit")
	public String preEdit(HttpServletRequest request, ModelMap map, @Param("id")String id) throws Exception
	{
		ChatSyllabus loc_syllabus = null;
		List<BoUser> loc_authUsers = null;
		if(StringUtils.isNotBlank(id)){
			loc_syllabus = syllabusService.getChatSyllabus(id);
			loc_authUsers = syllabusService.getAuthUsers(this.getSystemFlag(),loc_syllabus.getGroupType(), loc_syllabus.getGroupId());
		}
		else 
		{
			loc_syllabus = new ChatSyllabus();
			loc_authUsers = new ArrayList<BoUser>();
		}
		
		DictConstant dict=DictConstant.getInstance();
    	map.put("chatGroupList",this.formatTreeList(ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_TYPE)));
		map.addAttribute("syllabus", loc_syllabus);
		JSONObject obj=new JSONObject();
		obj.put("data", loc_syllabus.getStudioLink());
		map.addAttribute("studioLinkStr",JSONObject.toJSONString(loc_syllabus.getStudioLink()));
		map.put("publishStartStr", loc_syllabus.getPublishStart() == null ? "" : DateUtil.formatDate(loc_syllabus.getPublishStart(), "yyyy-MM-dd"));
		map.put("publishEndStr", loc_syllabus.getPublishEnd() == null ? "" : DateUtil.formatDate(loc_syllabus.getPublishEnd(), "yyyy-MM-dd"));
		map.addAttribute("days", new String[]{"星期一","星期二","星期三","星期四","星期五","星期六","星期天"});
		map.addAttribute("authUsers", loc_authUsers);
		
		return "chat/syllabus/syllabusEdit";
	}

	/**
	 * 功能：课程表修改
	 * @param request
	 * @param syllabus
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/chatSyllabusController/edit", method = RequestMethod.POST)
	@ActionVerification(key = "edit")
	@ResponseBody
	public Object edit(HttpServletRequest request, ChatSyllabus syllabus) throws Exception
	{
		String publishStartDateStr = request.getParameter("publishStartStr");
		String publishEndDateStr = request.getParameter("publishEndStr");
		syllabus.setPublishStart(DateUtil.parseDateFormat(publishStartDateStr + " 00:00:00"));
		syllabus.setPublishEnd(DateUtil.parseDateFormat(publishEndDateStr + " 23:59:59"));
   	 
        Date currDate = new Date();
        syllabus.setCreateUser(userParam.getUserNo());
        syllabus.setCreateDate(currDate);
        syllabus.setCreateIp(IPUtil.getClientIP(request));
        syllabus.setUpdateUser(userParam.getUserNo());
        syllabus.setUpdateDate(currDate);
        syllabus.setUpdateIp(IPUtil.getClientIP(request));
        ApiResult result = syllabusService.saveChatSyllabus(syllabus);
        String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 修改课程表:[id=" + syllabus.getId() + ", groupType=" + syllabus.getGroupType() + ", groupId=" + syllabus.getGroupId() + "]!";
		logger.info("<<edit()|"+message);
        return result;
	}

	/**
	 * 删除
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/chatSyllabusController/delete",method=RequestMethod.POST)
	@ActionVerification(key="delete")
	@ResponseBody
	public AjaxJson delete(HttpServletRequest request, @Param("id")String id){
		AjaxJson j = new AjaxJson();
		ApiResult result = syllabusService.delete(id);
		if(result.isOk()){
			j.setSuccess(true);
			String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除课程安排成功：" + id + "!";
			addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
			logger.info("<<delete()|"+message);
		}else{
			j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
			String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除课程安排失败：" + id + "!";
			addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
			logger.error("<<delete()|"+message+",ErrorMsg:"+result.toString());
		}
		return j;
	}
	
	 /**
   	 * 功能：提取作者/分析师列表
   	 */
    @RequestMapping(value = "/chatSyllabusController/getAuthorList", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
   	@ResponseBody
    public String getAuthorList(HttpServletRequest request,ModelMap map) throws Exception {
    	String groupType=request.getParameter("groupType"),groupId=request.getParameter("groupId"),authors=request.getParameter("authors");
       	List<TreeBean> treeList=new ArrayList<TreeBean>();
       	TreeBean tbean=null;
       	List<BoUser> loc_authUsers = syllabusService.getAuthUsers(getSystemFlag(),groupType,groupId);
       	authors=StringUtils.isBlank(authors)?"":(",".concat(authors).concat(","));
        if(loc_authUsers!=null && loc_authUsers.size()>0){
        	
           for(BoUser row:loc_authUsers){
      		 tbean=new TreeBean();
      		 tbean.setId(row.getUserNo());
      		 tbean.setIconImg(row.getAvatar());
      		 tbean.setText(row.getUserName());
      		 if(authors.contains(",".concat(row.getUserNo()).concat(","))){
   			   tbean.setChecked(true);
   		     }
      		 tbean.setParentId("");
  			 treeList.add(tbean);
          }
        }
       	return JsonUtil.formatListToTreeJson(treeList,false);
     }
}
