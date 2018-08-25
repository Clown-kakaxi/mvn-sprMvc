package com.yqx.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.config.AopNamespaceUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yqx.model.TreeMenu;
import com.yqx.service.MenuService;
import com.yqx.util.ProjConstants;

/**
 * 员工controller  员工的 增删改查功能
 * @author yqx
 *
 */
@Controller
@RequestMapping("/menu")
public class MenuController{

	private Logger log = LoggerFactory.getLogger(MenuController.class);
	
	@Autowired	
	private MenuService menuService;

	@RequestMapping(value="/getAllTreeMenu")
	@ResponseBody
	public Map<String, Object> getAllTreeMenu(String rows, String page, String menuId, String name, String treeLevel, String leafFlag) throws Exception{
		log.debug("*************  menu!getAllTreeMenu  *********************");
		int v_rows = Integer.valueOf(rows); //每页中显示的记录行数
		int v_page = Integer.valueOf(page); //当前的页码
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> treeMap = menuService.getAllTreeMenu(v_rows, v_page, menuId, name, treeLevel, leafFlag);
		int count = Integer.parseInt(String.valueOf(treeMap.get("count")));
		int totalPage = (count%v_rows==0) ? (count/v_rows) : ((count/v_rows) + 1);
		map.put("page", v_page);
		map.put("total", totalPage);
		map.put("records", treeMap.get("count"));
		map.put("rows", treeMap.get("data"));
		return map;
	}
	
	@RequestMapping(value="/saveMenu")
	@ResponseBody
	public Map<String, Object> saveMenu(TreeMenu treeMenu) throws Exception{
		log.debug("*************  menu!saveMenu:"+ReflectionToStringBuilder.toString(treeMenu)+"  *********************");
		Map<String, Object> map = new HashMap<String, Object>();
		TreeMenu newTree = new TreeMenu();
		try {
			Long id = treeMenu.getId();
			if(id != 0L){//修改方法
				menuService.updateMenu(treeMenu, id);
			}else{
				BeanUtils.copyProperties(treeMenu, newTree, new String[]{"id"});
				TreeMenu save_treeMenu = menuService.saveMenu(newTree);
				Long saveId = save_treeMenu.getId();
			}
			map.put("status", ProjConstants.SUCCESS_FLAG);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", ProjConstants.ERROR_FLAG);
			map.put("message", "保存失败，请打死程序猿！");
		}
		return map;
	}
	
	@RequestMapping(value="/updateMenu")
	@ResponseBody
	public Map<String, Object> updateMenu(TreeMenu treeMenu) throws Exception{
		log.debug("*************  menu!updateMenu:"+ReflectionToStringBuilder.toString(treeMenu)+"  *********************");
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
	
	@RequestMapping(value="/deleteMenu")
	@ResponseBody
	public Map<String, Object> deleteMenu(String id){
		log.debug("*************  menu!deleteMenu:[id:"+id+"]  *********************");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			menuService.deleteMenu(id);
			map.put("status", ProjConstants.SUCCESS_FLAG);
		} catch (Exception e) {
			map.put("status", ProjConstants.ERROR_FLAG);
			map.put("message", "删除失败，请打死程序猿！");
		}
		return map;
	}
	
}
