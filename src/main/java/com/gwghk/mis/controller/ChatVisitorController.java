package com.gwghk.mis.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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

import com.gwghk.mis.common.model.AjaxJson;
import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DataGrid;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.constant.DictConstant;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.enums.ChatClientGroup;
import com.gwghk.mis.enums.ChatOnlineDuration;
import com.gwghk.mis.model.BoDict;
import com.gwghk.mis.model.ChatGroup;
import com.gwghk.mis.model.ChatVisitor;
import com.gwghk.mis.service.ChatGroupService;
import com.gwghk.mis.service.ChatVisitorService;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.ExcelUtil;
import com.gwghk.mis.util.ResourceBundleUtil;
import com.gwghk.mis.util.ResourceUtil;
import com.gwghk.mis.util.StringUtil;
import com.sdk.orm.DataRowSet;
import com.sdk.orm.IRow;
import com.sdk.poi.POIExcelBuilder;

/**
 * 访客记录管理
 * 
 * @author Alan.wu
 * @date 2015/04/02
 */
@Scope("prototype")
@Controller
public class ChatVisitorController extends BaseController
{

	private static final Logger logger = LoggerFactory.getLogger(ChatVisitorController.class);

	@Autowired
	private ChatGroupService chatGroupService;

	@Autowired
	private ChatVisitorService chatVisitorService;

	/**
	 * 格式成树形列表
	 * 
	 * @param dictList
	 * @return
	 */
	private List<ChatGroup> formatTreeList(List<BoDict> dictList)
	{
		List<ChatGroup> nodeList = new ArrayList<ChatGroup>();
		List<ChatGroup> groupList = chatGroupService.getChatGroupList(getSystemFlag(),"id", "name", "groupType");
		ChatGroup tbean = null;
		for (BoDict dict : dictList)
		{
			tbean = new ChatGroup();
			tbean.setId(dict.getCode());
			tbean.setName(dict.getNameCN());
			nodeList.add(tbean);
			for (ChatGroup group : groupList)
			{
				if (group.getGroupType().equals(dict.getCode()))
				{
					group.setName(StringUtil.fillChar('　', 1) + group.getName());
					nodeList.add(group);
				}
			}
		}
		return nodeList;
	}
	
	/**
	 * 功能：访客记录-标签页
	 * @return
	 */
	@RequestMapping(value = "/chatVisitorController/index", method = RequestMethod.GET)
	public String index()
	{
		return "chat/visitor/visitorTab";
	}

