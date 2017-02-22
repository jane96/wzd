package com.web.common;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.subject.Subject;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.google.common.collect.Maps;
import com.web.dao.UserDao;
import com.web.entity.User;
import com.web.security.CaptchaException;
import com.web.security.SystemAuthorizingRealm.Principal;


/**
 * 获取用户工具类
 * @author hz
 *
 */
public class UserUtils {

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	

	public static final String CACHE_USER = "user";
	public static final String CACHE_ROLE_LIST = "roleList";
	public static final String CACHE_MENU_LIST = "menuList";
	/**
	 * 获取用户
	 * @return
	 */
	public static User getUser(){
		User user = null;
		if (user == null){
			try{
				Subject subject = SecurityUtils.getSubject();
				System.out.println("subject==null:" +(subject==null));
				Principal principal = (Principal)subject.getPrincipal();
				System.out.println("principal==null:" + (principal==null));
				if (principal!=null){
					System.out.println("Principal不为空");
					user = userDao.get(principal.getId());
	//					Hibernate.initialize(user.getRoleList());
					
				}
			}catch (UnavailableSecurityManagerException e) {
				throw new  CaptchaException("UnavailableSecurityManagerException");
			}catch (InvalidSessionException e){
				throw new  CaptchaException("InvalidSessionException");
				
			}
		}
		if (user == null){
			user = new User();
			try{
				SecurityUtils.getSubject().logout();
			}catch (UnavailableSecurityManagerException e) {
				
			}catch (InvalidSessionException e){
				
			}
		}
		return user;
	}
	
	/**
	 * 是否清除用户缓存
	 * @param isRefresh
	 * @return
	 */
	public static User getUser(boolean isRefresh){
		if (isRefresh){
			removeCache(CACHE_USER);
		}
		return getUser();
	}

	/*public static List<Role> getRoleList(){
		@SuppressWarnings("unchecked")
		List<Role> list = (List<Role>)getCache(CACHE_ROLE_LIST);
		if (list == null || list.size() <= 0){
			User user = getUser();
			DetachedCriteria dc = roleDao.createDetachedCriteria();
			dc.createAlias("office", "office");
//			dc.createAlias("userList", "userList", JoinType.LEFT_OUTER_JOIN);
//			dc.add(dataScopeFilter(user, "office", "userList"));
//			dc.createAlias("createBy","createBy");
//			dc.add(Restrictions.eq("createBy.id",user.getId()));
			dc.add(dataScopeFilter(user, "office", "createBy"));
			dc.add(Restrictions.eq(Role.FIELD_DEL_FLAG, Role.DEL_FLAG_NORMAL));
//			dc.addOrder(Order.asc("office.code")).addOrder(Order.asc("name"));
			list = roleDao.find(dc);
			putCache(CACHE_ROLE_LIST, list);
		}
		return list;
	}*/
	
	/**
	 * 超级管理员根据机构获取角色
	 * @return
	 */
	/*public static List<Role> getRoleListByOrgName(String orgId){
		@SuppressWarnings("unchecked")
		List<Role> list = (List<Role>)getCache(CACHE_ROLE_LIST);
		if (list == null || list.size() <= 0){
			User user = getUser();
			DetachedCriteria dc = roleDao.createDetachedCriteria();
			dc.createAlias("office", "office");
//			dc.createAlias("userList", "userList", JoinType.LEFT_OUTER_JOIN);
//			dc.add(dataScopeFilter(user, "office", "userList"));
//			dc.createAlias("createBy","createBy");
//			dc.add(Restrictions.eq("createBy.id",user.getId()));
			dc.add(dataScopeFilter(user, "office", "createBy"));
			dc.add(Restrictions.eq(Role.FIELD_DEL_FLAG, Role.DEL_FLAG_NORMAL));
//			dc.addOrder(Order.asc("office.code")).addOrder(Order.asc("name"));
			list = roleDao.find(dc);
			putCache(CACHE_ROLE_LIST, list);
		}
		return list;
	}
	*/
	/*public static List<Menu> getMenuList(){
		@SuppressWarnings("unchecked")
		List<Menu> menuList = (List<Menu>)getCache(CACHE_MENU_LIST);
		if (menuList == null){
			User user = getUser();
			if (user.isAdmin()){
				menuList = menuDao.findAllList();
			}else{
				menuList = menuDao.findByUserId(user.getId());
			}
			putCache(CACHE_MENU_LIST, menuList);
		}
		return menuList;
	}*/
	/**
	 * 获取用户ID
	 * @param id
	 * @return
	 */
	public static User getUserById(String id){
		if(StringUtils.isNotBlank(id)) {
			return userDao.get(id);
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	
	public static Object getCache(String key) {
		return getCache(key, null);
	}
	/**
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static Object getCache(String key, Object defaultValue) {
		Object obj = getCacheMap().get(key);
		return obj==null?defaultValue:obj;
	}
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public static void putCache(String key, Object value) {
		getCacheMap().put(key, value);
	}
	/**
	 * 
	 * @param key
	 */
	public static void removeCache(String key) {
		getCacheMap().remove(key);
	}
	/**
	 * 
	 * @return
	 */
	public static Map<String, Object> getCacheMap(){
		Map<String, Object> map = Maps.newHashMap();
		try{
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal)subject.getPrincipal();
			return principal!=null?principal.getCacheMap():map;
		}catch (UnavailableSecurityManagerException e) {
			
		}catch (InvalidSessionException e){
			
		}
		return map;
	}
}
