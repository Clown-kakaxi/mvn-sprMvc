package com.yqx.core;


import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

public class QueryAssistant {
	private static Logger log = Logger.getLogger(QueryAssistant.class);
    /** 主键字段 */
    private String primaryKey = "ID";
    /** 原始SQL */
    private String originSQL;
    /** db2 withSQL 需要在select前*/
    private String withSQL;
    /** 加上分页的SQL */
    private String pagingSQL;
    /** 数据库连接 */
    private Connection connection;
    /** SQL参数 */
    private List<Object> params;
    /** 分页信息类 */
    private PagingInfo paging;
    /** Oracle字典映射 */
    private ConcurrentHashMap<String, String> oracleMapping = new ConcurrentHashMap<String, String>();
   
//    LookupManager manager = LookupManager.getInstance();
    
    public String prepareSQL(String SQL) {

        StringBuilder builder = new StringBuilder(SQL);
        if(paging != null) {
        	if("ORACLE".equals(SystemConstance.DB_TYPE.toUpperCase())){
        		oraclePaging(builder);
        	}else if("DB2".equals(SystemConstance.DB_TYPE.toUpperCase())){
        		db2Paging(builder);
        	}else if("MYSQL".equals(SystemConstance.DB_TYPE.toUpperCase())){
        		mysqlPaging(builder);
        	}else{
        		oraclePaging(builder);
        	}
            log.info("JDBC分页查询语句："+builder.toString());
        }
        return builder.toString();
    }
    
    public void oraclePaging(StringBuilder builder){
    	builder.insert(0, "SELECT *  FROM (SELECT ROW_NUMBER () OVER (ORDER BY 1) AS RN, BUSINESS_QUERY.* FROM (");
        builder.append(") BUSINESS_QUERY) SUB_QUERY WHERE RN BETWEEN " + paging.getBeginRowNumber());
        builder.append(" AND " + paging.getEndRowNumber());  
    }
    public void db2Paging(StringBuilder builder){
    	builder.insert(0, "SELECT *  FROM (SELECT ROW_NUMBER () OVER (ORDER BY 1) AS RN, BUSINESS_QUERY.* FROM (");
	  	if( withSQL != null) {
	  		builder.insert(0, withSQL);
	  	}
	    builder.append(") BUSINESS_QUERY) SUB_QUERY WHERE RN BETWEEN " + paging.getBeginRowNumber());
	    builder.append(" AND " + paging.getEndRowNumber());   
    }
    public void mysqlPaging(StringBuilder builder){
    	builder.append(" limit " + (paging.getBeginRowNumber()-1));
        builder.append(" , " + paging.getPageSize());
    }
    
//    protected void processLookup(HashMap<String, Object> map, String name, Object code) {
//        if (oracleMapping.containsKey(name)) {
//        	if(code!=null){
//        		String lookupName = oracleMapping.get(name);
//        		ConcurrentHashMap<String, String> lookup = manager.getOracleValues(lookupName);
//        		String key = code.toString();
//        		if (lookup != null && lookup.containsKey(key)) {
//        			map.put(name + "_ORA", lookup.get(key));
//        		} else {
//        			map.put(name + "_ORA", code);
//        		}     
//        	}else{
//        		//如果源字段为空，转码字段值也赋值为""
//        		map.put(name + "_ORA", "");
//        	}
//        }        
//    }
    
    public void addOracleLookup(String columnName, String LookupName) {
        oracleMapping.put(columnName, LookupName);
    }
   
    public QueryAssistant(String SQL, Connection connection) {
        this.originSQL = SQL;
        this.connection = connection;
    }
    
    public QueryAssistant(String SQL, Connection connection, PagingInfo paging) {
        this.paging = paging;
        this.originSQL = SQL;
        this.connection = connection;
    }

    public QueryAssistant(String SQL, String withSQL, Connection connection, PagingInfo paging) {
        this.paging = paging;
        this.originSQL = SQL;
        this.withSQL = withSQL;
        this.connection = connection;
    }
    
