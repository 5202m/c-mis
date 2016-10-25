package com.gwghk.mis.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class TraninClient {

	/**
	 * id
	 */
	@Id
	private String id;
	
	/**
	 * 客户ID
	 */
	private String clientId;
		
	/**
	 * 客户名称
	 */
	private String nickname;
	
	/**
	 * 是否授权
	 */
	private Integer isAuth;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(Integer isAuth) {
		this.isAuth = isAuth;
	}
    
}
