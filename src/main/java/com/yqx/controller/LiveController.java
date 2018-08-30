package com.yqx.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yqx.model.AuthRole;
import com.yqx.model.LiveAnchor;
import com.yqx.model.TreeMenu;
import com.yqx.service.LiveService;
import com.yqx.service.RoleService;
import com.yqx.util.ProjConstants;

/**
 * 员工controller  员工的 增删改查功能
 * @author yqx
 *
 */
@Controller
@RequestMapping("/live")
public class LiveController{

	private Logger log = LoggerFactory.getLogger(LiveController.class);
	
	@Autowired	
	private LiveService liveService;
	
	@Autowired
	private RoleService roleService;

	@RequestMapping(value="/doLogin")
	@ResponseBody
	public Map<String, Object> doLogin(String userName, String password) throws Exception{
		log.debug("*************  live!doLogin  *********************");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", ProjConstants.ERROR_FLAG);
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("userName", userName);
		reqMap.put("password", password);
		if(StringUtils.isBlank(userName) || StringUtils.isBlank(password)){
			map.put("error", "请求参数为空！");
			return map;
		}
		boolean flag = liveService.validateLogin(reqMap);
		Long userId = Long.parseLong(userName);
		AuthRole authRole = roleService.getAuthRoleById(userId);
		if(flag){
			map.put("status", ProjConstants.SUCCESS_FLAG);
			map.put("data", authRole);
		}else{
			map.put("error", "登录用户名或密码错误！");
		}
		return map;
	}
	
	@RequestMapping(value="/doRegister")
	@ResponseBody
	public Map<String, Object> doRegister(LiveAnchor liveAnchor) throws Exception{
		log.debug("*************  live!doRegister  *********************");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", ProjConstants.ERROR_FLAG);
		if(liveAnchor == null){
			map.put("error", "请求参数为空！");
			return map;
		}
		String name = liveAnchor.getName();
		String password = liveAnchor.getPassword();
		String mobile = liveAnchor.getMobile();
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(password) || StringUtils.isEmpty(mobile)){
			map.put("error", "部分请求参数为空！");
			return map;
		}
		System.out.println("***************[LiveAnchor]:"+ReflectionToStringBuilder.toString(liveAnchor));
		
		boolean flag = liveService.registerLiveAnchor(liveAnchor);
		if(flag){
			map.put("status", ProjConstants.SUCCESS_FLAG);
		}else{
			map.put("error", "登录用户名或密码错误！");
		}
		return map;
	}
	
	@RequestMapping(value="/getUserInfo")
	@ResponseBody
	public Map<String, Object> getUserInfo(String userId) throws Exception{
		log.debug("*************  live!getUserInfo  *********************");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", ProjConstants.ERROR_FLAG);
		if(StringUtils.isBlank(userId)){
			map.put("error", "请求参数为空！");
			return map;
		}
		Long Id = Long.parseLong(userId);
		LiveAnchor liveAnchor = liveService.findLiveAnchorById(Id);
		if(liveAnchor != null){
			map.put("status", ProjConstants.SUCCESS_FLAG);
			map.put("data", liveAnchor);
		}else{
			map.put("data", liveAnchor);
			map.put("error", "获取用户信息异常！");
		}
		return map;
	}
	
	@RequestMapping(value="/getTreeMenu")
	@ResponseBody
	public Map<String, Object> getTreeMenu() throws Exception{
		log.debug("*************  live!getTreeMenu  *********************");
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> treeMap = liveService.findtreeMap();
		map.put("page", "1");
		map.put("total", "1");
		map.put("records", "9");
		map.put("rows", treeMap);
		return map;
	}

	
	@RequestMapping(value="/getMenuByLevel")
	@ResponseBody
	public Map<String, Object> getMenuByLevel(String level, String parentId) throws Exception{
		log.debug("*************  live!getMenuByLevel  *********************");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", ProjConstants.ERROR_FLAG);
		if(StringUtils.isBlank(level) || StringUtils.isBlank(parentId)){
			map.put("error", "请求参数为空！");
			return map;
		}
		int treeLevel = Integer.parseInt(level);
		int pId = Integer.parseInt(parentId);
		List<Map<String, Object>> treeMap = liveService.findMenuByLevel(treeLevel, pId);
		if(treeMap != null){
			map.put("status", ProjConstants.SUCCESS_FLAG);
			map.put("data", treeMap);
		}else{
			map.put("data", treeMap);
			map.put("error", "获取菜单树异常！");
		}
		return map;
	}
	
	@RequestMapping(value="/getAllTreeMenu")
	@ResponseBody
	public Map<String, Object> getAllTreeMenu(String rows, String page, String menuId) throws Exception{
		log.debug("*************  live!getAllTreeMenu  *********************");
		int v_rows = Integer.valueOf(rows); //每页中显示的记录行数
		int v_page = Integer.valueOf(page); //当前的页码
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> treeMap = liveService.getAllTreeMenu(v_rows, v_page);
		int count = Integer.parseInt(String.valueOf(treeMap.get("count")));
		int totalPage = (count%v_rows==0) ? (count/v_rows) : ((count/v_rows) + 1);
		map.put("page", v_page);
		map.put("total", totalPage);
		map.put("records", treeMap.get("count"));
		map.put("rows", treeMap.get("data"));
		return map;
	}
	
	@RequestMapping(value="/saveOrUpdateMenu")
	@ResponseBody
	public Map<String, Object> saveOrUpdateMenu(TreeMenu treeMenu) throws Exception{
		log.debug("*************  live!saveOrUpdateMenu:"+ReflectionToStringBuilder.toString(treeMenu)+"  *********************");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", ProjConstants.SUCCESS_FLAG);
//		Map<String, Object> treeMap = liveService.getAllTreeMenu(v_rows, v_page);
//		int count = Integer.parseInt(String.valueOf(treeMap.get("count")));
//		int totalPage = (count%v_rows==0) ? (count/v_rows) : ((count/v_rows) + 1);
//		map.put("page", v_page);
//		map.put("total", totalPage);
//		map.put("records", treeMap.get("count"));
//		map.put("rows", treeMap.get("data"));
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getTreeByAuthRole")
	@ResponseBody
	public Map<String, Object> getTreeByAuthRole(String roleId) throws Exception{
		log.debug("*************  live!getTreeByAuthRole  *********************");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", ProjConstants.ERROR_FLAG);
		if(StringUtils.isBlank(roleId)){
			map.put("error", "请求参数为空！");
			return map;
		}
		try {
			Map<String, Object> returnMap = liveService.findTreeMenuByAuthRole(roleId);
			map.put("status", ProjConstants.SUCCESS_FLAG);
			List<Map<String, Object>> treeMap = (List<Map<String, Object>>) returnMap.get("data");
			map.put("data", treeMap);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("error", "获取菜单树异常！");
		}
		return map;
	}
	
}
