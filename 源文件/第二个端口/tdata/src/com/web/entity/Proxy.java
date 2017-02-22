package com.web.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="proxy")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)//缓存的策略
@DynamicInsert @DynamicUpdate//即在插入和修改数据的时候,语句中只包括要插入或者修改的字段。
public class Proxy extends IdEntity<Proxy>{
	private String proxyPhone;
	private String proxyWeChat;
	private Date date;
	private User user;
	
	public Proxy(){
		
	}

	public Proxy(String proxyPhone, String proxyWeChat, Date date,User user) {
		super();
		this.proxyPhone = proxyPhone;
		this.proxyWeChat = proxyWeChat;
		this.date = date;
		this.user = user;
	}

	public String getProxyPhone() {
		return proxyPhone;
	}

	public void setProxyPhone(String proxyPhone) {
		this.proxyPhone = proxyPhone;
	}

	public String getProxyWeChat() {
		return proxyWeChat;
	}

	public void setProxyWeChat(String proxyWeChat) {
		this.proxyWeChat = proxyWeChat;
	}
	@Temporal(TemporalType.TIMESTAMP)//该属性上标注 @Temporal(TemporalType.TIMESTAMP) 会得到形如'YYYY-MM-DD hh:mm:ss' 格式的日期
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	@OneToOne
	@JoinColumn(name="user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
