package com.yqx.baseUtil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import org.springframework.stereotype.Repository;

import com.yqx.util.JDBCDataSource;

/**
 * 功能描述: 基于Jdbc的entity管理实现类 ， 提供基于jdbc的sql执行功能，包括存储过程执行， 同时提供基于sql的查询功能
 * ，主要用于多表关联查询，特别是左/右连接查询。
 * 
 * Copyright: Copyright (c) 2011 Company: 北京宇信易诚科技有限公司
 * 
 * @version 1.0
 * @see HISTORY
 *************************************************/
@Repository("jdbcBaseDAO")
public class JDBCBaseDAO {
	
	
	private Connection getJDBConn() throws SQLException {
		return JDBCDataSource.getConnection();
	}
	
	public void batchUpdate(String sql, List<Object[]> batchArgs, int cycleSize){
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = this.getJDBConn();
			ps = conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			int count = 0;
			System.out.println("-----------------更新开始------------------");
			for (Object[] obj : batchArgs) {
				count++;
				String info="";
				for (int i = 0; i < obj.length; i++) {
					info+=obj[i]+",";
					if(null != obj[i] && obj[i].getClass() == Date.class) {
						java.sql.Date sqlDate=new java.sql.Date(((Date) obj[i]).getTime());
						ps.setDate(i + 1, sqlDate);
						continue;
					}
					ps.setObject(i + 1, obj[i]);
				}
				System.out.println("执行第"+count+"条信息---"+sql+"("+info+")");
				ps.addBatch();
				if (count % cycleSize == 0) {
					ps.executeBatch();
					conn.commit();
				}
			}
			if (batchArgs.size() % cycleSize != 0) {
				ps.executeBatch();
				conn.commit();
			}
			System.out.println("-----------------更新结束------------------");
		}catch(SQLException se){
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			se.printStackTrace();
			if(se.getNextException() != null){				
				se.getNextException().printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <X, Y> Map<X, Y> getMappingByJDBCNativeSQL(String sql,
			final String keyCol, final String valCol) {
		PreparedStatement ps = null;
		Connection conn = null;
		ResultSet rs = null;
		Map<Object, Object> rstMap = new HashMap<Object, Object>();
		try {
			conn = this.getJDBConn();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				rstMap.put(rs.getObject(keyCol.toLowerCase()),
						rs.getObject(valCol.toUpperCase()));
			}
		}catch(SQLException se){
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			se.printStackTrace();
			if(se.getNextException() != null){				
				se.getNextException().printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return (Map<X, Y>) rstMap;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <X> List<X> getEntityListByJDBCNativeSQL(
			final Class clazz, Connection ds, String sql,
			final Object... values) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Object> rstList = new ArrayList<Object>();
		try {
			ps = ds.prepareStatement(sql);
			for (int i = 0; i < values.length; i++) {
				if(values[i] instanceof String) {
					ps.setString(i, (String) values[i]);
				}
			}
			rs = ps.executeQuery();
			Field[] fileds = clazz.getDeclaredFields();
			while(rs.next()) {
				Object obj = clazz.newInstance();
				for(int i=0;i<fileds.length;i++){
					Column col = fileds[i].getAnnotation(Column.class);
					fileds[i].setAccessible(true);
					fileds[i].set(obj, rs.getObject(col.name()));
				}
				rstList.add(obj);
			}
		}catch(SQLException se){
			try {
				ds.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			se.printStackTrace();
			if(se.getNextException() != null){				
				se.getNextException().printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(ds != null) {
				try {
					ds.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return (List<X>) rstList;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public List<Map<String, Object>> getListMapByJDBCNativeSQL(
			final Class clazz, Connection ds, String sql, List<Object> values) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		try {
			ps = ds.prepareStatement(sql);
			for (int i = 0; i < values.size(); i++) {
				if(values.get(i) instanceof String) {
					ps.setString(i+1, (String) values.get(i));
				}
			}
			rs = ps.executeQuery();
			ResultSetMetaData data= rs.getMetaData();
			while(rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for(int i=1;i<=data.getColumnCount();i++){
					map.put(data.getColumnName(i),
							rs.getObject(data.getColumnName(i)));
	            }  
				listMap.add(map);
			}
		}catch(SQLException se){
			try {
				ds.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			se.printStackTrace();
			if(se.getNextException() != null){				
				se.getNextException().printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(ds != null) {
				try {
					ds.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return listMap;
	}
	
	public List<String> getColumnList(Connection conn, String sql)
			throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> columnList = new ArrayList<String>();
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ResultSetMetaData data= rs.getMetaData();
			for(int i=1;i<=data.getColumnCount();i++){
				columnList.add(data.getColumnName(i));
            }  
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return columnList;
	}
	
	public String executUpdate(Connection conn, List<String> sqls) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String flag = "执行成功！";
		try {
			conn.setAutoCommit(false);
			if(null != sqls && sqls.size() > 0) {
				if(sqls.size() < 10) {
					for(String sql : sqls) {
						ps = conn.prepareStatement(sql);
						ps.executeUpdate();
					}
				}else {
					flag = "一次最多只可执行10个语句块！";
				}
			}
			conn.commit();
		}catch (Exception e) {
			flag = e.getMessage();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
	
	public String jdbcExecute(Connection conn, String proceText) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String flag = "执行成功！";
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(proceText);
			ps.execute();
			conn.commit();
		}catch (Exception e) {
			flag = e.getMessage();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
	public int getJdbcCount(Connection conn, String sql) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int rowCount = 0;
		StringBuffer sb = new StringBuffer("select count(*) NUM from (")
				.append(sql).append(")");
		try {
			ps = conn.prepareStatement(sb.toString());
			rs = ps.executeQuery();
			if(rs.next()){
				rowCount=rs.getInt("NUM");
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return rowCount;
	}
	
	/**
	* 查询唯一的结果
	* @return Object
	*/
	public Object findUniqueWithJDBCNativeSQL(Connection conn, 
			String sql, final Object... values) {
		Object object = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			if (null != values && values.length > 0) {
				for (int i = 0; i < values.length; i++) {
					ps.setObject(i + 1, values[i]);
				}
			}
			rs = ps.executeQuery();
			if (rs.next())
				object = rs.getObject(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return object;
	}

	/**
	* 查询唯一的结果
	* @return Object
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <X> X findUniqueEntityWithJDBCNativeSQL(final Class clazz,
			Connection conn, String sql, final Object... values) {
		Object object = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			if (null != values && values.length > 0) {
				for (int i = 0; i < values.length; i++) {
					ps.setObject(i + 1, values[i]);
				}
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				Field[] fileds = clazz.getDeclaredFields();
				try {
					object = clazz.newInstance();
					for(int i=0;i<fileds.length;i++){
						Column col = fileds[i].getAnnotation(Column.class);
						if(null == col) continue;
						fileds[i].setAccessible(true);
						fileds[i].set(object, rs.getObject(col.name()));
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return (X) object;
	}

	public int UpdateByNativeSQL(Connection conn, String sql,
			final Object... values) {
		PreparedStatement ps = null;
		int i = 0;
		try {
			ps = conn.prepareStatement(sql);
			if (null != values && values.length > 0) {
				for (int j = 0; j < values.length; j++) {
					ps.setObject(j + 1, values[j]);
				}
			}
			i = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return i;
	}
	
	public List<String> getSingleColumn(Connection conn, String sql,
			String column, final Object... values) {
		List<String> rstList = new ArrayList<String>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			if (null != values && values.length > 0) {
				for (int j = 0; j < values.length; j++) {
					ps.setObject(j + 1, values[j]);
				}
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				rstList.add(rs.getString(column.toUpperCase()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return rstList;
	}
}
