package com.yqx.baseUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 功能描述: 基于Jdbc和jpa的entity管理实现类 ，
 * 提供基于jdbc的sql执行功能和基于jpa的hql执行功能
 * 在CommonService 的基础上 增加了几个jdbc 的方法功能
 * 
 * @see HISTORY
 *************************************************/

@Service
public class BaseService<T> extends CommonService{
	
	@Autowired
	protected JDBCBaseDAO jdbcBaseDAO;
	
	@Autowired
	protected JPABaseDAO<T, Serializable> baseDAO;
	
    public BaseService(){
        JPABaseDAO<Object, Long> baseDao = new JPABaseDAO<Object, Long>(Object.class);
        super.setBaseDAO(baseDao);
        
        jdbcBaseDAO=new JDBCBaseDAO();
    }
    
    /**
     * 基于jql的实体类查询（无条件的查询）
     * @param entityClass 实体类的class对象
     * @return 该实体类的list
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <X> List<X> getAllEntityList(final Class entityClass) {
		String jql = "select obj from " + entityClass.getSimpleName() + " obj ";
		return (List<X>) this.baseDAO.findWithNameParm(jql, null);
	}
    
    /**
     * 基于jql的实体类查询（单个条件的查询）
     * @param entityClass 实体类的class对象
     * @param propertyName 实体类的字段属性名
     * @param value 对应的字段属性值(不包含in)
     * @return 该实体类的list
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <X> List<X> getEntityListByProperty(final Class entityClass, String propertyName, Object value) {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("pptName", value);
		String jql = "select obj from " + entityClass.getSimpleName() + " obj where obj." + propertyName + "= :pptName";
		return (List<X>) this.baseDAO.findWithNameParm(jql, values);
	}
	
    /**
     * 基于jql的实体类查询（单个条件的查询）
     * @param entityClass 实体类的class对象
     * @param propertyName 实体类的字段属性名
     * @param value 对应的字段属性值(只可以传入集合)
     * @return 该实体类的list
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <X> List<X> getEntityListByPropertyList(final Class entityClass, String propertyName, List<Object> values) {
		String jql = "select obj from " + entityClass.getSimpleName() + " obj where obj." + propertyName + " in ?1";
		return (List<X>) this.findWithIndexParam(jql, values);
	}
	
	/**
	 * 基于jql的实体类查询（两个条件的查询）
	 * @param entityClass  实体类的class对象
	 * @param propertyName1  实体类的字段1属性名
	 * @param value1   对应的字段1属性值
	 * @param logicRel 两个字段的逻辑关系
	 * @param propertyName2  实体类的字段2属性名
	 * @param value2   对应的字段2属性值
	 * @return 该实体类的list
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <X> List<X> getEntityListByProperty(final Class entityClass, String propertyName1, Object value1, String logicRel, String propertyName2, Object value2) {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("pptName1", value1);values.put("pptName2", value2);
		String jql = "select obj from " + entityClass.getSimpleName() + " obj where obj." + propertyName1 + "= :pptName1 " + logicRel + " obj." + propertyName2 + "= :pptName2 ";
		return (List<X>) this.baseDAO.findWithNameParm(jql, values);
	}
	
	/**
	 * 基于jql的实体类查询（通过实体class和主键ID查询）
	 * @param entityClass
	 * @param id
	 * @return 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <X> X findUniqueEntityByPkId(Class<?> entityClass, Serializable id) {
		return (X)this.em.find(entityClass, id);
	}
	
	/**
	 * 同一个一个条件获取唯一的一条数据
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public <X> X findUniqueEntityByProperty(final Class entityClass,
			String propertyName, Object value) {
		String jql = "select obj from " + entityClass.getSimpleName() + " obj where obj." + propertyName + "=?1";
		return this.findUniqueWithIndexParam(jql, value);
	}
	
	/**
	 * 通过字段条件获取唯一的一条数据
	 * @param jql
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <X> X findUniqueWithIndexParam(final String jql, final Object... values) {
		Query query = this.em.createQuery(jql);
		if (values != null && values.length > 0) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i+1, values[i]);
			}
		}
		try {
			return (X) query.getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			return null;
		}
	}
	
	/**
	 * 基于jql的实体类查询（单个或多个条件的查询）
	 * @param jql 自定义的jql（此查询只支持？号传参且？号的序列号从1开始）
	 * @param values 传入对应的条件值，可传入多个
	 * @return 该实体类的list
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> findWithIndexParam(final String jql, final Object... values) {
		Query query = this.em.createQuery(jql);
		if (values != null && values.length > 0) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i+1, values[i]);
			}
		}
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public <X> List<X> findByNativeSQLWithIndexParam(final String sql, List<Object> values) {
		Query query = this.em.createNativeQuery(sql);
		if(null != values && values.size() > 0) {
			for(int i=0;i<values.size();i++) {
				query.setParameter(i, values.get(i));
			}
		}
		return query.getResultList();
	}
	
	/**
	 * 按条件删除，传入任意字段和单个值，字段不可为空
	 * @param clazz 实体class
	 * @param propertyName 字段
	 * @param value 值
	 */
	@SuppressWarnings("rawtypes")
	public <X> void batchDeleteByColValue(final Class clazz, String propertyName, Object value) {
		Map<String, Object> params = new HashMap<String, Object>();
    	StringBuffer sb = new StringBuffer("delete from " + clazz.getSimpleName() + " obj ");
    	sb.append(" where obj."+ propertyName +" = :pName ");
    	params.put("pName", value);
    	if(StringUtils.isNotBlank(propertyName)) {//防误删全表
    		this.baseDAO.batchExecuteWithNameParam(sb.toString(), params);
    	}
	}
	
