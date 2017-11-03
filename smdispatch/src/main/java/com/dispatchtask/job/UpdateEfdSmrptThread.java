package com.dispatchtask.job;

import java.util.List;

import com.dispatchtask.dao.DataAccessDao;
import com.dispatchtask.dao.DataAccessDaoImpl;
import com.dispatchtask.util.PublicConstants;
import com.dispatchtask.vo.EfdSmrpt;

public class UpdateEfdSmrptThread implements Runnable {
	private List<EfdSmrpt> efdSmrptList;
	private DataAccessDao dao = new DataAccessDaoImpl();
	
	public  UpdateEfdSmrptThread(List<EfdSmrpt> efdSmrptList) {
		this.efdSmrptList = efdSmrptList;
	}
	@Override
	public void run() {
		try {
			long starTime = System.currentTimeMillis();
			dao.batchUpdateEfdSmrpt(efdSmrptList);
			PublicConstants.tasklog.info("updateEfdSmrpt完成Size（"+efdSmrptList.size()+"）耗时："+ (System.currentTimeMillis() - starTime) + " ms");
		} catch (Exception e) {
			PublicConstants.tasklog.warn("updateEfdSmrpt线程出现未知异常：",e);
		}

	}

}
