package com.web.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.common.BaseController;
import com.web.entity.Permission;
import com.web.security.DES;
import com.web.service.SystemService;
@Controller
@RequestMapping("permission")
public class PermissionController extends BaseController {
	@Autowired
	SystemService systemService;
	
	/**
	 * 增加权限信息
	 * 权限分为四种：
	 * 1.增加 save
	 * 2.删除 delete
	 * 3.更新 update
	 * 4.查找select 
	 */
	@RequestMapping("save")
	public String save(){
		ArrayList<String> list = new ArrayList<String>();
		list.add("save");
		list.add("delete");
		list.add("update");
		list.add("select");
		for(int i = 0; i < list.size(); i++){
			DES des = new DES();
			Permission permission = new Permission();
			permission.setPermissionName(des.base64encoder.encode(des.encrypt((list.get(i)).getBytes(), des.PASSWORD)));
			//存储到数据库
			systemService.save(permission);
		}
		return "index.html";
	}
}