	/**
	 * 按条件删除，传入任意字段和多个值，字段不可为空
	 * @param clazz 实体class
	 * @param propertyName 字段
	 * @param values 值
	 */
	@SuppressWarnings("rawtypes")
	public <X> void batchDeleteByColValues(final Class clazz, String propertyName, Collection<X> values) {
		Map<String, Object> params = new HashMap<String, Object>();
    	StringBuffer sb = new StringBuffer("delete from " + clazz.getSimpleName() + " obj ");
    	sb.append(" where obj."+ propertyName +" in :pName ");
    	params.put("pName", values);
    	if(StringUtils.isNotBlank(propertyName)) {//防误删全表
    		this.baseDAO.batchExecuteWithNameParam(sb.toString(), params);
    	}
	}
	
	/**
	 * 基于jql的实体类任意两个字段的Mapping映射（注意：如果传入key字段不是单一主键或有重复数据，则只能返回重复映射最后一对）
	 * @param entityClass 实体类的class对象
	 * @param values 查询的条件映射（key是字段名，value是对应的值）
	 * @param keyCol 返回Mapping的key字段对应的值
	 * @param valCol 返回Mapping的value字段对应的值
	 * @return Mapping
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <X> Map<X, X> getEntityMappingByProperty(final Class entityClass,
			Map<String, Object> conditions, final String keyCol,
			final String valCol) {
		Map rstMap = new HashMap();
		List<Object[]> entitys = null;
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer("select obj." + keyCol + ", obj."
				+ valCol + " from " + entityClass.getSimpleName() + " obj ");
		if(null != conditions && conditions.size() > 0) {
			sb.append(" where 1=1 ");
			int i = 0;
			for (String key : conditions.keySet()) {
				String pptName = "ppt" + i;
				if(Collection.class.isInstance(conditions.get(key))) {
					List<String> list = (List<String>) conditions.get(key);
					if(null == list || list.size() == 0) return rstMap;
					if(list.size() > 1000) {//规避Oracle1000
						List<List<String>> lists = CustomUtils.splitToMultiList(list);
						sb.append(" and ( ");
						for (int j=0;j<lists.size();j++) {
							pptName = "ppt" + i;
							if(j==0) {
								sb.append(" obj." + key + " in :" + pptName);
								params.put(pptName, lists.get(j));
							}else {
								sb.append(" or obj." + key + " in :" + pptName);
								params.put(pptName, lists.get(j));
							}
							i++;
						}
						sb.append(" ) ");
					}else {
						sb.append(" and obj." + key + " in :" + pptName);
						params.put(pptName, conditions.get(key));
						i++;
					}
					continue;
				}
				sb.append(" and obj." + key + " = :" + pptName);
				params.put(pptName, conditions.get(key));
				i++;
			}
			entitys = this.baseDAO.findWithNameParm(sb.toString(), params);
		}else {
			entitys = this.findWithIndexParam(sb.toString());
		}
		
		if (null != entitys && entitys.size() > 0) {
			for (Object[] obj : entitys) {
				if(null != obj)
				rstMap.put(obj[0] instanceof String ? (String) obj[0]
						.toString().trim() : obj[0], obj[1]);
			}
		}
		return rstMap;
	}
	
	/**
	 * 基于jdbc的批量更新
	 * @param entitys 传入要批量更新的实体类的实体Collection
	 */
	public <X> void JdbcBatchUpdate(Collection<X> entitys) {
		if (null != entitys && entitys.size() > 0) {
			Class<?> cla = entitys.iterator().next().getClass();
			String tableNm = cla.getAnnotation(Table.class).name();
			Map<String, String> fieldNms = getColumnsMapping(cla);
			if (StringUtils.isEmpty(tableNm) || fieldNms == null
					|| fieldNms.size() <= 0) {
				return;
			}
			StringBuilder uptSql = new StringBuilder("insert into ").append(
					tableNm).append("(");
			Iterator<String> it = fieldNms.keySet().iterator();
			StringBuilder valuesTmp = new StringBuilder("");
			boolean isFirst = true;
			while (it.hasNext()) {
				String fieldNm = it.next();
				if (!isFirst) {
					uptSql.append(" , ");
					valuesTmp.append(" , ");
				}
				uptSql.append(fieldNms.get(fieldNm));
				valuesTmp.append(" ? ");
				isFirst = false;
			}
			uptSql.append(") values (").append(valuesTmp).append(")");
			List<Object[]> params = new ArrayList<Object[]>();
			for (Object saveObjTmp : entitys) {
				int index = 0;
				Object[] objs = new Object[fieldNms.keySet().size()];
				for (String fieldNmTmp : fieldNms.keySet()) {
					objs[index] = this.getValueByField(fieldNmTmp,
							saveObjTmp);
					index++;
				}
				params.add(objs);
			}
			this.jdbcBaseDAO.batchUpdate(uptSql.toString(), params, 1000);
		}
	}
	
