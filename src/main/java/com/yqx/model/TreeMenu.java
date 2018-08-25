package com.yqx.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

/**
 * 菜单目录数
 * @author yqx
 *
 */
@Entity
@Table(name = "tree_menu")
public class TreeMenu implements Serializable{

	private static final long serialVersionUID = 7993321752512519213L;

	public TreeMenu(){
		
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator="generator_gen")  
	@TableGenerator(name = "generator_gen", table="tb_generator",
	    pkColumnName="gen_name",  valueColumnName="gen_value",  
	    pkColumnValue="TREEMENU_PK", allocationSize=1)  
	private long Id;				//主键
	
	@Column(name="name")
	private String name;			//菜单名称
	
	@Temporal(TemporalType.DATE)
	@Column(name="crt_date")
	private Date crtDate;			//创建日期
	
	@Column(name="app_id")
	private String appId;			//应用ID
	
	@Column(name="order_")
	private int order_;				//排序
	
	@Column(name="icon")
	private String icon;			//菜单图标
	
	@Column(name="leaf_flag")
	private int leafFlag;			//是否叶子节点
	
	@Column(name="tip")
	private String tip;				//菜单备注
	
	@Column(name="type")
	private String type;			//菜单类型

	@Column(name="moc_func_id")
	private String mocFuncId;		//关联功能页面
	
	@Column(name="parent_id")
	private long parentId;			//父节点ID
	
	@Column(name="isMobile")
	private int isMobile;			//0:pc功能；1:移动功能
	
	@Column(name="tree_level")
	private int treeLevel;			//树节点等级
	
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

	public Date getCrtDate() {
		return crtDate;
	}

	public void setCrtDate(Date crtDate) {
		this.crtDate = crtDate;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public int getOrder_() {
		return order_;
	}

	public void setOrder_(int order_) {
		this.order_ = order_;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getLeafFlag() {
		return leafFlag;
	}

	public void setLeafFlag(int leafFlag) {
		this.leafFlag = leafFlag;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMocFuncId() {
		return mocFuncId;
	}

	public void setMocFuncId(String mocFuncId) {
		this.mocFuncId = mocFuncId;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public int getIsMobile() {
		return isMobile;
	}

	public void setIsMobile(int isMobile) {
		this.isMobile = isMobile;
	}

	public int getTreeLevel() {
		return treeLevel;
	}

	public void setTreeLevel(int treeLevel) {
		this.treeLevel = treeLevel;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(TreeMenu.class);
	}
	
}
