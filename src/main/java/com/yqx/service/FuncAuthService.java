package com.yqx.service;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yqx.model.AuthRole;
import com.yqx.model.AuthRoleMenuRel;

@Service
public interface FuncAuthService{

	Map<String, Object> getFuncTreeData(String roleId, String name) throws SQLException;
	
	void saveRoleMenuRel(String roleId, String menuIds) throws SQLException;
	
}
