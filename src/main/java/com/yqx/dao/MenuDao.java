package com.yqx.dao;

import java.io.Serializable;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.yqx.baseUtil.JPABaseDAO;

@Repository("menuDao")
public class MenuDao implements Serializable{

	private Logger log = LoggerFactory.getLogger(MenuDao.class);
	
	private static final long serialVersionUID = -7772987411952960326L;
	
	@Resource
	private ComboPooledDataSource dataSource;
	
	public MenuDao(){
		System.out.println("实例化dao层");
	}
	
	/**
	 * 先保证数据库连接
	 * @param jdbcDataSource
	 */
	public MenuDao(ComboPooledDataSource pooledDataSource){
		this.dataSource = pooledDataSource;
	}
	
	
	
}
