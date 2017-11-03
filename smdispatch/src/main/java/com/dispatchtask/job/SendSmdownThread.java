package com.dispatchtask.job;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dispatchtask.dao.DataAccessDao;
import com.dispatchtask.dao.DataAccessDaoImpl;
import com.dispatchtask.util.PublicConstants;
import com.dispatchtask.util.PublicFunctions;
import com.dispatchtask.util.UMD5;
import com.dispatchtask.vo.Customer;
import com.dispatchtask.vo.EfdSmrpt;
import com.dispatchtask.vo.ResultVo;
import com.dispatchtask.vo.Smdown;
import com.dispatchtask.vo.SubmitRepVo;
import com.dispatchtask.vo.SysParam;

public class SendSmdownThread implements Runnable {
	private String cost_code = "";
	private String passWord = "";
	private String smsSvcUrl=null;
	private DataAccessDao dao = new DataAccessDaoImpl();
	private List<Smdown> smList;
	private boolean first;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public SendSmdownThread(List<Smdown> smList,boolean first){
		this.smList = smList;
		this.first = first;
	}
	public void run() {
		SysParam sms_svc_urlParam = null;
		try {
			sms_svc_urlParam = dao.getSysParamByName("sms_svc_url");
			smsSvcUrl = sms_svc_urlParam.getParam_value();
		} catch (Exception e) {
			PublicConstants.tasklog.info("获取sms_svc_url系统参数异常",e);
			return;
		}
		int cust_id =PublicConstants.custId;
		if(first){
			cust_id =PublicConstants.firstSmsCustId;
		}
		try {
			Customer customer = dao.getCusomerById(cust_id);
			if(customer==null || customer.getStatus().equals("0")){
				PublicConstants.tasklog.info("custome 不存在或停用 cust_id = "+cust_id);
				return;
			}
			cost_code = customer.getCust_code();
			passWord = customer.getPasswd();
			if(first){
				if(!PublicConstants.firstCustCode.equals(cost_code)){
					PublicConstants.firstCustCode = cost_code;
				}
				if(!PublicConstants.firstCustPassword.equals(passWord)){
					PublicConstants.firstCustPassword = passWord;
				}
			}else{
				if(!PublicConstants.custCode.equals(cost_code)){
					PublicConstants.custCode = cost_code;
				}
				if(!PublicConstants.custPassword.equals(passWord)){
					PublicConstants.custPassword = passWord;
				}
			}
		} catch (SQLException e) {
			PublicConstants.tasklog.info("custome 获取异常 ",e);
		}
		for(Smdown sm : smList){
			//发送
			try {
				long starTime =System.currentTimeMillis();
				String desc = sm.getSm_serialphones();
				PublicConstants.tasklog.info("downId:"+sm.getDownid()+",开始提交...");
				SendSms2Http(sm,desc);
				PublicConstants.tasklog.info("downId:"+sm.getDownid()+",发送完成...耗时:"+(System.currentTimeMillis()- starTime)+" ms");
			} catch (Exception e) {
				PublicConstants.tasklog.info("发送异常",e);
				continue;
			} 
			try {
				dao.updateTaskStatusToEnd(sm);
			} catch (SQLException e) {
				PublicConstants.tasklog.info("更改待发送状态失败.",e);
			}
		}
	}
	
	
	/**
	 * 发送至接口。
	 * @param starTime
	 * @param sessionId
	 * @param desc
	 * @throws UnsupportedEncodingException 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void SendSms2Http(Smdown sm,String desc) throws UnsupportedEncodingException{
		long starTime = System.currentTimeMillis();
		String sendTimeStr = sdf.format(new Date());
		String uid = desc.substring(0,desc.indexOf(":"));
		desc = desc.substring(desc.indexOf(":")+1);
		String[] mobileS = desc.split(",");
		String resultMsg = null;
		PublicConstants.tasklog.info("downId:"+sm.getDownid()+",提交接口："+ desc);
		String postData = null;
		try {
			String sign=UMD5.sign(sm.getSm_content(), passWord, "utf-8");
			Map<String,Object> submit = new HashMap<String,Object>();
			submit.put("cust_code", cost_code);
			submit.put("sp_code", sm.getSm_servicecode() == null ? "" : sm.getSm_servicecode());
			submit.put("content", sm.getSm_content());
			submit.put("destMobiles", desc);
			submit.put("sign", sign);
			submit.put("uid", uid);
			submit.put("need_report", "YES");
			postData = JSONObject.toJSONString(submit);
			resultMsg = PublicFunctions.postHttpRequest(smsSvcUrl+"/sendSms", postData);
		} catch (IOException e) {
			PublicConstants.tasklog.info("downId:"+sm.getDownid()+",提交接口异常："+ postData,e);
		}
		PublicConstants.tasklog.info("downId:"+sm.getDownid()+",提交接口响应时间："+ (System.currentTimeMillis()- starTime)+" ms");
		SubmitRepVo submitRepVo = JSON.parseObject(resultMsg, SubmitRepVo.class);
		String sendRepTimeStr = sdf.format(new Date());
		int excessFlowNum = 0;
		String excessMobile = "";
		
		
		if (submitRepVo.getStatus().equals("success")) {
			PublicConstants.tasklog.info("downId:"+sm.getDownid()+",这批次号码发送成功...resultMsg="+resultMsg);
			List<ResultVo> resultList = submitRepVo.getResult();
			for(ResultVo resultVo : resultList){
				long msgId = PublicFunctions.getMsgId();
				if (resultVo.getCode().equals("0")) {
					EfdSmrpt efdSmrpt = new EfdSmrpt(sm);
					efdSmrpt.setSm_recvphone(resultVo.getMobile());
					efdSmrpt.setSm_sendtime(sendTimeStr);
					efdSmrpt.setGatewayno(msgId);
					efdSmrpt.setMessageid(resultVo.getMsgid());
					efdSmrpt.setSendtime(sendTimeStr);
					efdSmrpt.setRes_status(0);
					efdSmrpt.setRes_time(sendRepTimeStr);
					efdSmrpt.setRpt_status(2);
					efdSmrpt.setRpt_time(sendRepTimeStr);
					efdSmrpt.setRpt_code("0");
					efdSmrpt.setPknumber(0);
					efdSmrpt.setPktotal(resultVo.getChargeNum());
					SaveMobileStatusThread.getInstance().addSmsTaskMobileToQueue(efdSmrpt);
				}else if (resultVo.getCode().equals("8")) {
					//超流操作
					excessFlowNum++;
					excessMobile = excessMobile + "," + resultVo.getMobile();
				} else {
					EfdSmrpt efdSmrpt = new EfdSmrpt(sm);
					efdSmrpt.setSm_recvphone(resultVo.getMobile());
					efdSmrpt.setSm_sendtime(sendTimeStr);
					efdSmrpt.setGatewayno(msgId);
					efdSmrpt.setMessageid(resultVo.getMsgid());
					efdSmrpt.setSendtime(sendTimeStr);
					efdSmrpt.setRes_status(1);
					efdSmrpt.setRes_time(sendRepTimeStr);
					efdSmrpt.setRpt_status(1);
					efdSmrpt.setRpt_time(sendRepTimeStr);
					efdSmrpt.setRpt_code(resultVo.getCode());
					efdSmrpt.setPknumber(0);
					efdSmrpt.setPktotal(resultVo.getChargeNum());
					SaveMobileStatusThread.getInstance().addSmsTaskMobileToQueue(efdSmrpt);
				}
			}
			if(excessFlowNum>0){
				//超流号码再次提交
				String desc2= uid+":"+excessMobile.substring(1);
				SendSms2Http(sm, desc2);
				PublicConstants.tasklog.info("downId:"+sm.getDownid()+",重发超流数据..."+ (System.currentTimeMillis()- starTime)+" ms");
			}
		} else {
			PublicConstants.tasklog.info("downId:"+sm.getDownid()+",这批次号码发送失败 resultMsg="+URLDecoder.decode(resultMsg, "UTF-8"));
			long msgId = PublicFunctions.getMsgId();
			for (String mobile : mobileS) {
				EfdSmrpt efdSmrpt = new EfdSmrpt(sm);
				efdSmrpt.setSm_recvphone(mobile);
				efdSmrpt.setSm_sendtime(sendTimeStr);
				efdSmrpt.setGatewayno(msgId);
				efdSmrpt.setMessageid(msgId+"");
				efdSmrpt.setSendtime(sendTimeStr);
				efdSmrpt.setRes_status(1);
				efdSmrpt.setRes_time(sendRepTimeStr);
				efdSmrpt.setRpt_status(1);
				efdSmrpt.setRpt_time(sendRepTimeStr);
				efdSmrpt.setRpt_code(submitRepVo.getRespCode());
				efdSmrpt.setPknumber(0);
				efdSmrpt.setPktotal(0);
				SaveMobileStatusThread.getInstance().addSmsTaskMobileToQueue(efdSmrpt);
			}
		}
	}
}
