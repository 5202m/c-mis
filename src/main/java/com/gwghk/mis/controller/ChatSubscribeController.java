package com.gwghk.mis.controller;

import com.gwghk.mis.model.ChatUserGroup;
import com.gwghk.mis.model.Member;
import com.gwghk.mis.service.MemberService;
import com.gwghk.mis.service.UserService;
import com.gwghk.mis.util.ExcelUtil;
import com.gwghk.mis.util.PropertiesUtil;
import com.gwghk.mis.util.StringUtil;
import com.sdk.orm.DataRowSet;
import com.sdk.orm.IRow;
import com.sdk.poi.POIExcelBuilder;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.gwghk.mis.authority.ActionVerification;
import com.gwghk.mis.common.model.AjaxJson;
import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DataGrid;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.constant.DictConstant;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.model.BoDict;
import com.gwghk.mis.model.BoUser;
import com.gwghk.mis.model.ChatGroup;
import com.gwghk.mis.model.ChatSubscribe;
import com.gwghk.mis.model.ChatSubscribeType;
import com.gwghk.mis.service.ChatGroupService;
import com.gwghk.mis.service.ChatSubscribeService;
import com.gwghk.mis.service.ChatSubscribeTypeService;
import com.gwghk.mis.util.BrowserUtils;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.IPUtil;
import com.gwghk.mis.util.ResourceBundleUtil;
import com.gwghk.mis.util.ResourceUtil;

/**
 * 
 * @description:   订阅控制类
 * @fileName:ChatSubscribeController.java 
 * @createTime:2016年8月29日 上午10:52:17  
 * @author:Jade.zhu
 * @version 1.0.0  
 *
 */
