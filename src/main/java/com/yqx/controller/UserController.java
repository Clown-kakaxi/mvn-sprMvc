package com.yqx.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yqx.service.UserService;

/**
 * 角色controller  角色的 增删改查功能
 * @author yqx
 *
 */
@Controller
@RequestMapping("/user")
public class UserController{

	private Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired	
	private UserService userService;

	@RequestMapping(value="/getUserList")
	@ResponseBody
	public Map<String, Object> getUserList(String rows, String page, String userId, String name) throws Exception{
		log.debug("*************  user!getUserList  *********************");
		int v_rows = Integer.valueOf(rows); //每页中显示的记录行数
		int v_page = Integer.valueOf(page); //当前的页码
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> userMap = userService.getUserList(v_rows, v_page, name);
		int count = Integer.parseInt(String.valueOf(userMap.get("count")));
		int totalPage = (count%v_rows==0) ? (count/v_rows) : ((count/v_rows) + 1);
		map.put("page", v_page);
		map.put("total", totalPage);
		map.put("records", userMap.get("count"));
		map.put("rows", userMap.get("data"));
		return map;
	}
	
}
