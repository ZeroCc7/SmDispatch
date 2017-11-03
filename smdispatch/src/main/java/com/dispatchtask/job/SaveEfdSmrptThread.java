package com.dispatchtask.job;

import java.util.ArrayList;
import java.util.List;

import com.dispatchtask.dao.DataAccessDao;
import com.dispatchtask.dao.DataAccessDaoImpl;
import com.dispatchtask.util.PublicConstants;
import com.dispatchtask.vo.EfdSmrpt;


public class SaveEfdSmrptThread  implements Runnable {
	private List<EfdSmrpt> efdSmrptList;
	private DataAccessDao dao = new DataAccessDaoImpl();

	public SaveEfdSmrptThread(List<EfdSmrpt> efdSmrptList) {
		this.efdSmrptList = efdSmrptList;
	}

	public void run() {
		try {
			List<EfdSmrpt> efdList = new ArrayList<EfdSmrpt>();
			efdList.addAll(efdSmrptList);
			if(efdList.size() == 0){
				return;
			}
			long starTime = System.currentTimeMillis();
			dao.batchInsertEfdSmrpt(efdList);
			PublicConstants.tasklog.info("写入完成Size（"+efdList.size()+"）耗时："+ (System.currentTimeMillis() - starTime) + " ms");
		} catch (Exception e) {
			PublicConstants.tasklog.warn("批量保存EfdSmrpt线程出现未知异常：",e);
		}
	}
}
