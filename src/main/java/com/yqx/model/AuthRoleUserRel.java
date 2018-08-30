package com.yqx.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;


/**
 * The persistent class for the auth_role_user_rel database table.
 * 
 */
@Entity
@Table(name="auth_role_user_rel")
@NamedQuery(name="AuthRoleUserRel.findAll", query="SELECT a FROM AuthRoleUserRel a")
public class AuthRoleUserRel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator="generator_gen")  
	@TableGenerator(name = "generator_gen", table="tb_generator",
	    pkColumnName="gen_name",  valueColumnName="gen_value",  
	    pkColumnValue="COMMON_PK", allocationSize=1)
	private Long Id;

	@Column(name="role_id")
	private Long roleId;

	private String status;

	@Column(name="user_id")
	private Long userId;

	@Column(name="user_type")
	private Integer userType;

	public AuthRoleUserRel() {
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

}