	/**
	 * 基于jdbc的批量更新
	 * @param entitys 传入要批量更新的实体类的实体Collection
	 */
	@SuppressWarnings("all")
	public <X> void JdbcBatchEdit(Collection<X> entitys,String... updates) {
		if (null != entitys && entitys.size() > 0) {
			Class<?> cla = entitys.iterator().next().getClass();
			String tableNm = cla.getAnnotation(Table.class).name();
			
			StringBuilder uptSql=new StringBuilder();
			List<Object[]> params = new ArrayList<Object[]>();
			
			if(updates.length>0){
				Map<String,String> map=getColumnMappingByColName(cla,updates);
				if(map==null){
					return ;
				}
				
				uptSql = new StringBuilder("update ").append(
						tableNm).append(" set ");
				boolean isFirst=true;
				for(String fn : map.keySet()){
					if(isFirst){
						uptSql.append(map.get(fn)+"=?");
						isFirst=false;
					}else {
						uptSql.append(","+map.get(fn)+"=?");
					}
				}
				uptSql.append(" where 1=1 ");
				Map<String, String> ids = getIDsMapping(cla);
				Iterator<String> it_id = ids.keySet().iterator();
				while (it_id.hasNext()) {
					String field_id = it_id.next();
					uptSql.append("and "+ids.get(field_id)+"=? ");
				}
				
				params=new ArrayList();
				for (Object saveObjTmp : entitys) {
					Object[] tmp=new Object[map.size()+ids.size()];
					int index=0;
					for(String fn : map.keySet()){
						tmp[index]=this.getValueByField(fn,saveObjTmp);
						index++;
					}
					for(String idTmp : ids.keySet()){
						tmp[index] = this.getValueByField(idTmp,saveObjTmp);
						index++;
					}
					params.add(tmp);
				}
			}else {
				Map<String, String> fieldNms = getColumnsMapping(cla);
				if (StringUtils.isEmpty(tableNm) || fieldNms == null
						|| fieldNms.size() <= 0) {
					return;
				}
				uptSql = new StringBuilder("update ").append(
						tableNm).append(" set ");
				Iterator<String> it = fieldNms.keySet().iterator();
				boolean isFirst = true;
				while (it.hasNext()) {
					String fieldNm = it.next();
					if (!isFirst) {
						uptSql.append(" , ");
					}
					uptSql.append(fieldNms.get(fieldNm)+"=?");
					isFirst = false;
				}
				uptSql.append(" where 1=1 ");
				Map<String, String> ids = getIDsMapping(cla);
				Iterator<String> it_id = ids.keySet().iterator();
				while (it_id.hasNext()) {
					String field_id = it_id.next();
					uptSql.append("and "+ids.get(field_id)+"=? ");
				}
				params = new ArrayList<Object[]>();
				for (Object saveObjTmp : entitys) {
					int index = 0;
					Object[] objs = new Object[fieldNms.keySet().size()+ids.keySet().size()];
					for (String fieldNmTmp : fieldNms.keySet()) {
						objs[index] = this.getValueByField(fieldNmTmp,
								saveObjTmp);
						index++;
					}
					for(String idTmp : ids.keySet()){
						objs[index] = this.getValueByField(idTmp,
								saveObjTmp);
						index++;
					}
					params.add(objs);
				}
			}
			this.jdbcBaseDAO.batchUpdate(uptSql.toString(), params, 1000);
		}
	}

