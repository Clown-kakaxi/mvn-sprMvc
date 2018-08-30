package com.yqx.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yqx.model.LiveAnchor;

@Service
public interface LiveService {

	/**
	 * 登录方法
	 * @param map	用户名 密码
	 * @return
	 */
	boolean validateLogin(Map<String, Object> map);
	
	/**
	 * 注册方法
	 * @param liveAnchor	主播对象
	 * @return
	 */
	boolean registerLiveAnchor(LiveAnchor liveAnchor);
	
	/**
	 * 根据主键返回 主播对象类
	 * @param Id
	 * @return
	 */
	LiveAnchor findLiveAnchorById(Long Id);
	
	/**
	 * 获取菜单树数据结构
	 * @return
	 */
	List<Map<String, Object>> findtreeMap();
	
	/**
	 * 根据【等级】、【父节点ID】值获取所有菜单元素值
     * @param {Object} level		等级值
     * @param {Object} parentId		父节点ID
	 * @return
	 */
	List<Map<String, Object>> findMenuByLevel(int level, int parentId);
	
	Map<String, Object> getAllTreeMenu(int rows, int page) throws SQLException;
	
	/**
	 * 根据【角色ID】值获取所有菜单元素值
	 * @param roleId
	 * @return
	 */
	Map<String, Object> findTreeMenuByAuthRole(String roleId) throws Exception;
	
}
