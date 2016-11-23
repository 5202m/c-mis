package com.gwghk.mis.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 摘要：财经日历
 * 
 * @author Dick.guo
 * @date 2016年03月18日
 */
@Document
public class ZxFinanceData extends BaseModel
{
	/** 财经日历ID */
	@Id
	private String dataId = null;

	/** 指标名称 */
	private String name = null;

	/** 指标国家 */
	private String country = null;

	/** 指标ID 财经日历详情页查询参数 */
	private String basicIndexId = null;

	/** 指标时期 */
	private String period = null;

	/** 指标重要性[low-1、mid-2、high-3] */
	private Integer importance = null;

	/** 预期值 */
	private String predictValue = null;

	/** 前值 */
	private String lastValue = null;

	/** 公布值 */
	private String value = null;

	/** 年份 */
	private int year;

	/** 利多项 */
	private String positiveItem = null;

	/** 利空项 */
	private String negativeItem = null;

	/** 指标级数 */
	private String level = null;

	/** 指标内页链接 */
	private String url = null;

	/** 指标日期 */
	private String date = null;

	/** 指标日期(开始) */
	private String dateStart = null;

	/** 指标日期(结束) */
	private String dateEnd = null;

	/** 指标时间 */
	private String time = null;

	/** 数据单位 */
	private String unit = null;

	/** 说明 */
	private String interpretation = null;

	/** 发布机构 */
	private String publishOrg = null;

	/** 发布频率 */
	private String publishFrequncy = null;

	/** 计算方法 */
	private String statisticMethod = null;

	/** 数据释义 */
	private String explanation = null;

	/** 指标影响 */
	private String influence = null;

	/** 指标最新公布时间 */
	private String nextPublishTime = null;
	
	/** 重要指数 */
	private Integer importanceLevel = null;

	/** 描述 */
	private String description = null;
	
	/** 数据类型(0：所有 1：外汇 2：贵金属 ) */
	private Integer dataType;

	/** 是否有效(0：无效 1：有效 ) */
	private Integer valid = null;

	/**
	 * 点评
	 */
	private List<ZxFinanceDataComment> comments = null;

	/**
	 * @return the dataId
	 */
	public String getDataId()
	{
		return dataId;
	}

