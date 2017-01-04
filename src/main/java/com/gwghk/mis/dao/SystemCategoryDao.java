package com.gwghk.mis.dao;

import com.gwghk.mis.common.dao.MongoDBBaseDao;
import com.gwghk.mis.model.BoSystemCategory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kirk on 2016/11/2.
 */
@Repository
public class SystemCategoryDao extends MongoDBBaseDao {
    public BoSystemCategory findByCode(String code){
        Query query = new Query();
        Criteria criteria = new Criteria("code");
        criteria.is(code);
        query.addCriteria(criteria);
        return this.findOne(BoSystemCategory.class,query);
    }

    /****
     * 返回可用的系统列表
     * @return
     */
    public List<BoSystemCategory> findAll(){
        Query query = new Query(Criteria.where("valid").is(1));
        return this.findList(BoSystemCategory.class,query);
    }
}
