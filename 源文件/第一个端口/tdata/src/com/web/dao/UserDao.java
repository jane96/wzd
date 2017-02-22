package com.web.dao;

import org.springframework.stereotype.Repository;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.entity.User;

/**
 * 用户dao类
 * @author hz
 */
@Repository("userDao")
public class UserDao extends BaseDao<User> {
	/**
	 * 通过hql获取用户实体对象
	 * @param loginId 用户ID
	 * @return User 返回的用户实体类
	 */
	public User findByLoginId(String loginId){
		User user = getByHql("from User where loginId = '" + loginId + "'");
		return user;
	}
	
	public User findByName(String name){
		User user = getByHql("from User where name = '" + name + "'");
		return user;
				
	}
}