    public QueryAssistant(String SQL, Connection connection, List<Object> params) {
        this.originSQL = SQL;
        this.connection = connection;
        this.params = params;
    }

    /*
    public ResultSet executeQuery() throws SQLException {
    	pagingSQL = prepareSQL(originSQL);
    	Statement stmt = connection.createStatement();
	    ResultSet   rs = stmt.executeQuery(pagingSQL);
        return rs;
    }*/
    
    @SuppressWarnings("resource")
	public Map<String, Object> getJSON() throws SQLException {
        pagingSQL = prepareSQL(originSQL);
        log.info("QueryAssistant.getJSON:originSQL:"+originSQL);
        log.info("QueryAssistant.getJSON:pagingSQL:"+pagingSQL);
        ResultSet rs = null;
        PreparedStatement preStmt = null;
        Map<String, Object> result = new HashMap<String, Object>();
		try {
			preStmt = connection.prepareStatement(pagingSQL);
			processParams(preStmt);
			rs = preStmt.executeQuery();
			
	        ResultSetMetaData metaData = rs.getMetaData();
	        int columnCount = metaData.getColumnCount();
	       
	        List<HashMap<String, Object>> rowsList = new ArrayList<HashMap<String, Object>>();
	        while (rs.next()) {
	            HashMap<String, Object> map = new HashMap<String, Object>();
	            for (int i = 0; i < columnCount; i++) {
	                String columnName = metaData.getColumnLabel(i + 1);
	                if ("RN".equals(columnName.toUpperCase())) {
	                    continue;
	                }
	                if(rs.getObject(columnName) != null) {
	                    if (rs.getObject(columnName) instanceof Clob) {
	                    	Clob clob = rs.getClob(columnName);
	                    	String value = clob.getSubString((long) 1, (int) clob.length());
	                    	map.put(columnName.toUpperCase(), value); 
	                    } else if (rs.getObject(columnName) instanceof Blob) {
	                    	Blob blob = rs.getBlob(columnName);
	                    	long len = blob.length();
	                        byte [] data = blob.getBytes(1,(int) len);
	                    	map.put(columnName.toUpperCase(), data); 
	                    }  else {
	                    	String value = rs.getObject(columnName).toString();
	                    	map.put(columnName.toUpperCase(), value); 
	                    }
	                } else {
	                    map.put(columnName.toUpperCase(), "");
	                }
	                //需要考虑源字段为空的情况
//	                processLookup(map, columnName.toUpperCase(), rs.getObject(columnName));
	            }
	            rowsList.add(map);
	        }
	        result.put("data", rowsList);
	        
	        if (paging != null) {
	            StringBuilder builder = new StringBuilder(originSQL);
	            builder.insert(0, "SELECT COUNT(1) AS TOTAL FROM (");
	            if("DB2".equals(SystemConstance.DB_TYPE) && withSQL != null) {
	          		builder.insert(0, withSQL);
	          	}
	            builder.append(") SUB_QUERY");
	            
	            preStmt = connection.prepareStatement(builder.toString());
    			processParams(preStmt);
    			rs = preStmt.executeQuery();
	            if(rs.next()) {
	                result.put("count", rs.getInt("TOTAL"));
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("关闭数据连接异常:", e);
					e.printStackTrace();
				}
			}
			if(preStmt!=null){
				try {
					preStmt.close();
				} catch (SQLException e) {
					log.error("关闭数据连接异常:", e);
					e.printStackTrace();
				}
			}
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e) {
					log.error("关闭数据连接异常:", e);
					e.printStackTrace();
				}
			}
		}
        return result;       
    }
    
    public Map<String, Object>  getJSON(String[] colNames) throws SQLException {
    	return this.getJSON(colNames,false); 
	}
    
    
    /**
     * 
     * @param colNames 驼峰式列名
     * @param queryTotalFlag 是否查询总条数
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("resource")
	public Map<String, Object>  getJSON(String[] colNames, boolean queryTotalFlag) throws SQLException {
    	pagingSQL = prepareSQL(originSQL);
        log.info("QueryAssistant.getJSON:originSQL:"+originSQL);
        log.info("QueryAssistant.getJSON:pagingSQL:"+pagingSQL);
        ResultSet rs = null;
        //Statement stmt = null;
        PreparedStatement stmt = null;
        Map<String, Object> result = new HashMap<String, Object>();
		try {
			//stmt = connection.createStatement();
			long getC = System.currentTimeMillis();
			//rs = stmt.executeQuery(pagingSQL);
			stmt = connection.prepareStatement(pagingSQL);
			rs = stmt.executeQuery();
			long used = System.currentTimeMillis() - getC;
			log.info("cost times ="+used);
	        ResultSetMetaData metaData = rs.getMetaData();
	        int columnCount = metaData.getColumnCount();
	       
	        List<HashMap<String, Object>> rowsList = new ArrayList<HashMap<String, Object>>();
	        while (rs.next()) {
	            HashMap<String, Object> map = new HashMap<String, Object>();
	            for (int i = 0; i < columnCount; i++) {
	                String columnName = metaData.getColumnLabel(i + 1);
	                String name = columnName.toUpperCase();
	                if(colNames.length>i){
	                	name = colNames[i];
	                }
	                if ("RN".equals(columnName.toUpperCase())) {
	                    continue;
	                }
	                if(rs.getObject(columnName) != null) {
	                    if (rs.getObject(columnName) instanceof Clob) {
	                    	Clob clob = rs.getClob(columnName);
	                    	String value = clob.getSubString((long) 1, (int) clob.length());
	                    	map.put(name, value); 
	                    } else if (rs.getObject(columnName) instanceof Blob) {
	                    	Blob blob = rs.getBlob(columnName);
	                    	long len = blob.length();
	                        byte [] data = blob.getBytes(1,(int) len);
	                    	map.put(name, data); 
	                    }  else {
	                    	String value = rs.getObject(columnName).toString();
	                    	map.put(name, value); 
	                    }
	                } else {
	                    map.put(name, "");
	                }
	                //需要考虑源字段为空的情况
//	                processLookup(map, name, rs.getObject(columnName));
	            }
	            rowsList.add(map);
	        }
	        result.put("data", rowsList);
	        
	        if (paging != null || queryTotalFlag) {
	            StringBuilder builder = new StringBuilder(originSQL);
	            builder.insert(0, "SELECT COUNT(1) AS TOTAL FROM (");
	            if("DB2".equals(SystemConstance.DB_TYPE) && withSQL != null) {
	          		builder.insert(0, withSQL);
	          	}
	            builder.append(") SUB_QUERY");
	            //rs = stmt.executeQuery(builder.toString());
				stmt = connection.prepareStatement(builder.toString());
				rs = stmt.executeQuery();
	            if(rs.next()) {
	                result.put("count", rs.getInt("TOTAL"));
	            }
	        }
	        
	        used = System.currentTimeMillis() - getC;
			log.info("cost times ="+used);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("关闭数据连接异常:", e);
					e.printStackTrace();
				}
			}
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					log.error("关闭数据连接异常:", e);
					e.printStackTrace();
				}
			}
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e) {
					log.error("关闭数据连接异常:", e);
					e.printStackTrace();
				}
			}
		}
        return result;    
	}
    
    /**
     * 
     * @param colNames 驼峰式列名
     * @param queryTotalFlag 是否查询总条数
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("resource")
	public Map<String, Object>  getJSONByCondition(String[] colNames, boolean queryTotalFlag, List<String[]> conditions) throws SQLException {
    	pagingSQL = prepareSQL(originSQL);
        log.info("QueryAssistant.getJSON:originSQL:"+originSQL);
        log.info("QueryAssistant.getJSON:pagingSQL:"+pagingSQL);
        ResultSet rs = null;
        //Statement stmt = null;
        PreparedStatement stmt = null;
        Map<String, Object> result = new HashMap<String, Object>();
		try {
			//stmt = connection.createStatement();
			long getC = System.currentTimeMillis();
			//rs = stmt.executeQuery(pagingSQL);
			stmt = connection.prepareStatement(pagingSQL);
			if(conditions != null&&conditions.size() > 0){
				for (int i = 0; i < conditions.size(); i++) {
	                String[] str = conditions.get(i);
	                String para = str[0];
	                stmt.setString(i + 1, para);
				}
			}
			//stmt.setString(1, "有数据");
			rs = stmt.executeQuery();
			long used = System.currentTimeMillis() - getC;
			log.info("cost times ="+used);
	        ResultSetMetaData metaData = rs.getMetaData();
	        int columnCount = metaData.getColumnCount();
	       
	        List<HashMap<String, Object>> rowsList = new ArrayList<HashMap<String, Object>>();
	        while (rs.next()) {
	            HashMap<String, Object> map = new HashMap<String, Object>();
	            for (int i = 0; i < columnCount; i++) {
	                String columnName = metaData.getColumnLabel(i + 1);
	                String name = columnName.toUpperCase();
	                if(colNames.length>i){
	                	name = colNames[i];
	                }
	                if ("RN".equals(columnName.toUpperCase())) {
	                    continue;
	                }
	                if(rs.getObject(columnName) != null) {
	                    if (rs.getObject(columnName) instanceof Clob) {
	                    	Clob clob = rs.getClob(columnName);
	                    	String value = clob.getSubString((long) 1, (int) clob.length());
	                    	map.put(name, value); 
	                    } else if (rs.getObject(columnName) instanceof Blob) {
	                    	Blob blob = rs.getBlob(columnName);
	                    	long len = blob.length();
	                        byte [] data = blob.getBytes(1,(int) len);
	                    	map.put(name, data); 
	                    }  else {
	                    	String value = rs.getObject(columnName).toString();
	                    	map.put(name, value); 
	                    }
	                } else {
	                    map.put(name, "");
	                }
	                //需要考虑源字段为空的情况
//	                processLookup(map, name, rs.getObject(columnName));
	            }
	            rowsList.add(map);
	        }
	        result.put("data", rowsList);
	        
	        if (paging != null || queryTotalFlag) {
	            StringBuilder builder = new StringBuilder(originSQL);
	            builder.insert(0, "SELECT COUNT(1) AS TOTAL FROM (");
	            if("DB2".equals(SystemConstance.DB_TYPE) && withSQL != null) {
	          		builder.insert(0, withSQL);
	          	}
	            builder.append(") SUB_QUERY");
	            //rs = stmt.executeQuery(builder.toString());
				stmt = connection.prepareStatement(builder.toString());
				if(conditions != null&&conditions.size() > 0){
					for (int i = 0; i < conditions.size(); i++) {
		                String[] str = conditions.get(i);
		                String para = str[0];
		                stmt.setString(i + 1, para);
					}
				}

				rs = stmt.executeQuery();
	            if(rs.next()) {
	                result.put("count", rs.getInt("TOTAL"));
	            }
	        }
	        
	        used = System.currentTimeMillis() - getC;
			log.info("cost times ="+used);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("关闭数据连接异常:", e);
					e.printStackTrace();
				}
			}
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					log.error("关闭数据连接异常:", e);
					e.printStackTrace();
				}
			}
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e) {
					log.error("关闭数据连接异常:", e);
					e.printStackTrace();
				}
			}
		}
        return result;    
	}
    
    @SuppressWarnings("resource")
	public Map<String, Object> getJSON(boolean needTrans) throws SQLException {
    	if(needTrans){
    		ResultSet rs = null;
    		Statement stmt = null;
    		Map<String, Object> result = new HashMap<String, Object>();
    		try {
    			pagingSQL = prepareSQL(originSQL);
    	        stmt = connection.createStatement();
    	        rs = stmt.executeQuery(pagingSQL);
    	        ResultSetMetaData metaData = rs.getMetaData();
    	        int columnCount = metaData.getColumnCount();
    	        
    	        List<HashMap<String, Object>> rowsList = new ArrayList<HashMap<String, Object>>();
    	        while (rs.next()) {
    	            HashMap<String, Object> map = new HashMap<String, Object>();
    	            for (int i = 0; i < columnCount; i++) {
    	                String columnName = metaData.getColumnLabel(i + 1);
    	                if ("RN".equals(columnName.toUpperCase())) {
    	                    continue;
    	                }
    	                if(rs.getObject(columnName) != null) {
    	                	if (rs.getObject(columnName) instanceof Clob) {
    	                    	Clob clob = rs.getClob(columnName);
    	                    	String value = clob.getSubString((long) 1, (int) clob.length());
    	                    	map.put(ColumnNameUtil.getModelField(columnName), value); 
    	                    } else if (rs.getObject(columnName) instanceof Blob) {
    	                    	Blob blob = rs.getBlob(columnName);
    	                    	long len = blob.length();
    	                        byte [] data = blob.getBytes(1,(int) len);
    	                    	map.put(ColumnNameUtil.getModelField(columnName), data); 
    	                    }  else {
    	                    	String value = rs.getObject(columnName).toString();
    	                    	map.put(ColumnNameUtil.getModelField(columnName), value); 
    	                    }
//    	                    processLookup(map, ColumnNameUtil.getModelField(columnName), rs.getObject(columnName));
    	                } else {
    	                    map.put(ColumnNameUtil.getModelField(columnName), "");
    	                }
    	            }
    	            rowsList.add(map);
    	        }
    	        result.put("data", rowsList);
    	        
    	        if (paging != null) {
    	            StringBuilder builder = new StringBuilder(originSQL);
    	            builder.insert(0, "SELECT COUNT(1) AS TOTAL FROM (");
    	            if("DB2".equals(SystemConstance.DB_TYPE) && withSQL != null) {
    	          		builder.insert(0, withSQL);
    	          	}
    	            builder.append(") SUB_QUERY");
    	            rs = stmt.executeQuery(builder.toString());
    	            if(rs.next()) {
    	                result.put("count", rs.getInt("TOTAL"));
    	            }
    	        }
	    	} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				if(rs!=null){
					try {
						rs.close();
					} catch (SQLException e) {
						log.error("关闭数据连接异常:", e);
						e.printStackTrace();
					}
				}
				if(stmt!=null){
					try {
						stmt.close();
					} catch (SQLException e) {
						log.error("关闭数据连接异常:", e);
						e.printStackTrace();
					}
				}
				if(connection!=null){
					try {
						connection.close();
					} catch (SQLException e) {
						log.error("关闭数据连接异常:", e);
						e.printStackTrace();
					}
				}
			}
    		return result;  
    	}else{
    		return this.getJSON();
    	}
    }
    
    private void processParams(PreparedStatement preStmt) throws SQLException {
		if(params != null) {
			for(int i = 0; i <params.size(); i++) {
				Object obj = params.get(i);
				if(obj instanceof Integer) {
					preStmt.setInt(i+1, (Integer)obj);
				} else if(obj instanceof Long) {
					preStmt.setLong(i+1, (Long)obj);
				} else if(obj instanceof Float) {
					preStmt.setFloat(i+1, (Float)obj);
				} else if(obj instanceof Double) {
					preStmt.setDouble(i+1, (Double)obj);
				} else if(obj instanceof Date) {
					preStmt.setDate(i+1, (Date)obj);
				} else {
					preStmt.setString(i+1, (String)obj);
				}
			}
		}
		
	}

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

	

}
