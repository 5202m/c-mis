package com.gwghk.mis.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 点赞实体类
 * Created by Jade.zhu on 2017/3/9.
 */
@Document
public class ChatPraise extends BaseModel{

  @Id
  private String id;

  /**
   * 老师ID
   */
  private String praiseId;

  /**
   * 点赞类型
   */
  private String praiseType;

  /**
   * 所属直播间
   */
  private String fromPlatform;

  /**
   * 点赞数
   */
  private Integer praiseNum;

  /**
   * 备注
   */
  private String remark;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPraiseId() {
    return praiseId;
  }

  public void setPraiseId(String praiseId) {
    this.praiseId = praiseId;
  }

  public String getPraiseType() {
    return praiseType;
  }

  public void setPraiseType(String praiseType) {
    this.praiseType = praiseType;
  }

  public String getFromPlatform() {
    return fromPlatform;
  }

  public void setFromPlatform(String fromPlatform) {
    this.fromPlatform = fromPlatform;
  }

  public Integer getPraiseNum() {
    return praiseNum;
  }

  public void setPraiseNum(Integer praiseNum) {
    this.praiseNum = praiseNum;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

}
