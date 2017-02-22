package com.web.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 角色实体类
 * @author hz
 * */
@Entity
@Table(name="role")
@DynamicInsert @DynamicUpdate//即在插入和修改数据的时候,语句中只包括要插入或者修改的字段。
public class Role extends IdEntity<Role> implements Serializable{
	private static final long serialVersionUID = 1L;
	private String roleName;
	private List<Permission>	rolePermission;
	public Role(){
		
	}
	public Role(String roleName, List<Permission> rolePermission) {
		super();
		this.roleName = roleName;
		this.rolePermission = rolePermission;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "role_permission", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "permission_id") })
	@NotFound(action = NotFoundAction.IGNORE)//找不到外键时忽略
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnore
	public List<Permission> getRolePermission() {
		return rolePermission;
	}
	public void setRolePermission(List<Permission> rolePermission) {
		this.rolePermission = rolePermission;
	}
	
	
}
