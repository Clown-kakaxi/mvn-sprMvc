package com.yqx.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.yqx.dao.EmpDao;
import com.yqx.model.Emp;
import com.yqx.service.EmpService;
import com.yqx.util.JDBCDataSource;

@Service("EmpService")
public class EmpServiceImpl implements EmpService{

	private Logger log = LoggerFactory.getLogger(EmpServiceImpl.class);
	
	@Resource
	private ComboPooledDataSource dataSource;
	
	@Resource
	private EmpDao empDao;
	
	@Override
	public Emp getEmpbyId(Integer empId) {
		
		Emp emp = new Emp();
		emp.setId(empId);
		emp.setName("王雅文");
		emp.setSalary(14500.32);
		emp.setAge(23);
		return emp;
	}
	
	public Emp findEmpById(Integer empId){
		System.out.println("利用jdbc查找emps");
		StringBuilder sb = new StringBuilder("");
		sb.append(" select * from emp where 1=1 ");
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = null;
			if(empId != null){//"+empId+"
				sb.append(" and id=? ");
				ps = conn.prepareStatement(sb.toString());
				ps.setInt(1, empId);
			}else{
				ps = conn.prepareStatement(sb.toString());
			}
			ResultSet rs = ps.executeQuery();
			Emp emp = null;
			while(rs.next()){
				emp = new Emp();
				emp.setId(rs.getInt("id"));
				emp.setName(rs.getString("name"));
				emp.setSalary(rs.getDouble("salary"));
				emp.setAge(rs.getInt("age"));
			}
			rs.close();
			ps.close();
			return emp;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询员工异常:"+e);
		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<Emp> findEmpList() {
		log.debug("*********** QUERY list [emp] ************************");
		List<Emp> list= new ArrayList<Emp>();
		StringBuilder sb = new StringBuilder("");
		sb.append(" select * from emp where 1=1 ");
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Emp emp = new Emp();
				emp.setId(rs.getInt("id"));
				emp.setName(rs.getString("name"));
				emp.setSalary(rs.getDouble("salary"));
				emp.setAge(rs.getInt("age"));
				list.add(emp);
			}
			rs.close();
			ps.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询员工异常:"+e);
		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 保存或修改 一个对象
	 */
	@Override
	public boolean saveOrUpdateEmp(Emp emp) {
		boolean flag = false;
		Integer id = emp.getId();
		Emp empModel = null;
		if(id == null){//新增
			empModel = new Emp();
			empModel.setName(emp.getName());
			empModel.setSalary(emp.getSalary());
			empModel.setAge(emp.getAge());
		}else{//修改
			empModel = getEmpbyId(id);
			empModel.setName(emp.getName());
			empModel.setSalary(emp.getSalary());
			empModel.setAge(emp.getAge());
		}
		flag = empDao.saveEmp(empModel);
		return flag;
	}
	
	
}
