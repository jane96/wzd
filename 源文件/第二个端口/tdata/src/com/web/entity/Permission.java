package com.web.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * 权限实体类
 * @author hz
 */
@Entity
@Table(name="permission")
@DynamicInsert @DynamicUpdate//即在插入和修改数据的时候,语句中只包括要插入或者修改的字段。
public class Permission extends IdEntity<Permission> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String permissionName;
	
	public Permission(){
		
	}
	
	public Permission(String permissionName) {
		super();
		this.permissionName = permissionName;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
	
	
}
