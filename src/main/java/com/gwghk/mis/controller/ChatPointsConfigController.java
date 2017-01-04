package com.gwghk.mis.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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

import com.gwghk.mis.authority.ActionVerification;
import com.gwghk.mis.common.model.AjaxJson;
import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DataGrid;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.constant.DictConstant;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.model.ChatPointsConfig;
import com.gwghk.mis.model.SmsInfo;
import com.gwghk.mis.service.ChatGroupService;
import com.gwghk.mis.service.ChatPointsConfigService;
import com.gwghk.mis.util.DateUtil;
import com.gwghk.mis.util.IPUtil;
import com.gwghk.mis.util.ResourceBundleUtil;
import com.gwghk.mis.util.ResourceUtil;

/**
 * 摘要：发送短信
 * @author Gavin.guo
 * @date   2015-07-10
 */
@Scope("prototype")
@Controller
public class ChatPointsConfigController extends BaseController{

	private static final Logger logger = LoggerFactory.getLogger(ChatPointsConfigController.class);

	@Autowired
	private ChatPointsConfigService chatPointsConfigService;
	
	@Autowired
	private ChatGroupService chatGroupService;
	
	/**
	 * 功能：积分配置管理-首页
	 */
	@RequestMapping(value = "/chatPointsConfig/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, ModelMap map) {
		DictConstant dict=DictConstant.getInstance();
		map.put("type", ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_POINTS_TYPE));
		map.put("item", ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_POINTS_ITEM));
		map.put("status", ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_USE_STATUS));
		map.put("groupList", ResourceUtil.getSubDictListByParentCode(getSystemFlag(),dict.DICT_CHAT_GROUP_TYPE));
		logger.debug("-->start into ChatPointsConfigController.index() and url is /ChatPointsConfigController/index.do");
		return "points/chatPointsConfigList";
	}

	/**
	 * 功能：获取dataGrid列表
	 * 
	 * @param request
	 * @param dataGrid
	 *            分页查询参数对象
	 * @param chatPointsConfig
	 *            积分配置实体查询参数对象
	 * @return Map<String,Object> dataGrid需要的数据
	 */
	@RequestMapping(value = "/chatPointsConfig/datagrid", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> datagrid(HttpServletRequest request, DataGrid dataGrid, ChatPointsConfig chatPointsConfig) {
		setSystemFlag(chatPointsConfig);
		Page<ChatPointsConfig> page = chatPointsConfigService.getChatPointsConfigs(this.createDetachedCriteria(dataGrid, chatPointsConfig));
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", null == page ? 0 : page.getTotalSize());
		result.put("rows", null == page ? new ArrayList<SmsInfo>() : page.getCollection());
		return result;
	}
	
	/**
	 * 跳转到修改页面
	 * @param request
	 * @param pointsCfgId
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/chatPointsConfig/preEdit",method=RequestMethod.GET)
	@ActionVerification(key="edit")
    public String preEdit(HttpServletRequest request,@Param("cfgId")String cfgId, ModelMap map){
        ChatPointsConfig chatPointsConfig = chatPointsConfigService.findById(cfgId);
        if(chatPointsConfig != null){
        	this.setRoomInfo(chatPointsConfig);
        	map.put("chatPointsConfig", chatPointsConfig);
        	map.addAttribute("clientGroupStr",StringUtils.join(chatPointsConfig.getClientGroup(), ","));
        }
        return "points/chatPointsConfigEdit";
    }
	
	/**
	 * 跳转到添加页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/chatPointsConfig/preAdd",method=RequestMethod.GET)
	@ActionVerification(key="add")
	public String preAdd(HttpServletRequest request){
		return "points/chatPointsConfigAdd";
	}

	/**
	 * 保存
	 * @param request
	 * @param chatPointsConfig
	 * @return
	 */
	@RequestMapping(value="/chatPointsConfig/save",method=RequestMethod.POST)
   	@ResponseBody
    public AjaxJson save(HttpServletRequest request, ChatPointsConfig chatPointsConfig){
		AjaxJson j = new AjaxJson();
       	if(chatPointsConfig.getClientGroup()==null){
       		j.setSuccess(false);
    		j.setMsg("客户组不能为空");
    		return j;
       	}
        ApiResult result = null;
        Date currDate = new Date();
        chatPointsConfig.setCreateUser(userParam.getUserNo());
        chatPointsConfig.setCreateDate(currDate);
        chatPointsConfig.setCreateIp(IPUtil.getClientIP(request));
        chatPointsConfig.setUpdateUser(chatPointsConfig.getCreateUser());
        chatPointsConfig.setUpdateDate(currDate);
        chatPointsConfig.setUpdateIp(chatPointsConfig.getCreateIp());
        
        if(StringUtils.isNotBlank(chatPointsConfig.getCfgId())){
        	result = chatPointsConfigService.update(chatPointsConfig);
        }else{
        	result = chatPointsConfigService.add(chatPointsConfig);
        }
    	if(result.isOk()){
	    	j.setSuccess(true);
	    	String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 保存积分配置成功：[" + chatPointsConfig.getType() + "-" + chatPointsConfig.getItem() + "]!";
    		addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
    		logger.info("<<save()|"+message);
    	}else{
    		j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
    		String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 保存积分配置失败：[" + chatPointsConfig.getType() + "-" + chatPointsConfig.getItem() + "]!";
    		addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
    		logger.error("<<save()|"+message+",ErrorMsg:"+result.toString());
    	}
		return j;
    }
	
	/**
	 * 删除
	 * @param request
	 * @param chatPointsConfig
	 * @return
	 */
	@RequestMapping(value="/chatPointsConfig/delete",method=RequestMethod.POST)
	@ActionVerification(key="delete")
	@ResponseBody
	public AjaxJson delete(HttpServletRequest request){
		String pointsCfgId = request.getParameter("id");
		AjaxJson j = new AjaxJson();
		ApiResult result = chatPointsConfigService.delete(pointsCfgId);
		if(result.isOk()){
			j.setSuccess(true);
			String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除积分配置成功：" + pointsCfgId + "!";
			addLog(message, WebConstant.Log_Leavel_INFO, WebConstant.Log_Type_INSERT);
			logger.info("<<delete()|"+message);
		}else{
			j.setSuccess(false);
    		j.setMsg(ResourceBundleUtil.getByMessage(result.getCode()));
			String message = " 用户: " + userParam.getUserNo() + " "+DateUtil.getDateSecondFormat(new Date()) + " 删除积分配置失败：" + pointsCfgId + "!";
			addLog(message, WebConstant.Log_Leavel_ERROR, WebConstant.Log_Type_INSERT);
			logger.error("<<delete()|"+message+",ErrorMsg:"+result.toString());
		}
		return j;
	}

	/**
	 * 设置房间信息
	 * @param chatPointsConfig
	 */
	private void setRoomInfo(ChatPointsConfig chatPointsConfig){
		if(StringUtils.isBlank(chatPointsConfig.getGroupId()) && StringUtils.isNotBlank(chatPointsConfig.getGroupType())){
        	chatPointsConfig.setGroupId(chatPointsConfig.getGroupType() + ",");
        }
	}
}
