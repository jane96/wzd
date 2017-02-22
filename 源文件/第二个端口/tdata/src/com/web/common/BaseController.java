package com.web.common;

import java.beans.PropertyEditorSupport;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;


import com.web.mapper.JsonMapper;


/**
 * 基本的控制类
 * @author hz
 */

public class BaseController {
	
	/**
	 * 将Date等类型绑定，controller类最先执行的
	 * @param binder 绑定的参数
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils
						.escapeHtml4(text.trim()));
			}

			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
		});
	}
	/**
	 * 将Object对象转为Json，同时输出到输出流
	 * @param object 需要转换的类型
	 * @param response 请求流
	 * @throws IOException 抛出的错误
	 */
	protected void sendObjectToJson(Object object, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		String message = JsonMapper.getInstance().toJson(object);
		System.out.println("json:" + message);
		pw.write(message);
		pw.flush();
		pw.close();
	}
	/**
	 * 
	 * @param ex 错误类
	 * @param request 请求
	 * @param response 响应
	 * @throws IOException 抛出的错误
	 */
	@ExceptionHandler(RuntimeException.class)
	public void handleException(Exception ex,HttpServletRequest request,HttpServletResponse response) throws IOException{
		System.out.println("抛异常了！"+ex.getLocalizedMessage());
	   response.getWriter().printf(ex.toString());
	    response.flushBuffer();
	}
	
}
