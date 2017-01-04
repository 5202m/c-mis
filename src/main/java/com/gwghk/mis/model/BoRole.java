package com.gwghk.mis.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 摘要：角户实体对象(系统管理)
 * @author Alan.wu
 * @date 2015/2/2
 */
@Document
public class BoRole extends BaseModelExt{ 

	/**
	 * 角户Id
	 */
	@Id
	private String roleId;
	
    /**
     * 角户账号
     */
	@Indexed
	private String roleNo;

     /**
      * 角色名称
      */
	private String roleName;


	/**
     * 是否删除
     */
	private Integer valid;

	/**
     * 状态
     */
	private Integer status;

	/**
     * 备注
     */
	private String remark;

	/**
	 * 角色名或角色号，用于权限类别中角色的查询
	 */
    private String roleNoOrName;
    
    /**
     * 聊天室组列表
     */
    private List<ChatGroup> chatGroupList;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRoleNo() {
		return roleNo;
	}

	public void setRoleNo(String roleNo) {
		this.roleNo = roleNo;
	}

	public String getRoleNoOrName() {
		return roleNoOrName;
	}

	public void setRoleNoOrName(String roleNoOrName) {
		this.roleNoOrName = roleNoOrName;
	}

	public List<ChatGroup> getChatGroupList() {
		return chatGroupList;
	}

	public void setChatGroupList(List<ChatGroup> chatGroupList) {
		this.chatGroupList = chatGroupList;
	}
}