	/**
	 * 功能：访客记录-首页
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/chatVisitorController/visitor", method = RequestMethod.GET)
	public String visitor(HttpServletRequest request, ModelMap map)
	{
		DictConstant dict = DictConstant.getInstance();
		map.put("chatGroupList", this.formatTreeList(ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_TYPE)));
		logger.debug(">>start into chatVisitorController.visitor() and url is /chatVisitorController/visitor.do");
		return "chat/visitor/visitorList";
	}

	/**
	 * 获取datagrid列表
	 * 
	 * @param request
	 * @param dataGrid
	 *            分页查询参数对象
	 * @param chatVisitor
	 *            实体查询参数对象
	 * @return Map<String,Object> datagrid需要的数据
	 */
	@RequestMapping(value = "/chatVisitorController/visitorDatagrid", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> datagridVisitor(HttpServletRequest request, DataGrid dataGrid, ChatVisitor chatVisitor)
	{
		this.resetRoomInfo(chatVisitor.getRoomId(), chatVisitor);
		Page<ChatVisitor> page = chatVisitorService.getChatVisitors(this.createDetachedCriteria(dataGrid, chatVisitor));
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", null == page ? 0 : page.getTotalSize());
		result.put("rows", null == page ? new ArrayList<ChatVisitor>() : page.getCollection());
		return result;
	}

	/**
	 * 功能：导出访客记录(以模板的方式导出)
	 * @param request
	 * @param response
	 * @param chatVisitor
	 */
	@RequestMapping(value = "/chatVisitorController/exportRecord", method = RequestMethod.GET)
	//@ActionVerification(key = "export")
	public void exportRecord(HttpServletRequest request, HttpServletResponse response, ChatVisitor chatVisitor)
	{
		this.resetRoomInfo(chatVisitor.getRoomId(), chatVisitor);
		try
		{
			POIExcelBuilder builder = new POIExcelBuilder(new File(request.getServletContext().getRealPath(WebConstant.CHAT_VISITOR_RECORDS_TEMPLATE_PATH)));
			DataGrid dataGrid = new DataGrid();
			dataGrid.setPage(0);
			dataGrid.setRows(0);
			dataGrid.setSort("updateDate");
			dataGrid.setOrder("desc");
			Page<ChatVisitor> page = chatVisitorService.getChatVisitors(this.createDetachedCriteria(dataGrid, chatVisitor));
			List<ChatVisitor> list = page.getCollection();
			List<ChatGroup> groupList = chatGroupService.getChatGroupList("id", "name", "groupType");
			int totalOnline = 0, totalLogin = 0, totalNeverLogin = 0, totalRegist = 0, totalHasLogin = 0;
			String[] strAgentArr={"iphone","ipod","ipad","android","mobile","playbook","bb10","meego"};
			String agentStr="PC";
			if (list != null && list.size() > 0)
			{
				DataRowSet dataSet = new DataRowSet();
				for (ChatVisitor cm : list)
				{
					IRow row = dataSet.append();
					row.set("userId", StringUtils.isBlank(cm.getUserId())?cm.getVisitorId():cm.getUserId());
					row.set("accountNo", cm.getAccountNo());
					row.set("nickname", cm.getNickname());
					row.set("mobile", cm.getMobile());
					row.set("roomId", "");
					for (ChatGroup rg : groupList)
					{
						if (rg.getId().equals(cm.getRoomId()))
						{
							row.set("roomId", rg.getName());
							break;
						}
					}
					row.set("visitTimes", cm.getVisitTimes());
					row.set("loginTimes", cm.getLoginTimes());
					row.set("onlinePreDate", DateUtil.getDateSecondFormat(cm.getOnlinePreDate()));
					row.set("onlineDate", DateUtil.getDateSecondFormat(cm.getOnlineDate()));
					row.set("loginPreDate", DateUtil.getDateSecondFormat(cm.getLoginPreDate()));
					row.set("loginDate", DateUtil.getDateSecondFormat(cm.getLoginDate()));
					if (cm.getOnlineStatus() != null && cm.getOnlineStatus() == 1)
					{
						row.set("onlineStatus", "在线");
						totalOnline += 1;
					}
					else
					{
						row.set("onlineStatus", "下线");
					}
					row.set("onLineDuration", cm.getOnLineDuration());
					if (cm.getLoginStatus() != null && cm.getLoginStatus() == 1)
					{
						row.set("loginStatus", "已登入");
						totalLogin += 1;
					}
					else
					{
						row.set("loginStatus", "已登出");
					}
					if (cm.getLoginTimes() == null || cm.getLoginTimes() == 0)
					{
						totalNeverLogin += 1;
						row.set("loginStatus", "从未登录");
					}
					else
					{
						totalHasLogin += 1;
					}
					if (StringUtils.isNotBlank(cm.getMobile()))
					{
						totalRegist += 1;
					}
					row.set("ip", cm.getIp());
					row.set("ipCity", cm.getIpCity());
					agentStr="PC";
					if(cm.getUserAgent()!=null){
						for(String str :strAgentArr){
							if(cm.getUserAgent().toLowerCase().contains(str)){
								agentStr="手机";
								break;
							}
						}
					}
					row.set("userAgent", agentStr);
				}
				builder.put("rowSet", dataSet);
			}
			else
			{
				builder.put("rowSet", new DataRowSet());
			}
			builder.put("totalOnline", totalOnline);
			builder.put("totalLogin", totalLogin);
			builder.put("totalNeverLogin", totalNeverLogin);
			builder.put("totalRegist", totalRegist);
			builder.put("totalHasLogin", totalHasLogin);
			builder.parse();
			ExcelUtil.wrapExcelExportResponse("访客记录", request, response);
			builder.write(response.getOutputStream());
			addLog("用户：" + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 导出访客记录操作成功", WebConstant.Log_Leavel_INFO, WebConstant.LOG_TYPE_EXPORT);
		}
		catch (Exception e)
		{
			logger.error("<<method:exportRecord()|chat visitor", e);
		}
	}
	
	/**
	 * 重置房间组别和房间信息
	 * @param groupId
	 * @param chatVisitor
	 * @return
	 */
	private String[] resetRoomInfo(String groupId, ChatVisitor chatVisitor)
	{
		String loc_groupType = groupId.replaceAll("(,|_.+)", "");
		if (groupId.endsWith(","))
		{
			groupId = null;
		}
		if(chatVisitor != null)
		{
			chatVisitor.setGroupType(loc_groupType);
			chatVisitor.setRoomId(groupId);
		}
		return new String[]{loc_groupType, groupId};
	}

	/**
	 * 功能：访客记录-删除
	 * @param request
	 * @param response
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/chatVisitorController/visitorDel", method = RequestMethod.POST)
	@ResponseBody
	//@ActionVerification(key = "delete")
	public AjaxJson del(HttpServletRequest request, HttpServletResponse response, @Param("ids")String ids)
	{
		AjaxJson j = new AjaxJson();
		ApiResult result = chatVisitorService.delete(ids.split(","));
		if (result.isOk())
		{
			j.setSuccess(true);
			String message = "用户：" + userParam.getUserNo() + " " + DateUtil.getDateSecondFormat(new Date()) + "删除访客记录成功";
			addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
			logger.info("<<method:batchDel()|" + message);
		}
		else
		{
			j.setSuccess(false);
			j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
			String message = "用户：" + userParam.getUserNo() + " " + DateUtil.getDateSecondFormat(new Date()) + " 删除访客记录失败";
			addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
			logger.error("<<method:batchDel()|" + message + ",ErrorMsg:" + result.toString());
		}
		return j;
	}
	
	/**
	 * 功能：访客记录-首页
	 * @param request
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/chatVisitorController/report", method = RequestMethod.GET)
	public String report(ModelMap map, @Param("type")String type)
	{
		DictConstant dict = DictConstant.getInstance();
		map.put("chatGroupList", this.formatTreeList(ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_TYPE)));
		Calendar calendar = Calendar.getInstance();
		map.put("today", DateUtil.formatDate(calendar.getTime(), "yyyy-MM-dd"));
		calendar.add(Calendar.DATE, -1);
		map.put("dayBefore1", DateUtil.formatDate(calendar.getTime(), "yyyy-MM-dd"));
		calendar.add(Calendar.DATE, -4);
		map.put("dayBefore5", DateUtil.formatDate(calendar.getTime(), "yyyy-MM-dd"));
		calendar.add(Calendar.DATE, -1);
		map.put("dayBefore6", DateUtil.formatDate(calendar.getTime(), "yyyy-MM-dd"));
		
		if("duration".equals(type)){//在线时长人数
			return "chat/visitorRepD";
		}else if("online".equals(type)){//各类在线人数
			return "chat/visitor/visitorRepO";
		}else{//整点在线人数
			// if("timePoint".equals(type))
			return "chat/visitor/visitorRepT";
		}
	}
	
	/**
	 * 获取报表查询参数
	 * @param request
	 * @return
	 */
	private Object[] getParams(HttpServletRequest request){
		Object[] loc_result = new Object[4];
		String groupId = request.getParameter("groupId");
		String[] groupInfos = this.resetRoomInfo(groupId, null);
		loc_result[0] = groupInfos[0];
		loc_result[1] = groupInfos[1];
		String dateStart = request.getParameter("dateDateStart");
		String dateEnd = request.getParameter("dateDateEnd");
		Date loc_date = StringUtils.isBlank(dateStart) ? DateUtil.getYesterday() : DateUtil.parseDateFormat(dateStart + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
		loc_result[2] = loc_date;
		loc_date = StringUtils.isBlank(dateEnd) ? DateUtil.getYesterday() : DateUtil.parseDateFormat(dateEnd + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
		loc_result[3] = loc_date;
		return loc_result;
	}

	/**
	 * 获取datagrid列表(报表-在线时长人数统计)
	 * @param request
	 * @param groupId
	 * @param dateStart
	 * @param dateEnd
	 * @return
	 */
	@RequestMapping(value = "/chatVisitorController/repDDatagrid", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> datagridRepD(HttpServletRequest request)
	{
		Object[] loc_params = this.getParams(request);
		JSONArray loc_result = chatVisitorService.repVisitorDuration((String)loc_params[0], (String)loc_params[1], (Date)loc_params[2], (Date)loc_params[3]);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", loc_result.size());
		result.put("rows", loc_result);
		return result;
	}

	/**
	 * 功能：导出访客记录(以模板的方式导出-在线时长人数统计)
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/chatVisitorController/exportRepD", method = RequestMethod.GET)
	public void exportRepD(HttpServletRequest request, HttpServletResponse response)
	{
		Object[] loc_params = this.getParams(request);
		try
		{
			POIExcelBuilder builder = new POIExcelBuilder(new File(request.getServletContext().getRealPath(WebConstant.CHAT_VISITOR_REP_D_TEMPLATE_PATH)));
			JSONArray loc_datas = chatVisitorService.repVisitorDuration((String)loc_params[0], (String)loc_params[1], (Date)loc_params[2], (Date)loc_params[3]);
			if (loc_datas != null && loc_datas.isEmpty() == false)
			{
				DataRowSet dataSet = new DataRowSet();
				JSONObject loc_data = null;
				double temp = 0, sum = 0;
				for (int i = 0, lenI = loc_datas.size(); i < lenI; i++)
				{
					loc_data = loc_datas.getJSONObject(i);
					IRow row = dataSet.append();
					row.set("dataDate", loc_data.get("dataDate"));
					sum = loc_data.getInt("statSum");
					for (ChatOnlineDuration duration : ChatOnlineDuration.values())
					{
						temp = loc_data.containsKey(duration.name()) ? loc_data.getInt(duration.name()) : 0;
						row.set(duration.name(), temp);
						row.set("rate_" + duration.name(), sum == 0 ? 0 : temp/sum);
					}
					row.set("statSum", sum);
				}
				builder.put("rowSet", dataSet);
			}
			else
			{
				builder.put("rowSet", new DataRowSet());
			}
			builder.parse();
			ExcelUtil.wrapExcelExportResponse("在线时长用户数量统计", request, response);
			builder.write(response.getOutputStream());
		}
		catch (Exception e)
		{
			logger.error("<<method:exportRepD()|chat visitor report duration", e);
		}
	}

	/**
	 * 获取datagrid列表(报表-各类在线人数统计)
	 * @param request
	 * @param groupId
	 * @param dateStart
	 * @param dateEnd
	 * @return
	 */
	@RequestMapping(value = "/chatVisitorController/repODatagrid", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> datagridRepO(HttpServletRequest request)
	{
		Object[] loc_params = this.getParams(request);
		JSONArray loc_result = chatVisitorService.repVisitorOnline((String)loc_params[0], (String)loc_params[1], (Date)loc_params[2], (Date)loc_params[3]);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", loc_result.size());
		result.put("rows", loc_result);
		return result;
	}

	/**
	 * 功能：导出访客记录(以模板的方式导出-各类在线人数统计)
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/chatVisitorController/exportRepO", method = RequestMethod.GET)
	public void exportRepO(HttpServletRequest request, HttpServletResponse response)
	{
		Object[] loc_params = this.getParams(request);
		try
		{
			POIExcelBuilder builder = new POIExcelBuilder(new File(request.getServletContext().getRealPath(WebConstant.CHAT_VISITOR_REP_O_TEMPLATE_PATH)));
			JSONArray loc_datas = chatVisitorService.repVisitorOnline((String)loc_params[0], (String)loc_params[1], (Date)loc_params[2], (Date)loc_params[3]);
			if (loc_datas != null && loc_datas.isEmpty() == false)
			{
				DataRowSet dataSet = new DataRowSet();
				JSONObject loc_data = null;
				double temp = 0, sum = 0;
				for (int i = 0, lenI = loc_datas.size(); i < lenI; i++)
				{
					loc_data = loc_datas.getJSONObject(i);
					IRow row = dataSet.append();
					row.set("dataDate", loc_data.get("dataDate"));
					sum = loc_data.getInt("statSum");
					for (ChatClientGroup group : ChatClientGroup.values())
					{
						temp = loc_data.containsKey(group.name()) ? loc_data.getInt(group.name()) : 0;
						row.set(group.name(), temp);
						row.set("rate_" + group.name(), sum == 0 ? 0 : temp/sum);
					}
					row.set("statSum", sum);
				}
				builder.put("rowSet", dataSet);
			}
			else
			{
				builder.put("rowSet", new DataRowSet());
			}
			builder.parse();
			ExcelUtil.wrapExcelExportResponse("各类在线人数统计", request, response);
			builder.write(response.getOutputStream());
		}
		catch (Exception e)
		{
			logger.error("<<method:exportRepO()|chat visitor report online", e);
		}
	}
	
	/**
	 * 获取datagrid列表(报表-整点在线人数统计)
	 * @param request
	 * @param groupId
	 * @param dateStart
	 * @param dateEnd
	 * @return
	 */
	@RequestMapping(value = "/chatVisitorController/repTDatagrid", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> datagridRepT(HttpServletRequest request)
	{
		Object[] loc_params = this.getParams(request);
		JSONArray loc_result = chatVisitorService.repVisitorTimePoint((String)loc_params[0], (String)loc_params[1], (Date)loc_params[2], (Date)loc_params[3]);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", loc_result.size());
		result.put("rows", loc_result);
		return result;
	}

	/**
	 * 功能：导出访客记录(以模板的方式导出-整点在线人数统计)
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/chatVisitorController/exportRepT", method = RequestMethod.GET)
	public void exportRepT(HttpServletRequest request, HttpServletResponse response)
	{
		Object[] loc_params = this.getParams(request);
		try
		{
			POIExcelBuilder builder = new POIExcelBuilder(new File(request.getServletContext().getRealPath(WebConstant.CHAT_VISITOR_REP_T_TEMPLATE_PATH)));
			JSONArray loc_datas = chatVisitorService.repVisitorTimePoint((String)loc_params[0], (String)loc_params[1], (Date)loc_params[2], (Date)loc_params[3]);
			if (loc_datas != null && loc_datas.isEmpty() == false)
			{
				DataRowSet dataSet = new DataRowSet();
				JSONObject loc_data = null;
				String time = null;
				for (int i = 0, lenI = loc_datas.size(); i < lenI; i++)
				{
					loc_data = loc_datas.getJSONObject(i);
					IRow row = dataSet.append();
					row.set("dataDate", loc_data.get("dataDate"));
					
					for (int j = 0; j < 24; j++)
					{
						time = (j < 10 ? "0" : "") + j + ":00";
						row.set(time, loc_data.has(time) ? loc_data.getInt(time) : 0);
					}
				}
				builder.put("rowSet", dataSet);
			}
			else
			{
				builder.put("rowSet", new DataRowSet());
			}
			builder.parse();
			ExcelUtil.wrapExcelExportResponse("整点在线人数统计", request, response);
			builder.write(response.getOutputStream());
		}
		catch (Exception e)
		{
			logger.error("<<method:exportRepT()|chat visitor report timePoint", e);
		}
	}
}
