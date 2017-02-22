package com.web.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.common.CookieUtils;

/**
 * 分页类
 * @author hz
 * */

public class Page<T> {
	private int pageNo = 1;//当前页码
	private int pageSize = 10;//每页显示的数量
	private long pageCount;//总页面数
	private long allCount;//总记录数
	private String orderBy = "desc-date";//排序规则
	private List<T> list = new ArrayList<T>();//返回的查询结果列表
	
	public Page(){
		
	}
	
	public Page(int pageNo, int pageSize, long pageCount, String orderBy) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.pageCount = pageCount;
		this.orderBy = orderBy;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public long getPageCount() {
		return pageCount;
	}
	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}
	public long getAllCount() {
		return allCount;
	}
	public void setAllCount(long allCount) {
		this.allCount = allCount;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	/**
	 * 获取前台传过来的页面参数
	 * @param request
	 * @param response
	 */
	public Page(HttpServletRequest request, HttpServletResponse response){
		// 设置页码参数
		String no = request.getParameter("pageNo");
		if (StringUtils.isNumeric(no)){
			CookieUtils.setCookie(response, "pageNo", no);
			this.setPageNo(Integer.parseInt(no));
		}
		// 设置页面大小参数
		String size = request.getParameter("pageSize");
		if (StringUtils.isNumeric(size)){
			CookieUtils.setCookie(response, "pageSize", size);
			this.setPageSize(Integer.parseInt(size));
		}
		// 设置排序参数
		String orderBy = request.getParameter("orderBy");
		if (StringUtils.isNotBlank(orderBy)){
			this.setOrderBy(orderBy);
		}
	}
	public long getTotalPage(){
		return pageCount;
	}
	/**
	 * 分页是否有效
	 * @return this.pageSize==-1
	 */
	@JsonIgnore
	public boolean isDisabled() {
		return this.pageSize==-1;
	}
	
	/**
	 * 是否进行总数统计
	 * @return this.count==-1
	 */
	@JsonIgnore
	public boolean isNotCount() {
		return this.allCount==-1;
	}
	/**
	 * 获取 Hibernate FirstResult
	 */
	public int getFirstResult(){
		int firstResult = (getPageNo() - 1) * getPageSize();
		if (firstResult >= getAllCount()) {
			firstResult = 0;
		}
		return firstResult;
	}
	
	public int getLastResult(){
		int lastResult = getFirstResult()+getMaxResults();
		if(lastResult>getAllCount()) {
			lastResult =(int) getAllCount();
		}
		return lastResult;
	}
	/**
	 * 获取 Hibernate MaxResults
	 */
	public int getMaxResults(){
		return getPageSize();
	}
}
