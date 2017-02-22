package com.web.entity;

import java.io.Serializable;


import java.util.Date;
import java.util.Map;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;

import com.web.common.UserUtils;
import com.web.persistence.Page;

/**
 * Entity支持类
 * @author hz
 *
 */
@MappedSuperclass
public abstract class BaseEntity<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected Page<T> page;

	/**
	 * 自定义SQL（SQL标识，SQL内容）
	 */
	protected Map<String, String> sqlMap;

	/**
	 *从页面搜索还是从菜单搜索（用于非页面搜索时设置默认搜索条件）
	 */
	private boolean searchFromPage;

	/**
	 *用于搜索多个ID的时候设置搜索条件
	 */
	private String ids;
	
	

	@JsonIgnore
	@XmlTransient
	@Transient
	public Page<T> getPage() {
		if (page == null){
			page = new Page<T>();
		}
		return page;
	}
	
	public Page<T> setPage(Page<T> page) {
		this.page = page;
		return page;
	}

	@JsonIgnore
	@XmlTransient
	@Transient
	public Map<String, String> getSqlMap() {
		if (sqlMap == null){
			sqlMap = Maps.newHashMap();
		}
		return sqlMap;
	}

	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}
	
	@Transient
	public boolean isSearchFromPage() {
		return searchFromPage;
	}

	@Transient
	public void setSearchFromPage(boolean searchFromPage) {
		this.searchFromPage = searchFromPage;
	}

	@Transient
	public String getIds() {
		return ids;
	}

	@Transient
	public void setIds(String ids) {
		this.ids = ids;
	}
	

	// 显示/隐藏
		public static final String SHOW = "1";
		public static final String HIDE = "0";
		
		// 是/否
		public static final String YES = "1";
		public static final String NO = "0";

		// 删除标记（0：正常；1：删除；2：审核；）
		public static final String FIELD_DEL_FLAG = "delFlag";
		public static final String DEL_FLAG_NORMAL = "0";
		public static final String DEL_FLAG_DELETE = "1";
		public static final String DEL_FLAG_AUDIT = "2";
		//类型标记
		public static final int IS_END_NO = 1;
		public static final int IS_END_YES = 2;
		public static final String SEL_ISEND ="isEnd";
		public static final String SEL_DEADLINE = "deadline";
		public static final Date SEl_DATE = new Date(); 
}

