package com.yqx.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yqx.model.AuthRole;
import com.yqx.service.RoleService;
import com.yqx.util.ProjConstants;

/**
 * 角色controller  角色的 增删改查功能
 * @author yqx
 *
 */
@Controller
@RequestMapping("/role")
public class RoleController{

	private Logger log = LoggerFactory.getLogger(RoleController.class);
	
	@Autowired	
	private RoleService roleService;

	@RequestMapping(value="/getAuthRoleList")
	@ResponseBody
	public Map<String, Object> getAuthRoleList(String rows, String page, String roleId, String name) throws Exception{
		log.debug("*************  role!getAuthRoleList  *********************");
		int v_rows = Integer.valueOf(rows); //每页中显示的记录行数
		int v_page = Integer.valueOf(page); //当前的页码
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> roleMap = roleService.getAuthRoleList(v_rows, v_page, roleId, name);
		int count = Integer.parseInt(String.valueOf(roleMap.get("count")));
		int totalPage = (count%v_rows==0) ? (count/v_rows) : ((count/v_rows) + 1);
		map.put("page", v_page);
		map.put("total", totalPage);
		map.put("records", roleMap.get("count"));
		map.put("rows", roleMap.get("data"));
		return map;
	}
	
	@RequestMapping(value="/saveRole")
	@ResponseBody
	public Map<String, Object> saveRole(AuthRole authRole) throws Exception{
		log.debug("*************  role!saveRole:"+ReflectionToStringBuilder.toString(authRole)+"  *********************");
		Map<String, Object> map = new HashMap<String, Object>();
		AuthRole newTree = new AuthRole();
		try {
			Long id = authRole.getId();
			if(id != 0L){//修改方法
				roleService.updateRole(authRole, id);
			}else{
				BeanUtils.copyProperties(authRole, newTree, new String[]{"id"});
				AuthRole save_AuthRole = roleService.saveRole(newTree);
			}
			map.put("status", ProjConstants.SUCCESS_FLAG);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", ProjConstants.ERROR_FLAG);
			map.put("message", "保存失败，请打死程序猿！");
		}
		return map;
	}
	
	@RequestMapping(value="/updateRole")
	@ResponseBody
	public Map<String, Object> updateRole(AuthRole authRole) throws Exception{
		log.debug("*************  role!updateRole:"+ReflectionToStringBuilder.toString(authRole)+"  *********************");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", ProjConstants.SUCCESS_FLAG);
		return map;
	}
	
	@RequestMapping(value="/deleteRole")
	@ResponseBody
	public Map<String, Object> deleteRole(String id){
		log.debug("*************  role!deleteRole:[id:"+id+"]  *********************");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			roleService.deleteRole(id);
			map.put("status", ProjConstants.SUCCESS_FLAG);
		} catch (Exception e) {
			map.put("status", ProjConstants.ERROR_FLAG);
			map.put("message", "删除失败，请打死程序猿！");
		}
		return map;
	}
	
}
