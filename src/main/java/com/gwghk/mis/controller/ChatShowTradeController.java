package com.gwghk.mis.controller;

import com.alibaba.fastjson.JSONArray;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.alibaba.druid.util.StringUtils;
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
import com.gwghk.mis.model.ChatShowTrade;
import com.gwghk.mis.service.ChatGroupService;
import com.gwghk.mis.service.ChatShowTradeService;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.IPUtil;
import com.gwghk.mis.util.ResourceBundleUtil;
import com.gwghk.mis.util.ResourceUtil;

/**
 * 摘要：晒单管理
 * @author henry.cao
 * @date   2016-06-22
 */
@Scope("prototype")
@Controller
public class ChatShowTradeController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(ChatShowTradeController.class);
	
	@Autowired
	private ChatShowTradeService chatShowTradeService;
	
	@Autowired
	private ChatGroupService chatGroupService;
	

	
	/**
	 * 功能：晒单管理-首页
	 */
	@RequestMapping(value = "/chatShowTradeController/index", method = RequestMethod.GET)
	public  String  index(HttpServletRequest request,ModelMap map, String opType){
		
		DictConstant dict=DictConstant.getInstance();
    	map.put("chatGroupList",this.formatTreeList(ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_TYPE)));
    	
		return "chat/trade/showTradeList";
	}
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
	 * 获取datagrid列表
	 * @param request
	 * @param dataGrid  分页查询参数对象
	 * @param chatShowTrade   实体查询参数对象
	 * @return Map<String,Object> datagrid需要的数据
	 */
	@RequestMapping(value = "/chatShowTradeController/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public  Map<String,Object>  datagrid(HttpServletRequest request, DataGrid dataGrid,ChatShowTrade chatShowTrade, String opType){
		 String userNo = request.getParameter("userNo");
		 String userName = request.getParameter("userName");
		 BoUser user=new BoUser();
		 if(userNo != null){
		     user.setUserNo(userNo);
		 }
		 if(userName != null){
			 user.setUserName(userName);
		 }
	     chatShowTrade.setBoUser(user);
	     setSystemFlag(chatShowTrade);
		 Page<ChatShowTrade> page = chatShowTradeService.getShowTradePage(this.createDetachedCriteria(dataGrid, chatShowTrade));
		 Map<String, Object> result = new HashMap<String, Object>();
		 result.put("total",null == page ? 0  : page.getTotalSize());
	     result.put("rows", null == page ? new ArrayList<ChatShowTrade>() : page.getCollection());
	     return result;
	}
	/**
	 * 功能：晒单管理-新增
	 */
    @RequestMapping(value="/chatShowTradeController/add", method = RequestMethod.GET)
    @ActionVerification(key="add")
    public String add(ModelMap map, String opType) throws Exception {
    	DictConstant dict=DictConstant.getInstance();
    	map.put("chatGroupList",this.formatTreeList(ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_TYPE)));
    	return "chat/trade/showTradeAdd";
    }
    
    /**
  	* 功能：晒单管理-单条记录删除
  	*/
    @RequestMapping(value="/chatShowTradeController/oneDel",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson oneDel(HttpServletRequest request,HttpServletResponse response){
    	String delId = request.getParameter("id");
    	AjaxJson j = new AjaxJson();
    	ApiResult result = chatShowTradeService.deleteTrade(new String[]{delId});
    	if(result.isOk()){
          	j.setSuccess(true);
          	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除晒单成功";
          	addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL
          					 );
    		logger.info("<<method:oneDel()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除晒单失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL
    						 );
    		logger.error("<<method:oneDel()|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
  		
    }
    /**
  	* 功能：晒单管理-多条记录删除
  	*/
    @RequestMapping(value="/chatShowTradeController/batchDel",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson batchDel(HttpServletRequest request,HttpServletResponse response){
    	String delIds = request.getParameter("ids");
    	AjaxJson j = new AjaxJson();
    	ApiResult result = chatShowTradeService.deleteTrade(delIds.contains(",")?delIds.split(","):new String[]{delIds});
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 批量删除晒单成功";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:batchDel()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 批量删除晒单失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:batchDel()|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
  		
    }
    
    /**
	 * 功能：晒单管理-查看
	 */
    @RequestMapping(value="/chatShowTradeController/{showTradeId}/view", method = RequestMethod.GET)
    @ActionVerification(key="view")
    public String view(@PathVariable String showTradeId , ModelMap map) throws Exception {
    	ChatShowTrade chatTrade=chatShowTradeService.getTradeById(showTradeId);
    	map.put("chatTrade",chatTrade);
    	
    	DictConstant dict=DictConstant.getInstance();
    	map.put("chatGroupList",this.formatTreeList(ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_TYPE)));
    	
    	map.put("showDateFormat" ,DateUtil.formatDate(chatTrade.getShowDate(), "yyyy-MM-dd HH:mm:ss"));
    	
    	return "chat/trade/showTradeView";
    }
	
    @ActionVerification(key="edit")
    @RequestMapping(value="/chatShowTradeController/{showTradeId}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable String showTradeId , ModelMap map, String opType) throws Exception {
    	ChatShowTrade chatTrade=chatShowTradeService.getTradeById(showTradeId);
    	map.put("chatTrade",chatTrade);
    	
    	DictConstant dict=DictConstant.getInstance();
    	map.put("chatGroupList",this.formatTreeList(ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_TYPE)));
    	
		return "chat/trade/showTradeEdit";
    }
    
    /**
   	 * 功能：晒单管理-保存新增
   	 */
    @RequestMapping(value="/chatShowTradeController/create",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="add")
    public AjaxJson create(HttpServletRequest request,ChatShowTrade chatShowTrade){
    	chatShowTrade.setCreateUser(userParam.getUserNo());
    	chatShowTrade.setCreateIp(IPUtil.getClientIP(request));
    	AjaxJson j = new AjaxJson();
    	String userNo = request.getParameter("userNo");
		 if(userNo != null){
		     BoUser user=new BoUser();
		     user.setUserNo(userNo);
		     chatShowTrade.setBoUser(user);
		 }
    	
    	ApiResult result =chatShowTradeService.saveTrade(chatShowTrade, false);
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功新增晒单："+userParam.getUserNo();
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
    		logger.info("<<method:create()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 新增晒单："+userParam.getUserNo()+" 失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<<method:create()|"+message+",ErrorMsg:"+result.toString());
    	}
		return j;
    }
       
   /**
   	* 功能：晒单管理-保存更新
   	*/
    @RequestMapping(value="/chatShowTradeController/update",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="edit")
    public AjaxJson update(HttpServletRequest request,ChatShowTrade chatShowTrade){
    	chatShowTrade.setUpdateUser(userParam.getUserNo());
    	chatShowTrade.setUpdateIp(IPUtil.getClientIP(request));
    	AjaxJson j = new AjaxJson();
    	String userNo = request.getParameter("userNo");
		 if(userNo != null){
		     BoUser user=new BoUser();
		     user.setUserNo(userNo);
		     if(chatShowTrade.getTradeType()==2){
		    	 String avatar = request.getParameter("avatar");
		    	 String userName = request.getParameter("userName");
		    	 String telePhone = request.getParameter("telephone"); 
		    	 userNo = request.getParameter("userId");
		    	 user.setUserNo(userNo);
		    	 user.setAvatar(avatar);
		    	 user.setUserName(userName);
		    	 user.setTelephone(telePhone);
		     }
		     chatShowTrade.setBoUser(user);
		 }
    	
    	ApiResult result =chatShowTradeService.saveTrade(chatShowTrade, true);
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功修改晒单："+userParam.getUserNo();
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_UPDATE);
    		logger.info("<--method:update()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 修改晒单："+userParam.getUserNo()+" 失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_UPDATE);
    		logger.error("<--method:update()|"+message+",ErrorMsg:"+result.toString());
    	}
   		return j;
     }
    
    /**
     * 批量审核晒单状态
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/chatShowTradeController/batchSetStatus",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="setStatus")
    public AjaxJson modifyTradeStatus(HttpServletRequest request, HttpServletResponse response){
    	String tradeIds = request.getParameter("tradeIds");
    	int status = StringUtils.stringToInteger(request.getParameter("status"));
    	AjaxJson j = new AjaxJson();
    	ApiResult result = chatShowTradeService.modifyTradeStatusByIds(tradeIds.contains(",")?tradeIds.split(","):new String[]{tradeIds}, status, getSystemFlag());
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 批量审核晒单成功";
    		addLog(message, WebConstant.Log_Leavel_INFO, (status == 1 ? WebConstant.Log_Type_APPROVE_ShowTrade : WebConstant.Log_Type_CANCEL_APPROVE_ShowTrade));
    		logger.info("<<method:batchDel()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 批量审核晒单失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, (status == 1 ? WebConstant.Log_Type_APPROVE_ShowTrade : WebConstant.Log_Type_CANCEL_APPROVE_ShowTrade));
    		logger.error("<<method:batchDel()|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
    }

	/**
	 * 跳转到评论页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/chatShowTradeController/review",method=RequestMethod.POST,produces = "plain/text; charset=UTF-8")
	@ActionVerification(key="review")
	@ResponseBody
	public String preReview(HttpServletRequest request, ModelMap map){
		String dataId = request.getParameter("sid");
		if(StringUtils.isEmpty(dataId)){
			return null;
		}
		ChatShowTrade data = chatShowTradeService.getTradeById(dataId);
		map.put("data", data);
		return JSONArray.toJSONString(map);
	}

	/**
	 * 删除评论
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/chatShowTradeController/delComment",method=RequestMethod.POST)
	@ActionVerification(key="delete")
	@ResponseBody
	public AjaxJson delComment(HttpServletRequest request){
		String sid = request.getParameter("sid");
		String cid = request.getParameter("cid");
		AjaxJson j = new AjaxJson();
		ApiResult result = chatShowTradeService.delComment(sid, cid);
		if(result.isOk()){
			j.setSuccess(true);
			String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除晒单评论成功：" + sid + "!";
			addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL_ShowTrade_Comment);
			logger.info("<<delete()|"+message);
		}else{
			j.setSuccess(false);
			j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
			String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除晒单评论失败：" + sid + "!";
			addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL_ShowTrade_Comment);
			logger.error("<<delete()|"+message+",ErrorMsg:"+result.toString());
		}
		return j;
	}

}