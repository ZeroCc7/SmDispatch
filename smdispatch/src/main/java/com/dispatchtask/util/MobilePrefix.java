package com.dispatchtask.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.dispatchtask.dao.DataAccessDao;
import com.dispatchtask.dao.DataAccessDaoImpl;
import com.dispatchtask.vo.ProviderMobilePrefix;

@XmlRootElement
public class MobilePrefix {
	protected static DataAccessDao dataAccessDao = new DataAccessDaoImpl();

	public static List<ProviderMobilePrefix> providerMobileList = null;
	private static MobilePrefix instance;

	public synchronized static MobilePrefix getInstance() {
		if (instance == null) {
			instance = new MobilePrefix();
			instance.intProviderMobileList();
		}
		return instance;
	}
	

	/**
	 * 获取运营商号段（缓存）
	 * 
	 * @return
	 */
	public void intProviderMobileList() {
		if (providerMobileList == null) {
			// 第一次加载
			providerMobileList = new ArrayList<ProviderMobilePrefix>();
			// 批量执行器
			providerMobileList = dataAccessDao.getProviderMobileList();
		}
	}

	/**
	 * 解析号段归属
	 * 
	 * @param mobile
	 * @param smsTask
	 * @return
	 */

	public int getProvider(String mobile) {
		int provider_id = 9;
		for (ProviderMobilePrefix pmpp : providerMobileList) {
			if (mobile.startsWith(pmpp.getMobile_prefix())) {
				provider_id = pmpp.getProvider_id();
			}
		}
		if (provider_id == 9) {
			ProviderMobilePrefix pfx = dataAccessDao.getProviderIdByMobile(mobile);
			if(pfx!=null){
				providerMobileList.add(pfx);
				provider_id = pfx.getProvider_id();
				
			}
		}
		switch (provider_id) {
		case 1:
			return 0;
		case 2:
			return 1;
		case 3:
			return 2;
		default:
			return 9;
		}
	}

}
