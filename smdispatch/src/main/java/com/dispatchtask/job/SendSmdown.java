package com.dispatchtask.job;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.dispatchtask.util.PublicConstants;
import com.dispatchtask.vo.Smdown;

public class SendSmdown {
	private ArrayBlockingQueue<Smdown> msgQueue1;
	private ArrayBlockingQueue<Smdown> msgQueue2;
	private int index;
	protected ThreadPoolExecutor taskExecutor;
	private SendSmsThread sendSmsThread;
	public SendSmdown(){
		msgQueue1 = new ArrayBlockingQueue<Smdown>(10000);
		msgQueue2 = new ArrayBlockingQueue<Smdown>(10000);
		taskExecutor = new ThreadPoolExecutor(PublicConstants.datCoreSize, PublicConstants.datMaxSize,
				10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(PublicConstants.datQueueSize),
				new ThreadPoolExecutor.AbortPolicy());
		startSendSmsThread();

	}
//	public void startSend() {
//		startSendSmsThread();
//	}
	
	public void sendToSmdown(Smdown st) {
		if (index == 1) {
			try {
				msgQueue1.put(st);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			try {
				msgQueue2.put(st);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void startSendSmsThread() {
		if (sendSmsThread == null) {
			sendSmsThread = new SendSmsThread();
		}
		ErrHandler handle = new ErrHandler();
		sendSmsThread.setUncaughtExceptionHandler(handle);
		sendSmsThread.start();
	}
	
	class SendSmsThread extends Thread {
		public void run() {
			PublicConstants.tasklog.info("入队线程启动....");
			while (true) {
				try {
					Thread.sleep(1000);
					List<Smdown> smList = new ArrayList<Smdown>();
					if (index == 1) {
						msgQueue2.drainTo(smList);
						index = 2;
					} else {
						msgQueue1.drainTo(smList);
						index = 1;
					}
					if(smList.size() == 0){
						continue;
					}
					taskExecutor.submit(new SendSmdownThread(smList,false));
				} catch (Exception ex) {
					PublicConstants.tasklog.info("发送线程出现异常：", ex);
					continue;
				}
			}
//			log.warn("下行短信发送线程已退出！");
		}
	}
	/**
	 * 自定义的一个UncaughtExceptionHandler
	 */
	class ErrHandler implements UncaughtExceptionHandler {
		/**
		 * 这里可以做任何针对异常的处理,比如记录日志等等
		 */
		public void uncaughtException(Thread a, Throwable e) {
			PublicConstants.tasklog.info("线程" + a.getName() + "异常退出,Message:", e);
			// 重启异常退出的线程
			startSendSmsThread();
		}
	}
	
}
