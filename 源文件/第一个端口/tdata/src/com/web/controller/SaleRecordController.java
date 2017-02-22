package com.web.controller;

import java.io.IOException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.common.BaseController;
import com.web.entity.SaleRecord;
import com.web.persistence.Page;
import com.web.security.DES;

import com.web.service.SystemService;

import antlr.StringUtils;
@Controller
@RequestMapping("saleRecord")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SaleRecordController extends BaseController{
	@Autowired
	SystemService systemService;
	@RequestMapping("find")
	public String find(	 HttpServletRequest request, HttpServletResponse response,SaleRecord sr, Model model) throws Exception
	{
		//将获取的列表数据解密
		DES des = new DES();
		//接收参数
		String searchDate = request.getParameter("searchDate");
		//将playerID转换成密文去数据库中查找
		String playerId = null;
		if(com.web.common.StringUtils.isNotBlank(sr.getPlayerId()))
			playerId = des.base64encoder.encode(des.encrypt(sr.getPlayerId().getBytes(), des.PASSWORD));
		//page中会去接收pageNo,pageSize,orderBy参数
		Page<SaleRecord> page = systemService.find(new Page<SaleRecord>(request, response),playerId,searchDate); 
		for(SaleRecord sRecord:page.getList()){
        	sRecord.setPlayerId(new String(des.decrypt(des.base64decoder.decodeBuffer(sRecord.getPlayerId()),des.PASSWORD)));
        	sRecord.setPlayerName(new String(des.decrypt(des.base64decoder.decodeBuffer(sRecord.getPlayerName()),des.PASSWORD)));
        	sRecord.setPlayerNumber(new String(des.decrypt(des.base64decoder.decodeBuffer(sRecord.getPlayerNumber()),des.PASSWORD)));
        	if(sRecord.getPlayerRemark() != "" && sRecord.getPlayerRemark() != null){
        		sRecord.setPlayerRemark(new String(des.decrypt(des.base64decoder.decodeBuffer(sRecord.getPlayerRemark()), des.PASSWORD),"UTF-8"));
        	}else{
        		sRecord.setPlayerRemark("");
        	}
        
        	
        }
	
        this.sendObjectToJson(page, response);
        return null;
		
	}
	@RequestMapping("save")
	@ResponseBody
	public String save(SaleRecord sr,HttpServletRequest request) throws Exception{
		/*DES des = new DES();
		for(int i = 0; i < 50; i++){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SaleRecord sr = new SaleRecord();
			sr.setPlayerId(("" + i));
			sr.setPlayerName( des.base64encoder.encode(des.encrypt(("user" + i).getBytes(), des.PASSWORD)));
			sr.setPlayerNumber(des.base64encoder.encode(des.encrypt((""+i).getBytes(),des.PASSWORD)));
			sr.setPlayerRechargeDate(new Date());
			systemService.save(sr);
		}
		for(int i = 50; i < 100; i++){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SaleRecord sr = new SaleRecord();
			sr.setPlayerId(("" + i));
			sr.setPlayerName( des.base64encoder.encode(des.encrypt(("user" + i).getBytes(), des.PASSWORD)));
			sr.setPlayerNumber(des.base64encoder.encode(des.encrypt((""+i).getBytes(),des.PASSWORD)));
			sr.setPlayerRechargeDate(new Date());
			systemService.save(sr);
		}
		return "index.html";*/
		/*DES des = new DES();
		System.out.println("玩家ID:" + sr.getPlayerId());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sr.setPlayerName( des.base64encoder.encode(des.encrypt(("user" + i).getBytes(), des.PASSWORD)));
		sr.setPlayerNumber(des.base64encoder.encode(des.encrypt((""+i).getBytes(),des.PASSWORD)));
		sr.setPlayerRechargeDate(new Date());
		systemService.save(sr);*/
		return null;
	}
}
