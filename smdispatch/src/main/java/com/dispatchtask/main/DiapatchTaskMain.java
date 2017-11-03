package com.dispatchtask.main;

import com.dispatchtask.job.QueryMo;
import com.dispatchtask.job.QueryReport;
import com.dispatchtask.util.PublicConstants;

public class DiapatchTaskMain {


	static boolean isStart = true;
	private static int flag = 1;
	private static int mod = 1;

	/**
	 * 调度程序入口
	 * @param args
	 */
	public static void main(String[] args) {
		
		//初始化系统变量
		PublicConstants.initPublicConstants();
				
		//群发短信任务调度线程
		SmsTaskDiapatch.getInstance();
		
		//启动状态报告定时扫描
		QueryReport queryReport = new QueryReport();
		queryReport.start();
		
		//启动上行定时扫描
		QueryMo queryMo = new QueryMo();
		queryMo.start();
	}

}
