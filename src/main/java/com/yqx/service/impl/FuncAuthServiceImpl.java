package com.yqx.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.yqx.baseUtil.CommonService;
import com.yqx.baseUtil.JPABaseDAO;
import com.yqx.core.QueryAssistant;
import com.yqx.model.AuthRoleMenuRel;
import com.yqx.service.FuncAuthService;

@Service("funcAuthService")
public class FuncAuthServiceImpl extends CommonService implements FuncAuthService{

	private Logger log = LoggerFactory.getLogger(FuncAuthServiceImpl.class);
	
	public FuncAuthServiceImpl()
    {
        JPABaseDAO<AuthRoleMenuRel, Long> baseDao = new JPABaseDAO<AuthRoleMenuRel, Long>(AuthRoleMenuRel.class);
        super.setBaseDAO(baseDao);
    }
	
	@Autowired
	@Qualifier("dsMysql")
	private DataSource ds;
	
	@Resource
	private ComboPooledDataSource dataSource;

	/**
	 * 获取角色--功能菜单树
	 */
	@Override
	public Map<String, Object> getFuncTreeData(String roleId, String name) throws SQLException {
		List<String[]> list = new ArrayList<String[]>();
		Connection conn = ds.getConnection();
		StringBuilder sb = new StringBuilder("");
		//case when mr.status='1' then 'true' else 'false' end as checked
		sb.append(" select tm.id,tm.name,tm.parent_id,mr.status "
				+ " from tree_menu tm "
				+ " left join auth_role_menu_rel mr on tm.id=mr.menu_id ");
		if(!StringUtils.isBlank(roleId)){
			sb.append(" and mr.role_id='"+roleId+"' ");
		}
		sb.append(" where 1=1 order by tm.tree_level,tm.order_ asc ");
		QueryAssistant qa = new QueryAssistant(sb.toString(), conn);
		String[] colNames = new String[]{"id","name","pId","status"};
		Map<String, Object> map = qa.getJSONByCondition(colNames, true, list);
		List<Map<String,Object>> funcList = (List<Map<String, Object>>) map.get("data");
		for(Map<String,Object> funcMap : funcList){
			String status = funcMap.get("status")+"";
			if(StringUtils.isEmpty(roleId)){
				funcMap.put("checked", false);
			}else if(!StringUtils.isEmpty(status) && "1".equals(status)){
				funcMap.put("checked", true);
			}else{
				funcMap.put("checked", false);
			}
		}
		return map;
	}

	/**
	 * 保存 1-n         角色-功能配置表
	 */
	@Override
	public void saveRoleMenuRel(String roleId, String menuIds) throws SQLException {
		String[] menuArr = menuIds.split(",");
		for(int i=0; i<menuArr.length; i++){
			AuthRoleMenuRel authRoleMenuRel = new AuthRoleMenuRel();
			authRoleMenuRel.setMenuId(Long.parseLong(menuArr[i]));
			authRoleMenuRel.setRoleId(Long.parseLong(roleId));
			authRoleMenuRel.setStatus("1");
			super.save(authRoleMenuRel);
		}
	}
	
}
