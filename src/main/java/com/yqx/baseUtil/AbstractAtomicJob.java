package com.yqx.baseUtil;

/**
 * 将多个操作作为同一事物处理
 */
import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractAtomicJob {

	private Connection conn;
	
	public AbstractAtomicJob(Connection conn) {
		this.conn=conn;
	}

	public JobResult startTran(boolean close) {
		JobResult r = new JobResult();;
		try{
			conn.setAutoCommit(false);
			doTran(conn);
			conn.commit();
			r.success=true;
		}catch(Exception e){
			r.success=false;
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}finally{}
		}finally{
			if(close){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return r;
	}
	
	protected abstract void doTran(Connection conn) throws Exception;
	
	public static class JobResult{
		public boolean success;
		
	}
	
	public static void main(String[] args) {
		//传入Connection
//		new AbstractAtomicJob(conn){
//			protected void doTran(Connection conn) throws Exception {
//				//执行多个sql语句
//				//bsd.JdbcBatchUpdate(delMeeting, conn, null);
//				//bsd.JdbcBatchSQLExecute("delete from ACRM_F_CI_MEETING where id=?", dels, conn,null);
//			}
//		}.startTran(false);
	}
	
}
