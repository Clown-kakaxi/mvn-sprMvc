package com.yqx.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.yqx.model.Emp;
import com.yqx.model.LiveAnchor;

@Repository("liveDao")
public class LiveDao implements Serializable{

	private Logger log = LoggerFactory.getLogger(EmpDao.class);
	
	private static final long serialVersionUID = -7772987411952960326L;
	
	@Resource
	private ComboPooledDataSource dataSource;
	
	public LiveDao(){
		System.out.println("实例化dao层");
	}
	
	/**
	 * 先保证数据库连接
	 * @param jdbcDataSource
	 */
	public LiveDao(ComboPooledDataSource pooledDataSource){
		this.dataSource = pooledDataSource;
	}
	
	/*
	 * 根据ID 获取到 LiveAnchor 实例
	 * @param courseId
	 * @return
	 */
	public LiveAnchor getLiveAnchorbyId(Integer Id) {
		
		System.out.println("利用jdbc查找LiveAnchor");
		StringBuilder sb = new StringBuilder("");
		sb.append(" select * from live_anchor where 1=1 ");
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = null;
			if(Id != null){//"+empId+"
				sb.append(" and id=? ");
				ps = conn.prepareStatement(sb.toString());
				ps.setInt(1, Id);
			}else{
				ps = conn.prepareStatement(sb.toString());
			}
			System.out.println("Query Sql:"+ps.toString());
			ResultSet rs = ps.executeQuery();
			LiveAnchor liveAnchor = new LiveAnchor();
			while(rs.next()){
				liveAnchor.setId(rs.getInt("id"));
				liveAnchor.setName(rs.getString("name"));
				liveAnchor.setLevel(rs.getInt("level"));
				liveAnchor.setEmpiricVal(rs.getBigDecimal("empiric_val"));
				liveAnchor.setHeadPic(rs.getString("head_pic"));
				liveAnchor.setNotice(rs.getString("notice"));
				liveAnchor.setStarVal(rs.getBigDecimal("star_val"));
				liveAnchor.setRegistTime(rs.getTimestamp("regist_time"));
				liveAnchor.setBirthDay(rs.getDate("birth_day"));
				liveAnchor.setPassword(rs.getString("password"));
			}
			rs.close();
			ps.close();
			return liveAnchor;
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
	
	/**
	 * 保存实体类方法
	 * @param emp
	 * @return
	 */
	public boolean saveLiveAnchor(LiveAnchor liveAnchor){
		log.debug("*********** Excute Save [LiveAnchor] ************************");
		boolean flag = false;
		StringBuilder sb = new StringBuilder("");
		sb.append(" insert into live_anchor(name, password, mobile) values ('"+liveAnchor.getName()+"','"+liveAnchor.getPassword()+"','"+liveAnchor.getMobile()+"')");
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sb.toString());
			ps.execute();
			flag = true;
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
	
	
//	@Override
//	public List<Emp> findTreeMenuList() {
//		log.debug("*********** QUERY list [emp] ************************");
//		List<Emp> list= new ArrayList<Emp>();
//		StringBuilder sb = new StringBuilder("");
//		sb.append(" select * from emp where 1=1 ");
//		Connection conn = null;
//		try {
//			conn = dataSource.getConnection();
//			PreparedStatement ps = conn.prepareStatement(sb.toString());
//			ResultSet rs = ps.executeQuery();
//			while(rs.next()){
//				Emp emp = new Emp();
//				emp.setId(rs.getInt("id"));
//				emp.setName(rs.getString("name"));
//				emp.setSalary(rs.getDouble("salary"));
//				emp.setAge(rs.getInt("age"));
//				list.add(emp);
//			}
//			rs.close();
//			ps.close();
//			return list;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new RuntimeException("查询员工异常:"+e);
//		} finally{
//			dataSource.close();
//		}
//	}
	
}
