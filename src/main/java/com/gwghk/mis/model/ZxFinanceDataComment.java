package com.gwghk.mis.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 摘要：财经日历点评
 *
 * @author Jade.zhu
 * @date 2016年11月17日
 */
@Document
public class ZxFinanceDataComment extends BaseModel {

    @Id
    private String id;//ObjectId

    /**
     * 点评用户ID
     */
    private String userId;

    /**
     * 点评用户名
     */
    private String userName;

    /**
     * 点评用户头像
     */
    private String avatar;

    /**
     * 点评内容
     */
    private String comment;

    /**
     * 是否删除，0是，1否
     */
    private Integer valid;

    /**
     * 所属事业部
     */
    private String systemCategory;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public String getSystemCategory() {
        return systemCategory;
    }

    public void setSystemCategory(String systemCategory) {
        this.systemCategory = systemCategory;
    }
}
