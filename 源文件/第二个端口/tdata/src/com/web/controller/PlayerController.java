package com.web.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.common.BaseController;
import com.web.entity.Player;
import com.web.persistence.Page;
import com.web.security.DES;
import com.web.service.SystemService;

/**
 * 玩家管理控制类
 * @author hz
 *
 */
@Controller
@RequestMapping("player")
public class PlayerController extends BaseController{
	@Autowired
	SystemService systemService;
	//加密解密类
	DES des = new DES();
	
	/**
	 * 查找玩家信息
	 * @param player 玩家对象
	 * @param request
	 * @param response
	 * @return Map 成功或者错误的json消息
	 * @throws Exception
	 */
	@RequestMapping("find")
	@ResponseBody
	public Map<String,Object> find(Player player, HttpServletRequest request, HttpServletResponse response) throws Exception{
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			if(!StringUtils.isNotBlank(player.getpId()))
			{
				map.put("status", 203);
				map.put("message", "ID参数错误");
				return map;
			}	
			//查找玩家信息
			Player player2 =systemService.getPlayerNameById(des.base64encoder.encode(des.encrypt(player.getpId().getBytes(), des.PASSWORD)));
			//将查找的信息进行解密显示
			player2.setpId(new String(des.decrypt(des.base64decoder.decodeBuffer(player2.getpId()),des.PASSWORD)));
			if(StringUtils.isNotBlank(player2.getpWechat())){
				player2.setpWechat(new String(des.decrypt(des.base64decoder.decodeBuffer(player2.getpWechat()),des.PASSWORD)));
			}
			player2.setpNumber(new String(des.decrypt(des.base64decoder.decodeBuffer(player2.getpNumber()),des.PASSWORD)));
			player2.setDate(player2.getDate());
			this.sendObjectToJson(player2, response);
			return null;
		}catch(Exception e){
			map.put("status", 202);//202，查询ID不存在
			map.put("message", "查询ID不存在");
			return map;
		}
		
	}
	/**
	 * 返回玩家列表
	 * @param player 玩家
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping("findPlayer")
	@ResponseBody
	public void findPlayer(Player player,HttpServletRequest request,HttpServletResponse response) throws Exception{
		Page<Player> page = systemService.find(new Page<Player>(request,response),player);
		try {
			for(Player player2 : page.getList()){//将玩家信息进行解码
				player2.setpId(new String(des.decrypt(des.base64decoder.decodeBuffer(player2.getpId()), des.PASSWORD)));
				player2.setpNumber(new String(des.decrypt(des.base64decoder.decodeBuffer(player2.getpNumber()), des.PASSWORD)));
				player2.setpWechat(new String(des.decrypt(des.base64decoder.decodeBuffer(player2.getpWechat()), des.PASSWORD)));
			}
			this.sendObjectToJson(page, response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 新增玩家信息
	 * @param player 玩家信息类
	 * @param request
	 * @return  Map 成功或者错误的json消息
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("save")
	@ResponseBody
	public Map<String,Object> save(Player player,HttpServletRequest request) throws UnsupportedEncodingException{
		HashMap<String,Object> map = new HashMap<String,Object>();
		try{
			//验证主键参数
			if(!StringUtils.isNotBlank(player.getpId()) ){
				map.put("status",203);
				map.put("message", "ID参数错误");
				return map;
			}
			//验证是否主键重复
			Player player2 = systemService.getPlayerNameById(des.base64encoder.encode(des.encrypt(player.getpId().getBytes(), des.PASSWORD)));
			player2.getpId();//如果不报错，说明存在该主键
			map.put("status", 201);//主键重复
			map.put("message", "ID重复");
			return map;
		}catch(Exception e){
			//不做操作
		}
		
		try{
			//验证是否存在该参数
			if(!(StringUtils.isNotBlank(player.getpNumber()))){
				map.put("status",203);
				map.put("message", "房卡数量参数不能为空");
				return map;
			}
			//设定ID
			player.setpId(des.base64encoder.encode(des.encrypt(player.getpId().getBytes(), des.PASSWORD)));
			//设定微信
			if(StringUtils.isNotBlank(player.getpWechat())){
				player.setpWechat(new String(des.base64encoder.encode(des.encrypt(player.getpWechat().getBytes(), des.PASSWORD))));
			}
			//验证房卡数以及设定
			if(StringUtils.isNumeric(player.getpNumber())){
				player.setpNumber(des.base64encoder.encode(des.encrypt(player.getpNumber().getBytes(), des.PASSWORD)));
			}else{
				map.put("status", 203);//参数错误
				map.put("message", "房卡数量参数只能为数字");
				return map;
			}
			player.setDate(new Date());
			//保存到数据库
			systemService.save(player);
			map.put("status", 100);
			map.put("message", "success");
			return map;
		}catch(Exception e2){
			map.put("status", 400);//未知异常
			map.put("message", e2.getMessage());
			return map;
		}
	}
	/**
	 * 更新玩家信息类
	 * @param player 用户类
	 * @param request
	 * @return Map 成功或者错误的json消息
	 */
	@RequestMapping("update")
	@ResponseBody
	public Map<String,Object> update(Player player,HttpServletRequest request){
		HashMap<String,Object> map = new HashMap<String,Object>();
		Player player2 = null;
		try{
			//验证更新的ID是否存在
			if(!StringUtils.isNotBlank(player.getpId())){
				map.put("status",203);//ID参数错误
				map.put("message", "ID参数错误");
				return map;
			}
			player2 = systemService.getPlayerNameById(des.base64encoder.encode(des.encrypt(player.getpId().getBytes(), des.PASSWORD)));
			player2.getpId();//如果查询不存在则抛出异常
		}catch(Exception e){
			map.put("status", 201);//更新ID不存在
			map.put("message", "更新ID不存在");
			return map;
		}
		//查找玩家信息
		try{
			//更新玩家微信名字
			if(StringUtils.isNotBlank(player.getpWechat())){
				player2.setpWechat(new String(des.base64encoder.encode(des.encrypt(player.getpWechat().getBytes(), des.PASSWORD))));
			}
			//更新玩家房卡数量
			if(StringUtils.isNotBlank(player.getpNumber())){
				if(StringUtils.isNumeric(player.getpNumber())){//验证是否为数字
					player2.setpNumber(des.base64encoder.encode(des.encrypt(player.getpNumber().getBytes(), des.PASSWORD)));
				}else{
					map.put("status", 203);//参数错误
					map.put("message", "房卡数量参数只能为数字");
					return map;
				}
			}else{
				map.put("status", 203);//参数错误
				map.put("message", "房卡数量参数不能为空");
				return map;
			}
			
			//进行更新操作
			systemService.update(player2);
			map.put("status", 100);
			map.put("message", "success");
			return map;
		}catch(Exception e){
			map.put("status", 400);//其他错误
			map.put("message", e.getMessage());
			return map;
		}
		
	}
	/**
	 * 删除玩家信息
	 * @param player 玩家信息
	 * @return
	 */
	@RequestMapping("delete")
	@ResponseBody
	public Map<String,Object> delete(Player player){
		HashMap<String,Object> map = new HashMap<String,Object>();
		Player player2 = null;
		try{
			//验证删除的ID是否存在
			if(!StringUtils.isNotBlank(player.getpId())){
				map.put("status",203);//ID参数错误
				map.put("message", "ID参数错误");
				return map;
			}
			player2 = systemService.getPlayerNameById(des.base64encoder.encode(des.encrypt(player.getpId().getBytes(), des.PASSWORD)));
			player2.getpId();//如果查询不存在则抛出异常
		}catch(Exception e){
			map.put("status", 202);//删除ID不存在
			map.put("message", "删除ID不存在");
			return map;
		}
		try{
			systemService.delete(player2);
			map.put("status", 100);
			map.put("message", "success");
			return map;
		}catch(Exception e){
			map.put("status", 400);//其他错误
			map.put("message", e.getMessage());
			return map;
		}
		
	}
	/**
	 * 减少玩家房卡数量类
	 * @param player 玩家信息类
	 * @param request
	 * @return  Map 成功或者错误的json消息
	 * @throws Exception 
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	@RequestMapping(value="decrease")
	@ResponseBody
	public Map<String,Object> decrease(Player player,HttpServletRequest request) throws NumberFormatException, IOException, Exception{
		HashMap<String,Object> map = new HashMap<String,Object>();
		Player player2 = null;
		try{
			//验证ID是否存在
			if(!StringUtils.isNotBlank(player.getpId())){
				map.put("status",203);//ID参数错误
				map.put("message", "ID参数错误");
				return map;
			}
			player2 = systemService.getPlayerNameById(des.base64encoder.encode(des.encrypt(player.getpId().getBytes(), des.PASSWORD)));
			player2.getpId();//如果查询不存在则抛出异常
		}catch(Exception e){
			map.put("status", 201);//ID不存在
			map.put("message", "ID不存在");
			return map;
		}
		//获取减少的房卡数量
		String dNumber = request.getParameter("dNumber");
		if(!StringUtils.isNotBlank(dNumber) || !StringUtils.isNumeric(dNumber)){
			map.put("status",203);//参数错误
			map.put("message", "减少数量参数不能为空");
			return map;
		}
		
		if(Long.parseLong((new String(des.decrypt(des.base64decoder.decodeBuffer(player2.getpNumber()),des.PASSWORD)))) < Long.parseLong(dNumber)){
			map.put("status", 203);//参数错误
			map.put("message", "减少的房卡数量大于拥有的数量");
			return map;
		}
		try{
			//将房卡数量更新同时加密
			player2.setpNumber((des.base64encoder.encode(des.encrypt(String.valueOf(Integer.parseInt(new String(des.decrypt(des.base64decoder.decodeBuffer(player2.getpNumber()),des.PASSWORD))) - Integer.parseInt(dNumber)).getBytes(),des.PASSWORD))));
			//更新到数据库
			systemService.update(player2);
			map.put("status", 100);
			map.put("message", "success");
			return map;
		}catch(Exception e){
			map.put("status", 400);//其他错误
			map.put("message", e.getMessage());
			return map;
		}	
	}
	
}
