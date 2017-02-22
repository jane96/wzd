package com.web.test;

import java.io.IOException;



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
/**
 * 测试类
 * @author hz
 *
 */
public class Test {
	public static void main(String[] args) throws IOException, Exception{
		String s = "QD7qtH0ddwG5dMOCKnQ0oS5aqqkSDFaAxvDnOg/yTkFpPNp8ZnpRJg==";
		
		DES des = new DES();
		String s2 = new String(des.decrypt(des.base64decoder.decodeBuffer(s),des.PASSWORD));
		System.out.println(s2);
		String s3 = "aTzafGZ6USY=";
		String s4 = new String(des.decrypt(des.base64decoder.decodeBuffer(s3),des.PASSWORD));
		System.out.println(s4);
		String s5 = des.base64encoder.encode(des.encrypt("5678900".getBytes(), des.PASSWORD));
		System.out.println(s5);
		String s6 = new String(des.decrypt(des.base64decoder.decodeBuffer(s5),des.PASSWORD));
		System.out.println(s6);
		System.out.println(Long.parseLong("111111111111111111"));
		System.out.println(new String("111111111111111111").length());
		
	}
	
}
