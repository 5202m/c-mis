package com.gwghk.mis.controller;

import com.gwghk.mis.authority.ActionVerification;
import com.gwghk.mis.common.model.AjaxJson;
import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DataGrid;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.constant.DictConstant;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.model.BoDict;
import com.gwghk.mis.model.BoUser;
import com.gwghk.mis.model.ChatClientGroup;
import com.gwghk.mis.model.ChatGroup;
import com.gwghk.mis.model.ChatMessage;
import com.gwghk.mis.model.ChatMsgToUser;
import com.gwghk.mis.service.ChatClientGroupService;
import com.gwghk.mis.service.ChatGroupService;
import com.gwghk.mis.service.ChatMessgeService;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.ExcelUtil;
import com.gwghk.mis.util.ResourceBundleUtil;
import com.gwghk.mis.util.ResourceUtil;
import com.gwghk.mis.util.StringUtil;
import com.sdk.orm.DataRowSet;
import com.sdk.orm.IRow;
import com.sdk.poi.POIExcelBuilder;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

/**
 * 聊天室信息管理
 * @author Alan.wu
 * @date   2015/04/02
 */
@Scope("prototype")
@Controller
public class ChatMessageController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(ChatMessageController.class);
	@Autowired
	private ChatMessgeService chatMessageService;
	@Autowired
	private ChatGroupService chatGroupService;
	@Autowired
	private ChatClientGroupService chatClientGroupService;
	
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
	 * 功能：聊天室信息管理-首页
	 */
	@RequestMapping(value = "/chatMessageController/index", method = RequestMethod.GET)
	public  String  index(HttpServletRequest request,ModelMap map){
		DictConstant dict=DictConstant.getInstance();
		List<BoDict> dictList=ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_USE_STATUS);
    	map.put("statusList", dictList);
    	map.put("chatGroupList",this.formatTreeList(ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_TYPE)));
    	map.put("clientGroupList", chatClientGroupService.getClientGroupList(null,getSystemFlag()));
		logger.debug(">>start into chatMessageController.index() and url is /chatMessageController/index.do");
		return "chat/message/messageList";
	}
	
	/**
	 * 设置通用查询
	 * @param request
	 * @param chatMessage
	 */
	private void setComSearch(HttpServletRequest request,ChatMessage chatMessage){
		 setSystemFlag(chatMessage);
		 chatMessage.setPublishStartDateStr(request.getParameter("publishStartDateStr"));
		 chatMessage.setPublishEndDateStr(request.getParameter("publishEndDateStr"));
		 String talkStyle=request.getParameter("talkStyle");
		 String userType = request.getParameter("toUserLabel");
		 ChatMsgToUser msgToUser=new ChatMsgToUser();
		 if(StringUtils.isNotBlank(talkStyle)){
			 msgToUser.setTalkStyle(Integer.parseInt(talkStyle));
		 }
		 if(StringUtils.isNotBlank(userType)){
			 msgToUser.setUserType(Integer.parseInt(userType));
		 }
		 if(msgToUser.getTalkStyle() != null || msgToUser.getUserType() != null) {
			 chatMessage.setToUser(msgToUser);
		 }
	}

	/**
	 * 获取datagrid列表
	 * @param request
	 * @param dataGrid  分页查询参数对象
	 * @param chatMessage   实体查询参数对象
	 * @return Map<String,Object> datagrid需要的数据
	 */
	@RequestMapping(value = "/chatMessageController/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public  Map<String,Object>  datagrid(HttpServletRequest request, DataGrid dataGrid,ChatMessage chatMessage){
		 this.setComSearch(request,chatMessage);
		 Page<ChatMessage> page = chatMessageService.getChatMessagePage(this.createDetachedCriteria(dataGrid, chatMessage), false);
		 Map<String, Object> result = new HashMap<String, Object>();
		 result.put("total",null == page ? 0  : page.getTotalSize());
	     result.put("rows", null == page ? new ArrayList<ChatMessage>() : page.getCollection());
	     return result;
	}
	
	/**
	 * 功能：导出聊天记录(以模板的方式导出)
	 */
	@RequestMapping(value = "/chatMessageController/exportRecord", method = RequestMethod.GET)
	@ActionVerification(key="export")
	public void exportRecord(HttpServletRequest request, HttpServletResponse response,ChatMessage chatMessage){
		try{
			this.setComSearch(request,chatMessage);
			DataGrid dataGrid = new DataGrid();
			dataGrid.setPage(0);
			dataGrid.setRows(0);
			dataGrid.setSort("publishTime");
			dataGrid.setOrder("desc");
			chatMessage.getContent().setMsgType("text");  //默认只导出文本类型
			Page<ChatMessage> page = chatMessageService.getChatMessagePage(this.createDetachedCriteria(dataGrid, chatMessage), true);
			List<ChatGroup> groupList=chatGroupService.getChatGroupList(getSystemFlag(),"id","name","groupType");
			List<ChatMessage>  chatMessageList = page.getCollection();
      List<ChatMessage> exportList=new ArrayList<ChatMessage>(),tmpList=null,filterList=null;
      if(chatMessage.getToUser() != null && chatMessage.getToUser().getUserId() != null) {
        Map<String, List<ChatMessage>> lstGrp = chatMessageList.stream().filter(r -> !r.getToUser().getUserId().equals(chatMessage.getUserId())).collect(Collectors.groupingBy(p -> p.getToUser().getUserId()));
        Object[] keyStr = lstGrp.keySet().toArray();
        for (Object e : keyStr) {
          tmpList = lstGrp.get(e);
          filterList = chatMessageList.stream().filter(t -> e.equals(t.getUserId()))
              .collect(Collectors.toList());
          if (filterList != null && filterList.size() > 0) {
            tmpList.addAll(filterList);
          }
          tmpList.sort((a, b) -> a.getPublishTime().compareTo(b.getPublishTime()));
          exportList.addAll(tmpList);
        }
      }else{
        exportList = chatMessageList;
      }
			ChatMsgToUser toUser=null;
			String toUserName="房间所有人";
			List<ChatClientGroup> clientGroups = chatClientGroupService.getClientGroupList(chatMessage.getGroupId().replaceAll("[,_].*$", ""),getSystemFlag());
			Map<String, String> clientGroupMap = new HashMap<String, String>();
			for(ChatClientGroup cg : clientGroups){
				clientGroupMap.put(cg.getClientGroupId(), cg.getName());
			}
			String pwd=StringUtil.random(6);
			POIExcelBuilder builder = new POIExcelBuilder(new File(request.getServletContext().getRealPath(WebConstant.CHAT_RECORDS_TEMPLATE_PATH)));
            builder.getHSSFWorkbook().getSheetAt(0).protectSheet(pwd);
			if(exportList != null && exportList.size() > 0){
				DataRowSet dataSet = new DataRowSet();
				for(ChatMessage cm : exportList){
					IRow row = dataSet.append();
					//row.set("userId", cm.getGroupType().contains("studio")?cm.getUserId():(StringUtils.isBlank(cm.getAccountNo())?cm.getUserId():cm.getAccountNo()));
					row.set("userId", StringUtils.isBlank(cm.getAccountNo())?cm.getUserId():cm.getAccountNo());
					String userTypeVal = "";
					String userType = cm.getUserType() == null ? "0" : cm.getUserType().toString();
					if("1".equals(userType)){
						userTypeVal = "管理员";
					}else if("2".equals(userType)){
						userTypeVal = "分析师";
					}else if("3".equals(userType)){
						userTypeVal = "客服";
					}else if("-1".equals(userType)){
						userTypeVal = "游客";
					}else{
						userTypeVal = "真实";
						if(clientGroupMap.containsKey(cm.getClientGroup())){
							userTypeVal = clientGroupMap.get(cm.getClientGroup());
						}
					}
					row.set("mobilePhone", cm.getMobilePhone());
					row.set("nickname", cm.getNickname());
					row.set("userType", userTypeVal);
					row.set("groupId", "");
					for(ChatGroup rg:groupList){
						if(rg.getId().equals(cm.getGroupId())){
							row.set("groupId", rg.getName());
							break;
						}
					}
					row.set("fromPlatform", cm.getFromPlatform());
					row.set("msgType", "文本");
					row.set("content", cm.getContent().getValue());
					row.set("publishTime",DateUtil.longMsTimeConvertToDateTime(Long.valueOf(cm.getPublishTime().split("_")[0])));
					row.set("approvalUserNo", cm.getApprovalUserNo());
					row.set("status", cm.getStatus()==1?"通过":(cm.getStatus()==2?"拒绝":"等待审批"));//0、等待审批，1、通过 ；2、拒绝
					row.set("valid", cm.getValid()==1?"正常":"删除");
					toUser=cm.getToUser();
					row.set("talkStyle", "公聊");
					if(toUser!=null && StringUtils.isNotBlank(toUser.getUserId())){
						toUserName=toUser.getNickname();
						row.set("talkStyle", toUser.getTalkStyle()==1?"私聊":"@TA");
					}
					row.set("toUserName", toUserName);
				}
				builder.put("rowSet",dataSet);
			}else{
				builder.put("rowSet",new DataRowSet());
			}
			builder.parse();
			String title=ExcelUtil.wrapExcelExportResponse("聊天记录", request, response);
			builder.write(response.getOutputStream());
			addLog("用户：" + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 导出聊天记录操作成功,excel密码【"+pwd+"】！", WebConstant.Log_Leavel_INFO, WebConstant.LOG_TYPE_EXPORT);
		}catch(Exception e){
			logger.error("<<method:exportRecord()|chat message export error!",e);
		}
	}
	
	/**
  	* 功能：聊天室信息管理-删除
  	*/
    @RequestMapping(value="/chatMessageController/approvalMsg",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="approve_chat_pass")
    public AjaxJson approve(HttpServletRequest request,HttpServletResponse response){
    	BoUser boUser = ResourceUtil.getSessionUser();
    	String publishTimeArr = request.getParameter("publishTimeArr"),
    			        fUserIdArr=request.getParameter("fuIdArr"),
    					status=request.getParameter("status"),
						groupId=request.getParameter("groupId");
    	AjaxJson j = new AjaxJson();
    	if(StringUtils.isBlank(publishTimeArr)||StringUtils.isBlank(fUserIdArr)||StringUtils.isBlank(status)||StringUtils.isBlank(groupId)){
    		j.setSuccess(false);
    		j.setMsg("参数有误，请检查！");
    		return j;
    	}
    	ApiResult result =chatMessageService.approvalMsg(boUser.getUserNo(),publishTimeArr,fUserIdArr,status,groupId, getSystemFlag());
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = "用户：" + boUser.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 审核聊天室信息成功";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:batchDel()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = "用户：" + boUser.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 审核聊天室信息失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:batchDel()|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
    }
	
   /**
  	* 功能：聊天室信息管理-删除
  	*/
    @RequestMapping(value="/chatMessageController/del",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson del(HttpServletRequest request,HttpServletResponse response){
    	BoUser boUser = ResourceUtil.getSessionUser();
    	String delIds = request.getParameter("ids");
    	String year=request.getParameter("year");
    	AjaxJson j = new AjaxJson();
    	if(StringUtils.isBlank(year)){
    		j.setSuccess(false);
    		j.setMsg("删除数据所在年份有误，请检查！");
    		return j;
    	}
    	if(StringUtils.isBlank(delIds)){
    		delIds = request.getParameter("id");
    	}
    	ApiResult result =chatMessageService.deleteChatMessage(delIds.split(","),Integer.parseInt(year), getSystemFlag());
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = "用户：" + boUser.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除聊天信息成功";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:batchDel()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = "用户：" + boUser.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除聊天信息失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:batchDel()|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
    }
}
