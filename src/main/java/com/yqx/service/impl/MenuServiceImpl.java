package com.yqx.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.yqx.dao.MenuDao;
import com.yqx.model.TreeMenu;
import com.yqx.service.MenuService;

@Service("menuService")
public class MenuServiceImpl extends CommonService implements MenuService{

	private Logger log = LoggerFactory.getLogger(MenuServiceImpl.class);
	
	public MenuServiceImpl()
    {
        JPABaseDAO<TreeMenu, Long> baseDao = new JPABaseDAO<TreeMenu, Long>(TreeMenu.class);
        super.setBaseDAO(baseDao);
    }
	
	@Autowired
	@Qualifier("dsMysql")
	private DataSource ds;
	
	@Resource
	private MenuDao menuDao;
	
	@Resource
	private ComboPooledDataSource dataSource;

	@Override
	public Map<String, Object> getAllTreeMenu(int rows, int page, String menuId, String name, String treeLevel, String leafFlag) throws SQLException {
		List<String[]> list = new ArrayList<String[]>();
		Connection conn = ds.getConnection();
		PagingInfo pageInfo = new PagingInfo(rows, page);
		StringBuilder sb = new StringBuilder("");
		sb.append(" select * from tree_menu where 1=1  ");
		if(!StringUtils.isBlank(name)){
			sb.append(" and name like '%"+name+"%' ");
		}
		sb.append(" order by tree_level,order_ asc ");
		QueryAssistant qa = new QueryAssistant(sb.toString(), conn, pageInfo);
		String[] colNames = new String[]{"id","name","crtDate","appId","order_","icon","leafFlag","tip","type","mocFuncId","parentId","isMobile","treeLevel"};
		Map<String, Object> map = qa.getJSONByCondition(colNames, true, list);
		return map;
	}
	
	/**
	 * 根据父节点ID 查询该节点下所有子节点
	 * @param parent_id		父节点ID
	 * @return
	 */
	public String queryChildMenuSql(int parent_id){
		String sql = "SELECT * FROM tree_menu WHERE FIND_IN_SET(parent_id,queryChildMenu("+parent_id+"))";
		return sql;
	}
	
	/**
	 * 根据子节点ID 查询该节点所有的上级节点
	 * @param id  子节点ID
	 * @return
	 */
	public String queryFatherMenuSql(int id){
		String sql = "SELECT * FROM tree_menu WHERE FIND_IN_SET(id,queryFatherMenu("+id+"))";
		return sql;
	}

	/**
	 * 根据【等级】、【父节点ID】值获取所有菜单元素值
     * @param {Object} level		等级值
     * @param {Object} parentId		父节点ID
	 * @return
	 */
	public List<Map<String, Object>> findMenuByLevel(int level, int parentId) {
		List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
		StringBuilder sb = new StringBuilder("");
		sb.append(" SELECT * FROM tree_menu WHERE FIND_IN_SET(id,queryChildMenu("+parentId+")) and tree_level="+level+" ");
		sb.append(" order by order_ asc ");
		Connection conn = null;
		try {
			conn = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Map<String, Object> treeMap = new HashMap<String, Object>();
				treeMap.put("id", rs.getInt("id"));
				treeMap.put("name", rs.getString("name"));
				treeMap.put("crtDate", rs.getDate("crt_date"));
				treeMap.put("appId", rs.getString("app_id"));
				treeMap.put("order", rs.getInt("order_"));
				treeMap.put("icon", rs.getString("icon"));
				treeMap.put("leafFlag", rs.getInt("leaf_flag"));
				treeMap.put("tip", rs.getString("tip"));
				treeMap.put("type", rs.getString("type"));
				treeMap.put("mocFuncId", rs.getString("moc_func_id"));
				treeMap.put("parentId", rs.getInt("parent_id"));
				treeMap.put("isMobile", rs.getInt("isMobile"));
				treeMap.put("treeLevel", rs.getInt("tree_level"));
				treeList.add(treeMap);
			}
			rs.close();
			ps.close();
			return treeList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询员工异常:"+e);
		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 保存菜单功能
	 */
	@Override
	public TreeMenu saveMenu(TreeMenu treeMenu) {
		treeMenu.setCrtDate(new Date());
		treeMenu.setAppId("12");
		TreeMenu ret_treeMenu = (TreeMenu) super.save(treeMenu);
		return ret_treeMenu;
	}

	/**
	 * 删除菜单功能
	 */
	@Override
	public void deleteMenu(String ids) throws Exception{
//		String sql = " delete from tree_menu where id=? ";
//		super.baseDAO.batchExecuteWithIndexParam(sql, id);
		super.batchRemove(ids);
	}

	/**
	 * 修改菜单功能
	 */
	@Override
	public void updateMenu(TreeMenu treeMenu, Long id) {
		TreeMenu oldTreeMenu = this.em.find(TreeMenu.class, id);
		BeanUtils.copyProperties(treeMenu, oldTreeMenu, new String[]{"crtDate","appId","isMobile","treeLevel"});
		oldTreeMenu.setCrtDate(new Date());
		oldTreeMenu.setAppId("12");
		super.save(oldTreeMenu);
		
	}

}
