package com.web.entity;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.web.common.CustomDateDetailSerializer;
import com.web.common.DateUtils;
import com.web.common.UserUtils;
/**
 * 实体数据类
 * @author hz
 * */
@MappedSuperclass  
public abstract class DataEntity<T> extends BaseEntity<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String remarks;	// 备注
	protected User createBy;	// 创建者
	protected Date createDate;// 创建日期
	protected User updateBy;	// 更新者
	protected Date updateDate;// 更新日期

	protected Date createDateStart;
	protected Date createDateEnd;
	protected Date updateDateStart;
	protected Date updateDateEnd;
	
	public DataEntity() {
		super();
		
	}
	
	@PrePersist//之前进行持久化
	public void prePersist(){
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getLoginId())){
			this.updateBy = user;
			this.createBy = user;
		}
		this.updateDate = new Date();
		this.createDate = this.updateDate;
	}
	
	@PreUpdate//实体同步到数据库之前发生
	public void preUpdate(){
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getLoginId())){
			this.updateBy = user;
		}
		this.updateDate = new Date();
	}
	
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@JsonIgnore//作用是json序列化时将java bean中的一些属性忽略掉,序列化和反序列化都受影响。
	@ManyToOne(fetch=FetchType.LAZY)//如果是EAGER，那么表示取出这条数据时，它关联的数据也同时取出放入内存中 ,如果是LAZY，那么取出这条数据时，它关联的数据并不取出来，
	@NotFound(action = NotFoundAction.IGNORE)//，意思是找不到引用的外键数据时忽略
	public User getCreateBy() {
		return createBy;
	}
																								
	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.TIMESTAMP)//该属性上标注 @Temporal(TemporalType.TIMESTAMP) 会得到形如'HH:MM:SS' 格式的日期
	@JsonFormat(pattern = "yyyy-MM-dd")
	@JsonSerialize(using=CustomDateDetailSerializer.class)
	public Date getCreateDate(){
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public User getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(User updateBy) {
		this.updateBy = updateBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	@DateBridge(resolution = Resolution.DAY)
	@JsonSerialize(using=CustomDateDetailSerializer.class)
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Temporal(TemporalType.DATE)
	@Transient
	public Date getCreateDateStart() {
		return DateUtils.getDateStart(createDateStart);
	}

	public void setCreateDateStart(Date createDateStart) {
		this.createDateStart = createDateStart;
	}

	@Temporal(TemporalType.DATE)
	@Transient
	public Date getCreateDateEnd() {
		return DateUtils.getDateEnd(createDateEnd);
	}

	public void setCreateDateEnd(Date createDateEnd) {
		this.createDateEnd = createDateEnd;
	}

	@Temporal(TemporalType.DATE)
	@Transient
	public Date getUpdateDateStart() {
		return DateUtils.getDateStart(updateDateStart);
	}

	public void setUpdateDateStart(Date updateDateStart) {
		this.updateDateStart = updateDateStart;
	}

	@Temporal(TemporalType.DATE)
	@Transient
	public Date getUpdateDateEnd() {
		return DateUtils.getDateEnd(updateDateEnd);
	}

	public void setUpdateDateEnd(Date updateDateEnd) {
		this.updateDateEnd = updateDateEnd;
	}
}
