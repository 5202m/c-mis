package com.gwghk.mis.service;

import cn.jpush.api.report.UsersResult.User;
import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.dao.ChatPraiseDao;
import com.gwghk.mis.enums.ResultCode;
import com.gwghk.mis.model.BoUser;
import com.gwghk.mis.model.ChatPraise;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * 点赞管理相关类
 * Created by Jade.zhu on 2017/3/9.
 */
@Service
public class ChatPraiseService {

  @Autowired
  private ChatPraiseDao chatPraiseDao;

  @Autowired
  private UserService userService;

  /**
   * 分页查询点赞数据
   * @param dCriteria
   * @return
   */
  public Page<ChatPraise> getPraisePage(DetachedCriteria<ChatPraise> dCriteria){
    Query query=new Query();
    ChatPraise chatPraise = dCriteria.getSearchModel();
    Criteria criteria = Criteria.where("praiseType").is("user");
    if(chatPraise!=null){
      if(StringUtils.isNotBlank(chatPraise.getPraiseId())){
        criteria.and("praiseId").in(chatPraise.getPraiseId().split(","));
      } else {
        List<BoUser> allAnalysts = userService.getUserListByRole("analyst");
        String analysts = "梁育诗,罗恩•威廉,黃湛铭,赵相宾,周游,刘敏,陈杭霞,金道研究院";
        if(allAnalysts != null && allAnalysts.size() > 0) {
          for (BoUser row : allAnalysts) {
            if(StringUtils.isNotBlank(analysts)){
              analysts += ",";
            }
            analysts += row.getUserNo();
          }
        }
        criteria.and("praiseId").in(analysts.split(","));
      }
      if(StringUtils.isNotBlank(chatPraise.getFromPlatform())){
        criteria.and("fromPlatform").is(chatPraise.getFromPlatform());
      }
    }
    query.addCriteria(criteria);
    return chatPraiseDao.getPraisePage(query, dCriteria);
  }

  /**
   * 更新点赞数
   * @param chatPraise
   * @return
   */
  public ApiResult modifyPraise(ChatPraise chatPraise){
    ApiResult api=new ApiResult();
    boolean isSuccess=chatPraiseDao.modifyPraiseNum(chatPraise);
    return api.setCode(isSuccess ? ResultCode.OK : ResultCode.FAIL);
  }

  /**
   *
   * @function:  获取单条数据
   * @param praiseId
   * @return ChatPraise
   * @exception
   * @author:Jade.zhu
   * @since  1.0.0
   */
  public ChatPraise getPraiseById(String praiseId){
    return chatPraiseDao.getPraiseById(praiseId);
  }
}
