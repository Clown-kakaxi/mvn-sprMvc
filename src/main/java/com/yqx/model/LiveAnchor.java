package com.yqx.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 主播
 * @author yqx
 *
 */
public class LiveAnchor implements Serializable{

	public LiveAnchor(){
		
	}
	
	private static final long serialVersionUID = -1241020316843943762L;

	private long Id;				//主键
	
	private String name;			//主播名称
	
	private int level;				//主播等级
	
	private BigDecimal empiricVal;	//主播经验值ֵ
	
	private String headPic;			//主播头像
	
	private String notice;			//公告
	
	private BigDecimal starVal;		//星值ֵ
	
	private Date registTime;		//注册时间
	
	private Date birthDay;			//主播生日

	private String password;		//登录密码
	
	private String mobile;			//手机号
	
	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public BigDecimal getEmpiricVal() {
		return empiricVal;
	}

	public void setEmpiricVal(BigDecimal empiricVal) {
		this.empiricVal = empiricVal;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public BigDecimal getStarVal() {
		return starVal;
	}

	public void setStarVal(BigDecimal starVal) {
		this.starVal = starVal;
	}

	public Date getRegistTime() {
		return registTime;
	}

	public void setRegistTime(Date registTime) {
		this.registTime = registTime;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(LiveAnchor.class);
//		return "LiveAnchor [Id=" + Id + ", name=" + name + ", level=" + level + ", empiricVal=" + empiricVal
//				+ ", headPic=" + headPic + ", notice=" + notice + ", starVal=" + starVal + ", registTime=" + registTime
//				+ ", birthDay=" + birthDay + ", password="+password+  ", mobile="+mobile+ " ]";
	}
	
}
