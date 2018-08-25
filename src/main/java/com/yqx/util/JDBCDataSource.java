package com.yqx.util;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 利用spring 创建 JDBCDataSource 的 bean 对象 ，  会自动注入driver等属性值
 * 然后调用getConnection方法即可连接数据库
 * @author yqx
 *
 */
public class JDBCDataSource implements Serializable{

	private static final long serialVersionUID = -1663831874908129339L;
	
	private static String driver;
	private static String url;
	private static String user;
	private static String pwd;
	
	public String getDriver() {
		return driver;
	}
	
	//设值给driver
	public void setDriver(String driver) {
		try {
			//注册数据库驱动
			Class.forName(driver);
			JDBCDataSource.driver = driver;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		JDBCDataSource.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		JDBCDataSource.user = user;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		JDBCDataSource.pwd = pwd;
	}
	
	public static Connection getConnection() throws SQLException{
		Connection conn = DriverManager.getConnection(url, user, pwd);
		return conn;
	}
	
	public void close(Connection conn){
		if(null != conn){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
