package com.web.dao;

import org.springframework.beans.factory.annotation.Autowired;




import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.web.common.Reflections;
import com.web.common.StringUtils;
import com.web.entity.User;
import com.web.persistence.Page;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;

/**
 * 基本的DAO类
 * @author hz
 *
 * @param <T> 泛型类型
 */
public class BaseDao<T>{
	@Autowired
	private SessionFactory sessionFactory;
	/**
	 * 实体类类型(由构造方法自动赋值)
	 */
	private Class<?> entityClass;
	
	/**
	 * 构造方法，根据实例类自动获取实体类类型
	 */
	public BaseDao() {
		entityClass = Reflections.getClassGenricType(getClass());
	}
	/**
	 * 获取session
	 * @return Session 获取的session连接
	 */
	public Session getSession(){
		try{
			if(sessionFactory == null)
				System.out.println("未注入");
			else
				System.out.println("sessionFactory不为空");
		}catch(Exception e){
			e.printStackTrace();
		}
		return sessionFactory.getCurrentSession();
	}
	/**
	 * 更新
	 * @param entity
	 */
	public void updateOnly(T entity){
		
		getSession().update(entity);
	}
	/**
	 * 保存或者更新
	 */
	public void save(T entity){
		try {
			// 获取实体编号
			Object id = null;
			for (Method method : entity.getClass().getMethods()){
				Id idAnn = method.getAnnotation(Id.class);
				if (idAnn != null){
					id = method.invoke(entity);
					break;
				}
			}
			// 插入前执行方法
			if (null == (String)id){
				for (Method method : entity.getClass().getMethods()){
					PrePersist pp = method.getAnnotation(PrePersist.class);
					if (pp != null){
						method.invoke(entity);
						break;
					}
				}
			}
			// 更新前执行方法
			else{
				for (Method method : entity.getClass().getMethods()){
					PreUpdate pu = method.getAnnotation(PreUpdate.class);
					if (pu != null){
						method.invoke(entity);
						break;
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		getSession().saveOrUpdate(entity);
		
	}
	/**
	 * 保存实体列表
	 * @param entityList
	 */
	public void save(List<T> entityList){
		for (T entity : entityList){
			save(entity);
		}
	}
	/**
	 * 只保存实体
	 * @param entity
	 */
	public void saveOnly(T entity){
		try {
			// 获取实体编号
			Object id = null;
			for (Method method : entity.getClass().getMethods()){
				Id idAnn = method.getAnnotation(Id.class);
				if (idAnn != null){
					id = method.invoke(entity);
					break;
				}
			}
			// 插入前执行方法
			if (StringUtils.isBlank((String)id)){
				for (Method method : entity.getClass().getMethods()){
					PrePersist pp = method.getAnnotation(PrePersist.class);
					if (pp != null){
						method.invoke(entity);
						break;
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		getSession().save(entity);
	}
	/**
	 * 通过hql获取一条数据
	 * @param qlString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T getByHql(String qlString){
		Query query = createQuery(qlString);
		query.setCacheable(true);
		return (T)query.uniqueResult();
	}
	/**
	 * 通过hql获得数据列表
	 * @param qlString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T getByHqlList(String qlString){
		Query query = createQuery(qlString);
		query.setCacheable(true);
		return (T)query.list();
	}
	/**
	 * 通过query获取数据
	 * @param qlString
	 * @return
	 */
	public Query createQuery(String qlString){
		Query query = getSession().createQuery(qlString);
		return query;
	}
	/**
	 * 通过序列化的id获取实体对象
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T get(Serializable id){
		return (T)getSession().get(entityClass, id);
	}
	/**
	 * 使用检索标准对象分页查询
	 * @param page
	 * @param detachedCriteria
	 * @return
	 */
	public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria) {
		return find(page, detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
	}
	/**
	 * 使用检索标准对象分页查询
	 * @param page
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria, ResultTransformer resultTransformer) {
		// get count
		if (!page.isDisabled() && !page.isNotCount()){
			page.setAllCount(count(detachedCriteria));//获得总记录数
			if (page.getAllCount() < 1) {
				return page;
			}
		}
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		criteria.setCacheable(true);
		criteria.setResultTransformer(resultTransformer);
		// set page	
		if (!page.isDisabled()){
	        criteria.setFirstResult(page.getFirstResult());
	        criteria.setMaxResults(page.getMaxResults()); 
		}
		// order by
		if (StringUtils.isNotBlank(page.getOrderBy())){
			
				//目前只考虑单列排序
				String orderStr = page.getOrderBy();
				if(orderStr.split("-")[0].equals("desc"))
				{
					criteria.addOrder(Order.desc(orderStr.split("-")[1]));
				}
				else
				{
					criteria.addOrder(Order.asc(orderStr.split("-")[1]));
				}
			
		}
		//pageCount
		long number = page.getAllCount();
		long pageNumber = 0;
		if(number % page.getPageSize() ==0)
			pageNumber = number / page.getPageSize();
		else
			pageNumber = number / page.getPageSize() + 1;
		page.setPageCount(pageNumber);
		
		page.setList(criteria.list());
		return page;
	}
	/**
	 * 创建与会话无关的检索标准对象
	 * @param criterions Restrictions.eq("name", value);
	 * @return 
	 */
	public DetachedCriteria createDetachedCriteria(Criterion... criterions) {
		DetachedCriteria dc = DetachedCriteria.forClass(entityClass);
		for (Criterion c : criterions) {
			dc.add(c);
		}
		return dc;
	}
	/**
	 * 获取查询的总条数
	 * @param detachedCriteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public long count(DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		long totalCount = 0;
		try {
			// Get orders
			Field field = CriteriaImpl.class.getDeclaredField("orderEntries");
			field.setAccessible(true);
			List orderEntrys = (List)field.get(criteria);
			// Remove orders
			field.set(criteria, new ArrayList());
			// Get count
			criteria.setProjection(Projections.rowCount());
			totalCount = Long.valueOf(criteria.uniqueResult().toString());
			// Clean count
			criteria.setProjection(null);
			// Restore orders
			field.set(criteria, orderEntrys);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return totalCount;
	}
	/**
	 * SQL 更新
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public int updateBySql(String sqlString){
		return createSqlQuery(sqlString).executeUpdate();
	}
	
	/**
	 * 创建 SQL 查询对象
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public SQLQuery createSqlQuery(String sqlString){
		SQLQuery query = getSession().createSQLQuery(sqlString);
		return query;
	}
	/**
	 * 更新
	 * @param qlString
	 * @return
	 */
	public int update(String qlString){
		return createQuery(qlString).executeUpdate();
	}
	/**
	 * 获取泛型实体对象
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T load(Serializable id) {
		T t = null;
		try {
			t = (T) getSession().load(entityClass, id);
		} catch (Exception e) {
		}
		return t;
	}
	/**
	 * 数据删除
	 * @param id
	 * @return
	 */
	public void deleteForId(Serializable id){
		T t = load(id);
		getSession().delete(t);
	}
	
	/**
	 * 更新信息
	 * @param id
	 * @param likeParentIds
	 * @return
	 */
	public int deleteById(Serializable id){
		return update("update "+entityClass.getSimpleName()+ "' where id = '" +id);
	}
}
