package com.yqx.service;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yqx.model.AuthRole;

@Service
public interface RoleService{

	Map<String, Object> getAuthRoleList(int rows, int page, String roleId, String name) throws SQLException;
	
	AuthRole saveRole(AuthRole authRole);
	
	void deleteRole(String ids) throws Exception;
	
	void updateRole(AuthRole authRole, Long id);
	
	/**
	 * 根据 【用户id】 获取角色信息
	 */
	AuthRole getAuthRoleById(Long roleId);
	
	void saveRoleUserRel(String roleId, String userIds);
}
