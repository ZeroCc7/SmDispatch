package com.dispatchtask.main;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.dispatchtask.dao.DataAccessDao;
import com.dispatchtask.dao.DataAccessDaoImpl;
import com.dispatchtask.job.FirstSendSmdown;
import com.dispatchtask.job.SendSmdown;
import com.dispatchtask.util.PublicConstants;
import com.dispatchtask.vo.Smdown;



public class SmsTaskDiapatch {
	
	private static volatile Thread smsTaskQueueScheduler; // 消息队列调度器
	private static SmsTaskDiapatch instance;
	public static Map<Integer, Smdown> smsTaskMap;
	private SendSmdown sendSmdown;
	private FirstSendSmdown firstSendSmdown;

	
	private DataAccessDao dataAccess = new DataAccessDaoImpl();

	public static SmsTaskDiapatch getInstance() {
		if(instance == null){
			instance = new SmsTaskDiapatch();
			instance.init();
			instance.startSmsTaskQueueScheduler();
		}
		return instance;
	}
	
	
	private void init() {
		sendSmdown = new SendSmdown();
		firstSendSmdown = new FirstSendSmdown();
		smsTaskMap = new HashMap<Integer, Smdown>();
	}


	// 启动消息队列调度器
	public void startSmsTaskQueueScheduler() {
		PublicConstants.tasklog.info("短信群发调度启动...");
		//第一次执行
		try {
			dataAccess.initTaskStatus();
		} catch (SQLException e1) {
			PublicConstants.tasklog.info("初始化异常",e1);
			e1.printStackTrace();
			return;
		}
		if("".equals(PublicConstants.custCode) ||  "".equals(PublicConstants.firstCustCode)){
			PublicConstants.tasklog.info("customer 或 firstCustomer 错误！");
			return;
		}
		
		Runnable r = new Runnable() {
			public void run() {
				
				while (true) {
					try {
						long starTime = System.currentTimeMillis();
						List<Smdown> stList = dataAccess.getAllWaitSendSmsTask();
						dataAccess.batchUpdateTaskStatus(stList);
						PublicConstants.tasklog.info("获取(size="+stList.size()+")任务耗时："+(System.currentTimeMillis() - starTime) + " ms");
						for (Smdown st : stList) {
							if(st.getSendlevel() == 0){
								sendSmdown.sendToSmdown(st);
							}else{
								firstSendSmdown.sendToSmdown(st);
							}
						}
					} catch (Exception ex) {
						PublicConstants.tasklog.warn("获取待发表异常", ex);
					}
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		};
		smsTaskQueueScheduler = new Thread(r);
		smsTaskQueueScheduler.start();
	}
	
	
	
	
	
	
	
}
