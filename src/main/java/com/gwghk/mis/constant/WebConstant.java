package com.gwghk.mis.constant;


/**
 * 摘要： 常量类
 * @author Gavin
 * @date   2014-10-16
 */
public class WebConstant {
	//加密key值
	public final static String MD5_KEY = "GOLDENWAY";
	
	public final static String default_msg = "操作成功"; 
	public final static String default_fail_msg = "操作失败";
	/**
	 * 平台类型
	 */
	public final static String PLATFORM_GW = "GTS";
	public final static String PLATFORM_MT5 = "MT5";
	public final static String PLATFORM_MT4 = "MT4";
	public final static String PLATFORM_MTF = "MTF";
	/**
	 *日志级别定义
	 */
	public final static Integer Log_Leavel_INFO = 1;
	public final static Integer Log_Leavel_WARRING = 2;
	public final static Integer Log_Leavel_ERROR = 3;
	
	/**
	 * 数据字典的key
	 */
	public final static String DICT_KEY = "DICT_KEY";
	
	/**
	 * 文件附件key
	 */
	public final static String ATTACHMENT_KEY = "ATTACHMENT_KEY";
	
	public final static String DEFAULT_PWD = "admin888";
	
	/**
	 * 日志类型
	 */
	 public final static String Log_Type_LOGIN = "1"; //登陆
	 public final static String Log_Type_EXIT = "2";  //退出
	 public final static String Log_Type_INSERT = "3"; //新增
	 public final static String Log_Type_DEL = "4"; //删除
	 public final static String Log_Type_UPDATE = "5"; //更新
	 public final static String Log_Type_OTHER = "6"; //其他
	 public final static String LOG_TYPE_APPROVE = "7"; //审批
	 public final static String LOG_TYPE_CANCEL_APPROVE = "8"; //取消审批
	 public final static String LOG_TYPE_EXPORT = "9"; //导出记录
	public final static String Log_Type_GAG = "A"; //禁言
	public final static String Log_Type_APPROVE_ShowTrade = "10";//审核晒单
	public final static String Log_Type_CANCEL_APPROVE_ShowTrade = "11";//取消审核晒单
	public final static String Log_Type_DEL_ShowTrade_Comment = "12"; //删除晒单评论

	/**登录用户的session key*/
	 public final static String SESSION_LOGIN_FLAG_KEY = "SESSION_LOGIN_FLAG_KEY";
	 
	 /**菜单的session key*/
	 public final static String SESSION_MENU_KEY = "SESSION_MENU_KEY";
	 
	 /**国际化语言key*/
	 public final static String WW_TRANS_I18N_LOCALE = "WW_TRANS_I18N_LOCALE";
	 public final static String LOCALE_FOR_COOKIE="LOCALE_FOR_COOKIE";
	 
	 public final static String LOCALE_ZH_TW = "zh_TW";
	 public final static String LOCALE_ZH_CN = "zh_CN";
	 public final static String LOCALE_EN_US = "en_US";
	 
	 public final static String UPLOAD_PATH="/24k/uploadfiles/";// "D:\\uploadfiles\\";
	 public final static String BACKUP_PATH="/24kbackup/";// "D:\\24kbackup\\";
	 
	 /**文件类型-银行*/
	 public final static String  FILE_TYPE_BANK = "file_type_bank";
	 /**文件类型-身份证正面*/
	 public final static String  FILE_TYPE_IDCARD_Z = "file_type_idcard_z";
	 /**文件类型-身份证反面*/
	 public final static String  FILE_TYPE_IDCARD_F = "file_type_idcard_f";
	 /**文件类型-其他文件1*/
	 public final static String  FILE_TYPE_OTHER_1 = "file_type_other_1";
	 /**文件类型-其他文件2*/
	 public final static String  FILE_TYPE_OTHER_2 = "file_type_other_2";
	 
	 public static final String FILESTORE_UPLOADFILES="uploadfiles";
	 
	 /**聊天记录模板的路径*/
	 public final static String CHAT_RECORDS_TEMPLATE_PATH ="/template/chat_records_data.xls";
	 public final static String CHAT_USER_RECORDS_TEMPLATE_PATH ="/template/chat_user_data.xls";
	 /**反馈记录模板的路径*/
	 public final static String FEEDBACK_RECORDS_TEMPLATE_PATH ="/template/feedback_records_data.xls";
	 /**社区成员管理模板的路径*/
	 public final static String FINANCEUSER_RECORDS_TEMPLATE_PATH ="/template/financeuser_records_data.xls";
	 /**访客记录模板的路径*/
	 public final static String CHAT_VISITOR_RECORDS_TEMPLATE_PATH ="/template/chat_visitor_data.xls";
	 /**在线时长用户数量统计*/
	 public final static String CHAT_VISITOR_REP_D_TEMPLATE_PATH ="/template/chat_visitor_repD_data.xls";
	 /**各类在线人数数量统计*/
	 public final static String CHAT_VISITOR_REP_O_TEMPLATE_PATH ="/template/chat_visitor_repO_data.xls";
	 /**整点在线人数数量统计*/
	 public final static String CHAT_VISITOR_REP_T_TEMPLATE_PATH ="/template/chat_visitor_repT_data.xls";
	/**房间未授权用户列表**/
	public final static String ROOM_UN_AUTH_CLINET_DATA = "/template/room_un_auth_client_data.xls";
	/**房间未授权用户列表**/
	public final static String ROOM_TRAIN_CLINET_DATA = "/template/room_train_client_data.xls";
	/**积分明细模板的路径*/
	public final static String CHAT_POINTS_DETAILS_RECORDS_TEMPLATE_PATH ="/template/chat_points_details_data.xls";
	/**积分汇总模板的路径*/
	public final static String CHAT_POINTS_RECORDS_TEMPLATE_PATH ="/template/chat_points_data.xls";
	/**订阅模板的路径*/
	public final static String CHAT_SUBSCRIBE_RECORDS_TEMPLATE_PATH ="/template/chat_subscribe_data.xls";

	/****
	 * 分析师角色 编号前缀
	 */
	public final static String ROLE_NO_ANALYST_PRE = "analyst";
	/**超级用户标记**/
	public final static String SESSION_SUPER_FLAG = "SESSION_SUPER_FLAG";
	/**登录用户session**/
	public final static String SESSION_LOGIN_USER = "SESSION_LOGIN_USER";
}
