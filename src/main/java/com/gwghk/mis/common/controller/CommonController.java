package com.gwghk.mis.common.controller;

import com.gwghk.mis.model.ChatGroup;
import com.gwghk.mis.service.ChatGroupService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gwghk.mis.common.model.TreeBean;
import com.gwghk.mis.constant.DictConstant;
import com.gwghk.mis.controller.BaseController;
import com.gwghk.mis.model.BoDict;
import com.gwghk.mis.util.JsonUtil;
import com.gwghk.mis.util.ResourceUtil;

/**
 * 摘要：通用Controller
 * @author Alan.wu
 * @date   2015/3/19
 */
@Scope("prototype")
@Controller
public class CommonController extends BaseController{

	@Autowired
	private ChatGroupService chatGroupService;
    /**
   	 * 功能：应用平台类型
   	 */
    @RequestMapping(value = "/commonController/getPlatformList", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
   	@ResponseBody
    public String getPlatform(HttpServletRequest request,ModelMap map) throws Exception {
    	String platform=request.getParameter("platform");
       	List<TreeBean> treeList=new ArrayList<TreeBean>();
       	TreeBean tbean=null;
       	String dictCode=DictConstant.getInstance().DICT_PLATFORM;
       	Map<String, List<BoDict>> dictMap=ResourceUtil.getDictListByLocale(getSystemFlag(),new String[]{dictCode});
       	List<BoDict> subList=dictMap.get(dictCode);
			List<ChatGroup> chatGroupList = chatGroupService.getChatGroupList(getSystemFlag(), "_id", "name");
        platform=StringUtils.isBlank(platform)?"":(",".concat(platform).concat(","));
        if(subList!=null && subList.size()>0){
           for(BoDict row:subList){
						 tbean=new TreeBean();
						 tbean.setId(row.getCode());
						 tbean.setText(row.getName());
						 if(platform.contains(",".concat(row.getCode()).concat(","))){
						 tbean.setChecked(true);
						 }
					 tbean.setParentId("");
						treeList.add(tbean);
          }
        }
        if(chatGroupList!=null && chatGroupList.size()>0){
					for(ChatGroup row:chatGroupList){
						tbean=new TreeBean();
						tbean.setId(row.getId());
						tbean.setText(row.getName());
						if(platform.contains(",".concat(row.getId()).concat(","))){
							tbean.setChecked(true);
						}
						tbean.setParentId("");
						treeList.add(tbean);
					}
				}
       	return JsonUtil.formatListToTreeJson(treeList,false);
     }
    
    /**
   	 * 功能：聊天方式
   	 */
    @RequestMapping(value = "/commonController/getTalkStyleList", method = RequestMethod.POST,produces = "plain/text; charset=UTF-8")
   	@ResponseBody
    public String getTalkStyleList(HttpServletRequest request,ModelMap map) throws Exception {
    	String talkStyle=request.getParameter("talkStyle");
       	List<TreeBean> treeList=new ArrayList<TreeBean>();
       	TreeBean tbean=null;
       	String dictCode=DictConstant.getInstance().DICT_CHAT_TALK_STYLE;
       	Map<String, List<BoDict>> dictMap=ResourceUtil.getDictListByLocale(getSystemFlag(),new String[]{dictCode});
       	List<BoDict> subList=dictMap.get(dictCode);
       	talkStyle=StringUtils.isBlank(talkStyle)?"":(",".concat(talkStyle).concat(","));
       	for(BoDict row:subList){
       		 tbean=new TreeBean();
       		 tbean.setId(row.getCode());
       		 tbean.setText(row.getName());
       		 if(talkStyle.contains(",".concat(row.getCode()).concat(","))){
    			tbean.setChecked(true);
    		 }
       		 tbean.setParentId("");
   			 treeList.add(tbean);
       	}
       	return JsonUtil.formatListToTreeJson(treeList,false);
     }
}
