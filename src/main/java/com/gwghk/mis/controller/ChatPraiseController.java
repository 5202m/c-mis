package com.gwghk.mis.controller;

import com.gwghk.mis.common.model.DataGrid;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.constant.DictConstant;
import com.gwghk.mis.model.BoDict;
import com.gwghk.mis.model.ChatGroup;
import com.gwghk.mis.model.ChatPraise;
import com.gwghk.mis.service.ChatGroupService;
import com.gwghk.mis.service.ChatPraiseService;
import com.gwghk.mis.util.ResourceUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
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
 * 点赞管理
 * Created by Jade.zhu on 2017/3/9.
 */
@Scope("prototype")
@Controller
public class ChatPraiseController extends BaseController{

  private static final Logger logger = LoggerFactory.getLogger(ChatPraise.class);

  @Autowired
  private ChatPraiseService chatPraiseService;

  @Autowired
  private ChatGroupService chatGroupService;

  /**
   * 首页
   * @param request
   * @param map
   * @return
   */
  @RequestMapping(value = "/chatPraiseController/index", method = RequestMethod.GET)
  public String index(HttpServletRequest request,ModelMap map){
    DictConstant dict=DictConstant.getInstance();
    map.put("chatGroupList",this.formatTreeList(ResourceUtil.getSubDictListByParentCode(dict.DICT_CHAT_GROUP_TYPE)));
    return "chat/chatPraiseList";
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
   * 获取点赞列表
   * @param request
   * @param dataGrid
   * @param chatPraise
   * @return
   */
  @RequestMapping(value = "/chatPraiseController/datagrid", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, Object> datagrid(HttpServletRequest request, DataGrid dataGrid, ChatPraise chatPraise){
    String praiseId = request.getParameter("praiseId");
    String fromPlatform = request.getParameter("groupType");
    chatPraise = new ChatPraise();
    if(StringUtils.isNotBlank(praiseId)){
      chatPraise.setPraiseId(praiseId);
    }
    if(StringUtils.isNotBlank(fromPlatform)){
      chatPraise.setFromPlatform(fromPlatform);
    }
    Page<ChatPraise> page = chatPraiseService.getPraisePage(this.createDetachedCriteria(dataGrid, chatPraise));
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("total",null == page ? 0  : page.getTotalSize());
    result.put("rows", null == page ? new ArrayList<ChatPraise>() : page.getCollection());
    return result;
  }

}
