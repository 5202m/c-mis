package com.gwghk.mis.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.gwghk.mis.common.model.DetachedCriteria;
import com.gwghk.mis.common.model.Page;
import com.gwghk.mis.dao.SystemCategoryDao;
import com.gwghk.mis.enums.IdSeq;
import com.gwghk.mis.model.BoSystemCategory;
import com.gwghk.mis.util.BeanUtils;

/**
 * 系统类别服务类
 * @author alan.wu
 * @date 2016/11/21
 */
@Service
public class SystemCategoryService {
    @Autowired
    private SystemCategoryDao systemCategoryDao;

    /**
     * 列表信息
     * @param dCriteria
     * @return
     */
    public Page<BoSystemCategory> list(DetachedCriteria<BoSystemCategory> dCriteria) {
        Query query = new Query();
        return systemCategoryDao.findPage(BoSystemCategory.class, query, dCriteria);
    }
    
    /**
     * 通过id查记录
     * @param id
     * @return
     */
    public BoSystemCategory getSystemCategory(String id){
        return systemCategoryDao.findById(BoSystemCategory.class,id);
    }
    
    /**
     * 保存操作
     * @param systemCategory
     * @throws Exception
     */
    public void save(BoSystemCategory systemCategory) throws Exception{
        if(StringUtils.isBlank(systemCategory.getId())){
            systemCategory.setId(null);
        }
        //code 必须唯一
        BoSystemCategory oldSystemCategory = systemCategoryDao.findByCode(systemCategory.getCode());
        systemCategory.setValid(1);
        if(systemCategory.getId() != null){
        	if(oldSystemCategory==null){
        		throw  new Exception("修改失败，请联系管理员！");
        	}
            BeanUtils.copyExceptNull(oldSystemCategory, systemCategory);
            systemCategoryDao.update(oldSystemCategory);
        }else{
        	 if(oldSystemCategory != null ){
                 throw  new Exception("记录已存在");
             }
            systemCategory.setId(systemCategoryDao.getNextSeqId(IdSeq.SystemCategory));
            systemCategoryDao.add(systemCategory);
        }
    }
    
    /**
     * 删除操作
     * @param id
     */
    public void delete(String id){
        BoSystemCategory systemCategory = new BoSystemCategory();
        systemCategory.setId(id);
        systemCategoryDao.remove(systemCategory);
    }
    
    /**
     * 通过code查询记录
     * @param code
     * @return
     */
    public BoSystemCategory getSystemCategoryByCode(String code){
        return systemCategoryDao.findByCode(code);
    }

    /****
     * 返回所有有效系统
     * @return
     */
    public List<BoSystemCategory> list(){
        return systemCategoryDao.findAll();
    }
}
