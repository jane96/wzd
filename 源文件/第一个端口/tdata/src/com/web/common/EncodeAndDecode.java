package com.web.common;

import java.io.IOException;

import com.web.security.DES;

public class EncodeAndDecode {
	static DES des = new DES();
	//加密
	public static String encode(String obj){
		return des.base64encoder.encode(des.encrypt(obj.getBytes(), des.PASSWORD));
	}
	//解密
	public static String decode(String s) throws IOException, Exception{
		return new String(des.decrypt(des.base64decoder.decodeBuffer(s),des.PASSWORD));
	}
	//使用
	public static void main(String[] args) throws IOException, Exception{
		
		String s1 = "wzadmin004";//需要加密的数据
		String result1 = encode(s1);//加密完的数据
		
		//解密数据
		String s2 = "T74xJ5Ku2VppPNp8ZnpRJg==";//需要解密的数据
		String result2 = decode(s2);
		
		System.out.println(result1);//输出加密的数据
		System.out.println(result2);//输出解密的数据
	}
	
}
