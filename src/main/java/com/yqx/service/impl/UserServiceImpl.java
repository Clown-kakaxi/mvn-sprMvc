package com.yqx.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.yqx.baseUtil.CommonService;
import com.yqx.baseUtil.JPABaseDAO;
import com.yqx.core.PagingInfo;
import com.yqx.core.QueryAssistant;
import com.yqx.model.AuthRole;
import com.yqx.model.LiveUser;
import com.yqx.service.UserService;

@Service("userService")
public class UserServiceImpl extends CommonService implements UserService{

	private Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	
	public UserServiceImpl()
    {
        JPABaseDAO<LiveUser, Long> baseDao = new JPABaseDAO<LiveUser, Long>(LiveUser.class);
        super.setBaseDAO(baseDao);
    }
	
	@Autowired
	@Qualifier("dsMysql")
	private DataSource ds;
	

	@Override
	public Map<String, Object> getUserList(int rows, int page, String name) throws SQLException {
		List<String[]> list = new ArrayList<String[]>();
		Connection conn = ds.getConnection();
		PagingInfo pageInfo = new PagingInfo(rows, page);
		StringBuilder sb = new StringBuilder("");
		sb.append(" select * from live_user where 1=1  ");
		if(!StringUtils.isBlank(name)){
			sb.append(" and nickname like '%"+name+"%' ");
		}
		sb.append(" order by id asc ");
		QueryAssistant qa = new QueryAssistant(sb.toString(), conn, pageInfo);
		String[] colNames = new String[]{"id","name","level","freeCoin","coin","isMember","pic","password","anchorId","registTime"};
		Map<String, Object> map = qa.getJSONByCondition(colNames, true, list);
		return map;
	}
	
}
