package com.yqx.service;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yqx.model.AuthRole;

@Service
public interface UserService{

	Map<String, Object> getUserList(int rows, int page, String name) throws SQLException;
	
}
