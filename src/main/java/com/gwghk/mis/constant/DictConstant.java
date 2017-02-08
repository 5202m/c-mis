package com.gwghk.mis.constant;

/**
 * 摘要： 数据字典常量类
 * @author Gavin
 * @date   2014-10-20
 */
public class DictConstant {
    
   	private DictConstant() { 
	}
	private static class DictConstantInstance {
		private static final DictConstant instance = new DictConstant();
	}
	public static DictConstant getInstance() {
		return DictConstantInstance.instance; 
	}
   	
	/**初始化平台*/
	public  String DICT_PLATFORM = "platform";
	
	/**使用状态*/
	public  String DICT_USE_STATUS = "use_status";
	
	/**聊天室规则类别*/
	public  String DICT_CHAT_GROUP_RULE = "chat_group_rule";
	
	/**对话方式*/
	public  String DICT_CHAT_TALK_STYLE = "chat_talk_style";
	
	/**组类别*/
	public  String DICT_CHAT_GROUP_TYPE = "chat_group_type";
	
	/**房间等级*/
	public  String DICT_CHAT_GROUP_LEVEL = "chat_group_level";
	
	/**短信应用点*/
	public  String DICT_SMS_USE_TYPE = "sms_use_type";
	
	/**积分类别*/
	public  String DICT_POINTS_TYPE = "points_type";
	
	/**积分项目*/
	public  String DICT_POINTS_ITEM = "points_item";
	
	/**角色类别*/
	public  String DICT_ROLE_TYPE = "role_type";
	
	/**PC直播链接*/
	public String DICT_LIVE_URL_PC = "live_url_pc";
	
	/**mb直播链接*/
	public String DICT_LIVE_URL_MB = "live_url_mb";
	
	/**audio直播链接*/
	public String DICT_LIVE_URL_AUDIO = "live_url_audio";

	public String getDICT_PLATFORM() {
		return DICT_PLATFORM;
	}

	public void setDICT_PLATFORM(String dICT_PLATFORM) {
		DICT_PLATFORM = dICT_PLATFORM;
	}

	public String getDICT_USE_STATUS() {
		return DICT_USE_STATUS;
	}

	public void setDICT_USE_STATUS(String dICT_USE_STATUS) {
		DICT_USE_STATUS = dICT_USE_STATUS;
	}

	public String getDICT_CHAT_GROUP_RULE() {
		return DICT_CHAT_GROUP_RULE;
	}

	public void setDICT_CHAT_GROUP_RULE(String dICT_CHAT_GROUP_RULE) {
		DICT_CHAT_GROUP_RULE = dICT_CHAT_GROUP_RULE;
	}

	public String getDICT_CHAT_TALK_STYLE() {
		return DICT_CHAT_TALK_STYLE;
	}

	public void setDICT_CHAT_TALK_STYLE(String dICT_CHAT_TALK_STYLE) {
		DICT_CHAT_TALK_STYLE = dICT_CHAT_TALK_STYLE;
	}

	public String getDICT_CHAT_GROUP_TYPE() {
		return DICT_CHAT_GROUP_TYPE;
	}

	public void setDICT_CHAT_GROUP_TYPE(String dICT_CHAT_GROUP_TYPE) {
		DICT_CHAT_GROUP_TYPE = dICT_CHAT_GROUP_TYPE;
	}

	public String getDICT_CHAT_GROUP_LEVEL() {
		return DICT_CHAT_GROUP_LEVEL;
	}

	public void setDICT_CHAT_GROUP_LEVEL(String dICT_CHAT_GROUP_LEVEL) {
		DICT_CHAT_GROUP_LEVEL = dICT_CHAT_GROUP_LEVEL;
	}

	public String getDICT_SMS_USE_TYPE() {
		return DICT_SMS_USE_TYPE;
	}

	public void setDICT_SMS_USE_TYPE(String dICT_SMS_USE_TYPE) {
		DICT_SMS_USE_TYPE = dICT_SMS_USE_TYPE;
	}

	public String getDICT_POINTS_TYPE() {
		return DICT_POINTS_TYPE;
	}

	public void setDICT_POINTS_TYPE(String dICT_POINTS_TYPE) {
		DICT_POINTS_TYPE = dICT_POINTS_TYPE;
	}

	public String getDICT_POINTS_ITEM() {
		return DICT_POINTS_ITEM;
	}

	public void setDICT_POINTS_ITEM(String dICT_POINTS_ITEM) {
		DICT_POINTS_ITEM = dICT_POINTS_ITEM;
	}

	public String getDICT_ROLE_TYPE() {
		return DICT_ROLE_TYPE;
	}

	public void setDICT_ROLE_TYPE(String dICT_ROLE_TYPE) {
		DICT_ROLE_TYPE = dICT_ROLE_TYPE;
	}

	public String getDICT_LIVE_URL_PC() {
		return DICT_LIVE_URL_PC;
	}

	public void setDICT_LIVE_URL_PC(String dICT_LIVE_URL_PC) {
		DICT_LIVE_URL_PC = dICT_LIVE_URL_PC;
	}

	public String getDICT_LIVE_URL_MB() {
		return DICT_LIVE_URL_MB;
	}

	public void setDICT_LIVE_URL_MB(String dICT_LIVE_URL_MB) {
		DICT_LIVE_URL_MB = dICT_LIVE_URL_MB;
	}

	public String getDICT_LIVE_URL_AUDIO() {
		return DICT_LIVE_URL_AUDIO;
	}

	public void setDICT_LIVE_URL_AUDIO(String dICT_LIVE_URL_AUDIO) {
		DICT_LIVE_URL_AUDIO = dICT_LIVE_URL_AUDIO;
	}
}
