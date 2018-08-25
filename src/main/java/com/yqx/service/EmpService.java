package com.yqx.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yqx.model.Emp;

@Service
public interface EmpService {

	Emp getEmpbyId(Integer empId);
	
	Emp findEmpById(Integer empId);
	
	List<Emp> findEmpList();
	
	boolean saveOrUpdateEmp(Emp emp);
}
