package com.gwghk.mis.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 摘要：文章管理实体对象
 * @author Alan.wu
 * @date   2015年3月16日
 */
@Document
public class Article extends BaseModel{ 
	/**
	 * 资讯Id
	 */
	@Id
	private String id;
	
	/**
     * 模板
     */
	private String template;
	
	/**
     * 栏目id
     */
	@Indexed
	private String categoryId;
	
	/**
     * 状态,0为禁用，1为启用，默认为1
     */
	private Integer status;
	
	
	/**
	 * 应用平台(数据字典的code，多个则逗号分隔）
	 */
	private String platform;
	
	/**
	 * 文章种类
	 */
	private String type;
	
	/**
	 * 媒体地址路径
	 */
	private String  mediaUrl;
	
	/**
	 * 媒体图片（视频专用字段）
	 */
	private String mediaImgUrl;
	
	/**
	 * 点击媒体链接的路径
	 */
	private String  linkUrl;
	
	/**
	 * 序列
	 */
	private Integer sequence;

	/**
	 * 发布时间
	 */
	public Date publishStartDate;
	
	/**
	 * 发布时间
	 */
	public Date publishEndDate;
	
	/**
	 * 点赞数
	 */
	public Integer praise;
	
	 /**
     * 文章详细信息
     */
	private List<ArticleDetail> detailList;
	
	/**
	 * 是否删除/有效(1为有效，0为无效）
	 */
	private Integer valid;
	
	/**
	 * 文章来源id (如是数据导入，则对应id)
	 */
	private Integer srcId;
	
	/**
	 * 栏目路径名(仅用于输出)
	 */
	private String categoryNamePath;
	
	/**
	 * 所需积分，资料下载
	 */
	private Integer point;

	/**
	 * 系统标识
	 */
	private String systemCategory;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<ArticleDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<ArticleDetail> detailList) {
		this.detailList = detailList;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public Date getPublishStartDate() {
		return publishStartDate;
	}

	public void setPublishStartDate(Date publishStartDate) {
		this.publishStartDate = publishStartDate;
	}

	public Date getPublishEndDate() {
		return publishEndDate;
	}

	public void setPublishEndDate(Date publishEndDate) {
		this.publishEndDate = publishEndDate;
	}
	
	/**
	 * @return the praise
	 */
	public Integer getPraise() {
		return praise;
	}

	/**
	 * @param praise the praise to set
	 */
	public void setPraise(Integer praise) {
		this.praise = praise;
	}

	public String getCategoryNamePath() {
		return categoryNamePath;
	}

	public void setCategoryNamePath(String categoryNamePath) {
		this.categoryNamePath = categoryNamePath;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public String getMediaImgUrl() {
		return mediaImgUrl;
	}

	public void setMediaImgUrl(String mediaImgUrl) {
		this.mediaImgUrl = mediaImgUrl;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Integer getSrcId() {
		return srcId;
	}

	public void setSrcId(Integer srcId) {
		this.srcId = srcId;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public String getSystemCategory() {
		return systemCategory;
	}

	public void setSystemCategory(String systemCategory) {
		this.systemCategory = systemCategory;
	}
}