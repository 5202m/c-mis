/**   
 * @projectName:pm_mis  
 * @packageName:com.gwghk.mis.controller  
 * @className:ChatSubscribeTypeController.java  
 *   
 * @createTime:2016年8月31日-下午1:38:08  
 *  
 *    
 */
package com.gwghk.mis.controller;

import com.gwghk.mis.util.PropertiesUtil;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
import com.gwghk.mis.model.ChatSubscribeType;
import com.gwghk.mis.service.ChatGroupService;
import com.gwghk.mis.service.ChatSubscribeTypeService;
import com.gwghk.mis.service.UserService;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.IPUtil;
import com.gwghk.mis.util.JsonUtil;
import com.gwghk.mis.util.ResourceBundleUtil;
import com.gwghk.mis.util.ResourceUtil;

/**   
 * @description:   订阅配置Controller类
 * @fileName:ChatSubscribeTypeController.java 
 * @createTime:2016年8月31日 下午1:38:08  
 * @author:Jade.zhu
 * @version 1.0.0  
 *    
 */
@Scope("prototype")
@Controller
public class ChatSubscribeTypeController extends BaseController {
private static final Logger logger = LoggerFactory.getLogger(ChatSubscribeTypeController.class);
	
	@Autowired
	private ChatSubscribeTypeService chatSubscribeTypeService;
	
