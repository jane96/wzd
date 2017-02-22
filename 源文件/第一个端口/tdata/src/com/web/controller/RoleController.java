package com.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.entity.Permission;
import com.web.entity.Role;
import com.web.security.DES;
import com.web.service.SystemService;

@Controller
@RequestMapping("role")
public class RoleController {
	@Autowired
	SystemService systemService;
	/*
	 * 存储角色
	 * 角色分为：
	 * 	1.系统角色(所有权限)
	 * 	2.管理员角色(所有权限)
	 * 	3.代理角色(查看权限)
	 * */
	@RequestMapping("save")
	public String save() throws IOException, Exception{
		DES des = new DES();
		/**
		 *赋予system角色所有权限 
		 */
		Role role1 = new Role();
		role1.setRoleName( des.base64encoder.encode(des.encrypt(("system").getBytes(), des.PASSWORD)));
		//查找系统角色的权限
		List<Permission> list1 = systemService.findAllList();
		role1.setRolePermission(list1);
		systemService.save(role1);
		/**
		 * 赋予admin角色所有权限
		 */
		//查找管理员角色权限
		Role role2 = new Role();
		role2.setRoleName( des.base64encoder.encode(des.encrypt(("admin").getBytes(), des.PASSWORD)));
		List<Permission> list2 = systemService.findAllList();
		role2.setRolePermission(list2);
		systemService.save(role2);
		/**
		 * 赋予代理用户select权限
		 */
		//查找代理角色权限
		Role role3 = new Role();
		role3.setRoleName( des.base64encoder.encode(des.encrypt(("proxy").getBytes(), des.PASSWORD)));
		List<Permission> list3 = new ArrayList<Permission>();
		for(int i = 0; i < list2.size(); i++){//查找所有权限中等于select的，赋予给代理角色
			if("select".equals(new String(des.decrypt(des.base64decoder.decodeBuffer(list2.get(i).getPermissionName()),des.PASSWORD)))){
				list3.add(list2.get(i));
				role3.setRolePermission(list3);
				break;
			}
		}
		systemService.save(role3);
		return "index.html";
	}
}
