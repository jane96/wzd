package com.web.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.common.BaseController;
import com.web.common.Md5Util;
import com.web.entity.Proxy;
import com.web.entity.Role;
import com.web.entity.User;
import com.web.persistence.Page;
import com.web.security.DES;
import com.web.service.SystemService;

/**
 * 用户信息管理控制类
 * @author hz
 *
 */
@Controller
@RequestMapping("proxy")
public class ProxyController extends BaseController{
	@Autowired
	SystemService systemService;
	
	/**
	 * 保存代理用户
	 * @return Map 成功或者错误的json消息
	 * @throws Exception 
	 * @throws IOException 
	 */
	@RequestMapping("save")
	@RequiresPermissions(value={"update","save"})//需要的相应权限
	@ResponseBody
	public String saveProxy(HttpServletResponse response,HttpServletRequest request) throws IOException, Exception{
		User user = new User();
		DES des = new DES();
		// 查找用户角色
		Role role = systemService.findRoleByName("from Role where roleName = '"
				+ new String(des.base64encoder.encode(des.encrypt("proxy".getBytes(), des.PASSWORD))) + "'");
		user.setRole(role);
		// 对登陆ID进行存储
		String newId = productId();
		user.setLoginId(newId);
		
		// 对登录账号名进行加密存储
		user.setName(des.base64encoder.encode(des.encrypt((productRandomLoginName() + newId).getBytes(), des.PASSWORD)));
		
		// 对密码进行DES加密存储
		user.setPassword(des.base64encoder.encode(des.encrypt(productRandomPassword().getBytes(), des.PASSWORD)));
		
		// 对房卡数量进行DES加密存储,初始房卡数量为：0
		user.setRecordNumber(des.base64encoder.encode(des.encrypt(String.valueOf(0).getBytes(), des.PASSWORD)));
		
		// 堆当前房卡数量进行DES加密存储，初始房卡数量为：0
		user.setCurrentNumber(des.base64encoder.encode(des.encrypt(String.valueOf(0).getBytes(), des.PASSWORD)));
		
		// 存储代理用户
		systemService.save(user);

		// 处理代理用户信息
		Proxy proxy = new Proxy();
		proxy.setDate(new Date());// 日期不加密
		proxy.setUser(user);
		
		// 存储代理账户信息
		systemService.save(proxy);
		
		// 获取代理账户列表
		findProxy(response, request);
		
		return null;
	}
	
