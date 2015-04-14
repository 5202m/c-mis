package com.gwghk.mis.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.DictResult;
import com.gwghk.mis.dao.DictDao;
import com.gwghk.mis.enums.IdSeq;
import com.gwghk.mis.enums.ResultCode;
import com.gwghk.mis.model.BoDict;
import com.gwghk.mis.util.BeanUtils;

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
		return result;
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
    	  	dictDao.addChildDict(parentCode,dictParam);
    	}
		result.setReturnObj(new Object[]{dictParam});
		return result;
    }
    
    
    /**
	 * 功能：删除字典信息
	 */
    public ApiResult deleteDict(String id,boolean isParentId){
    	ApiResult result = new ApiResult();
    	boolean isSuccess=false;
    	if(isParentId){
    		isSuccess=dictDao.deleteParentById(id);
		}else{
			isSuccess=dictDao.deleteChildById(id);
		}
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
	public List<BoDict> getDictList(String name,String code){
		if(StringUtils.isBlank(name) && StringUtils.isBlank(code)){
			return dictDao.getDictList();
		}else{
			return dictDao.getDictListByNameOrCode(name,code);
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
	 * 查询页面
	 * @param detachedCriteria
	 * @return
	 */
	public List<BoDict> getDictList(DetachedCriteria<BoDict> dCriteria) {
		return dictDao.findList(BoDict.class, Query.query(Criteria.where("valid").is(1)), dCriteria);
	}
}
