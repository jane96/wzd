package com.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.common.BaseController;
import com.web.common.UserUtils;
import com.web.entity.Player;
import com.web.entity.SaleRecord;
import com.web.entity.User;
import com.web.security.DES;
import com.web.service.SystemService;
/**
 * 充值控制类
 * @author hz
 *
 */
@Controller
@RequestMapping("recharge")
public class RechargeController extends BaseController {
	@Autowired
	SystemService systemService;
	//加密解密类
	DES des = new DES();
	/**
	 * 新增房卡充值记录
	 * @param sr 充值记录类
	 * @return Map 成功或者错误的json消息
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings({ "deprecation", "static-access" })
	@RequestMapping("save")
	@ResponseBody
	public Map update(SaleRecord sr,HttpServletRequest request) throws UnsupportedEncodingException{
		Properties initProp = new Properties(System.getProperties());
		System.out.println("file.encoding:" + initProp.getProperty("file.encoding"));
		System.out.println("file.encoding:" + initProp.getProperty("user.language"));
		Map<String, Object> map = new HashMap<String, Object>();
		//通过将playerId转换为密文进行查找
		Player player = systemService.getPlayerNameById(des.base64encoder.encode(des.encrypt(sr.getPlayerId().getBytes(), des.PASSWORD)));
		String remark = "";
		try{
			//获取玩家ID，如果不存在就捕获错误消息
			player.getpId();
			//获取充值数量
			String rechargeNumber =sr.getPlayerNumber();
			//获取当前用户
			User user = UserUtils.getUser();
			//获取用户角色
			String role = new String(des.decrypt(des.base64decoder.decodeBuffer(user.getRole().getRoleName()), des.PASSWORD));
			//判断当前用户的角色以及房卡数量是否大于充值数量；如果是官方角色则不判断
			if(role.equals("proxy")
					&& Integer.parseInt(new String(des.decrypt(des.base64decoder.decodeBuffer(user.getCurrentNumber()), des.PASSWORD))) < Integer.parseInt(rechargeNumber)){
				map.put("status", "false");
				map.put("message", "拥有房卡数量不足！");
				return map;
			}
			//更新代理商房卡数量
			if(role.equals("proxy")){
				//剩下的房卡数量
				int remain = Integer.parseInt(new String(des.decrypt(des.base64decoder.decodeBuffer(user.getCurrentNumber()), des.PASSWORD)))
						- Integer.parseInt(rechargeNumber);
				user.setCurrentNumber(des.base64encoder.encode(des.encrypt(String.valueOf(remain).getBytes(), des.PASSWORD)));
				systemService.update(user);
			}
			//更新玩家房卡数量
			systemService.updatePlayerNumber(player,sr.getPlayerNumber());
		}catch(Exception e){
			map.put("status", "false");
			map.put("message", "充值的ID不存在;或其他异常! " + e.getMessage());
			return map;
		}
		
		try{
			//将销售记录信息加密之后保存
			sr.setPlayerId(des.base64encoder.encode(des.encrypt(sr.getPlayerId().getBytes(), des.PASSWORD)));
			sr.setPlayerName(new String(player.getpWechat()));
			sr.setPlayerNumber(des.base64encoder.encode(des.encrypt((sr.getPlayerNumber()).getBytes(),des.PASSWORD)));
			if(StringUtils.isNotBlank(sr.getPlayerRemark())){//防止ie下的乱码,进行UTF-8的解码	
				sr.setPlayerRemark(new String(des.base64encoder.encode(des.encrypt(URLDecoder.decode(sr.getPlayerRemark(),"UTF-8").getBytes(), des.PASSWORD))));
			}
			//日期不加密
			sr.setPlayerRechargeDate(new Date());
			//存储房卡充值记录
			systemService.save(sr);
			map.put("status", "true");
			map.put("message", "充值成功！");
		}catch(Exception e){
			map.put("status", "false");
			map.put("message", "充值失败或未知错误！"+ e.getMessage());
		}
		return map;
	}
}
