package com.dispatchtask.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.dispatchtask.job.UpdateEfdSmrptStatusThread;
import com.dispatchtask.util.PublicConstants;
import com.dispatchtask.vo.Customer;
import com.dispatchtask.vo.EfdSmrpt;
import com.dispatchtask.vo.EfdSmup;
import com.dispatchtask.vo.ProviderMobilePrefix;
import com.dispatchtask.vo.Smdown;
import com.dispatchtask.vo.SysParam;


public class DataAccessDaoImpl extends MyBatisConnectionFactory implements DataAccessDao {
	private SqlSessionFactory smsSqlSessionFactory;
	private SqlSessionFactory efdSqlSessionFactory;

	public DataAccessDaoImpl() {
		smsSqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory(SMSDB);
		efdSqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory(EFDDB);

	}

	public SysParam getSysParamByName(String string) throws SQLException {
		SysParam sysParam = null;
		SqlSession session = smsSqlSessionFactory.openSession();
		try {
			sysParam = (SysParam) session.selectOne(
					"DataAccess.getSysParamByName", string);
		} finally {
			session.close();
		}
		return sysParam;
	}
	
	public List<Smdown> getAllWaitSendSmsTask() throws SQLException {
		SqlSession session = efdSqlSessionFactory.openSession();
		List<Smdown> taskList= null;
		try {
			taskList = session.selectList("DataAccess.getAllWaitSendSmsTask");
		} finally {
			session.close();
		}
		return taskList;
	}

	public Customer getCusomerById(int cust_id) throws SQLException {
		Customer customer = null;
		SqlSession session = smsSqlSessionFactory.openSession();
		try {
			customer = (Customer) session.selectOne(
					"DataAccess.getCusomerById", cust_id);
		} finally {
			session.close();
		}
		return customer;
//		return (Customer)sqlMap.queryForObject("DataAccess.getCusomerById", cust_id);
	}

	public void batchUpdateTaskStatus(List<Smdown> stList) throws SQLException {
		SqlSession session = efdSqlSessionFactory.openSession();
		try {
			for (Smdown smdown : stList) {
				session.update("DataAccess.updateSmdownStatus", smdown);
			}
			session.commit();
			session.clearCache();
		} finally {
			session.close();
		}
	}

	public void batchInsertEfdSmrpt(List<EfdSmrpt> efdList) throws SQLException {
		SqlSession session = efdSqlSessionFactory.openSession();
		try {
			for (EfdSmrpt efdSmrpt : efdList) {
				session.insert("DataAccess.insertEfdSmrpt", efdSmrpt);
			}
			session.commit();
			session.clearCache();
		} finally {
			session.close();
		}
	}

	public void batchUpdateTaskStatusToEnd(List<Smdown> smList) throws SQLException {
		SqlSession session = efdSqlSessionFactory.openSession();
		try {
			for (Smdown smdown : smList) {
				session.update("DataAccess.updateTaskStatusToEnd", smdown);
			}
			session.commit();
			session.clearCache();
		} finally {
			session.close();
		}
	}

	public void initTaskStatus() throws SQLException {
		SqlSession session = efdSqlSessionFactory.openSession();
		try {
			session.update("DataAccess.initTaskStatus");
			session.commit();
			session.clearCache();
		} finally {
			session.close();
		}
//	    sqlMap.update("DataAccess.initTaskStatus");
	}

	public void insertEfdSmrpt(EfdSmrpt efd) throws SQLException {
		SqlSession session = efdSqlSessionFactory.openSession();
		try {
			session.insert("DataAccess.insertEfdSmrpt", efd);
		} finally {
			session.close();
		}
	
//		 sqlMap.insert("DataAccess.insertEfdSmrpt", efd);
	}

	public void updateTaskStatusToEnd(Smdown sm) throws SQLException {
		SqlSession session = efdSqlSessionFactory.openSession();
		try {
			session.update("DataAccess.updateTaskStatusToEnd", sm);
			session.commit();
			session.clearCache();
		} finally {
			session.close();
		}
	}

	@Override
	public void batchUpdateEfdSmrpt(List<EfdSmrpt> efdList) {
		SqlSession session = efdSqlSessionFactory.openSession();
		try {
			for (EfdSmrpt efdSmrpt : efdList) {
				int x = session.update("DataAccess.updateEfdSmrpt", efdSmrpt);
				if(x==0){
					//没有改变对象就重新入队
					int upTime = efdSmrpt.getUpTime();
					upTime = upTime+1;
					if(upTime>3){
						continue;
					}
					PublicConstants.tasklog.info("efdSmrpt msgid: "+efdSmrpt.getMessageid()+" : "+efdSmrpt.getSm_recvphone()+" 不存在与efdSmrpt,第 "+upTime+ " 次重新入队");
					efdSmrpt.setUpTime(upTime);
					UpdateEfdSmrptStatusThread.getInstance().addEfdSmrptToQueue(efdSmrpt);
				}
			}
			session.commit();
			session.clearCache();
		} finally {
			session.close();
		}
	}

	@Override
	public void batchInsertEfdSmup(List<EfdSmup> efdList) {
		SqlSession session = efdSqlSessionFactory.openSession();
		try {
			for (EfdSmup efdSmup : efdList) {
				session.insert("DataAccess.insertEfdSmup", efdSmup);
			}
			session.commit();
			session.clearCache();
		} finally {
			session.close();
		}
	}

	@Override
	public List<ProviderMobilePrefix> getProviderMobileList() {
		List<ProviderMobilePrefix> list = null;
		SqlSession session = smsSqlSessionFactory.openSession();
		try {
			list = session.selectList(
					"DataAccess.getProviderMobileList");
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public ProviderMobilePrefix getProviderIdByMobile(String mobile) {
		ProviderMobilePrefix pmf = null;
		SqlSession session = smsSqlSessionFactory.openSession();
		try {
			pmf = (ProviderMobilePrefix) session.selectOne(
					"DataAccess.getProviderIdByMobile", mobile);
		} finally {
			session.close();
		}
		return pmf;
	}

}
