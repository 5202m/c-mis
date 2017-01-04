package com.gwghk.mis.enums;

/**
 * 自定义序列枚举
 * @author alan.wu
 * @date 2015/2/5
 */
public enum IdSeq {
    User("U",1000000),
	Role("R",1000000),
	Menu("M",1000000),
	Log("L",1000000),
    Dict("D",1000000),
    AppCategory("AC",1000000),
    App("AP",1000000),
    Advertisement("AD",1000000),
    Account("AC",1000000),
    Category("",1),
    Article("",10000000),
    ChatGroupRule("CGR",1000),
    TokenAccess("TA",1000000),
    Product("PD",1000000),
    SubjectType("ST",1000000),
    Topic("TP",1000000),
    Reply("RP",1000000),
    FinanceProductSetting("FPS",1000000),
    FinanceTradeOrder("FTO",1000000),
    FinancePosition("FP",1000000),
    FinanceTradeRecord("FTR",1000000),
    FinanceQuotaRecord("FQR",1000000),
    Media("",10000000),
    PushMessage("PM",10000000),
    AppVersion("AV",10000000),
    SystemCategory("SCY",10000000),
    ChatGroup("",1);
	public static final char[] charArray = new char[] { 'A', 'B', 'C', 'D',
		'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S',
		'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	private String prefix;
	private long startNum;

	IdSeq(String prefix,long startNum){
		this.prefix = prefix;
		this.startNum = startNum;
	}

	public String getPrefix() {
		return prefix;
	}
	
	public long getStartNum() {
		return startNum;
	}
}
