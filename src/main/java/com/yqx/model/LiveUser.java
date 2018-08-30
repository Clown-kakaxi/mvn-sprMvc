package com.yqx.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;


/**
 * The persistent class for the live_user database table.
 * 
 */
@Entity
@Table(name="live_user")
@NamedQuery(name="LiveUser.findAll", query="SELECT l FROM LiveUser l")
public class LiveUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="anchor_id")
	private BigInteger anchorId;

	private BigInteger coin;

	@Column(name="free_coin")
	private BigInteger freeCoin;

	@Column(name="is_member")
	private byte isMember;

	private byte level;

	private String nickname;

	private String password;

	private String pic;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="regist_time")
	private Date registTime;

	public LiveUser() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigInteger getAnchorId() {
		return this.anchorId;
	}

	public void setAnchorId(BigInteger anchorId) {
		this.anchorId = anchorId;
	}

	public BigInteger getCoin() {
		return this.coin;
	}

	public void setCoin(BigInteger coin) {
		this.coin = coin;
	}

	public BigInteger getFreeCoin() {
		return this.freeCoin;
	}

	public void setFreeCoin(BigInteger freeCoin) {
		this.freeCoin = freeCoin;
	}

	public byte getIsMember() {
		return this.isMember;
	}

	public void setIsMember(byte isMember) {
		this.isMember = isMember;
	}

	public byte getLevel() {
		return this.level;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPic() {
		return this.pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public Date getRegistTime() {
		return registTime;
	}

	public void setRegistTime(Date registTime) {
		this.registTime = registTime;
	}

}