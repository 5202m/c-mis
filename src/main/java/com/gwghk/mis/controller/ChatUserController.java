package com.gwghk.mis.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gwghk.mis.authority.ActionVerification;
import com.gwghk.mis.common.model.AjaxJson;
import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DataGrid;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.constant.DictConstant;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.model.BoDict;
import com.gwghk.mis.model.ChatClientGroup;
import com.gwghk.mis.model.ChatGroup;
import com.gwghk.mis.model.ChatMessage;
import com.gwghk.mis.model.ChatRoom;
import com.gwghk.mis.model.ChatUserGroup;
import com.gwghk.mis.model.Member;
import com.gwghk.mis.service.ChatApiService;
import com.gwghk.mis.service.ChatClientGroupService;
import com.gwghk.mis.service.ChatGroupService;
import com.gwghk.mis.service.MemberService;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.ExcelUtil;
import com.gwghk.mis.util.ResourceBundleUtil;
import com.gwghk.mis.util.ResourceUtil;
import com.gwghk.mis.util.StringUtil;
import com.sdk.orm.DataRowSet;
import com.sdk.orm.IRow;
import com.sdk.poi.POIExcelBuilder;

/**
 * 聊天室内容管理
 * @author Alan.wu
 * @date   2015/04/02
 */
@Scope("prototype")
@Controller
public class ChatUserController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(ChatUserController.class);
	@Autowired
	private MemberService memberService;
	@Autowired
	private ChatGroupService chatGroupService;
	@Autowired
	private ChatClientGroupService chatClientGroupService;
	@Autowired
	private ChatApiService chatApiService;

	/**
	 * 格式成树形列表
	 * @param dictList
	 * @return
	 */
	private List<ChatGroup> formatTreeList(List<BoDict> dictList){
    	List<ChatGroup> nodeList = new ArrayList<ChatGroup>(); 
    	List<ChatGroup> groupList=chatGroupService.getChatGroupList(getSystemFlag(),"id","name","groupType");
    	ChatGroup tbean=null;
    	for(BoDict dict:dictList){
    		tbean=new ChatGroup();
    		tbean.setId(dict.getCode());
    		tbean.setName(dict.getNameCN());
    		nodeList.add(tbean);
    		for(ChatGroup group:groupList){
    			if(group.getGroupType().equals(dict.getCode())){
    				group.setName(StringUtil.fillChar('　', 1)+group.getName());
    				nodeList.add(group);
    			}
    		}
    	}
    	return nodeList;
	}
	
	/**
	 * 功能：退出房间
	 */
	@RequestMapping(value = "/chatUserController/toExitRoom", method = RequestMethod.GET)
	public  String  toExitRoom(HttpServletRequest request,ModelMap map){
		DictConstant dict=DictConstant.getInstance();
    	map.put("userIds", request.getParameter("userIds"));
    	map.put("chatGroupList",this.formatTreeList(ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_TYPE)));
		logger.debug(">>start into chatUserController.toExitRoom() and url is /chatUserController/toExitRoom.do");
		return "chat/analyst/exitRoom";
	}
	
	/**
	 * 强制离开房间
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/chatUserController/exitRoom",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="exitRoom")
    public AjaxJson exitRoom(HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		String userIds = request.getParameter("userIds"),chatUserRoomId = request.getParameter("chatUserRoomId");
		if(StringUtils.isBlank(userIds)||StringUtils.isBlank(chatUserRoomId)){
			j.setSuccess(false);
			j.setMsg("输入参数有误，请检查！");
			return j;
		}
		if(chatUserRoomId.endsWith(",")){//逗号结尾为房间大类
			List<ChatGroup> list=chatGroupService.findByGroupType(getSystemFlag(),chatUserRoomId.replace(",",""));
			ArrayList<String> roomIdList=new ArrayList<String>();
			for(ChatGroup row :list){
				roomIdList.add(row.getId());
				chatUserRoomId=StringUtils.join(roomIdList, ",");
			}
		}
		ApiResult apiResult = chatApiService.leaveRoom(chatUserRoomId,userIds);
		if(apiResult.isOk()){
			j.setSuccess(true);
    		String message = "用户：" + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 设置退出房间成功";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:exitRoom()|"+message);
		}else{
			j.setSuccess(false);
			j.setMsg(ResourceBundleUtil.getByMessage(apiResult.getCode()));
    		String message = "用户：" + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 设置退出房间失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:exitRoom()|"+message+",ErrorMsg:"+apiResult.toString());
		}
		return j;
	}
	/**
	 * 功能：聊天室内容管理-首页
	 */
	@RequestMapping(value = "/chatUserController/index", method = RequestMethod.GET)
	public  String  index(HttpServletRequest request,ModelMap map){
		DictConstant dict=DictConstant.getInstance();
    	map.put("clientGroupList", chatClientGroupService.getClientGroupList(getSystemFlag(),null));
		List<BoDict> dictList=ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_USE_STATUS);
    	map.put("statusList", dictList);
    	map.put("chatGroupList",this.formatTreeList(ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_TYPE)));
		logger.debug(">>start into chatUserController.index() and url is /chatUserController/index.do");
		return "chat/member/userList";
	}

	/**
	 * 获取datagrid列表
	 * @param request
	 * @param dataGrid  分页查询参数对象
	 * @param chatOnlineUser   实体查询参数对象
	 * @return Map<String,Object> datagrid需要的数据
	 */
	@RequestMapping(value = "/chatUserController/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public  Map<String,Object>  datagrid(HttpServletRequest request, DataGrid dataGrid,Member member){
		 Date onlineStartDate=null,onlineEndDate=null;
		 String onlineStartDateStr=request.getParameter("onlineStartDate"),onlineEndDateStr=request.getParameter("onlineEndDate");
		 if(StringUtils.isNotBlank(onlineStartDateStr)){
			 onlineStartDate=DateUtil.parseDateSecondFormat(onlineStartDateStr);
		 }
		 if(StringUtils.isNotBlank(onlineEndDateStr)){
			 onlineEndDate=DateUtil.parseDateSecondFormat(onlineEndDateStr);
		 }
		 Date createDateStart=null,createDateEnd=null;
		 String createDateStartStr=request.getParameter("userCreateDateStart"),createDateEndStr=request.getParameter("userCreateDateEnd");
		 if(StringUtils.isNotBlank(createDateStartStr)){
			 createDateStart=DateUtil.parseDateSecondFormat(createDateStartStr);
		 }
		 if(StringUtils.isNotBlank(createDateEndStr)){
			 createDateEnd=DateUtil.parseDateSecondFormat(createDateEndStr);
		 }
		 Page<Member> page = memberService.getChatUserPage(this.createDetachedCriteria(dataGrid, member),onlineStartDate,onlineEndDate,createDateStart,createDateEnd);
		 Map<String, Object> result = new HashMap<String, Object>();
		 result.put("total",null == page ? 0  : page.getTotalSize());
	     result.put("rows", null == page ? new ArrayList<ChatMessage>() : page.getCollection());
	     return result;
	}
	
	/**
	 * 功能：进入用户禁言页面
	 */
    @RequestMapping(value="/chatUserController/toUserGag", method = RequestMethod.GET)
    @ActionVerification(key="setGagTime")
    public String toUserGag(HttpServletRequest request,ModelMap map) throws Exception {
    	 String memberId = request.getParameter("memberId"),
    			groupId = request.getParameter("groupId"),
    			groupType = request.getParameter("groupType");
    	 Member member = memberService.getByMemberId(memberId);
    	 List<ChatUserGroup> userGroupList = member.getLoginPlatform().getChatUserGroup();
    	 //groupId为空时，针对用户组别设置禁言。
    	 boolean isSet4Group = StringUtils.isEmpty(groupId);
 		 if(userGroupList != null && userGroupList.size() > 0){
 			for(ChatUserGroup cg : userGroupList){
 				if(groupType.equals(cg.getId())){
 					if(isSet4Group){
 						map.put("gagDate", cg.getGagDate());
	 			    	map.put("gagTips", cg.getGagTips());
	 			    	map.put("gagRemark", cg.getGagRemark());
 					}else{
 						List<ChatRoom> roomList=cg.getRooms();
 						for(ChatRoom room:roomList){
 							if(room.getId().equals(groupId)){
 								map.put("gagDate", room.getGagDate());
 								map.put("gagTips", room.getGagTips());
 								map.put("gagRemark", room.getGagRemark());
 								break;
 							}
 						}
 					}
 					break;
 				}
 			}
 		 }
 		 map.put("memberId", memberId);
    	 map.put("groupId", groupId);
    	 map.put("groupType", groupType);
    	 return "chat/member/userGag";
    }
    
	/**
	 * 功能：设置用户禁言
	 */
	@RequestMapping(value="/chatUserController/setUserGag",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="setGagTime")
    public AjaxJson setUserGag(HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		String groupType = request.getParameter("groupType");
		String memberId = request.getParameter("memberId");
		String groupId = request.getParameter("groupId");
		String gagDate = request.getParameter("gagDate");
		String gagTips = request.getParameter("gagTips");
		String remark = request.getParameter("gagRemark");
		ApiResult apiResult = memberService.saveUserGag(groupType,memberId,groupId,gagDate,gagTips,remark);
		if(apiResult.isOk()){
			j.setSuccess(true);
    		String message = "用户：" + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 设置用户禁言成功";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:setUserGag()|"+message);
		}else{
			j.setSuccess(false);
			j.setMsg(ResourceBundleUtil.getByMessage(apiResult.getCode()));
    		String message = "用户：" + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 设置用户禁言失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:setUserGag()|"+message+",ErrorMsg:"+apiResult.toString());
		}
		return j;
	}
	
	/**
	 * 功能：进入用户禁言页面
	 */
    @RequestMapping(value="/chatUserController/toUserSetting", method = RequestMethod.GET)
    @ActionVerification(key="userSetting")
    public String toUserSetting(HttpServletRequest request,ModelMap map) throws Exception {
    	 String memberId = request.getParameter("memberId"),
    			groupType = request.getParameter("groupType"),
    			valueUser=request.getParameter("valueUser"),
    			vipUser=request.getParameter("vipUser"),
    			type=request.getParameter("type"),
    	        valueUserRemark=request.getParameter("valueUserRemark"),
    	        vipUserRemark=request.getParameter("vipUserRemark"),
    	        clientGroup=request.getParameter("clientGroup"),
    	        accountNo=request.getParameter("accountNo");
 		 map.put("memberId", memberId);
    	 map.put("groupType", groupType);
    	 map.put("valueUser", valueUser);
    	 map.put("vipUser", vipUser);
    	 map.put("type", type);
    	 map.put("valueUserRemark", valueUserRemark);
    	 map.put("vipUserRemark", vipUserRemark);
    	 map.put("clientGroup", clientGroup);
    	 map.put("accountNo", accountNo);
     	 map.put("clientGroupList", chatClientGroupService.getClientGroupList(getSystemFlag(),groupType));
    	 return "chat/member/userSetting";
    }
    
    /**
	 * 功能：设置用户禁言
	 */
	@RequestMapping(value="/chatUserController/userSetting",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="userSetting")
    public AjaxJson userSetting(HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		String memberId = request.getParameter("memberId"),
				groupType = request.getParameter("groupType"),
				type=request.getParameter("type"),
				value=request.getParameter("value"),
				remark=request.getParameter("remark"),
				clientGroup=request.getParameter("clientGroup"),
				accountNo=request.getParameter("accountNo");
		ApiResult apiResult = memberService.saveUserSetting(memberId, groupType, type,Boolean.valueOf(value), remark, clientGroup, accountNo);
		if(apiResult.isOk()){
			j.setSuccess(true);
    		String message = "用户：" + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 设置用户成功";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:userSetting()|"+message);
		}else{
			j.setSuccess(false);
			j.setMsg(ResourceBundleUtil.getByMessage(apiResult.getCode()));
    		String message = "用户：" + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 设置用户失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:userSetting()|"+message+",ErrorMsg:"+apiResult.toString());
		}
		return j;
	}
	
	/**
	 * 功能:修改用户昵称
	 */
	@RequestMapping(value="/chatUserController/modifyName",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="modifyName")
    public AjaxJson modifyName(HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		String oldname = request.getParameter("oldname"),
			   nickname = request.getParameter("nickname"),
			   groupType=request.getParameter("groupType"),
			   mobile=request.getParameter("mobilePhone");
		if(StringUtils.isBlank(nickname)){
			j.setSuccess(false);
			j.setMsg("昵称不能为空");
			return j;
		}
		if(StringUtils.isBlank(groupType)||StringUtils.isBlank(mobile)){
			j.setSuccess(false);
			j.setMsg("请求参数有误，请联系管理员！");
			return j;
		}
		ApiResult apiResult = memberService.modifyName(getSystemFlag(),mobile,groupType,nickname);
		if(apiResult.isOk()){
			j.setSuccess(true);
			j.setObj(nickname);
    		String message = "用户：" + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 操作成功提示:把客户("+mobile+")的昵称【"+oldname+"】改成【"+nickname+"】";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:userSetting()|"+message);
		}else{
			j.setSuccess(false);
			j.setMsg(apiResult.getErrorMsg());
    		String message = "用户：" + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 操作失败提示:把客户("+mobile+")的昵称【"+oldname+"】改成【"+nickname+"】";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:userSetting()|"+message+",ErrorMsg:"+apiResult.toString());
		}
		return j;
	}
	
	
	
	/**
	 * 功能：导出成员记录(以模板的方式导出)
	 */
	@RequestMapping(value = "/chatUserController/exportRecord", method = RequestMethod.GET)
	@ActionVerification(key="export")
	public void exportRecord(HttpServletRequest request, HttpServletResponse response,Member member){
		String groupType = request.getParameter("groupType");
		try{
			DataGrid dataGrid = new DataGrid();
			dataGrid.setPage(0);
			dataGrid.setRows(0);
			dataGrid.setSort("mobilePhone");
			dataGrid.setOrder("desc");
			Date onlineStartDate=null,onlineEndDate=null;
			String onlineStartDateStr=request.getParameter("onlineStartDate"),onlineEndDateStr=request.getParameter("onlineEndDate");
			if(StringUtils.isNotBlank(onlineStartDateStr)){
			   onlineStartDate=DateUtil.parseDateSecondFormat(onlineStartDateStr);
			}
			if(StringUtils.isNotBlank(onlineEndDateStr)){
			   onlineEndDate=DateUtil.parseDateSecondFormat(onlineEndDateStr);
			}
			Date createDateStart=null,createDateEnd=null;
			 String createDateStartStr=request.getParameter("userCreateDateStart"),createDateEndStr=request.getParameter("userCreateDateEnd");
			 if(StringUtils.isNotBlank(createDateStartStr)){
				 createDateStart=DateUtil.parseDateSecondFormat(createDateStartStr);
			 }
			 if(StringUtils.isNotBlank(createDateEndStr)){
				 createDateEnd=DateUtil.parseDateSecondFormat(createDateEndStr);
			 }
			POIExcelBuilder builder = new POIExcelBuilder(new File(request.getServletContext().getRealPath(WebConstant.CHAT_USER_RECORDS_TEMPLATE_PATH)));
			String pwd=StringUtil.random(6);
			builder.getHSSFWorkbook().getSheetAt(0).protectSheet(pwd);
			Page<Member> page = memberService.getChatUserPage(this.createDetachedCriteria(dataGrid, member),onlineStartDate,onlineEndDate,createDateStart,createDateEnd);
			List<Member>  memberList = page.getCollection();
			List<ChatGroup> chatGroupList=chatGroupService.getChatGroupAllList(getSystemFlag(),"id","name");
			List<ChatClientGroup> clientGroupList=chatClientGroupService.getClientGroupList(getSystemFlag(),groupType);
			ChatUserGroup userGroup=null;
			Member cm=null;
			if(memberList != null && memberList.size() > 0){
				DataRowSet dataSet = new DataRowSet();
				for(int i=0;i<memberList.size();i++){
					cm=memberList.get(i);
					userGroup=cm.getLoginPlatform().getChatUserGroup().get(0);
					for(ChatRoom room :userGroup.getRooms()){
						setIRow(i+1,dataSet,cm.getMobilePhone(),userGroup,room,chatGroupList,clientGroupList);
					}
				}
				builder.put("rowSet",dataSet);
			}else{
				builder.put("rowSet",new DataRowSet());
			}
			builder.parse();
			ExcelUtil.wrapExcelExportResponse("成员记录", request, response);
			builder.write(response.getOutputStream());
    		addLog("用户：" + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 导出成员记录操作成功,excel密码【"+pwd+"】", WebConstant.Log_Leavel_INFO, WebConstant.LOG_TYPE_EXPORT);
		}catch(Exception e){
			logger.error("<<method:exportRecord()|chat user export error!",e);
		}
	}
	
	/**
	 * 设置导出数据
	 * @param index
	 * @param row
	 * @param mobilePhone
	 * @param userGroup
	 * @param room
	 * @param cgList
	 * @param clgList
	 */
	private void setIRow(int index,DataRowSet dataSet,String mobilePhone,ChatUserGroup userGroup,ChatRoom room,List<ChatGroup> cgList,List<ChatClientGroup> clgList){
		String groupName=room.getId();
		for(ChatGroup cg:cgList){
			if(cg.getId().equals(room.getId())){
				groupName=cg.getName();
				break;
			}
		}
		IRow row=dataSet.append();
		row.set("no", index);
		row.set("mobilePhone", mobilePhone);
		row.set("accountNo", userGroup.getAccountNo());
		row.set("userId",userGroup.getUserId());
		String nickname = userGroup.getNickname();
		if(nickname == null && userGroup.getUserId() != null && userGroup.getUserId().length() == 13){
			nickname = "匿名_"+userGroup.getUserId().substring(8,12);
		}
		row.set("nicknameStr",nickname);
		if(userGroup.getId().contains("studio")){
			if(userGroup.getVipUser()!=null && userGroup.getVipUser()){
				row.set("clientGroup", "VIP用户");
			}else{
				row.set("clientGroup", "真实用户");
				for(ChatClientGroup cg:clgList){
					if(cg.getClientGroupId().equals(userGroup.getClientGroup())){
						row.set("clientGroup", cg.getName());
						break;
					}
				}
			}
		}else{
			row.set("clientGroup", "真实用户");
		}
		row.set("valueUser", userGroup.getValueUser()!=null&&userGroup.getValueUser()?"是":"否");
		row.set("createDate", userGroup.getCreateDate());
		row.set("groupName",groupName);
		row.set("onlineStatus",(room.getOnlineStatus()!=null && 1==room.getOnlineStatus())?"在线":"下线");
		row.set("onlineDate", room.getOnlineDate());
		row.set("gagDate", DateUtil.formatDateWeekTime(room.getGagDate()));
		row.set("gagTimes", userGroup.getGagTimes());
		row.set("sendMsgCount", room.getSendMsgCount()==null?0:room.getSendMsgCount());
	}
}
