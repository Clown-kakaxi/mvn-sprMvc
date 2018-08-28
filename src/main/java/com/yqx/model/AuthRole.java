package com.yqx.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the auth_role database table.
 * 
 */
@Entity
@Table(name="auth_role")
@NamedQuery(name="AuthRole.findAll", query="SELECT a FROM AuthRole a")
public class AuthRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator="generator_gen")  
	@TableGenerator(name = "generator_gen", table="tb_generator",
	    pkColumnName="gen_name",  valueColumnName="gen_value",  
	    pkColumnValue="ROLE_PK", allocationSize=10)
	private Long Id;

	@Temporal(TemporalType.DATE)
	@Column(name="crt_date")
	private Date crtDate;

	private String name;

	public AuthRole() {
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		this.Id = id;
	}

	public Date getCrtDate() {
		return this.crtDate;
	}

	public void setCrtDate(Date crtDate) {
		this.crtDate = crtDate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}