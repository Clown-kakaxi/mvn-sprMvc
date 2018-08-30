package com.yqx.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.yqx.baseUtil.CommonService;
import com.yqx.baseUtil.JPABaseDAO;
import com.yqx.core.PagingInfo;
import com.yqx.core.QueryAssistant;
import com.yqx.model.AuthRole;
import com.yqx.model.AuthRoleUserRel;
import com.yqx.service.RoleService;

@Service("roleService")
public class RoleServiceImpl extends CommonService implements RoleService{

	private Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);
	
	public RoleServiceImpl()
    {
        JPABaseDAO<AuthRole, Long> baseDao = new JPABaseDAO<AuthRole, Long>(AuthRole.class);
        super.setBaseDAO(baseDao);
    }
	
	@Autowired
	@Qualifier("dsMysql")
	private DataSource ds;
	
	@Resource
	private ComboPooledDataSource dataSource;

	@Override
	public Map<String, Object> getAuthRoleList(int rows, int page, String roleId, String name) throws SQLException {
		List<String[]> list = new ArrayList<String[]>();
		Connection conn = ds.getConnection();
		PagingInfo pageInfo = new PagingInfo(rows, page);
		StringBuilder sb = new StringBuilder("");
		sb.append(" select * from auth_role where 1=1  ");
		if(!StringUtils.isBlank(name)){
			sb.append(" and name like '%"+name+"%' ");
		}
		sb.append(" order by id asc ");
		QueryAssistant qa = new QueryAssistant(sb.toString(), conn, pageInfo);
		String[] colNames = new String[]{"id","name","crtDate"};
		Map<String, Object> map = qa.getJSONByCondition(colNames, true, list);
		return map;
	}
	
	/**
	 * 保存菜单功能
	 */
	@Override
	public AuthRole saveRole(AuthRole authRole) {
		authRole.setCrtDate(new Date());
		AuthRole ret_AuthRole = (AuthRole) super.save(authRole);
		return ret_AuthRole;
	}

	/**
	 * 删除菜单功能
	 */
	@Override
	public void deleteRole(String ids) throws Exception{
//		String sql = " delete from tree_Role where id=? ";
//		super.baseDAO.batchExecuteWithIndexParam(sql, id);
		super.batchRemove(ids);
	}

	/**
	 * 修改菜单功能
	 */
	@Override
	public void updateRole(AuthRole authRole, Long id) {
		AuthRole oldAuthRole = this.em.find(AuthRole.class, id);
		BeanUtils.copyProperties(authRole, oldAuthRole, new String[]{"crtDate"});
		oldAuthRole.setCrtDate(new Date());
		super.save(oldAuthRole);
		
	}

	/**
	 * 根据 【用户id】 获取角色信息
	 */
	@Override
	public AuthRole getAuthRoleById(Long roleId) {
		AuthRole authRole = super.em.find(AuthRole.class, roleId);
		return authRole;
	}

	@Override
	public void saveRoleUserRel(String roleId, String userIds) {
		String[] userArr = userIds.split(",");
		for(int i=0; i<userArr.length; i++){
			AuthRoleUserRel authRoleUserRel = new AuthRoleUserRel();
			authRoleUserRel.setUserType(2);//2为用户
			authRoleUserRel.setUserId(Long.parseLong(userArr[i]));
			authRoleUserRel.setRoleId(Long.parseLong(roleId));
			authRoleUserRel.setStatus("1");
			super.save(authRoleUserRel);
		}
	}
	
}
