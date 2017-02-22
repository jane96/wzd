
package com.web.security;

import java.io.IOException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.web.common.Encodes;
import com.web.common.SpringContextHolder;
import com.web.common.UserUtils;
import com.web.common.ValidateCodeServlet;
import com.web.controller.LoginController;
import com.web.entity.Permission;
import com.web.entity.User;
import com.web.service.SystemService;


/**
 * 系统安全认证实现类
 * @author hz
 * 
 */

public class SystemAuthorizingRealm extends AuthorizingRealm {

	private SystemService systemService;

	/**
	 * 认证回调函数, 登录时调用
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		DES des = new DES();
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		if (LoginController.isValidateCodeLogin(token.getUsername(), false, false)){
			// 判断验证码
			Session session = SecurityUtils.getSubject().getSession();
			String code = ValidateCodeServlet.gCode;//获取全局的验证码
			System.out.println(code);
			if (token.getCaptcha() == null || !token.getCaptcha().toUpperCase().equals(code)){
				throw new CaptchaException("验证码错误.");//返回到登录页面
			}
		}
		User user = null;
		try{
			user = getSystemService().getUserByLoginId(token.getUsername());
		}catch(Exception e){
			
		}
		
		if (user != null) {
			 des = new DES();
			//byte[] salt = Encodes.decodeHex(user.getPassword().substring(0,16));
			return new SimpleAuthenticationInfo(new Principal(user),user.getPassword(), getName());
		} else {
			return null;
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 *当有@RequiresPermissions时才调用，是从当前用户对应的角色的权限
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("授权查询回调函数-------------");
		Principal principal = (Principal) getAvailablePrincipal(principals);
		User user = getSystemService().getUserByLoginId(principal.getLoginId());
		if (user != null) {
			UserUtils.putCache("user", user);
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			/**
			 *  添加基于Permission的权限信息，先查找用户角色，再查找角色拥有的权限
			 */
			DES des = new DES();
			List<String> list = new ArrayList<String>();
			List<Permission> permissions = user.getRole().getRolePermission();
			for(Permission p:permissions){
				String s = null;
				try {
					s = new String(des.decrypt(des.base64decoder.decodeBuffer(p.getPermissionName()),des.PASSWORD));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				list.add(s);
			}
			info.addStringPermissions(list);
			// 更新登录IP和时间
			//getSystemService().updateUserLoginInfo(user.getId());
			return info;
		} else {
			return null;
		}
	}
	
	/**
	 * 设定密码校验的Hash算法与迭代次数
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		//		matcher.setHashIterations(SystemService.HASH_INTERATIONS);
		//使用自定义的验证方法
		setCredentialsMatcher(new CustomCredentialsMatcher());
	}
	
	/**
	 * 清空用户关联权限认证，待下次使用时重新加载
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清空所有关联认证
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

	/**
	 * 获取系统业务对象
	 */
	public SystemService getSystemService() {
		if (systemService == null){
			systemService = SpringContextHolder.getBean(SystemService.class);
		}
		return systemService;
	}
	
	/**
	 * 授权用户信息
	 */
	public static class Principal implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private String id;
		private String loginId;
		
		public String getLoginId() {
			return loginId;
		}

		public void setLoginId(String loginId) {
			this.loginId = loginId;
		}



		private Map<String, Object> cacheMap;

		public Principal(User user) {
			this.id = user.getLoginId();
			this.loginId = user.getLoginId();
			
		}

		public String getId() {
			return id;
		}

		

		public Map<String, Object> getCacheMap() {
			if (cacheMap==null){
				cacheMap = new HashMap<String, Object>();
			}
			return cacheMap;
		}

	}
}
