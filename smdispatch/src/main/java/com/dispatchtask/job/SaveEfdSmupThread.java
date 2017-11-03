package com.dispatchtask.job;

import java.util.ArrayList;
import java.util.List;

import com.dispatchtask.dao.DataAccessDao;
import com.dispatchtask.dao.DataAccessDaoImpl;
import com.dispatchtask.util.PublicConstants;
import com.dispatchtask.vo.EfdSmup;


public class SaveEfdSmupThread  implements Runnable {
	private List<EfdSmup> efdSmupList;
	private DataAccessDao dao = new DataAccessDaoImpl();

	public SaveEfdSmupThread(List<EfdSmup> efdSmupList) {
		this.efdSmupList = efdSmupList;
	}

	public void run() {
		try {
			List<EfdSmup> efdList = new ArrayList<EfdSmup>();
			efdList.addAll(efdSmupList);
			if(efdList.size() == 0){
				return;
			}
			long starTime = System.currentTimeMillis();
			dao.batchInsertEfdSmup(efdList);
			PublicConstants.tasklog.info("EfdSmup写入完成 Size（"+efdList.size()+"）耗时："+ (System.currentTimeMillis() - starTime) + " ms");
		} catch (Exception e) {
			PublicConstants.tasklog.warn("批量保存EfdSmup线程出现未知异常：",e);
		}
	}
}