@Scope("prototype")
@Controller
public class ChatSubscribeController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ChatSubscribeController.class);
	
	@Autowired
	private ChatSubscribeService chatSubscribeService;
	
	@Autowired
	private ChatGroupService chatGroupService;
	
	@Autowired
	private ChatSubscribeTypeService chatSubscribeTypeService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/chatSubscribeController/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request,ModelMap map, String opType){
		DictConstant dict=DictConstant.getInstance();
    	map.put("chatGroupList",this.formatTreeList(ResourceUtil.getSubDictListByParentCode(dict.DICT_CHAT_GROUP_TYPE)));
    	map.put("chatSubscribeType", chatSubscribeTypeService.getSubscribeType());
		return "chat/subscribeList";
	}
	
	/**
	 * 格式成树形列表
	 * @param dictList
	 * @return
	 */
	private List<ChatGroup> formatTreeList(List<BoDict> dictList){
    	List<ChatGroup> nodeList = new ArrayList<ChatGroup>(); 
    	List<ChatGroup> groupList=chatGroupService.getChatGroupList("id","name","groupType");
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
	 * 
	 * @function:  获取订阅列表
	 * @param request
	 * @param dataGrid
	 * @param subscribe
	 * @param opType
	 * @return Map<String,Object>   
	 * @exception 
	 * @author:Jade.zhu
	 * @since  1.0.0
	 */
	@RequestMapping(value = "/chatSubscribeController/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public  Map<String,Object>  datagrid(HttpServletRequest request, DataGrid dataGrid, ChatSubscribe subscribe, String opType){
		
		Page<ChatSubscribe> page = chatSubscribeService.getSubscribePage(this.createDetachedCriteria(dataGrid, subscribe));
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total",null == page ? 0  : page.getTotalSize());
		result.put("rows", null == page ? new ArrayList<ChatSubscribe>() : page.getCollection());
		return result;
	}
	
	/**
	 * 功能：订阅管理-新增
	 */
    @RequestMapping(value="/chatSubscribeController/add", method = RequestMethod.GET)
    @ActionVerification(key="add")
    public String add(ModelMap map, String opType) throws Exception {
    	DictConstant dict=DictConstant.getInstance();
    	map.put("chatGroupList",this.formatTreeList(ResourceUtil.getSubDictListByParentCode(dict.DICT_CHAT_GROUP_TYPE)));
    	map.put("chatSubscribeTypeObj", JSONArray.toJSONString(chatSubscribeTypeService.getSubscribeType()));
    	return "chat/subscribeAdd";
    }

    /**
	 * 功能：晒单管理-查看
	 */
    @RequestMapping(value="/chatSubscribeController/{subscribeId}/view", method = RequestMethod.GET)
    @ActionVerification(key="view")
    public String view(@PathVariable String subscribeId , ModelMap map) throws Exception {
    	ChatSubscribe subscribe=chatSubscribeService.getSubscribeById(subscribeId);
    	map.put("chatSubscribe",subscribe);
    	map.put("startDateStr",DateUtil.getDateSecondFormat(subscribe.getStartDate()));
    	map.put("endDateStr",DateUtil.getDateSecondFormat(subscribe.getEndDate()));
    	
    	DictConstant dict=DictConstant.getInstance();
    	map.put("chatGroupList",this.formatTreeList(ResourceUtil.getSubDictListByParentCode(dict.DICT_CHAT_GROUP_TYPE)));
    	
    	return "chat/subscribeView";
    }
	
    /**
     * 
     * @function:  订阅管理，修改
     * @param subscribeId
     * @param map
     * @param opType
     * @return
     * @throws Exception String   
     * @exception 
     * @author:Jade.zhu
     * @since  1.0.0
     */
    @ActionVerification(key="edit")
    @RequestMapping(value="/chatSubscribeController/{subscribeId}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable String subscribeId , ModelMap map, String opType) throws Exception {
    	ChatSubscribe subscribe=chatSubscribeService.getSubscribeById(subscribeId);
    	map.put("chatSubscribe",subscribe);
    	map.put("startDateStr",DateUtil.getDateSecondFormat(subscribe.getStartDate()));
    	map.put("endDateStr",DateUtil.getDateSecondFormat(subscribe.getEndDate()));
    	
    	DictConstant dict=DictConstant.getInstance();
    	map.put("chatGroupList",this.formatTreeList(ResourceUtil.getSubDictListByParentCode(dict.DICT_CHAT_GROUP_TYPE)));
    	map.put("chatSubscribeTypeObj", JSONArray.toJSONString(chatSubscribeTypeService.getSubscribeType()));
		return "chat/subscribeEdit";
    }
    

    /**
   	 * 功能：订阅管理-保存新增
   	 */
    @RequestMapping(value="/chatSubscribeController/create",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="add")
    public AjaxJson create(HttpServletRequest request,ChatSubscribe chatSubscribe){
    	chatSubscribe.setCreateUser(userParam.getUserNo());
    	chatSubscribe.setCreateIp(IPUtil.getClientIP(request));
    	AjaxJson j = new AjaxJson();
    	
    	String noticeCycle = request.getParameter("noticeCycle");
		Date startDate = DateUtil.parseDateSecondFormat(DateUtil.getDateMilliFormat());
		Date endDate = DateUtil.parseDateSecondFormat(DateUtil.getDateMilliFormat());
    	if("week".equals(noticeCycle)){
        	chatSubscribe.setStartDate(startDate);
    		chatSubscribe.setEndDate(DateUtil.getNextWeek(endDate));
    	} else if("month".equals(noticeCycle)){
        	chatSubscribe.setStartDate(startDate);
    		chatSubscribe.setEndDate(DateUtil.getNextMonth(endDate));
    	}
    	
    	ApiResult result =chatSubscribeService.saveSubscribe(chatSubscribe, false);
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功新增订阅："+userParam.getUserNo();
    		logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.info("<<method:create()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 新增订阅："+userParam.getUserNo()+" 失败";
    		logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.error("<<method:create()|"+message+",ErrorMsg:"+result.toString());
    	}
		return j;
    }
       
   /**
   	* 功能：订阅管理-保存更新
   	*/
    @RequestMapping(value="/chatSubscribeController/update",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="edit")
    public AjaxJson update(HttpServletRequest request,ChatSubscribe chatSubscribe){
    	chatSubscribe.setUpdateUser(userParam.getUserNo());
    	chatSubscribe.setUpdateIp(IPUtil.getClientIP(request));
    	AjaxJson j = new AjaxJson();
    	
    	ApiResult result =chatSubscribeService.saveSubscribe(chatSubscribe, true);
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功修改订阅："+userParam.getUserNo();
    		logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_UPDATE
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.info("<--method:update()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 修改订阅："+userParam.getUserNo()+" 失败";
    		logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.error("<--method:update()|"+message+",ErrorMsg:"+result.toString());
    	}
   		return j;
     }

    /**
  	* 功能：晒单管理-单条记录删除
  	*/
    @RequestMapping(value="/chatSubscribeController/oneDel",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson oneDel(HttpServletRequest request,HttpServletResponse response){
    	
  		BoUser userParam = ResourceUtil.getSessionUser();
    	String delId = request.getParameter("id");
    	AjaxJson j = new AjaxJson();
    	ApiResult result = chatSubscribeService.deleteSubscibe(new String[]{delId});
    	if(result.isOk()){
          	j.setSuccess(true);
          	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除订阅成功";
          	logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL
          					 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.info("<<method:oneDel()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除订阅失败";
    		logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.error("<<method:oneDel()|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
  		
    }
    /**
  	* 功能：晒单管理-多条记录删除
  	*/
    @RequestMapping(value="/chatSubscribeController/batchDel",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson batchDel(HttpServletRequest request,HttpServletResponse response){
    	
  		BoUser userParam = ResourceUtil.getSessionUser();
    	String delIds = request.getParameter("ids");
    	AjaxJson j = new AjaxJson();
    	ApiResult result = chatSubscribeService.deleteSubscibe(delIds.contains(",")?delIds.split(","):new String[]{delIds});
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 批量删除订阅成功";
    		logService.addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.info("<<method:batchDel()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 批量删除订阅失败";
    		logService.addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL
    						 ,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
    		logger.error("<<method:batchDel()|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
  		
    }

	/**
	 * 导出客户订阅服务列表
	 * @param request
	 * @param response
	 * @param chatSubscribe
	 */
	@RequestMapping(value = "/chatSubscribeController/exportRecord", method = RequestMethod.GET)
	public void exportRecord(HttpServletRequest request, HttpServletResponse response, ChatSubscribe chatSubscribe){
		try {
			DictConstant dict=DictConstant.getInstance();

			POIExcelBuilder builder = new POIExcelBuilder(new File(request.getServletContext().getRealPath(WebConstant.CHAT_SUBSCRIBE_RECORDS_TEMPLATE_PATH)));
			String pwd= StringUtil.random(6);
			builder.getHSSFWorkbook().getSheetAt(0).protectSheet(pwd);
			DataGrid dataGrid = new DataGrid();
			dataGrid.setPage(0);
			dataGrid.setRows(0);
			dataGrid.setSort("updateDate");
			dataGrid.setOrder("desc");
			Page<ChatSubscribe> page = chatSubscribeService.getSubscribePage(this.createDetachedCriteria(dataGrid, chatSubscribe));
			List<ChatSubscribe> list = page.getCollection();
			List<BoDict> boDictList = ResourceUtil.getSubDictListByParentCode(dict.DICT_CHAT_GROUP_TYPE);
			DataGrid subscribeTypedataGrid = new DataGrid();
			subscribeTypedataGrid.setPage(0);
			subscribeTypedataGrid.setRows(0);
			subscribeTypedataGrid.setSort("updateDate");
			subscribeTypedataGrid.setOrder("desc");
			List<Member> memberList = null;
			ChatSubscribeType chatSubscribeType = new ChatSubscribeType();
			if(StringUtils.isNotBlank(chatSubscribe.getGroupType())) {
				chatSubscribeType.setGroupType(chatSubscribe.getGroupType());
			}
			memberList = memberService.getMemberByGroupType(chatSubscribe.getGroupType());
			Page<ChatSubscribeType> chatSubscribeTypePage = chatSubscribeTypeService.getSubscribeTypePage(this.createDetachedCriteria(subscribeTypedataGrid, chatSubscribeType));
			List<ChatSubscribeType> chatSubscribeTypeList = chatSubscribeTypePage.getCollection();
			List<BoUser> allAnalysts = getAnalystsList();
			if (list != null && list.size() > 0) {
				DataRowSet dataSet = new DataRowSet();
				for(ChatSubscribe cs : list){
					String roomName = "", accountNo = "", nickName = "", analystName = "", subscribeType = "";
					for (BoDict bd : boDictList) {
						if (bd.getCode().equals(cs.getGroupType())) {
							roomName = bd.getNameCN();
							break;
						}
					}
					if(memberList != null && memberList.size() > 0){
						for (Member member : memberList) {
							if (StringUtils.isNotBlank(cs.getUserId()) && StringUtils.isNotBlank(member.getMobilePhone()) && member.getMobilePhone().equals(cs.getUserId())) {
								if(member.getLoginPlatform().getChatUserGroup().size()>0){
									List<ChatUserGroup> chatUserGroupList = member.getLoginPlatform().getChatUserGroup();
									for(ChatUserGroup cug : chatUserGroupList){
										if(cug.getId().equals(cs.getGroupType())){
											if (StringUtils.isNotBlank(cug.getAccountNo())) {
												accountNo = cug.getAccountNo();
											}
											if (StringUtils.isNotBlank(cug.getNickname())) {
												nickName = cug.getNickname();
											}
											break;
										}
									}
								} else {
									ChatUserGroup chatUserGroup = member.getLoginPlatform().getChatUserGroup().get(0);
									if (StringUtils.isNotBlank(chatUserGroup.getAccountNo())) {
										accountNo = chatUserGroup.getAccountNo();
									}
									if (StringUtils.isNotBlank(chatUserGroup.getNickname())) {
										nickName = chatUserGroup.getNickname();
									}
								}
								break;
							}
						}
					}
					if(chatSubscribeTypeList != null && chatSubscribeTypeList.size() > 0){
						for(ChatSubscribeType cst : chatSubscribeTypeList){
							if(cst.getCode().equals(cs.getType())){
								subscribeType = cst.getName();
								break;
							}
						}
					}
					IRow row = dataSet.append();
					row.set("roomName", roomName);
					row.set("mobilePhone", cs.getUserId());
					row.set("accountNo", accountNo);
					row.set("type", subscribeType);
					row.set("nickName", nickName);
					row.set("userId", StringUtils.isNumeric(cs.getUserId())?StringUtil.formatMobileToUserId(cs.getUserId()):cs.getUserId());
					if(StringUtils.isNotBlank(cs.getNoticeType())) {
						row.set("noticeType", cs.getNoticeType() == "email" ? "邮件" : "短信");
					}else{
						row.set("noticeType", "");
					}
					row.set("startDate", cs.getStartDate());
					row.set("endDate", cs.getEndDate());
					row.set("remarks", cs.getRemarks());
					for(BoUser user : allAnalysts){
						if(StringUtils.indexOf(user.getUserNo(), cs.getAnalyst()) > -1){
							if(StringUtils.isNotBlank(analystName)){
								analystName += ",";
							}
							analystName += user.getUserName();
						}
					}
					row.set("analysts", analystName);
					row.set("valid", cs.getValid()==1?"有效":"无效");
				}
				builder.put("rowSet", dataSet);
			} else {
				builder.put("rowSet", new DataRowSet());
			}
			builder.parse();
			ExcelUtil.wrapExcelExportResponse("客户订阅服务列表", request, response);
			builder.write(response.getOutputStream());
			logService.addLog("用户：" + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 导出客户订阅服务操作成功,excel密码【"+pwd+"】", WebConstant.Log_Leavel_INFO, WebConstant.LOG_TYPE_EXPORT,BrowserUtils.checkBrowse(request),IPUtil.getClientIP(request));
		} catch (Exception e) {
			logger.error("<<method:exportRecord()|chat Subscribe>>", e);
		}
	}

	/**
	 * 获取所有分析师
	 * @return
	 */
	private List<BoUser> getAnalystsList(){
		List<BoUser> allAnalysts = userService.getUserListByRole("analyst");
		String[] nameArr={"梁育诗","罗恩•威廉","黃湛铭","赵相宾","周游","刘敏","陈杭霞","金道研究院"};
		BoUser user=null;
		for(int i = 0, len = nameArr.length; i < len; i++){
			user = new BoUser();
			user.setUserId(nameArr[i]);
			user.setUserNo(nameArr[i]);
			user.setUserName(nameArr[i]);
			user.setPosition("金道研究院");
			allAnalysts.add(user);
		}
		return allAnalysts;
	}
}
