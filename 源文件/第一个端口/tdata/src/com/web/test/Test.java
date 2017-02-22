package com.web.test;

import java.io.IOException;
import java.net.URLDecoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.dao.BaseDao;
import com.web.dao.UserDao;
import com.web.entity.User;
import com.web.security.DES;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
/**
 * 测试类
 * @author hz
 *
 */
public class Test {
	public static void main(String[] args) throws IOException, Exception{
		String s = "w9Y42XU3jPDHmPLjqdA9sQ==";
		
		DES des = new DES();
		String s2 = new String(des.decrypt(des.base64decoder.decodeBuffer(s),des.PASSWORD));
		System.out.println(s2);
		String s3 = "GudemajkPqlpPNp8ZnpRJg==";
		System.out.println(URLDecoder.decode(s3, "UTF-8"));
		String s4 = new String(des.decrypt(des.base64decoder.decodeBuffer("k6Mk57XguehpPNp8ZnpRJg=="),des.PASSWORD));
		System.out.println(s4);
		Properties initProp = new Properties(System.getProperties());
		System.out.println("file.encoding:" + initProp.getProperty("file.encoding"));
		System.out.println("file.encoding:" + initProp.getProperty("user.language"));
		
		
	}
	
}
