package com.gwghk.mis.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.DictResult;
import com.gwghk.mis.constant.WebConstant;
import com.gwghk.mis.dao.DictDao;
import com.gwghk.mis.enums.IdSeq;
import com.gwghk.mis.enums.ResultCode;
import com.gwghk.mis.enums.SortDirection;
import com.gwghk.mis.model.BoDict;
import com.gwghk.mis.util.BeanUtils;
import com.gwghk.mis.util.CacheManager;
import com.gwghk.mis.util.StringUtil;

/**
 * 摘要：数据字典相关Service
 * @author Alan.wu
 * @date   2015年3月12日
 */
@Service
public class DictService{

	@Resource
	private DictDao dictDao;
	
	/**
	 * 功能：新增或修改父类字典信息
	 */
    public ApiResult saveParentDict(BoDict dictParam,boolean isUpdate){
    	BoDict dict=null;
    	ApiResult result = new ApiResult();
    	dictParam.setSystemCategory(null);
    	if(isUpdate){
        	dict=dictDao.getDictByCode(dictParam.getCode());
           	BeanUtils.copyExceptNull(dict, dictParam);
        	dictDao.update(dict);
    	}else{
    		if(dictDao.getDictByCode(dictParam.getCode())!=null){
    			result.setMsg(ResultCode.Error100);
    			return result;
    		}
    		dictParam.setValid(1);
    		dictParam.setId(dictDao.getNextSeqId(IdSeq.Dict));
    		dictDao.add(dictParam);
    	}
		result.setReturnObj(new Object[]{dictParam});
		this.synchDictCache();
		return result;
    }
    
    /**
     * 同步字典缓存
     */
    public void synchDictCache(){
    	LinkedHashMap<String,SortDirection> dictMap = new LinkedHashMap<String,SortDirection>();
		dictMap.put("id", SortDirection.ASC);
		dictMap.put("sort", SortDirection.ASC);
		DetachedCriteria<BoDict> detachedCriteria = new DetachedCriteria<BoDict>();
		detachedCriteria.setOrderbyMap(dictMap);
		List<BoDict> dictParamList =this.getDictList(detachedCriteria);
		if(dictParamList != null && dictParamList.size() > 0){//将数据字典列表放到缓存中
			CacheManager.putContent(WebConstant.DICT_KEY, dictParamList, -1);
		}
    }
    
    /**
	 * 功能：新增或修改子类字典信息
	 */
    public ApiResult saveChildrenDict(String parentCode,BoDict dictParam,boolean isUpdate){
    	ApiResult result = new ApiResult();
    	if(isUpdate){
        	dictDao.updateChild(parentCode,dictParam);
    	}else{
    		if(StringUtils.isBlank(dictParam.getCode())){
	    		return result.setCode(ResultCode.Error103);
    		}
    		if(dictDao.getDictByChildCode(parentCode, dictParam.getCode())!=null){
    			return result.setCode(ResultCode.Error100);
    		}
    		dictParam.setValid(1);
    	  	dictDao.addChildDict(parentCode,dictParam);
    	}
		result.setReturnObj(new Object[]{dictParam});
		this.synchDictCache();
		return result;
    }
    
    
    /**
	 * 功能：删除字典信息
	 */
    public ApiResult deleteDict(String id, String cid, boolean isParentId){
    	ApiResult result = new ApiResult();
    	boolean isSuccess=false;
    	if(isParentId){
    		isSuccess=dictDao.deleteParentById(id);
			}else{
				isSuccess=dictDao.deleteChildById(id, cid);
			}
    	this.synchDictCache();
    	return result.setCode(isSuccess?ResultCode.OK:ResultCode.FAIL);
    }
	
    /**
	 * 功能：根据Id -->获取数据字典对象
	 */
	public BoDict getDictById(String id){
		return dictDao.getDictById(id);
	}
	
    /**
	 * 功能：根据ChildId -->获取数据字典对象
	*/
	public BoDict getDictByChildId(String id){
		return dictDao.findByChildId(id);
	}
	