	public void JdbcBatchSQLExecute(String sql,List<Object[]> obj) {
		this.jdbcBaseDAO.batchUpdate(sql,obj,1000);
	}
	
	private Map<String, String> getColumnMappingByColName(Class<?> clz, String[] updates) {
		Map<String, String> rs=new HashMap<>();
		if(updates==null || updates.length==0){
			return rs;
		}
		Field[] fs=clz.getDeclaredFields();
		for(String cn : updates){
			for(Field f : fs){
				Column c=f.getAnnotation(Column.class);
				if(c!=null && c.name().equals(cn)){
					rs.put(f.getName(), cn);
				}
			}
		}
		return rs;
	}
	
	private Map<String, String> getColumnsMapping(Class<?> bean) {
		Map<String, String> columns = new HashMap<String, String>();
		if (bean != null) {
			Field[] fs = bean.getDeclaredFields();
			if (fs != null) {
				for (int i = 0; i < fs.length; i++) {
					int modify = fs[i].getModifiers();
					if (modify == 26) {
						// if field -> private(2) + static(8) + final(16)
						continue;
					}
					String fieldName = fs[i].getName();
					if (fs[i].isAnnotationPresent(Transient.class)) {
						// 若是jpa不持久化的属性
						continue;
					}
					if (fs[i].isAnnotationPresent(EmbeddedId.class)) {
						// 是联合主键
						Map<String, String> pkFields = getColumnsMapping(fs[i]
								.getType());
						for (String keyTmp : pkFields.keySet()) {
							columns.put(fieldName + "." + keyTmp,pkFields.get(keyTmp));
						}
					} else {
						if (columns.containsKey(fieldName)) {
							continue;
						}
						String columnName = "";
						Column c = fs[i].getAnnotation(Column.class);
						if (c != null && c.name() != null
								&& !"".equals(c.name())) {
							columnName = c.name();
						} else {
							columnName = fieldName;
						}
						columns.put(fieldName, columnName);
					}
				}
			}
		}
		return columns;
	}
	
