package com.yqx.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.yqx.core.PagingInfo;
import com.yqx.core.QueryAssistant;
import com.yqx.dao.LiveDao;
import com.yqx.model.LiveAnchor;
import com.yqx.service.LiveService;
import com.yqx.util.JDBCDataSource;

@Service("liveService")
public class LiveServiceImpl implements LiveService{

	private Logger log = LoggerFactory.getLogger(EmpServiceImpl.class);
	
	@Autowired
	@Qualifier("dsMysql")
	private DataSource ds;
	
	@Resource
	private LiveDao liveDao;
	
	@Resource
	private ComboPooledDataSource dataSource;

	/**
	 * 登录方法
	 * @param map	用户名 密码
	 * @return
	 */
	@Override
	public boolean validateLogin(Map<String, Object> map) {
		boolean flag = false;
		String userName = String.valueOf(map.get("userName"));
		String password = String.valueOf(map.get("password"));
		Integer id = Integer.parseInt(userName);
		LiveAnchor liveAnchor = liveDao.getLiveAnchorbyId(id);
		if(liveAnchor != null){
			String pwd_ds = liveAnchor.getPassword();
			if(!StringUtils.isEmpty(pwd_ds) && !StringUtils.isEmpty(password)){
				if(pwd_ds.equals(password)){
					flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * 注册方法
	 * @param liveAnchor	主播对象
	 * @return
	 */
	@Override
	public boolean registerLiveAnchor(LiveAnchor liveAnchor) {
		return liveDao.saveLiveAnchor(liveAnchor);
	}
	
	/**
	 * 根据主键返回 主播对象类
	 * @param Id
	 * @return
	 */
	public LiveAnchor findLiveAnchorById(Long Id){
		System.out.println("利用jdbc查找liveAnchor");
		StringBuilder sb = new StringBuilder("");
		sb.append(" select * from live_anchor where 1=1 ");
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = null;
			if(Id != null){//"+empId+"
				sb.append(" and id=? ");
				ps = conn.prepareStatement(sb.toString());
				ps.setLong(1, Id);
			}else{
				ps = conn.prepareStatement(sb.toString());
			}
			ResultSet rs = ps.executeQuery();
			LiveAnchor liveAnchor = null;
			while(rs.next()){
				liveAnchor = new LiveAnchor();
				liveAnchor.setId(rs.getInt("id"));
				liveAnchor.setName(rs.getString("name"));
				liveAnchor.setLevel(rs.getInt("level"));
				liveAnchor.setEmpiricVal(rs.getBigDecimal("empiric_val"));
				liveAnchor.setHeadPic(rs.getString("head_pic"));
				liveAnchor.setNotice(rs.getString("notice"));
				liveAnchor.setStarVal(rs.getBigDecimal("star_val"));
				liveAnchor.setRegistTime(rs.getDate("regist_time"));
				liveAnchor.setBirthDay(rs.getDate("birth_day"));
				liveAnchor.setPassword(rs.getString("password"));
				liveAnchor.setMobile(rs.getString("mobile"));
			}
			rs.close();
			ps.close();
			return liveAnchor;
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
	 * 获取菜单树数据结构
	 * @return
	 */
	public List<Map<String, Object>> findtreeMap(){
		List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
		StringBuilder sb = new StringBuilder("");
		sb.append(" select * from tree_menu where 1=1 ");
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
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
	@Override
	public List<Map<String, Object>> findMenuByLevel(int level, int parentId) {
		List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
		StringBuilder sb = new StringBuilder("");
		sb.append(" SELECT * FROM tree_menu WHERE FIND_IN_SET(id,queryChildMenu("+parentId+")) and tree_level="+level+" ");
		sb.append(" order by order_ asc ");
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
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

	@Override
	public Map<String, Object> getAllTreeMenu(int rows, int page) throws SQLException {
		List<String[]> list = new ArrayList<String[]>();
		Connection conn = dataSource.getConnection();
		PagingInfo pageInfo = new PagingInfo(rows, page);
		StringBuilder sb = new StringBuilder("");
		sb.append(" select * from tree_menu where 1=1 order by tree_level,order_ asc ");
		QueryAssistant qa = new QueryAssistant(sb.toString(), conn, pageInfo);
		String[] colNames = new String[]{"id","name","crtDate","appId","order_","icon","leafFlag","tip","type","mocFuncId","parentId","isMobile","treeLevel"};
		Map<String, Object> map = qa.getJSONByCondition(colNames, true, list);
		return map;
	}

	
	@Override
	public Map<String, Object> findTreeMenuByAuthRole(String roleId) throws Exception {
		StringBuilder sb = new StringBuilder("");
		List<String[]> list = new ArrayList<String[]>();
		sb.append(" select tm.* from tree_menu tm ");
		sb.append(" left join auth_role_menu_rel mr on mr.menu_id=tm.id ");
		sb.append(" where mr.status='1' ");
		if(!StringUtils.isEmpty(roleId)){
			sb.append(" and mr.role_id=? ");
			String[] params = new String[1];
			params[0] = roleId;
			list.add(params);
		}
		sb.append(" order by tm.tree_level,tm.order_ asc ");
		QueryAssistant qa = new QueryAssistant(sb.toString(), ds.getConnection());
		String[] colNames = new String[]{"id","name","crtDate","appId","order_","icon","leafFlag","tip","type","mocFuncId","parentId","isMobile","treeLevel"};
		Map<String, Object> map = qa.getJSONByCondition(colNames, true, list);
		return map;
	}
	
}