	/**
     * 功能：根据父code、子code -->提取字典信息
     */
	public BoDict getDictByCode(String parentCode,String childCode){
		if(StringUtils.isBlank(childCode)){
			return dictDao.getDictByCode(parentCode);
		}else{
			return dictDao.getByCodeAndChildCode(parentCode,childCode);
		}
	}
	
	/**
	 * 功能：根据名称、code --> 获取字典列表
	 */
	public List<BoDict> getDictList(String name,String code, String status){
		if(StringUtils.isBlank(name) && StringUtils.isBlank(code) && StringUtils.isBlank(status)){
			return dictDao.getDictList();
		}else{
			return dictDao.getDictListByNameOrCode(name,code,status);
		}
	}
	/**
	 * 功能：根据父code -->获取子数据字典列表
	 */
	public List<BoDict> getDictChildList(String parentCode){
		if(StringUtils.isBlank(parentCode)){
			return null;
		}
		return dictDao.getDictChildList(parentCode);
	}
	
	/**
	 * 功能：根据父code数组 -->获取数据字典列表
	 */
	public DictResult getDictChildByCodeArrList(String[] parentCodeArr){
		DictResult dictResult=new DictResult();
		if(parentCodeArr==null||parentCodeArr.length==0){
			return dictResult;
		}
		List<BoDict> result = dictDao.getDictListByParentCodeArr(parentCodeArr);
		Map<String,List<BoDict>> dictMap=new LinkedHashMap<String,List<BoDict>>();
		List<BoDict> newDict=null;
		if(result!=null&&result.size()>0){ 
			for(Object parentCode:parentCodeArr){
				Predicate<BoDict> pre=dictPoPre->dictPoPre.getCode().equals(parentCode);
				newDict=result.stream().filter(pre).collect(Collectors.toList());
				if(newDict!=null && newDict.size()>0){
					dictMap.put(parentCode.toString(),newDict.get(0).getChildren());
				}
			}
	    }
		dictResult.setDictMap(dictMap);
		return dictResult;
	}

	/**
	 * 查询字典列表，并过滤禁用的字典。
	 * @param detachedCriteria
	 * @return
	 */
	public List<BoDict> getDictList(DetachedCriteria<BoDict> dCriteria) {
		List<BoDict> dictList = dictDao.findList(BoDict.class, Query.query(Criteria.where("valid").is(1).and("status").is(1)), dCriteria);
		List<BoDict> dictChildrenTmp = null;
		List<BoDict> dictChildren = null;
		if(dictList != null){
			for (BoDict dict : dictList) {
				dictChildrenTmp = dict.getChildren();
				dictChildren = new ArrayList<BoDict>();
				if(dictChildrenTmp != null){
					for (BoDict dictChild : dictChildrenTmp) {
						if(new Integer(1).equals(dictChild.getStatus())){
							dictChildren.add(dictChild);
						}
					}
				}
				dict.setChildren(dictChildren);
			}
		}
		return dictList;
	}
	
	/**
	 * 查询字典列表，并过滤禁用的字典。
	 * @param detachedCriteria
	 * @return
	 */
	public List<BoDict> getDictListByPrefix(String prefix,String systemCategory) {
		List<BoDict> dictList = dictDao.findList(BoDict.class, Query.query(Criteria.where("valid").is(1).and("status").is(1).and("code").regex(StringUtil.toFuzzyMatch(prefix))).with(new Sort(new Order(Direction.ASC,"sort"))));
		List<BoDict> dictChildrenTmp = null;
		List<BoDict> dictChildren = null;
		if(dictList != null){
			for (BoDict dict : dictList) {
				dictChildrenTmp = dict.getChildren();
				dictChildren = new ArrayList<BoDict>();
				if(dictChildrenTmp != null){
					for (BoDict dictChild : dictChildrenTmp) {
						if(new Integer(1).equals(dictChild.getStatus()) && dictChild.getSystemCategory()!=null && StringUtil.containKeyword(dictChild.getSystemCategory(), systemCategory, false)){
							dictChildren.add(dictChild);
						}
					}
				}
				dict.setChildren(dictChildren);
			}
		}
		return dictList;
	}
}
