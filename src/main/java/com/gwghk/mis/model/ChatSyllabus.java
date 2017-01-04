package com.gwghk.mis.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 聊天室课程表
 * @author Dick.guo
 * @date   2015年12月24日
 */
@Document
public class ChatSyllabus extends BaseModelExt{
	
	/**
	 * 课程表编号
	 */
	@Id
	private String id;
	
	/**
	 * 房间号
	 */
	@Indexed
	private String groupId;
	
	/**
	 * 房间类别
	 */
	@Indexed
	private String groupType;
	
	/**
	 * 课程,json字符串
	 * {days : [{day: Integer, status : Integer}], timeBuckets : [startTime : String, endTime : String, course : [{context : String, courseType : Integer,lecturer:String,lecturerId:String,title : String, status : Integer}]]}
	 * 备注： status 0-休市, 1-有效, 2-无效
	 */
	private String courses;
	
	/**
	 * 发布开始时间
	 */
	private Date publishStart;
	
	/**
	 * 发布结束时间
	 */
	private Date publishEnd;
	
	/**
	 * 是否删除 (1-删除 0-未删除)
	 */
	private Integer isDeleted;
    
	/**
	 * 描述
	 */
	private String context;
	
	/**
	 * 课程类型 0 文字直播    1 视频直播   2 oneTv
	 */
	private Integer courseType;
	
	/**
	 * 直播链接地址 
	 * 数据格式：[{code:"1",url:''},{code:"2",url:''}]
	 * 备注：1:视频直播,2:oneTv
	 */
	private String  studioLink;
	
	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId()
	{
		return groupId;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}

	/**
	 * @return the groupType
	 */
	public String getGroupType()
	{
		return groupType;
	}

	/**
	 * @param groupType the groupType to set
	 */
	public void setGroupType(String groupType)
	{
		this.groupType = groupType;
	}

	/**
	 * @return the courses
	 */
	public String getCourses()
	{
		return courses;
	}

	/**
	 * @param courses the courses to set
	 */
	public void setCourses(String courses)
	{
		this.courses = courses;
	}

	/**
	 * @return the publishStart
	 */
	public Date getPublishStart()
	{
		return publishStart;
	}

	/**
	 * @param publishStart the publishStart to set
	 */
	public void setPublishStart(Date publishStart)
	{
		this.publishStart = publishStart;
	}

	/**
	 * @return the publishEnd
	 */
	public Date getPublishEnd()
	{
		return publishEnd;
	}

	/**
	 * @param publishEnd the publishEnd to set
	 */
	public void setPublishEnd(Date publishEnd)
	{
		this.publishEnd = publishEnd;
	}

	/**
	 * @return the isDeleted
	 */
	public Integer getIsDeleted()
	{
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(Integer isDeleted)
	{
		this.isDeleted = isDeleted;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Integer getCourseType() {
		return courseType;
	}

	public void setCourseType(Integer courseType) {
		this.courseType = courseType;
	}

	public String getStudioLink() {
		return studioLink;
	}

	public void setStudioLink(String studioLink) {
		this.studioLink = studioLink;
	}
	
}
