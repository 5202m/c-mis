package com.gwghk.mis.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.gwghk.mis.common.dao.MongoDBBaseDao;
import com.gwghk.mis.enums.IdSeq;
import com.gwghk.mis.model.BoDict;
import com.gwghk.mis.util.StringUtil;
import com.mongodb.WriteResult;

/**
 * 摘要：系统管理-数据字典相关DAO
 * @author Alan.wu
 * @date   2015年3月13日
 */
@Repository
public class DictDao extends MongoDBBaseDao{
	
	/**
	 * 功能：根据Id -->获取数据字典对象
	 */
	public BoDict getDictById(String id){
	   return this.findById(BoDict.class, id);
	}

	/**
	 * 功能：根据code -->获取数据字典对象
	 */
	public BoDict getDictByCode(String code){
		return this.findOne(BoDict.class, Query.query(new Criteria().andOperator(Criteria.where("valid").is(1),Criteria.where("code").is(code))));
	}
	
    /**
     * 通过子类字典id找记录
     * @param childId
     * @return
     */
	public BoDict findByChildId(String childId) {
		return this.findOne(BoDict.class, Query.query(new Criteria().andOperator(Criteria.where("valid").is(1),Criteria.where("children.id").is(childId))));
	}
	
	/**
     * 通过子类字典code找记录
     * @param parentCode
	 * @param childCode
     * @return
     */
	public BoDict getDictByChildCode(String parentCode,String childCode) {
		return this.findOne(BoDict.class, Query.query(new Criteria().andOperator(Criteria.where("code").is(parentCode),Criteria.where("children.code").is(childCode))));
	}
	
	/**
	 * 新增子类字典记录
	 * @param parentCode
	 * @param childDict
	 */
	public void addChildDict(String parentCode,BoDict childDict) {
		childDict.setCreateDate(new Date());
		childDict.setValid(1);
		childDict.setId(this.getNextSeqId(IdSeq.Dict));
    	this.mongoTemplate.updateFirst(new Query(Criteria.where("code").is(parentCode)), new Update().push("children", childDict), BoDict.class);
	}
	
	/**
	 * 更新子字典条目
	 * @param parentCode
	 * @param childDict
	 */
    public void updateChild(String parentCode,BoDict childDict) {
    	childDict.setUpdateDate(new Date());
    	childDict.setValid(1);
    	Criteria criteria=new Criteria();
    	criteria.andOperator(Criteria.where("code").is(parentCode),Criteria.where("children.code").is(childDict.getCode()));
    	this.mongoTemplate.updateFirst(new Query(criteria), new Update().set("children.$", childDict), BoDict.class);
	}
	
    /**
     * 删除父类记录（包括删除子类）
     * @param id
     */
    public boolean deleteParentById(String id) {
    	//更新父类为删除
    	WriteResult wr=this.mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(id)), new Update().set("valid", 0).unset("children"), BoDict.class);
    	return (wr!=null&&wr.getN()>0);
	}
	
    /**
     * 删除子类记录
     * @param id
		 * @param cid
     */
    public boolean deleteChildById(String id, String cid) {
    	BoDict boDict=new BoDict();
    	boDict.setId(cid);
    	WriteResult wr=this.mongoTemplate.updateMulti(Query.query(Criteria.where("_id").is(id)), new Update().pull("children",boDict), BoDict.class);
    	return (wr!=null&&wr.getN()>0);
    }
    
	/**
	 * 功能：根据父code、子code -->获取数据字典对象
	 */
	public BoDict getByCodeAndChildCode(String code,String childrenCode){
		Criteria criteria = Criteria.where("valid").is(1);
		if(StringUtils.isNotBlank(code)){
			criteria.and("code").is(code);
		}
		if(StringUtils.isNotBlank(childrenCode)){
			criteria.and("children.code").is(code);
		}
		return this.findOne(BoDict.class, Query.query(criteria));
	}
	
	/**
	 * 功能：获取数据字典列表
	 */
	public List<BoDict> getDictList(){
		return this.findList(BoDict.class,new Query(Criteria.where("valid").is(1)));
	}
	
	/**
	 * 功能：根据code和name-->获取数据字典列表
	 */
	public List<BoDict> getDictListByNameOrCode(String name,String code, String status){
		List<Criteria> criteriaTmps = new ArrayList<Criteria>();
		criteriaTmps.add(Criteria.where("valid").is(1));
		if(StringUtils.isNotBlank(name)){
			criteriaTmps.add(new Criteria().orOperator(
						Criteria.where("nameCN").regex(StringUtil.toFuzzyMatch(name)),
						Criteria.where("children.nameCN").regex(StringUtil.toFuzzyMatch(name)),
						Criteria.where("nameTW").regex(StringUtil.toFuzzyMatch(name)),
						Criteria.where("children.nameTW").regex(StringUtil.toFuzzyMatch(name)),
						Criteria.where("nameEN").regex(StringUtil.toFuzzyMatch(name)),
						Criteria.where("children.nameEN").regex(StringUtil.toFuzzyMatch(name))
					));
		}
		if(StringUtils.isNotBlank(code)){
			criteriaTmps.add(new Criteria().orOperator(
						Criteria.where("code").regex(StringUtil.toFuzzyMatch(code)),
						Criteria.where("children.code").regex(StringUtil.toFuzzyMatch(code))
					));
		}
		if(StringUtils.isNotBlank(status)){
			criteriaTmps.add(new Criteria().orOperator(
						Criteria.where("status").is(Integer.parseInt(status)),
						Criteria.where("children.status").is(Integer.parseInt(status))
					));
		}
		Criteria criteria = new Criteria().andOperator(criteriaTmps.toArray(new Criteria[0]));
		return this.findList(BoDict.class, Query.query(criteria));
	}

	/**
	 * 功能：根据父code -->获取子数据字典列表
	 */
	public List<BoDict> getDictChildList(String parentCode){
		return this.findOne(BoDict.class, Query.query(Criteria.where("code").is(parentCode))).getChildren();
	}
	
	/**
	 * 功能：根据父code数组 -->获取数据字典列表
	 */
	public List<BoDict> getDictListByParentCodeArr(Object[] parentCodeArr){
		return this.findList(BoDict.class, Query.query(Criteria.where("code").in(parentCodeArr)));
	}
}