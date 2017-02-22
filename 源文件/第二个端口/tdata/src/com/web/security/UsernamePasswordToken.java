/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/sccl/attech">attech</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.web.security;

/**
 * 用户和密码（包含验证码）令牌类
 * @author hz
 * 
 */
public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken {

	private static final long serialVersionUID = 1L;

	private String captcha;

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public UsernamePasswordToken() {
		super();
	}

	public UsernamePasswordToken(String username, char[] password,
			boolean rememberMe, String host, String captcha) {
		super(username, password, rememberMe, host);
		this.captcha = captcha;
	}

}