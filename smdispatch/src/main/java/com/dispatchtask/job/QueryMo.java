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
import com.dispatchtask.util.MobilePrefix;
import com.dispatchtask.util.PublicConstants;
import com.dispatchtask.util.PublicFunctions;
import com.dispatchtask.vo.EfdSmup;
import com.dispatchtask.vo.MoVo;

public class QueryMo extends Thread {
	private String url;
	private volatile boolean state = true;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected DataAccessDao dataAccessDao = new DataAccessDaoImpl();
	private int index;
	public QueryMo(){
		this.url = "http://"+PublicConstants.baseUrl;
	
	}
	@SuppressWarnings("rawtypes")
	public void run() {
		
		while (state) {
			
			try {
				Thread.sleep(PublicConstants.moSleepTime);
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
//				System.out.println("MO tokenStr = "+tokenStr);
				JSONObject  jasonObject ;
				try{
					jasonObject = JSONObject.parseObject(tokenStr);
				} catch (Exception e){
					PublicConstants.tasklog.info("上行获取解析token响应异常 tokenStr= "+URLDecoder.decode(tokenStr,"UTF-8"),e);
					continue;
				}
				Map map = (Map)jasonObject;
				String token_id = (String) map.get("token_id");
				String token = (String) map.get("token");
				String sign = getSign(token+password);
				submit.clear();
				submit.put("cust_code", cust_code);
				submit.put("token_id", token_id);
				submit.put("sign", sign);
				postData =  JSONObject.toJSONString(submit);
				PublicConstants.tasklog.info("cust_code = "+cust_code+" get mo......");
				String respStr = PublicFunctions.postHttpRequest(url+"/getMo", postData);
//				respStr = "[{\"recv_time\":\"2017-09-25 16:53:14.0\",\"msg_content\":\"事实上\",\"sp_code\":\"1065502523584123\",\"src_mobile\":\"13174537097\",\"msg_id\":\"150632959391913174537097\"}]";
				List<MoVo> moList ;
				try{
					moList = JSONArray.parseArray(respStr, MoVo.class);
				} catch (Exception e){
					PublicConstants.tasklog.info("上行获取解析响应异常 respStr= "+URLDecoder.decode(respStr,"UTF-8"),e);
					continue;
				}
				if(moList.size()==0){
					continue;
				}
				PublicConstants.tasklog.info("cust_code = "+cust_code+" get mo = "+respStr);
				for(MoVo vo : moList){
					EfdSmup efdUp = new EfdSmup();
					efdUp.setFullcode(vo.getSp_code());
					efdUp.setSm_servicecode("");
					efdUp.setUpphone(vo.getSrc_mobile());
					efdUp.setUpcontent(vo.getMsg_content());
					String recvT = vo.getRecv_time();
					if(vo.getRecv_time().contains(".")){
						recvT = recvT.substring(0, recvT.indexOf("."));
//						recvT.replace(".0", "");
					}
					efdUp.setUptime(recvT);
					efdUp.setIsmgtype(MobilePrefix.getInstance().getProvider(vo.getSrc_mobile()));
					efdUp.setGatewayno(PublicFunctions.getMsgId()+"");
					SaveMoThread.getInstance().addEfdSmupToQueue(efdUp);
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
