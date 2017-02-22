package com.web.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 玩家实体类
 * @author hz
 * */
@Entity
@Table(name="player")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)//缓存的策略,不开启
@DynamicInsert @DynamicUpdate//即在插入和修改数据的时候,语句中只包括要插入或者修改的字段。
public class Player implements Serializable {
	private String pId;
	private String pWechat;
	private String pNumber;
	private Date date;
	public Player(){}
	public Player(String pId, String pWechat, String pNumber,Date date) {
		super();
		this.pId = pId;
		this.pWechat = pWechat;
		this.pNumber = pNumber;
		this.date = date;
	}
	@Id
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
		
	}
	
	public String getpNumber() {
		return pNumber;
	}
	public void setpNumber(String pNumber) {
		this.pNumber = pNumber;
	}
	public String getpWechat() {
		return pWechat;
	}
	public void setpWechat(String pWechat) {
		this.pWechat = pWechat;
	}
	@Temporal(TemporalType.TIMESTAMP)//该属性上标注 @Temporal(TemporalType.TIMESTAMP) 会得到形如'YYYY-MM-DD hh:mm:ss' 格式的日期
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")//json传过来的格式
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
