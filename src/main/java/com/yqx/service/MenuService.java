package com.yqx.service;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yqx.model.TreeMenu;

@Service
public interface MenuService{

	Map<String, Object> getAllTreeMenu(int rows, int page, String menuId, String name, String treeLevel, String leafFlag) throws SQLException;
	
	TreeMenu saveMenu(TreeMenu treeMenu);
	
	void deleteMenu(String ids) throws Exception;
	
	void updateMenu(TreeMenu treeMenu, Long id);
}
