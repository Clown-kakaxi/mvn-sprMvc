package com.yqx.model;

/**
 * 员工表实体类
 * @author yqx
 *
 */
//@Entity
public class Emp {

	// 员工Id
	private Integer id;
	// 员工姓名
	private String name;
	// 员工薪水
	private Double salary;
	// 员工年龄
	private Integer age;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getSalary() {
		return salary;
	}
	public void setSalary(Double salary) {
		this.salary = salary;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
}
