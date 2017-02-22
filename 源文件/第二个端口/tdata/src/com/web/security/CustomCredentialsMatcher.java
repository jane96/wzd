package com.web.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
/**
 * 自定义的密码验证类
 * */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
	@Override
	public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
		DES des = new DES();
		//获取token
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		Object tokenCredentials =  des.base64encoder.encode(des.encrypt((String.valueOf(token.getPassword())).getBytes(), des.PASSWORD));
		Object accountCredentials = getCredentials(info);
		//将密码加密与系统加密后的密码校验，内容一致就返回true,不一致就返回false
		return equals(tokenCredentials, accountCredentials);
}
}