	/**
	 * @param dataId
	 *            the dataId to set
	 */
	public void setDataId(String dataId)
	{
		this.dataId = dataId;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the country
	 */
	public String getCountry()
	{
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country)
	{
		this.country = country;
	}

	/**
	 * @return the basicIndexId
	 */
	public String getBasicIndexId()
	{
		return basicIndexId;
	}

	/**
	 * @param basicIndexId
	 *            the basicIndexId to set
	 */
	public void setBasicIndexId(String basicIndexId)
	{
		this.basicIndexId = basicIndexId;
	}

	/**
	 * @return the period
	 */
	public String getPeriod()
	{
		return period;
	}

	/**
	 * @param period
	 *            the period to set
	 */
	public void setPeriod(String period)
	{
		this.period = period;
	}

	/**
	 * @return the importance
	 */
	public Integer getImportance()
	{
		return importance;
	}

	/**
	 * @param importance
	 *            the importance to set
	 */
	public void setImportance(Integer importance)
	{
		this.importance = importance;
	}

	/**
	 * @return the predictValue
	 */
	public String getPredictValue()
	{
		return predictValue;
	}

	/**
	 * @param predictValue
	 *            the predictValue to set
	 */
	public void setPredictValue(String predictValue)
	{
		this.predictValue = predictValue;
	}

	/**
	 * @return the lastValue
	 */
	public String getLastValue()
	{
		return lastValue;
	}

	/**
	 * @param lastValue
	 *            the lastValue to set
	 */
	public void setLastValue(String lastValue)
	{
		this.lastValue = lastValue;
	}

	/**
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value)
	{
		this.value = value;
	}

	/**
	 * @return the year
	 */
	public int getYear()
	{
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(int year)
	{
		this.year = year;
	}

	/**
	 * @return the positiveItem
	 */
	public String getPositiveItem()
	{
		return positiveItem;
	}

	/**
	 * @param positiveItem
	 *            the positiveItem to set
	 */
	public void setPositiveItem(String positiveItem)
	{
		this.positiveItem = positiveItem;
	}

	/**
	 * @return the negativeItem
	 */
	public String getNegativeItem()
	{
		return negativeItem;
	}

	/**
	 * @param negativeItem
	 *            the negativeItem to set
	 */
	public void setNegativeItem(String negativeItem)
	{
		this.negativeItem = negativeItem;
	}

	/**
	 * @return the level
	 */
	public String getLevel()
	{
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(String level)
	{
		this.level = level;
	}

	/**
	 * @return the url
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}

	/**
	 * @return the date
	 */
	public String getDate()
	{
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date)
	{
		this.date = date;
	}
	
	/**
	 * @return the dateStart
	 */
	public String getDateStart()
	{
		return dateStart;
	}

	/**
	 * @param dateStart the dateStart to set
	 */
	public void setDateStart(String dateStart)
	{
		this.dateStart = dateStart;
	}

	/**
	 * @return the dateEnd
	 */
	public String getDateEnd()
	{
		return dateEnd;
	}

	/**
	 * @param dateEnd the dateEnd to set
	 */
	public void setDateEnd(String dateEnd)
	{
		this.dateEnd = dateEnd;
	}

	/**
	 * @return the time
	 */
	public String getTime()
	{
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(String time)
	{
		this.time = time;
	}

	/**
	 * @return the unit
	 */
	public String getUnit()
	{
		return unit;
	}

	/**
	 * @param unit
	 *            the unit to set
	 */
	public void setUnit(String unit)
	{
		this.unit = unit;
	}

	/**
	 * @return the interpretation
	 */
	public String getInterpretation()
	{
		return interpretation;
	}

	/**
	 * @param interpretation
	 *            the interpretation to set
	 */
	public void setInterpretation(String interpretation)
	{
		this.interpretation = interpretation;
	}

	/**
	 * @return the publishOrg
	 */
	public String getPublishOrg()
	{
		return publishOrg;
	}

	/**
	 * @param publishOrg
	 *            the publishOrg to set
	 */
	public void setPublishOrg(String publishOrg)
	{
		this.publishOrg = publishOrg;
	}

	/**
	 * @return the publishFrequncy
	 */
	public String getPublishFrequncy()
	{
		return publishFrequncy;
	}

	/**
	 * @param publishFrequncy
	 *            the publishFrequncy to set
	 */
	public void setPublishFrequncy(String publishFrequncy)
	{
		this.publishFrequncy = publishFrequncy;
	}

	/**
	 * @return the statisticMethod
	 */
	public String getStatisticMethod()
	{
		return statisticMethod;
	}

	/**
	 * @param statisticMethod
	 *            the statisticMethod to set
	 */
	public void setStatisticMethod(String statisticMethod)
	{
		this.statisticMethod = statisticMethod;
	}

	/**
	 * @return the explanation
	 */
	public String getExplanation()
	{
		return explanation;
	}

	/**
	 * @param explanation
	 *            the explanation to set
	 */
	public void setExplanation(String explanation)
	{
		this.explanation = explanation;
	}

	/**
	 * @return the influence
	 */
	public String getInfluence()
	{
		return influence;
	}

	/**
	 * @param influence
	 *            the influence to set
	 */
	public void setInfluence(String influence)
	{
		this.influence = influence;
	}

	/**
	 * @return the nextPublishTime
	 */
	public String getNextPublishTime()
	{
		return nextPublishTime;
	}

	/**
	 * @param nextPublishTime
	 *            the nextPublishTime to set
	 */
	public void setNextPublishTime(String nextPublishTime)
	{
		this.nextPublishTime = nextPublishTime;
	}
	
	/**
	 * @return the importanceLevel
	 */
	public Integer getImportanceLevel()
	{
		return importanceLevel;
	}

	/**
	 * @param importanceLevel the importanceLevel to set
	 */
	public void setImportanceLevel(Integer importanceLevel)
	{
		this.importanceLevel = importanceLevel;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * @return the dataType
	 */
	public Integer getDataType()
	{
		return dataType;
	}

	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(Integer dataType)
	{
		this.dataType = dataType;
	}

	/**
	 * @return the valid
	 */
	public Integer getValid()
	{
		return valid;
	}

	/**
	 * @param valid the valid to set
	 */
	public void setValid(Integer valid)
	{
		this.valid = valid;
	}

	public List<ZxFinanceDataComment> getComments() {
		return comments;
	}

	public void setComments(List<ZxFinanceDataComment> comments) {
		this.comments = comments;
	}

	/**
	 * 
	 */
	public ZxFinanceData()
	{
		super();
	}
	
	/**
	 * 通过API数据刷新财经日历数据
	 * @param src
	 * @param data
	 * @param detail
	 */
	public static ZxFinanceData refresh(ZxFinanceData src, ZxFinanceDataApi data, ZxFinanceDetailApi detail)
	{
		if (src == null)
		{
			src = new ZxFinanceData();
		}
		src.setName(data.getName());
		src.setCountry(data.getCountry());
		src.setBasicIndexId(data.getBasicIndexId());
		src.setPeriod(data.getPeriod());
		src.setImportance(ZxFinanceData.formatImportance(data.getImportance()));
		src.setPredictValue(data.getPredictValue());
		src.setLastValue(data.getLastValue());
		src.setValue(data.getValue());
		src.setYear(data.getYear());
		src.setPositiveItem(data.getPositiveItem());
		src.setNegativeItem(data.getNegativeItem());
		src.setLevel(data.getLevel());
		src.setUrl(data.getUrl());
		src.setDate(data.getDate());
		src.setTime(data.getTime());
		
		if (detail != null)
		{
			src.setUnit(detail.getUnit());
			src.setInterpretation(detail.getInterpretation());
			src.setPublishOrg(detail.getPublishOrganization());
			src.setPublishFrequncy(detail.getPublishFrequncy());
			src.setStatisticMethod(detail.getStatisticMethod());
			src.setExplanation(detail.getExplanation());
			src.setInfluence(detail.getInfluence());
			src.setNextPublishTime(detail.getNextPublishTime());
		}
		return src;
	}
	
	/**
	 * 转化重要性：low-1、mid-2、high-3
	 * @param importance
	 * @return
	 */
	private static int formatImportance(String importance){
		if ("high".equals(importance)){
			return 3;
		}else if ("mid".equals(importance)) {
			return 2;
		}else if ("low".equals(importance)) {
			return 1;
		}
		return 0;
	}
}
