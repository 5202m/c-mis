package com.gwghk.mis.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gwghk.mis.common.model.ApiResult;
import com.gwghk.mis.dao.CategoryDao;
import com.gwghk.mis.enums.ResultCode;
import com.gwghk.mis.model.Category;
import com.gwghk.mis.util.BeanUtils;
import com.gwghk.mis.util.StringUtil;

/**
 * 摘要：栏目理服务类
 * @author Alan.wu
 * @date   2015年3月16日
 */
@Service
public class CategoryService{

	@Autowired
	private CategoryDao categoryDao;
	
	public List<Category> getCategoryList(String name,String code,String status) {
		return categoryDao.getListByNameAndCode(name,code,status);
	}
	
	/**
	 * 提取指定类型的栏目列表
	 * @return
	 */
	public List<Category> getCategoryList(Integer type) {
		return categoryDao.getListByType(type);
	}

	/**
	 * 保存栏目信息
	 * @param category
	 * @param b
	 * @return
	 */
	public ApiResult saveCategory(String parentId,Category categoryParam, boolean isUpdate) {
		Category category=null;
    	ApiResult result = new ApiResult();
    	category=categoryDao.getById(categoryParam.getId());
    	if(isUpdate){
    		String name=category.getName();
           	BeanUtils.copyExceptNull(category, categoryParam);
           	setParent(parentId,category);
        	categoryDao.update(category);
        	if(!name.equals(categoryParam.getName())){//如名称修改了，更新父类名的路径
        		categoryDao.updateParentNamePath(category.getId(),categoryParam.getName());
        	}
    	}else{
    		if(category!=null){
    			result.setMsg(ResultCode.Error102);
    			return result;
    		}
    		setParent(parentId,categoryParam);
    		categoryDao.addCategory(categoryParam);
    	}
		result.setReturnObj(new Object[]{categoryParam});
		return result;
		
	}
	
	/**
	 * 提取组合的父类路径
	 * @param parentId
	 * @return
	 */
	private void setParent(String parentId,Category category){
		if(StringUtils.isNotBlank(parentId)){//判断是否有父类，有则取其父类路径重新组合到子类记录中
       		Category parentCategory=categoryDao.getById(parentId);
       		String parentIdPath="",parentNamePath="";
       		if(parentCategory!=null){
       			parentIdPath=parentCategory.getParentIdPath();
       			parentNamePath=parentCategory.getParentNamePath();
       			if(StringUtils.isNotBlank(parentIdPath)){
           			parentIdPath=StringUtil.concatStr(parentIdPath,",",parentId);
       			}else{
       				parentIdPath=parentId;
       			}
       			if(StringUtils.isNotBlank(parentNamePath)){
       				parentNamePath=StringUtil.concatStr(parentNamePath,",",parentCategory.getName());
       			}else{
       				parentNamePath=parentCategory.getName();
       			}
           		category.setParentId(parentId);
           		category.setParentIdPath(parentIdPath);
           		category.setParentNamePath(parentNamePath);
           		return;
       		}
       	}
		category.setParentId("");
   		category.setParentIdPath("");
   		category.setParentNamePath("");
	}

	/**
	 * 通过id找对应记录
	 * @param id
	 * @return
	 */
	public Category getCategoryById(String id) {
		return categoryDao.getById(id);
	}

	/**
	 * 删除栏目
	 * @param delId
	 * @return
	 */
	public ApiResult deleteCategory(String id) {
		return new ApiResult().setCode(categoryDao.deleteCategory(id)?ResultCode.OK:ResultCode.FAIL);
	}
}