	private Map<String, String> getIDsMapping(Class<?> bean) {
		Map<String, String> columns = new HashMap<String, String>();
		if (bean != null) {
			Field[] fs = bean.getDeclaredFields();
			if (fs != null) {
				for (int i = 0; i < fs.length; i++) {
					int modify = fs[i].getModifiers();
					if (modify == 26) {
						// if field -> private(2) + static(8) + final(16)
						continue;
					}
					String fieldName = fs[i].getName();
					if (fs[i].isAnnotationPresent(Id.class)) {
						// 若是jpa不持久化的属性
						Column c = fs[i].getAnnotation(Column.class);
						String columnName = c.name();
						columns.put(fieldName, columnName);
					}else if(fs[i].isAnnotationPresent(EmbeddedId.class)){
						Class<?> io= fs[i].getType();
						Field[] nc=io.getDeclaredFields();
						for(Field f:nc){
							if(f.isAnnotationPresent(Column.class)){
								Column jk=f.getAnnotation(Column.class);
								String lp=fs[i].getName()+"."+f.getName();
								String bg=jk.name();
								columns.put(lp, bg);
							}
						}
					}
				}
			}
		}
		return columns;
	}
	
	private Object getValueByField(String field, Object obj) {
		Object val = null;
		if (!StringUtils.isEmpty(field)) {
			String[] fieldDetails = field.split("\\.");
			val = obj;
			for (int i = 0; i < fieldDetails.length; i++) {
				if (fieldDetails[i] == null
						|| "".equals(fieldDetails[i].trim())) {
					continue;
				}
				try {
					val = getField(val, fieldDetails[i].trim());
				} catch (Exception e) {
					e.printStackTrace();
					val = null;
				}
			}
		}
		return val;
	}
	
	public Object getField(Object obj, String field) throws Exception {
		String firstLetter = field.substring(0, 1).toUpperCase();
		String getMethodName = "get" + firstLetter + field.substring(1);
		Method method = obj.getClass().getMethod(getMethodName);
		return method.invoke(obj);
	}
	
	@SuppressWarnings("rawtypes")
	public Map<String, Object> getMappingByJDBCNativeSQL(final Class entityClass,
			Map<String, Object> conditions, final String keyCol,
			final String valCol) {
		return null;
	}
	
	/**
	 * 传入Connection 批量执行sql方法
	 * @param sql
	 * @param batchArgs
	 * @param cycleSize
	 */
	public boolean batchUpdateWithConn(Connection conn,String sql, List<Object[]> batchArgs, int cycleSize){
		boolean flag = false;
		PreparedStatement ps = null;
		try {
			if(conn == null){
				throw new Exception("--------------Connection is Null-------------------");
			}
			ps = conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			int count = 0;
			System.out.println("-----------------更新开始------------------");
			for (Object[] obj : batchArgs) {
				count++;
				String info="";
				for (int i = 0; i < obj.length; i++) {
					info+=obj[i]+",";
					if(null != obj[i] && obj[i].getClass() == Date.class) {
						java.sql.Date sqlDate=new java.sql.Date(((Date) obj[i]).getTime());
						ps.setDate(i + 1, sqlDate);
						continue;
					}
					ps.setObject(i + 1, obj[i]);
				}
				System.out.println("执行第"+count+"条信息---"+sql+"("+info+")");
				ps.addBatch();
				if (count % cycleSize == 0) {
					ps.executeBatch();
					conn.commit();
				}
			}
			if (batchArgs.size() % cycleSize != 0) {
				ps.executeBatch();
				conn.commit();
			}
			System.out.println("-----------------更新结束------------------");
			flag = true;
		}catch(SQLException se){
			flag = false;
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			se.printStackTrace();
			if(se.getNextException() != null){				
				se.getNextException().printStackTrace();
			}
		}catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
	
}
