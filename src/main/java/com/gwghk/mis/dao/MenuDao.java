package com.gwghk.mis.dao;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import com.gwghk.mis.common.dao.MongoDBBaseDao;
import com.gwghk.mis.enums.IdSeq;
import com.gwghk.mis.model.BoMenu;
import com.gwghk.mis.model.BoRole;
import com.mongodb.WriteResult;

/**
 * 摘要：系统管理-菜单相关DAO实现
 * @author Gavin.guo
 * @date   2015年3月9日
 */
@Repository(value = "menuDao")
public class MenuDao extends MongoDBBaseDao{
	
	/**
	 * 功能：根据Id -->获取菜单对象
	 */
	public BoMenu getMenuById(String menuId){
		return this.findById(BoMenu.class, menuId);
	}
	
	/**
	 * 功能：根据code -->获取菜单对象
	 */
	public BoMenu getMenuByCode(String menuCode){
		return this.findOne(BoMenu.class, Query.query(new Criteria().andOperator(Criteria.where("code").is(menuCode),Criteria.where("valid").is(1))));
	}
	
	/**
	 * 功能：新增菜单
	 */
	public void addMenu(BoMenu menu){
		menu.setValid(1);
		menu.setMenuId(this.getNextSeqId(IdSeq.Menu));
		this.add(menu);
	}
	
	/**
	 * 功能：更新菜单
	 */
	public void updateMenu(BoMenu menu){
		Query query = new Query();
	    query.addCriteria(Criteria.where("menuId").is(menu.getMenuId()));
	    this.update(query, menu);
	}
	
	/**
	 * 功能：批量删除菜单信息
	 */
	public void deleteMenu(Object[] menuIds){
		Query query = new Query();
		query.addCriteria(Criteria.where("menuId").in(menuIds));
		this.batchUpdate(query, Update.update("valid", 0), BoMenu.class);
	}
	
	/**
	 * 功能：查询菜单列表(不包含功能)
	 */
	public List<BoMenu> getMenuList(){
		Query query = new Query();
		query.addCriteria(Criteria.where("valid").is(1));
		query.addCriteria(Criteria.where("type").is(0));
		return this.findList(BoMenu.class, query);
	}
	
	/**
	 * 功能：查询所有菜单列表
	 */
	public List<BoMenu> getAllMenuList(){
		Query query = new Query();
		query.with(new Sort(Direction.ASC,"sort","id"));
		query.addCriteria(Criteria.where("valid").is(1));
		return this.findList(BoMenu.class, query);
	}
	
	/**
	 * 功能：获取父级菜单列表
	 */
	public List<BoMenu> getMenuParentList(){
		Query query = new Query();
		query.addCriteria(Criteria.where("valid").is(1));
		query.addCriteria(Criteria.where("type").is(0));
		query.addCriteria(new Criteria().orOperator(Criteria.where("parentMenuId").is(""),Criteria.where("parentMenuId").is(null)));
		return this.findList(BoMenu.class, query);
	}

	/**
	 * 功能：获取子功能列表
	 */
	public List<BoMenu> getSubFunByMenuId(String menuId,String roleId){
		Criteria criteria=new Criteria();
		criteria.and("valid").is(1);
		criteria.and("type").is(1);
		criteria.and("parentMenuId").is(menuId);
		criteria.and("roleList.roleId").is(roleId);
		return this.findList(BoMenu.class, Query.query(criteria).with(new Sort(new Order(Direction.ASC, "sort"))));
	}
	
	/**
	 * 通过角色id找菜单列表
	 * @param roleId
	 * @return
	 */
	public List<BoMenu> getMenuByRoleId(String roleId) {
		Query query = new Query();
		query.with(new Sort(Direction.ASC,"sort","id"));
		return this.findList(BoMenu.class,query.addCriteria(new Criteria().andOperator(Criteria.where("roleList.roleId").is(roleId),Criteria.where("valid").is(1))));
	}
	
	/**
	 * 根据菜单id数组及角色id删除菜单角色
	 * @param menuIds
	 * @param roleId
	 * @return
	 */
	public boolean deleteMenuRole(Object[] menuIds,String roleId){
		Criteria criteria=new Criteria();
		criteria=Criteria.where("valid").is(1);
		if(menuIds!=null){
			criteria.and("menuId").nin(menuIds);
		}
		BoRole role=new BoRole();
		role.setRoleId(roleId);
		criteria.and("roleList.roleId").is(roleId);
		WriteResult wr=this.mongoTemplate.updateMulti(Query.query(criteria),new Update().pull("roleList",role),BoMenu.class);
		return wr!=null&&wr.getN()>=0;
	}
	
	/**
	 * 根据菜单id数组及角色id设置菜单角色
	 * @param menuIds
	 * @param roleId
	 * @return
	 */
	public boolean addMenuRole(Object[] menuIds,String roleId){
		BoRole role=new BoRole();
		role.setRoleId(roleId);
		Query query=Query.query(new Criteria().andOperator(Criteria.where("valid").is(1),Criteria.where("menuId").in(menuIds),
				Criteria.where("roleList.roleId").ne(roleId)));
		WriteResult wr=this.mongoTemplate.updateMulti(query,new Update().push("roleList",role),BoMenu.class);
		return wr!=null&&wr.getN()>=0;
	}

	/**
	 * 通过code与父类菜单id找对应记录
	 * @param parentMenuId
	 * @param code
	 * @return
	 */
	public BoMenu getByParentIdAndCode(String parentMenuId, String code) {
		return this.findOne(BoMenu.class, Query.query(new Criteria().andOperator(Criteria.where("valid").is(1),Criteria.where("parentMenuId").is(parentMenuId),Criteria.where("code").is(code))));
	}
	
}
