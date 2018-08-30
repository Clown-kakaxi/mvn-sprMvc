package com.yqx.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/**
 * The persistent class for the auth_role_menu_rel database table.
 * 
 */
@Entity
@Table(name="auth_role_menu_rel")
@NamedQuery(name="AuthRoleMenuRel.findAll", query="SELECT a FROM AuthRoleMenuRel a")
public class AuthRoleMenuRel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator="generator_gen")  
	@TableGenerator(name = "generator_gen", table="tb_generator",
	    pkColumnName="gen_name",  valueColumnName="gen_value",  
	    pkColumnValue="COMMON_PK", allocationSize=1)
	private Long Id;

	@Column(name="menu_id")
	private Long menuId;

	@Column(name="role_id")
	private Long roleId;

	private String status;

	public AuthRoleMenuRel() {
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}