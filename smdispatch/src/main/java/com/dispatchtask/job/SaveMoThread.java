package com.dispatchtask.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.dispatchtask.dao.DataAccessDao;
import com.dispatchtask.dao.DataAccessDaoImpl;
import com.dispatchtask.util.PublicFunctions;
import com.dispatchtask.vo.EfdSmup;

public class SaveMoThread extends Thread {
	private final static Logger logger = Logger.getLogger(SaveMoThread.class);
	private final static Logger tasklog = Logger.getLogger("TaskMonitor");
	private volatile static ArrayBlockingQueue<EfdSmup> efdSmupQueue1 = new ArrayBlockingQueue<EfdSmup>(200000);
	private volatile static ArrayBlockingQueue<EfdSmup> efdSmupQueue2 = new ArrayBlockingQueue<EfdSmup>(200000);
	
	private volatile static int index = 1;
	private static SaveMoThread instance;
	private Thread submitThread;
	protected ThreadPoolExecutor taskExecutor = new ThreadPoolExecutor(20, 200,
			50, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10000),
			new ThreadPoolExecutor.AbortPolicy());

	public synchronized static SaveMoThread getInstance() {
		if (instance == null) {
			instance = new SaveMoThread();
			instance.handleBatchSaveQueueThread();
		}
		return instance;
	}

	/*
	 * 
	 * public boolean add(E e) 方法将抛出IllegalStateException异常，说明队列已满。 public
	 * boolean offer(E e) 方法则不会抛异常，只会返回boolean值，告诉你添加成功与否，队列已满，当然返回false。 public
	 * void put(E e) throws InterruptedException
	 * 方法则一直阻塞（即等待，直到线程池中有线程运行完毕，可以加入队列为止）。
	 */
	public boolean addEfdSmupToQueue(EfdSmup efdSmup) {
		if (index == 1) {
			boolean success = efdSmupQueue1.add(efdSmup);
			return success;
		} else {
			boolean success = efdSmupQueue2.add(efdSmup);
			return success;
		}
	}


	public void handleBatchSaveQueueThread() {
		Runnable r = new Runnable() {
			public void run() {
				long starTime = new Date().getTime();
				while (true) {
					try {
						long beginTime = System.currentTimeMillis();
						if (index == 1) {
							index = 2;
							batchSaveMobile(efdSmupQueue1);
						} else {
							index = 1;
							batchSaveMobile(efdSmupQueue2);
						}
						if(beginTime-starTime > 10000){
							starTime = beginTime;
							//5S 打印一次
							tasklog.info(String.format(
									"[SaveMoThread 批量保存队列监控] [%d/%d] Queue:[%d/%d], Active: %d, Completed: %d, Task: %d",
									taskExecutor.getPoolSize(), taskExecutor.getCorePoolSize(),
									taskExecutor.getQueue().size(), 10000,
									taskExecutor.getActiveCount(), taskExecutor.getCompletedTaskCount(),
									taskExecutor.getTaskCount()));
						}
						
						try {
							long finishTime = System.currentTimeMillis();
							long executeTime = finishTime - beginTime;
							if (executeTime < 500) {
								// 如果执行时间小于0.5秒，说明没有大量操作，则sleep。否则，说明有大量操作，不需要sleep
								Thread.sleep(1000 - executeTime);
							}
						} catch (InterruptedException e) {
							logger.error("批量保存SaveMoThread调度线程中断等待", e);
							continue;
						}
					} catch (Exception ex) {
						logger.error("批量保存SaveMoThread调度线程出现异常", ex);
					}
				}
			}
		};
		submitThread = new Thread(r);
		submitThread.start();
	}

	@SuppressWarnings("unchecked")
	public void batchSaveMobile(ArrayBlockingQueue<EfdSmup> efdQueue) {
		
		// 批量保存短信状任务号码状态
		List<EfdSmup> efdList = new ArrayList<EfdSmup>();
		efdQueue.drainTo(efdList);
		if (!PublicFunctions.isBlankList(efdList)) {
			if (efdList.size() > 500) {
				// list超过1000,进行分割成小list
				List<EfdSmup>[] efdSubLists = PublicFunctions.splitList(efdList, 500);
				for (List<EfdSmup> efdSmup : efdSubLists) {
					Runnable task = new SaveEfdSmupThread(efdSmup);
					try {
						taskExecutor.submit(task);
					} catch (RejectedExecutionException ex) {
						logger.error("批量SaveMoThread调度队列满， 保存失败", ex);
					}
				}
			} else {
				Runnable task = new SaveEfdSmupThread(efdList);
				try {
					taskExecutor.submit(task);
				} catch (RejectedExecutionException ex) {
					logger.error("批量SaveMoThread调度队列满， 保存失败", ex);
				}
			}
		}
	}

	
	
}
