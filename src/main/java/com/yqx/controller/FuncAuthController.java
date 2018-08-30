package com.yqx.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yqx.service.FuncAuthService;
import com.yqx.util.ProjConstants;

/**
 * 角色--菜单配置controller  角色--菜单配置功能
 * @author yqx
 *
 */
@Controller
@RequestMapping("/funcAuth")
public class FuncAuthController{

	private Logger log = LoggerFactory.getLogger(RoleController.class);
	
	@Autowired	
	private FuncAuthService funcAuthService;

	@RequestMapping(value="/getFuncTreeData")
	@ResponseBody
	public Map<String, Object> getFuncTreeData(String roleId, String name) throws Exception{
		log.debug("*************  funcAuth!getFuncTreeData  *********************");
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> roleMap = funcAuthService.getFuncTreeData(roleId, name);
		map.put("status", ProjConstants.SUCCESS_FLAG);
		map.put("data", roleMap.get("data"));
		return map;
	}
	
	@RequestMapping(value="/saveRoleMenuRel")
	@ResponseBody
	public Map<String, Object> saveRoleMenuRel(String roleId, String menuIds) throws Exception{
		log.debug("*************  funcAuth!saveRoleMenuRel  *********************");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", ProjConstants.ERROR_FLAG);
		if(StringUtils.isEmpty(roleId) || StringUtils.isEmpty(menuIds)){
			map.put("message", "请求参数为空！");
			return map;
		}
		try {
			funcAuthService.saveRoleMenuRel(roleId, menuIds);
			map.put("status", ProjConstants.SUCCESS_FLAG);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("message", "保存配置信息报错，请打死程序猿！");
		}
		return map;
	}
	
}
