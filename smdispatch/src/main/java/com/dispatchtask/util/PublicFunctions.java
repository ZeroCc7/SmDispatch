package com.dispatchtask.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.dispatchtask.vo.Smdown;

public class PublicFunctions {

	
	/**
	 * 判断list 是否为空
	 * @param list
	 * @return
	 */
	public static <T> boolean isBlankList(List<T> list) {
		if (list == null || list.size() == 0)
			return true;
		return false;
	}
	
	/**
	 * 拆分list
	 * @param list
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List[] splitList(List list, int pageSize) {   
	    int total = list.size();   
	    int pageCount = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;   
	    List[] result = new List[pageCount];   
	    for(int i = 0; i < pageCount; i++) {   
	        int start = i * pageSize;   
	        int end = start + pageSize > total ? total : start + pageSize;   
	        List subList = list.subList(start, end);   
	        result[i] = subList;   
	    }   
	    return result;   
	}

	
	// msgId 产生方法.
	private static int sequence = 0;

	public synchronized static long getMsgId() {
		sequence++;
		if (sequence >= 65536) {
			sequence = 1;
		}
		Calendar now = Calendar.getInstance();
		int mon = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int min = now.get(Calendar.MINUTE);
		int sec = now.get(Calendar.SECOND);
		int ismgId = 59112;
		long msgId = mon;
		msgId = (msgId << 5) | day;
		msgId = (msgId << 5) | hour;
		msgId = (msgId << 6) | min;
		msgId = (msgId << 6) | sec;
		msgId = (msgId << 18) | ismgId;
		msgId = (msgId << 16) | sequence;
		return msgId;
	}

	
	public static String postHttpRequest(String url, String postData)
			throws IOException {
		String sessionId = UUID.randomUUID().toString().replace("-", "");

		URL myurl = new URL(url);
		URLConnection urlc = myurl.openConnection();
		urlc.setReadTimeout(1000 * 300);
		urlc.setDoOutput(true);
		urlc.setDoInput(true);
		urlc.setAllowUserInteraction(false);

		DataOutputStream server = new DataOutputStream(urlc.getOutputStream());
		
//		PublicConstants.tasklog.info("SessionID:"+sessionId+", 发送数据=" + postData);

		server.write(postData.getBytes("utf-8"));
		server.close();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				urlc.getInputStream(), "utf-8"));
		String resXml = "", s = "";
		while ((s = in.readLine()) != null)
			resXml = resXml + s;
		in.close();
//		PublicConstants.tasklog.info("SessionID:"+sessionId+",接收数据=" + resXml);
		return resXml;
	}
}
