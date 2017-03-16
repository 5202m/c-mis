package com.gwghk.mis.task;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gwghk.mis.service.ChatVisitorService;

/** 
 * 聊天室访客定时任务<BR>
 * ------------------------------------------<BR> 
 * <BR>
 * Copyright (c) 2016<BR>
 * Author      : Dick.guo <BR>
 * Date        : 2016年1月14日 <BR>
 * Description : <BR>
 * <p>
 * 聊天室访客定时任务
 * </p>
 */
//@Component
public class ChatVisitorTask
{
	private static final Logger logger = LoggerFactory.getLogger(ChatVisitorTask.class);
	
	@Autowired
	private ChatVisitorService chatVisitorService;
	
	/**
	 * 访客统计——在线时长分类统计、各类在线人数统计
	 */
//	@Scheduled(cron="0 0 0 * * ?")
	public void statVisitorsDay(){
		if (TaskManager.off()){
			return;
		}
		logger.info("系统开始自动执行任务==>每天凌晨0点自动统计访客记录信息(在线时长分类统计、各类在线人数统计)!");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date loc_endTime = calendar.getTime();
		calendar.add(Calendar.DATE, -1);
		Date loc_startTime = calendar.getTime();
		chatVisitorService.statVisitors(loc_startTime, loc_endTime);
	}
	
	/**
	 * 访客统计——整点在线人数统计
	 */
//	@Scheduled(cron="0 0 * * * ?")
	public void statVisitorsTimePoint(){
		if (TaskManager.off()){
			return;
		}
		logger.info("系统开始自动执行任务==>每天整点自动统计访客记录信息(整点在线人数统计)!");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date loc_endTime = calendar.getTime();
		calendar.add(Calendar.HOUR_OF_DAY, -1);
		Date loc_startTime = calendar.getTime();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		Date loc_statDate = calendar.getTime();
		chatVisitorService.statVisitors(loc_statDate, loc_startTime, loc_endTime);
	}
}
