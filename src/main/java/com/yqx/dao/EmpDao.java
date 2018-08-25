package com.yqx.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.yqx.model.Emp;

@Repository("userDao")
public class EmpDao implements Serializable{

	private Logger log = LoggerFactory.getLogger(EmpDao.class);
	
	private static final long serialVersionUID = -7772987411952960326L;
	
	@Resource
	private ComboPooledDataSource dataSource;
	
	public EmpDao(){
		System.out.println("实例化dao层");
	}
	
	/**
	 * 先保证数据库连接
	 * @param jdbcDataSource
	 */
	public EmpDao(ComboPooledDataSource pooledDataSource){
		this.dataSource = pooledDataSource;
	}
	
	/**
	 * 保存实体类方法
	 * @param emp
	 * @return
	 */
	public boolean saveEmp(Emp emp){
		log.debug("*********** Excute Save [Emp] ************************");
		boolean flag = false;
		StringBuilder sb = new StringBuilder("");
		sb.append(" insert into emp(name, salary, age) values ('"+emp.getName()+"',"+emp.getSalary()+","+emp.getAge()+")");
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sb.toString());
			flag = ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("保存员工信息异常:"+e);
		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
}
