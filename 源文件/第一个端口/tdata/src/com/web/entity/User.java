package com.web.entity;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.common.UserUtils;
/**
 * 用户实体类
 * @author hz
 * */
@Entity
@Table(name="users")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE),不开启二级缓存
@DynamicInsert @DynamicUpdate//即在插入和修改数据的时候,语句中只包括要插入或者修改的字段。
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String loginId;
	private String name;
	private String password;
	private String recordNumber;
	private String currentNumber;
	private Role role;
	public User(){
		
	}
	
	
	public User(String loginId, String password, String recordNumber,String currentNumber,Role role,String name) {
		super();
		this.loginId = loginId;
		this.password = password;
		this.recordNumber = recordNumber;
		this.currentNumber = currentNumber;
		this.role = role;
		this.name = name;
	}

	@Id 
	public String getLoginId() {
		return loginId;
	}


	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRecordNumber() {
		return recordNumber;
	}
	

	public String getCurrentNumber() {
		return currentNumber;
	}


	public void setCurrentNumber(String currentNumber) {
		this.currentNumber = currentNumber;
	}


	public void setRecordNumber(String recordNumber) {
		this.recordNumber = recordNumber;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@ManyToOne
	@JoinColumn(name = "role_id")
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	
}
