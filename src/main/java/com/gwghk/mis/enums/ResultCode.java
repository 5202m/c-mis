package com.gwghk.mis.enums;

/**
 * Api response code
 * @author charles.so
 * 2014年10月29日
 */
public enum ResultCode{
	OK("OK", "成功"), 
	FAIL("FAIL", "失败"),
	Error0("0","连接失败"),
	Error100("100","该菜单代码已存在，请输入其他代码"),
	Error101("101","该字典代码已存在，请输入其他代码"),
	Error102("102","该记录已存在"),
	Error103("103","输入参数有误，请检查输入参数！"),
	Error104("104","操作的记录不存在，请检查！"),
	Error105("105","不能擅自修改自己所属的角色！"),
	Error1006("1001","用户名或密码错误！"),
	Error1007("1007","上传图片失败！"),
	Error1008("1008","上传图片格式不对！"),
	Error1009("1009","裁剪图片失败！"),
	Error1010("1010","旧密码不对！"),
	Error1011("1011","后台会员用户已经存在！"),
	Error1012("1012","请先在社区成员管理功能中添加后台成员！"),
	Error1013("1013","该关键字已经存在！");
	
	private String code;
	private String message;

	private ResultCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
