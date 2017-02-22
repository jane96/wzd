package com.web.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.service.SystemService;
/**
 * 销售记录实体类
 * @author hz
 * */
@Entity
@Table(name="saleRecord")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE),不配置二级
@DynamicInsert @DynamicUpdate//即在插入和修改数据的时候,语句中只包括要插入或者修改的字段。
public class SaleRecord extends IdEntity<SaleRecord> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String playerId;
	private String playerName;
	private String playerNumber;
	private String playerRemark;
	private Date playerRechargeDate;
	
	public SaleRecord() {
		
	}

	public SaleRecord(String playerId, String playerName, String playerNumber,String playerMark, Date playerRechargeDate) {
		super();
		this.playerId = playerId;
		this.playerName = playerName;
		this.playerNumber = playerNumber;
		this.playerRemark = playerMark;
		this.playerRechargeDate = playerRechargeDate;
	}


	public String getPlayerId() {
		return playerId;
	}




	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}


	public String getPlayerName() {
		return playerName;
	}




	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}




	public String getPlayerNumber() {
		return playerNumber;
	}




	public void setPlayerNumber(String playerNumber) {
		this.playerNumber = playerNumber;
	}
	



	public String getPlayerRemark() {
		return playerRemark;
	}

	public void setPlayerRemark(String playerRemark) {
		this.playerRemark = playerRemark;
	}

	@Temporal(TemporalType.TIMESTAMP)//该属性上标注 @Temporal(TemporalType.TIMESTAMP) 会得到形如'YYYY-MM-DD HH:mm:ss' 格式的日期
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	public Date getPlayerRechargeDate() {
		return playerRechargeDate;
	}

	public void setPlayerRechargeDate(Date playerRechargeDate) {
		this.playerRechargeDate = playerRechargeDate;
	}

	

	
	
}
