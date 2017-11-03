package com.dispatchtask.dao;

import java.sql.SQLException;
import java.util.List;

import com.dispatchtask.vo.Customer;
import com.dispatchtask.vo.EfdSmrpt;
import com.dispatchtask.vo.EfdSmup;
import com.dispatchtask.vo.ProviderMobilePrefix;
import com.dispatchtask.vo.Smdown;
import com.dispatchtask.vo.SysParam;


public interface DataAccessDao {

	/**
	 * 获取所有待发送任务
	 * @return
	 * @throws SQLException 
	 */
	List<Smdown> getAllWaitSendSmsTask() throws SQLException;


	/**
	 * 获取系统参数
	 * @param string
	 * @return
	 * @throws SQLException 
	 */
	SysParam getSysParamByName(String string) throws SQLException;

	/**
	 * 获取代理
	 * @param cust_id
	 * @return
	 * @throws SQLException 
	 */
	Customer getCusomerById(int cust_id) throws SQLException;




	/**
	 * 批量更新任务状态
	 * @param stList
	 * @throws SQLException 
	 */
	void batchUpdateTaskStatus(List<Smdown> stList) throws SQLException;

	/**
	 * 批量保存EfdSmrpt
	 * @param efdList
	 * @throws SQLException 
	 */
	void batchInsertEfdSmrpt(List<EfdSmrpt> efdList)throws SQLException;

	/**
	 * 批量更新任务状态
	 * @param stList
	 * @throws SQLException 
	 */
	void batchUpdateTaskStatusToEnd(List<Smdown> smList)throws SQLException;

	/**
	 * 初始化
	 * @throws SQLException 
	 */
	void initTaskStatus() throws SQLException;

	/**
	 * 插入状态报告
	 * @param efd
	 * @throws SQLException 
	 */
	void insertEfdSmrpt(EfdSmrpt efd) throws SQLException;

	/**
	 * 更改任务状态结束
	 * @param sm
	 * @throws SQLException 
	 */
	void updateTaskStatusToEnd(Smdown sm) throws SQLException;

	/**
	 * 批量更新状态表
	 * @param efdList
	 */
	void batchUpdateEfdSmrpt(List<EfdSmrpt> efdList);

	/**
	 * 批量保存上行记录
	 * @param efdList
	 */
	void batchInsertEfdSmup(List<EfdSmup> efdList);

	/**
	 * 获取号段归属表
	 * @return
	 */
	List<ProviderMobilePrefix> getProviderMobileList();

	/**
	 * 根据号码获取归属
	 * @param mobile 
	 * @return
	 */
	ProviderMobilePrefix getProviderIdByMobile(String mobile);
	
}
