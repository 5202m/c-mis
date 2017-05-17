package com.gwghk.mis.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * 用户组对象
 * 备注:（用于内嵌在LoginPlatfrom里，记录用户登录第三聊天室所需信息）
 * @author Alan.wu
 * @date   2015年4月10日
 */
public class ChatUserGroup {
	/**
	 * 组id
	 */
	@Id
	private String id;
	
	/**
	 * 用户id
	 */
	private String userId;
	
	/**
	 * 密码
	 */
	private String pwd;
		
	/**
	 * 交易账户
	 */
	@Indexed
	private String accountNo;
	
	/**
	 * 用户组别
	 */
	private String clientGroup;
	
	/**
	 * 头像
	 */
	private String avatar;
	
	/**
	 * 昵称
	 */
	@Indexed
	private String nickname;
	
	
	/**
	 * 是否价值用户
	 */
	private Boolean valueUser;
	
	/**
	 * vip用户
	 */
	private Boolean vipUser;
	
	/**
	 * 用户类型 0--普通用户、1-管理员、2-分析师、3-客服
	 */
	private Integer userType;
	
	/**
	 * 价值用户备注
	 */
	private String vipUserRemark;
	
	/**
	 * 价值用户备注
	 */
	private String valueUserRemark;
	
	/**注册时间*/
	private Date createDate;

	/**
     * 禁言状态
     */
	private Boolean gagStatus; 
	
	/**
     * 禁言，保存格式{beginDate:yyyy-MM-dd,endDate:yyyy-MM-dd,weekTime:[{week:0..6,beginTime:'HH:mm:ss',endTime:'HH:mm:ss'}]}
     */
	private String gagDate;
	
	/**
	 * 禁言次数
	 */
	private Integer gagTimes;
	
	/**
     * 禁言提示语
     */
	private String gagTips; 
	
	/**
	 * 禁言备注
	 */
	private String gagRemark;
	
	/**
	 * 房间组
	 */
	private List<ChatRoom> rooms;

	/**
	 * 邮箱地址
	 */
	private String email;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
	public String getClientGroup() {
		return clientGroup;
	}

	public void setClientGroup(String clientGroup) {
		this.clientGroup = clientGroup;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Boolean getValueUser() {
		return valueUser;
	}

	public void setValueUser(Boolean valueUser) {
		this.valueUser = valueUser;
	}

	public Boolean getVipUser() {
		return vipUser;
	}

	public void setVipUser(Boolean vipUser) {
		this.vipUser = vipUser;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getVipUserRemark() {
		return vipUserRemark;
	}

	public void setVipUserRemark(String vipUserRemark) {
		this.vipUserRemark = vipUserRemark;
	}

	public String getValueUserRemark() {
		return valueUserRemark;
	}

	public void setValueUserRemark(String valueUserRemark) {
		this.valueUserRemark = valueUserRemark;
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	/**
	 * @return the gagStatus
	 */
	public Boolean getGagStatus() {
		return gagStatus;
	}

	/**
	 * @param gagStatus the gagStatus to set
	 */
	public void setGagStatus(Boolean gagStatus) {
		this.gagStatus = gagStatus;
	}

	/**
	 * @return the gagDate
	 */
	public String getGagDate() {
		return gagDate;
	}

	/**
	 * @param gagDate the gagDate to set
	 */
	public void setGagDate(String gagDate) {
		this.gagDate = gagDate;
	}
	
	public Integer getGagTimes() {
		return gagTimes;
	}

	public void setGagTimes(Integer gagTimes) {
		this.gagTimes = gagTimes;
	}

	/**
	 * @return the gagTips
	 */
	public String getGagTips() {
		return gagTips;
	}

	/**
	 * @param gagTips the gagTips to set
	 */
	public void setGagTips(String gagTips) {
		this.gagTips = gagTips;
	}

	/**
	 * @return the gagRemark
	 */
	public String getGagRemark() {
		return gagRemark;
	}

	/**
	 * @param gagRemark the gagRemark to set
	 */
	public void setGagRemark(String gagRemark) {
		this.gagRemark = gagRemark;
	}

	public List<ChatRoom> getRooms() {
		return rooms;
	}

	public void setRooms(List<ChatRoom> rooms) {
		this.rooms = rooms;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
