package com.yqx.baseUtil;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "yqxjpaTransactionManager")
public class CommonService{
	
	protected EntityManager em ;

	public EntityManager getEntityManager() {
		return this.em;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
        this.baseDAO.setEntityManager(em);		
		this.em = em;
	}

	
	@SuppressWarnings({ "rawtypes" })
	protected JPABaseDAO baseDAO = null;

	@SuppressWarnings({ "rawtypes" })
	public JPABaseDAO getBaseDAO() {
		return baseDAO;
	}

	public void setBaseDAO(@SuppressWarnings("rawtypes") JPABaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}

	// 查询所有记录
	public List<?> findAll() {
		return baseDAO.getAll();
	}

	// 按照JqL进行查询
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findByJql(String jql, Map<String, Object> values) {
		return baseDAO.findWithNameParm(jql, values);
	}
	// 按照JqL进行分页查询
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> findPageByJql(int firstResult, int maxResult,
			String jql, Map<String, Object> values) {
		Map<String, Object> results = new HashMap<String, Object>();
		SearchResult searchResult = baseDAO.findPageWithNameParam(firstResult,
				maxResult, jql, values);
		results.put("data",searchResult.getResult());
		results.put("count",searchResult.getTotalCount());
		return results;
	}
	
	 //根据ID是否为空进行新增或者修改
	@SuppressWarnings("unchecked")
	public Object save(Object obj) {
		return baseDAO.save(obj);	
		}

	// 删除一条记录
    @SuppressWarnings("unchecked")
	public void remove(Object id) {
    	 Object obj = find(id);
        	if (obj != null) {
        		baseDAO.remove(obj);
            
		}
    }
    
	//批量删除
	@SuppressWarnings("unchecked")
	public void batchRemove(String idStr) {
		String[] strarray = idStr.split(",");
		for (int i = 0; i < strarray.length; i++) {
			long id = Long.parseLong(strarray[i]);
			Object obj = find(id);
			if (obj != null) {
				baseDAO.remove(obj);

			}
		}
	}
	/**
	 * 执行JQL进行批量修改/删除操作.
	 * 
	 * @param jql 更新或删除语句
	 * For Example : 
	 * UPDATE PollOption p SET p.optionItem = :value WHERE p.optionId = :optionId
	 * values.put("value","aaa");
	 * values.put("optionId",1);
	 * UPDATE PollOption p SET p.optionItem = :value WHERE p.optionId in (1,2,3)
	 * DELETE FROM PollOption p WHERE p.optionId = :optionId 
	 * DELETE FROM PollOption p WHERE p.optionId in (1,2,3,4)
	 * @param values  map(参数、参数值)
	 *     
	 * @return 更新记录数.*/
	@SuppressWarnings("unchecked")
	public int batchUpdateByName(String jql, Map<String, Object> values) {
		return baseDAO.batchExecuteWithNameParam(jql, values);
	}
    //获取唯一对象
	@SuppressWarnings("unchecked")
	public  Object find(Object id) {
		return baseDAO.get((Serializable) id);
	}
	
//	/**
//     * 获取当前用户session
//     */
//    public AuthUser getUserSession() {
//    	return (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    }
//    
//	/**
//     * 构造最终的查询语句，包括数据权限
//     * 此方法从CommonAction中移植过来
//     * 适用于service中的原生SQL查询
//     * @param branchFileldName  数据权限机构过滤字段
//     * @param groupByFields  分组字段
//     * @param primaryKey  主键
//     */
//	public void processSQL(String SQL, String branchFileldName,String groupByFields ,String primaryKey) {
//        StringBuilder builder = new StringBuilder(SQL);
//        boolean hasWhere = SQL.toUpperCase().indexOf(" WHERE ") > 0;
//        AuthUser auth = (AuthUser) SecurityContextHolder.getContext()
//                .getAuthentication().getPrincipal();
//		if (null != auth.getUnitInfo() && branchFileldName != null
//				&& !"".equals(branchFileldName)) {
//            if (hasWhere) {
//                builder.append(" AND ");
//            } else {
//                builder.append(" WHERE ");
//                hasWhere = true;
//            }
//            builder.append(branchFileldName);
//            builder.append(" IN (");
//			builder.append("SELECT SYSUNIT.UNITID FROM SYS_UNITS SYSUNIT WHERE SYSUNIT.UNITSEQ LIKE '"
//					+ auth.getUnitInfo().get("UNITSEQ") + "%'");
//            builder.append(")");
//        }
//        /**
//         * TODO 增加数据权限过滤中的SQL过滤体条件
//         */
//        String filterSql = auth.getFilterString(getClass().getSimpleName(), AuthUser.METHOD_SELECT);
//		if (null != filterSql && !"".equals(filterSql)) {
//        	  if (hasWhere) {
//			    	builder.append(" AND ");
//			    } else {
//			    	builder.append(" WHERE ");
//			    }
//        	builder.append(filterSql);
//        }
//        
//		if (!"".equals(groupByFields)) {
//            builder.append(groupByFields);
//        }
//        if (!"ID".equals(primaryKey)) {
//            builder.append(" ORDER BY ");
//            builder.append(primaryKey);
//        }
//        SQL = builder.toString();
//    }
	
}