	@Autowired
	private ChatGroupService chatGroupService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/chatSubscribeTypeController/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request,ModelMap map, String opType){
		DictConstant dict=DictConstant.getInstance();
    	map.put("chatGroupList",this.formatTreeList(ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_TYPE)));
		return "chat/subscribe/subscribeTypeList";
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
	 * @function:  订阅配置列表
	 * @param request
	 * @param dataGrid
	 * @param subscribeType
	 * @param opType
	 * @return Map<String,Object>   
	 * @exception 
	 * @author:Jade.zhu
	 * @since  1.0.0
	 */
	@RequestMapping(value = "/chatSubscribeTypeController/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public  Map<String,Object>  datagrid(HttpServletRequest request, DataGrid dataGrid, ChatSubscribeType subscribeType, String opType){
		
		Page<ChatSubscribeType> page = chatSubscribeTypeService.getSubscribeTypePage(this.createDetachedCriteria(dataGrid, subscribeType));
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total",null == page ? 0  : page.getTotalSize());
		result.put("rows", null == page ? new ArrayList<ChatSubscribeType>() : page.getCollection());
		return result;
	}
	
	/**
	 * 
	 * @function:  订阅配置添加页面
	 * @param map
	 * @param opType
	 * @return
	 * @throws Exception String   
	 * @exception 
	 * @author:Jade.zhu
	 * @since  1.0.0
	 */
	@RequestMapping(value="/chatSubscribeTypeController/add", method = RequestMethod.GET)
    @ActionVerification(key="add")
    public String add(ModelMap map, String opType) throws Exception {
    	DictConstant dict=DictConstant.getInstance();
    	map.put("chatGroupList",this.formatTreeList(ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_TYPE)));
    	return "chat/subscribe/subscribeTypeAdd";
    }
	
	/**
	 * 
	 * @function:  订阅配置查看页面
	 * @param subscribeTypeId
	 * @param map
	 * @return
	 * @throws Exception String   
	 * @exception 
	 * @author:Jade.zhu
	 * @since  1.0.0
	 */
	@RequestMapping(value="/chatSubscribeTypeController/{subscribeTypeId}/view", method = RequestMethod.GET)
    @ActionVerification(key="view")
    public String view(@PathVariable String subscribeTypeId , ModelMap map) throws Exception {
    	ChatSubscribeType subscribeType=chatSubscribeTypeService.getSubscribeTypeById(subscribeTypeId);
    	map.put("chatSubscribeType",subscribeType);
    	
    	DictConstant dict=DictConstant.getInstance();
    	map.put("chatGroupList",this.formatTreeList(ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_TYPE)));
    	
    	return "chat/subscribe/subscribeTypeView";
    }
	
	/**
	 * 
	 * @function: 订阅配置编辑页面  
	 * @param subscribeTypeId
	 * @param map
	 * @param opType
	 * @return
	 * @throws Exception String   
	 * @exception 
	 * @author:Jade.zhu
	 * @since  1.0.0
	 */
	@ActionVerification(key="edit")
    @RequestMapping(value="/chatSubscribeTypeController/{subscribeTypeId}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable String subscribeTypeId , ModelMap map, String opType) throws Exception {
		DictConstant dict=DictConstant.getInstance();
    	ChatSubscribeType subscribeType=chatSubscribeTypeService.getSubscribeTypeById(subscribeTypeId);
    	map.put("chatGroupList",this.formatTreeList(ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_TYPE)));
    	map.put("chatSubscribeType",subscribeType);
    	map.put("startDateStr",DateUtil.getDateSecondFormat(subscribeType.getStartDate()));
    	map.put("endDateStr",DateUtil.getDateSecondFormat(subscribeType.getEndDate()));
		return "chat/subscribe/subscribeTypeEdit";
    }
	
	/**
	 * 
	 * @function:  订阅配置保存
	 * @param request
	 * @param chatSubscribeType
	 * @return AjaxJson   
	 * @exception 
	 * @author:Jade.zhu   
	 * @since  1.0.0
	 */
	@RequestMapping(value="/chatSubscribeTypeController/create",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="add")
    public AjaxJson create(HttpServletRequest request,ChatSubscribeType chatSubscribeType){
		String startDate = request.getParameter("startDateStr");
		String endDate = request.getParameter("endDateStr");
		chatSubscribeType.setStartDate(DateUtil.parseDateSecondFormat(startDate));
		chatSubscribeType.setEndDate(DateUtil.parseDateSecondFormat(endDate));
		chatSubscribeType.setCreateUser(userParam.getUserNo());
		chatSubscribeType.setCreateIp(IPUtil.getClientIP(request));
    	AjaxJson j = new AjaxJson();
    	
    	ApiResult result =chatSubscribeTypeService.saveSubscribeType(chatSubscribeType, false);
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功新增订阅配置："+userParam.getUserNo();
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
    		logger.info("<<method:create()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 新增订阅配置："+userParam.getUserNo()+" 失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<<method:create()|"+message+",ErrorMsg:"+result.toString());
    	}
		return j;
    }
	
	/**
	 * 
	 * @function:  订阅配置更新
	 * @param request
	 * @param chatSubscribeType
	 * @return AjaxJson   
	 * @exception 
	 * @author:Jade.zhu
	 * @since  1.0.0
	 */
	@RequestMapping(value="/chatSubscribeTypeController/update",method=RequestMethod.POST)
   	@ResponseBody
    @ActionVerification(key="edit")
    public AjaxJson update(HttpServletRequest request,ChatSubscribeType chatSubscribeType){
		String startDate = request.getParameter("startDateStr");
		String endDate = request.getParameter("endDateStr");
		chatSubscribeType.setStartDate(DateUtil.parseDateSecondFormat(startDate));
		chatSubscribeType.setEndDate(DateUtil.parseDateSecondFormat(endDate));
		chatSubscribeType.setUpdateUser(userParam.getUserNo());
		chatSubscribeType.setUpdateIp(IPUtil.getClientIP(request));
    	AjaxJson j = new AjaxJson();

    	ApiResult result =chatSubscribeTypeService.saveSubscribeType(chatSubscribeType, true);
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 成功修改订阅配置："+userParam.getUserNo();
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_UPDATE);
    		logger.info("<--method:update()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 修改订阅配置："+userParam.getUserNo()+" 失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<--method:update()|"+message+",ErrorMsg:"+result.toString());
    	}
   		return j;
     }
	
	/**
	 * 
	 * @function:  删除单条订阅配置
	 * @param request
	 * @param response
	 * @return AjaxJson   
	 * @exception 
	 * @author:Jade.zhu
	 * @since  1.0.0
	 */
	@RequestMapping(value="/chatSubscribeTypeController/oneDel",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson oneDel(HttpServletRequest request,HttpServletResponse response){
    	
  		BoUser userParam = ResourceUtil.getSessionUser();
    	String delId = request.getParameter("id");
    	AjaxJson j = new AjaxJson();
    	ApiResult result = chatSubscribeTypeService.deleteSubscibeType(new String[]{delId});
    	if(result.isOk()){
          	j.setSuccess(true);
          	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除订阅配置成功";
          	addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:oneDel()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除订阅配置失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:oneDel()|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
  		
    }
	
	/**
	 * 
	 * @function:批量删除订阅配置  
	 * @param request
	 * @param response
	 * @return AjaxJson   
	 * @exception 
	 * @author:Jade.zhu 
	 * @since  1.0.0
	 */
	@RequestMapping(value="/chatSubscribeTypeController/batchDel",method=RequestMethod.POST)
    @ResponseBody
    @ActionVerification(key="delete")
    public AjaxJson batchDel(HttpServletRequest request,HttpServletResponse response){
    	String delIds = request.getParameter("ids");
    	AjaxJson j = new AjaxJson();
    	ApiResult result = chatSubscribeTypeService.deleteSubscibeType(delIds.contains(",")?delIds.split(","):new String[]{delIds});
    	if(result.isOk()){
    		j.setSuccess(true);
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 批量删除订阅配置成功";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_DEL);
    		logger.info("<<method:batchDel()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 批量删除订阅配置失败";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_DEL);
    		logger.error("<<method:batchDel()|"+message+",ErrorMsg:"+result.toString());
    	}
  		return j;
  		
    }
	
	@RequestMapping(value="/chatSubscribeTypeController/getMultipleCkAnalystList", method=RequestMethod.POST, produces = "plain/text; charset=UTF-8")
   	@ResponseBody
    public String getMultipleCkAnalystList(HttpServletRequest request,ModelMap map) throws Exception {
    	String analysts=request.getParameter("analysts");
       	List<TreeBean> treeList=new ArrayList<TreeBean>();
       	TreeBean tbean=null;
       	List<BoUser> allAnalysts = userService.getUserListByRole(getSystemFlag(),"analyst");
				String hasOther=request.getParameter("hasOther");
				String apiUrl = PropertiesUtil.getInstance().getProperty("pmApiUrl");
				if(StringUtils.isNotBlank(hasOther) && !apiUrl.contains("hx9999.com")) {
					String[] nameArr = {"梁育诗", "罗恩•威廉", "黃湛铭", "赵相宾", "周游", "刘敏", "陈杭霞", "金道研究院"};
					BoUser user = null;
					for (int i = 0; i < nameArr.length; i++) {
						user = new BoUser();
						user.setUserNo(nameArr[i]);
						user.setUserName(nameArr[i]);
						user.setPosition("金道研究院");
						allAnalysts.add(user);
					}
				}
       	analysts=StringUtils.isBlank(analysts)?"":(",".concat(analysts).concat(","));
        if(allAnalysts!=null && allAnalysts.size()>0){
           for(BoUser row:allAnalysts){
      		 tbean=new TreeBean();
      		 tbean.setId(row.getUserNo());
      		 tbean.setText(row.getUserName());
      		 if(analysts.contains(",".concat(row.getUserNo()).concat(","))){
   			   tbean.setChecked(true);
   		     }
      		 tbean.setParentId("");
  			 treeList.add(tbean);
          }
        }
       	return JsonUtil.formatListToTreeJson(treeList,false);
     }
	
	/*public List<ChatSubscribeType> getSubscribeTypeList(){
		return chatSubscribeTypeService.getSubscribeType();
	}*/
}
