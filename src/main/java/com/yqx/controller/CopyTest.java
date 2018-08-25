package com.yqx.controller;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.springframework.beans.BeanUtils;

import com.yqx.model.Emp;

public class CopyTest {

	public static void main(String[] args) {
		Emp emp1 = new Emp();
		emp1.setId(1);
		emp1.setName("test1");
		emp1.setAge(16);
		emp1.setSalary(100.00);
		System.out.println("emp1:"+ReflectionToStringBuilder.toString(emp1));
		
		Emp emp2 = new Emp();
		emp2.setId(2);
		emp2.setName("test2");
		System.out.println("emp2:"+ReflectionToStringBuilder.toString(emp2));
		
		BeanUtils.copyProperties(emp2, emp1);
		System.out.println("---------------------------  改变后  ----------------------------");
		System.out.println("emp1:"+ReflectionToStringBuilder.toString(emp1));
		System.out.println("emp2:"+ReflectionToStringBuilder.toString(emp2));
	}
	
}
