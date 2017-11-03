package com.dispatchtask.job;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dispatchtask.dao.DataAccessDao;
import com.dispatchtask.dao.DataAccessDaoImpl;
import com.dispatchtask.util.MD5;
import com.dispatchtask.util.PublicConstants;
import com.dispatchtask.util.PublicFunctions;
import com.dispatchtask.vo.EfdSmrpt;
import com.dispatchtask.vo.MoVo;
import com.dispatchtask.vo.ReportVo;

public class QueryReport extends Thread {
	private String url;
	private volatile boolean state = true;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected DataAccessDao dataAccessDao = new DataAccessDaoImpl();
	private int index;
	public QueryReport(){
		this.url = "http://"+PublicConstants.baseUrl;
	
	}
	@SuppressWarnings("rawtypes")
	public void run() {
		
		while (state) {
			
			try {
				Thread.sleep(PublicConstants.reportSleepTime);
				String cust_code = "";
				String password = "";
				if(index==0){
					index = 1;
					cust_code = PublicConstants.custCode;
					password = PublicConstants.custPassword;
				}else{
					index = 0;
					cust_code = PublicConstants.firstCustCode;
					password = PublicConstants.firstCustPassword;
				}
				Map<String,Object> submit = new HashMap<String,Object>();
				submit.put("cust_code", cust_code);
				String postData = JSONObject.toJSONString(submit);
				String tokenStr = PublicFunctions.postHttpRequest(url+"/getToken", postData);
				JSONObject  jasonObject ;
				try{
					jasonObject = JSONObject.parseObject(tokenStr);
				} catch (Exception e){
					PublicConstants.tasklog.info("状态获取解析token响应异常 tokenStr= "+URLDecoder.decode(tokenStr,"UTF-8"),e);
					continue;
				}				Map map = (Map)jasonObject;
				String token_id = (String) map.get("token_id");
				String token = (String) map.get("token");
				String sign = getSign(token+password);
				submit.clear();
				submit.put("cust_code", cust_code);
				submit.put("token_id", token_id);
				submit.put("sign", sign);
				postData =  JSONObject.toJSONString(submit);
				PublicConstants.tasklog.info("cust_code = "+cust_code+" get report......");
				String respStr = PublicFunctions.postHttpRequest(url+"/getReport", postData);
				List<ReportVo> reportList;
				try{
					reportList = JSONArray.parseArray(respStr, ReportVo.class);
				} catch (Exception e){
					PublicConstants.tasklog.info("上行获取解析响应异常 respStr= "+URLDecoder.decode(respStr,"UTF-8"),e);
					continue;
				}
				if(reportList.size()==0){
					continue;
				}
				PublicConstants.tasklog.info("cust_code = "+cust_code+" get report = "+respStr);
				String repTimeStr = sdf.format(new Date());
				for(ReportVo rvo : reportList){
					int rptStatus = 1;
					if(rvo.getReport_status().equals("SUCCESS")){
						rptStatus=0;
					}
					
					EfdSmrpt efd = new EfdSmrpt();
					efd.setMessageid(rvo.getMsgid());
					efd.setRpt_status(rptStatus);
					efd.setRpt_time(repTimeStr);
					if(rvo.getReport()==null || "".equals(rvo.getReport())){
						efd.setRpt_code("FAIL");
					}else{
						efd.setRpt_code(rvo.getReport());

					}
					efd.setSm_recvphone(rvo.getMobile());
					UpdateEfdSmrptStatusThread.getInstance().addEfdSmrptToQueue(efd);
				}
				
			} catch (IOException e) {
				PublicConstants.tasklog.info("获取状态报告异常 ",e);
			} catch (InterruptedException e) {
				PublicConstants.tasklog.info("获取状态报告异常 ",e);
			} catch (Exception e) {
				PublicConstants.tasklog.info("获取状态报告异常 ",e);
			}
			

		}
	}
	
	private String getSign(String str) {
		return MD5.getMD5(str.getBytes());
	}
	public void stopQueryReport() {
		this.state = false;
		this.interrupt();
	}

}
