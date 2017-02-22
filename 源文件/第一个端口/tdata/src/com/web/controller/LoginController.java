package com.web.controller;

import java.io.IOException;






import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.google.common.collect.Maps;

import com.web.common.BaseController;
import com.web.common.CacheUtils;
import com.web.common.Md5Util;
import com.web.common.StringUtils;
import com.web.common.UserUtils;
import com.web.common.ValidateCodeServlet;
import com.web.entity.Role;
import com.web.entity.User;
import com.web.mapper.JsonMapper;
import com.web.security.DES;
import com.web.service.SystemService;
import com.web.test.Test; 
@Controller
/**
 * 管理登录控制器
 */
public class LoginController extends BaseController {
	
	@Autowired
	SystemService systemService;
	
	/**
	 * 获取用户角色
	 * @return Map 
	 */
	@RequestMapping("role")
	@ResponseBody
	public Map get() {
		Map map = new HashMap();
		User user = UserUtils.getUser();//获取当前用户
		DES des = new DES();
		try{
			if(StringUtils.isNotEmpty(user.getRole().getRoleName()))//判断是否是空
				map.put("role",new String(des.decrypt(des.base64decoder.decodeBuffer(user.getRole().getRoleName()),des.PASSWORD)));
			else{
				return map;
			}
			return map;
		}catch(Exception e){
			return map;
		}
	}
	/**
	 * get方式登陆
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String loginGet(HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		// 如果已经登录则注销
		if(user.getLoginId() != null){
			return "redirect:"+"/" ;//到注销页面，清除session
		}
		return "/html/sysLogin.jsp";
	}
	
	/**
	 * post登陆
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String loginPost(HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("进入postLogin页面");
		User user = UserUtils.getUser();
		// 如果已经登录，则跳转到管理首页
		if(user.getLoginId() != null){
			return "redirect:"+"/" ;
		}
		return "/html/sysLogin.jsp";
	}

	/**
	 * 登录失败，真正登录的POST请求由Filter完成
	 */
	@RequestMapping(value = "loginSuccess", method = RequestMethod.GET)
	public String login(@RequestParam(value=FormAuthenticationFilter.DEFAULT_USERNAME_PARAM,required=false) String username, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		System.out.println(user.getLoginId());
		// 如果已经登录，则跳转到管理首页
		if(user.getLoginId() != null){
			System.out.println("登录成功！");
			return "/html/index.html";
		}
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
		return "/html/sysLogin.jsp";
	}
	/**
	 * 任何方式都进入登陆页面
	 * @return
	 */
	@RequestMapping(value="/")
	public String login(){
		System.out.println("login");
		try{
			User user = UserUtils.getUser();
			user.getLoginId();
			SecurityUtils.getSubject().logout();//如果存在用户，则注销重新登录
		}catch(Exception e){
			
		}
		return "/html/sysLogin.jsp";
		
	}
	/**
	 * 错误页面
	 * @return
	 */
	@RequestMapping("error")
	public String error(){
		System.out.println("没有相应的权限");
		return "/html/error.html";
	}
	/**
	 * 验证码验证
	 */
	@RequestMapping("ValidateCodeServlet")
	public void validateLogin(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		System.out.println("进入ValidateCodeServlet Controller");
		new ValidateCodeServlet().doGet(request, response);
	}
	/**
	 * 是否是验证码登录
	 * @param useruame 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return boolean 是否是验证码登陆
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean){
		Map<String, Integer> loginFailMap = (Map<String, Integer>)CacheUtils.get("loginFailMap");
		if (loginFailMap==null){
			loginFailMap = Maps.newHashMap();//类似于Map
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum==null){
			loginFailNum = 0;
		}
		if (isFail){
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean){
			loginFailMap.remove(useruame);
		}
		
		return true;//启用验证码
	}
	/**
	 * 查找用户
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "findLoginUser")
	@ResponseBody
	public User findLoginUser(HttpServletResponse response ) throws Exception{
		HashMap<String,Object> map = new HashMap<String,Object>();
		DES des = new DES();
		User user = UserUtils.getUser();
		user.setName(new String(des.decrypt(des.base64decoder.decodeBuffer(user.getName()),des.PASSWORD)));
		//如果为代理用户，返回当前房卡数量
		if(new String(des.decrypt(des.base64decoder.decodeBuffer(user.getRole().getRoleName()), des.PASSWORD)).equals("proxy")){
			user.setRecordNumber(new String(des.decrypt(des.base64decoder.decodeBuffer(user.getRecordNumber()),des.PASSWORD)));
			user.setCurrentNumber(new String(des.decrypt(des.base64decoder.decodeBuffer(user.getCurrentNumber()), des.PASSWORD)));
		}
		this.sendObjectToJson(user, response);
		return null;
	}
	@RequestMapping(value="checkLogout")
	@ResponseBody
	public Map checkLogout(HttpServletResponse response){
		System.out.println("进入检查logout");
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			User user = UserUtils.getUser();
			user.getLoginId();
			map.put("status", 1);
			return map;
		}catch(Exception e){
			map.put("status",2);
			return map;
		}
	}
}