	/**
	 * 更新代理用户
	 * @param user 用户类
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("update")
	@RequiresPermissions(value = { "update","save" })//该权限才能操作
	@ResponseBody
	public Map updateProxy(Proxy proxy,HttpServletResponse response,HttpServletRequest request) throws IOException{
		Properties initProp = new Properties(System.getProperties());
		System.out.println("file.encoding:" + initProp.getProperty("file.encoding"));
		System.out.println("file.encoding:" + initProp.getProperty("user.language"));
		DES des = new DES();
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			
			String password = request.getParameter("password");//获取手机号
			String rechargeNumber = request.getParameter("rechargeNumber");//获取充值数量
			Proxy proxy2 = systemService.getProxy(proxy.getId());
			
			if(StringUtils.isNotBlank(proxy.getProxyPhone())){//设定手机号，手机号不加密
				proxy2.setProxyPhone(proxy.getProxyPhone());
			}
			if(StringUtils.isNotBlank(proxy.getProxyWeChat())){//设定微信号,java.net.URLDecoder解码UTF-8的编码，防止ie乱码
				
				proxy2.setProxyWeChat(new String(des.base64encoder.encode(des.encrypt(URLDecoder.decode(proxy.getProxyWeChat(),"UTF-8").getBytes(), des.PASSWORD))));
			}
			if(StringUtils.isNotBlank(password)){//设定密码
				proxy2.getUser().setPassword(des.base64encoder.encode(des.encrypt(password.getBytes(), des.PASSWORD)));
			}
			
			if(StringUtils.isNotBlank(rechargeNumber)){//设定充值数量
				String currentNumber = new String(des.decrypt(des.base64decoder.decodeBuffer(proxy2.getUser().getCurrentNumber()),des.PASSWORD));
				String recordNumber = new String(des.decrypt(des.base64decoder.decodeBuffer(proxy2.getUser().getRecordNumber()),des.PASSWORD));
				proxy2.getUser().setCurrentNumber(des.base64encoder.encode(des.encrypt(String.valueOf(Long.parseLong(currentNumber) + Long.parseLong(rechargeNumber)).getBytes(),des.PASSWORD)));
				proxy2.getUser().setRecordNumber(des.base64encoder.encode(des.encrypt(String.valueOf(Long.parseLong(recordNumber) + Long.parseLong(rechargeNumber)).getBytes(),des.PASSWORD)));
			}
			
			systemService.update(proxy2);
			
			map.put("status", 1);
			map.put("message", "更改代理商信息成功！");
		}catch(Exception e){
			map.put("status", 2);
			map.put("message", "" + e.getMessage());
		}
		return map;
	}
	
	/**
	 * 查找代理用户
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("find")
	@RequiresPermissions(value={"update","save"})//需要的相应权限
	public String findProxy(HttpServletResponse response,HttpServletRequest request) throws Exception{
		//加密解密类
		DES des = new DES();
		//获取参数
		String phone = request.getParameter("phone");
		String loginId = request.getParameter("loginId");
		//查找生成的代理商
		System.out.println("进入代理商查询");
		Page<Proxy> page = systemService.findProxy(new Page<Proxy>(request,response),phone,loginId);
		List<Proxy> list = page.getList();
		for(Proxy p : list){
			if(StringUtils.isNotBlank(p.getProxyWeChat())){//微信解密 
				p.setProxyWeChat(new String(des.decrypt(des.base64decoder.decodeBuffer(p.getProxyWeChat()),des.PASSWORD)));
			}
			p.getUser().setCurrentNumber(new String(des.decrypt(des.base64decoder.decodeBuffer(p.getUser().getCurrentNumber()),des.PASSWORD)));
				p.getUser().setRecordNumber(new String(des.decrypt(des.base64decoder.decodeBuffer(p.getUser().getRecordNumber()),des.PASSWORD)));
			p.getUser().setName(new String(des.decrypt(des.base64decoder.decodeBuffer(p.getUser().getName()),des.PASSWORD)));
			p.getUser().setPassword(new String(des.decrypt(des.base64decoder.decodeBuffer(p.getUser().getPassword()),des.PASSWORD)));
		}
		//输出json流
		this.sendObjectToJson(page, response);
		return null;
	}
	
	@RequestMapping(value="updatePassword")
	@ResponseBody
	public Map<String,Object> updatePassword(User user){
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			User user2 = systemService.getUserByLoginId(user.getLoginId());
			user2.setPassword(DES.base64encoder.encode(DES.encrypt(user.getPassword().getBytes(), DES.PASSWORD)));
			systemService.update(user2);
			map.put("status", 1);
			map.put("message", "success");
		}catch(Exception e){
			map.put("status", 2);
			map.put("message", "更改密码失败：" + e.getMessage());
		}
		return map;
	} 
	/**
	 * 生成最大Id
	 * @return String 最大的id字符串
	 * @throws IOException
	 * @throws Exception
	 */
	public String productId() throws IOException, Exception{
		DES des = new DES();
		//找出最大的登录Id
		
		List<Proxy> proxys = systemService.findAllProxy();//查找代理用户
		int max = 0;
		int current = 0;
		for(Proxy proxy : proxys){//找出最大的id号
			String s = proxy.getUser().getLoginId();
			current = Integer.parseInt(s);
			if(current > max){
				max = current;
			}
		}
		if(proxys.size() > 0){//如果存在用户
			max = max + 1;
		}else{//如果不存在用户
			max = 0;
		}
		String maxString = String.valueOf(max);
		String result = maxString;
		for(int i = maxString.length(); i <=4; i++){
			result = "0" + result;
		}
		System.out.println("id结果为：" + result);
		return result;
		
	}
	 /**
	  * 生成ID
	  * @return String 生成的ID
	  */
	public String productRandomLoginName(){
		String id = "wzd";
		return id;
	}
	/**
	 * 生成随机密码
	 * @return String 返回的随机密码
	 */
	public String productRandomPassword(){
		String password = "";
		for(int i = 0; i < 7; i++){
			int number = (int) (Math.random() * 10);
			password = password + number + "";
		}
		return password;
	}
}
