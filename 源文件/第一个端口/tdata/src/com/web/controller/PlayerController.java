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
				if(StringUtils.isNotBlank(player2.getpWechat())){
					player2.setpWechat(new String(des.decrypt(des.base64decoder.decodeBuffer(player2.getpWechat()), des.PASSWORD)));
				}
			}
			this.sendObjectToJson(page, response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
