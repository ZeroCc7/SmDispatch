package com.dispatchtask.util;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class HttpUtil {
	
	public static void requestData(String url,String data,String requestMethod) throws IOException {  
		
		URL postUrl = new URL(url);  
	       HttpURLConnection connection = (HttpURLConnection) postUrl  
	               .openConnection();  
	       connection.setDoOutput(true);  
	       connection.setDoInput(true);  
	       connection.setRequestMethod(requestMethod);  
	       connection.setUseCaches(false);  
	       connection.setInstanceFollowRedirects(true);  
	       connection.setRequestProperty("Content-Type",  
	               "text/xml");  
	       connection.setConnectTimeout(10000);//连接超时 单位毫秒
	       connection.setReadTimeout(2000);//读取超时 单位毫秒
	       /**//* 
	        * 与readContentFromPost()最大的不同，设置了块大小为5字节 
	        */  
	       //connection.setChunkedStreamingMode(5);  
	       connection.connect();
	       DataOutputStream out = new DataOutputStream(connection  
	               .getOutputStream());  
	       String content = "postData=" + URLEncoder.encode(data, "utf-8");  
	       out.writeBytes(content);   
	  
	       out.flush();  
	       out.close(); // 到此时服务器已经收到了完整的http request了，而在readContentFromPost()函数里，要等到下一句服务器才能收到http请求。  
	       BufferedReader reader = new BufferedReader(new InputStreamReader(  
	               connection.getInputStream()));  
	         
	       out.flush();  
	       out.close(); // flush and close  
	       String line;  
	       System.out.println("=============================");  
	       System.out.println("Contents of post request");  
	       System.out.println("=============================");  
	       while ((line = reader.readLine()) != null) {  
	           System.out.println(line);  
	       }  
	       System.out.println("=============================");  
	       System.out.println("Contents of post request ends");  
	       System.out.println("=============================");  
	       reader.close();  
	       connection.disconnect();  
		
	}
	
	
	
	
//	public static String postData(String url,String data) throws IOException {  
//		
//		URL postUrl = new URL(url);  
//	       HttpURLConnection connection = (HttpURLConnection) postUrl  
//	               .openConnection();  
//	       connection.setDoOutput(true);  
//	       connection.setDoInput(true);  
//	       connection.setRequestMethod("POST");  
//	       connection.setUseCaches(false);  
//	       connection.setInstanceFollowRedirects(true);  
//	      
//	       connection.setConnectTimeout(10000);//连接超时 单位毫秒
//	       connection.setReadTimeout(2000);//读取超时 单位毫秒
//	       /**//* 
//	        * 与readContentFromPost()最大的不同，设置了块大小为5字节 
//	        */  
//	       //connection.setChunkedStreamingMode(5);  
//	       //connection.connect();
//	       DataOutputStream out = new DataOutputStream(connection  
//	               .getOutputStream());  
//	       String content =  data;  
//	       Logger.info("postData:"+content);
//	       //System.out.println(content);
//	       out.write(content.getBytes("utf-8"));   
//	  
//	       out.flush();  
//	       out.close(); // 到此时服务器已经收到了完整的http request了，而在readContentFromPost()函数里，要等到下一句服务器才能收到http请求。  
//	      
//	       BufferedReader in = new BufferedReader(new InputStreamReader(
//	    		   connection.getInputStream(), "utf-8"));
//			String resXml = "", s = "";
//			while ((s = in.readLine()) != null)
//				resXml = resXml + s + "\r\n";
//			in.close();
//			System.out.println("接收数据=" + resXml);
//			return resXml;  
//	   
//	       
//	       //connection.disconnect();  
//		
//	}
	
//	public static int registerSmsLabel(String appId,String spId,String pwd,String ims,String key,String sign,String url){
//		ReqRegisterSmsLabel reqRegisterSmsLabel = new ReqRegisterSmsLabel();
//		Head head = new Head();
//		head.setAppid(appId);
//		
//		head.setMethodName("SmsSign");
//		head.setPasswd(pwd);
//		head.setSpid(spId);
//		String timestamp ="";
//		try {
//			 timestamp =SystemDateUtil.FormatDateString(new Date(),"yyyyMMddhhmmss");
//			head.setTimestamp(timestamp);
//			
//			head.setAuthenticator(pwd);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Body body = new Body() ;
//		body.setIms(ims);
//		body.setKey(key);
//		body.setType("1");
//		body.setSign(sign);
//		
//		reqRegisterSmsLabel.setHead(head);
//		reqRegisterSmsLabel.setBody(body);
//		//System.out.println(CommonUtil.getXmlFromObject(reqRegisterSmsLabel));1
//		String data ="postData=" +CommonUtil.getXmlFromObject(reqRegisterSmsLabel);
//		try {
//			String resultInfo=HttpUtil.postData(url, data);
//			System.out.println("resultInfo:"+resultInfo);
//			RspRegisterSmsLabel rsp =(RspRegisterSmsLabel)CommonUtil.getObjectFromXml(RspRegisterSmsLabel.class, resultInfo);
//			return Integer.parseInt(rsp.getHead().getResult());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return -1;
//		}
//	}
	
	public static boolean sendFile(InputStream in, String urlStr,
			String fileName, String filePath) {

		try {
			// in = new FileInputStream(file);
			String BOUNDARY = "---------7d4a6d158c9"; // 定义数据分隔线
			// String fileName = "3.jpg";
			// String urlStr = "http://172.20.0.225:8088/fileserver/file";
			URL url = new URL(urlStr);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);
			conn.setRequestProperty("flag", "upload");
			conn.setRequestProperty("filePath", filePath);
			conn.connect();
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线
			StringBuilder sb = new StringBuilder();
			sb.append("--");
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"file" + 1
					+ "\";filename=\"" + fileName + "\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");
			byte[] data = sb.toString().getBytes();
			out.write(data);

			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
			in.close();
			out.write(end_data);

			out.flush();
			out.close();
			BufferedReader in1 = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf-8"));
			String resXml = "", s = "";
			while ((s = in1.readLine()) != null)
				resXml = s;
			in1.close();
			conn.disconnect();

			if (resXml.equals("ok")) {
				return true;
			} else {
				System.out.println("resXml:"+resXml);
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public static boolean delFile(String urlStr,
			String filePath) {

		try {
			// in = new FileInputStream(file);
			String BOUNDARY = "---------7d4a6d158c9"; // 定义数据分隔线
			// String fileName = "3.jpg";
			// String urlStr = "http://172.20.0.225:8088/fileserver/file";
			URL url = new URL(urlStr);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("flag", "del");
			//conn.setRequestProperty("fileName", fileName);
			conn.setRequestProperty("filePath", filePath);
			conn.connect();

			BufferedReader in1 = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf-8"));
			String resXml = "", s = "";
			while ((s = in1.readLine()) != null)
				resXml = s;
			in1.close();
			conn.disconnect();

			if (resXml.equals("ok")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public static InputStream downloadFile(String urlStr, 
			String filePath) {
		/*InputStream inputStream = null;
		try {
			// in = new FileInputStream(file);
			String BOUNDARY = "---------7d4a6d158c9"; // 定义数据分隔线
			// String fileName = "3.jpg";
			// String urlStr = "http://172.20.0.225:8088/fileserver/file";
			URL url = new URL(urlStr);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("flag", "download");
			//conn.setRequestProperty("fileName", fileName);
			conn.setRequestProperty("filePath", filePath);
			conn.connect();
			//inputStream =  conn.getInputStream();
			
			InputStream in =new FileInputStream(filePath);
			
			//Thread.sleep(5000);
			return in;
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}*/
		try {
			// in = new FileInputStream(file);
			/*String BOUNDARY = "---------7d4a6d158c9"; // 定义数据分隔线
			// String fileName = "3.jpg";
			// String urlStr = "http://172.20.0.225:8088/fileserver/file";
			URL url = new URL(urlStr);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("flag", "download");
			//conn.setRequestProperty("fileName", fileName);
			conn.setRequestProperty("filePath", filePath);
			conn.connect();*/
			return new FileInputStream(filePath);
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}

	}
	
	
	/**
	 * 腾讯云同步数据黑名单数据API
	 * @param cr
	 * @param blacklist
	 * @return
	 */
//	public static Map<String,String> callingInterface(CustInterfaceRelation cr,
//			Blacklist blacklist) {
//		Map<String,String> result = new HashMap<String,String>();
//		JSONObject crJson = JSONObject.parseObject(cr.getParams());
//		String appkey = crJson.getString("appkey");
//		Integer sdkappid = Integer.valueOf(crJson.getString("sdkappid"));
//		String mobile = blacklist.getMobile();
//		String url = cr.getUrl();
//		JSONObject json =  new JSONObject();
//		json.put("sign", blacklist.getSmslabel());
//		json.put("mobile", mobile);
//		json.put("remarks", "黑名单添加");
//		json.put("sdkappid", sdkappid);
//		int reason = 0;
//		if(blacklist.getType() == 3){//投诉黑名单
//			reason = 2;
//		}
//		if(blacklist.getType() == 4){//退订黑名单
//			reason = 1;
//		}
//		json.put("reason", reason);
//		result.put("parameter", json.toJSONString());
//		InputStream is =IOUtils.toInputStream(appkey+mobile);
//		String sig= "";
//			try {
//				sig=UMD5.md5sums(is);
//			} catch (NoSuchAlgorithmException e1) {
//				e1.printStackTrace();
//				return result;
//			} catch (FileNotFoundException e1) {
//				e1.printStackTrace();
//				return result;
//			}
//		json.put("sig", sig);
//		//返回请求参数
//		result.put("parameter", json.toJSONString());
//		try {
//			String str = HttpUtil.postData(url, json.toJSONString());
//			result.put("result", str);
//			System.out.println(str);
//			return result;
//		} catch (IOException e) {
//			e.printStackTrace();
//			return result;
//		}
//	}
//	
//	public static void main(String[] args) {
//		JSONObject json =  new JSONObject();
//		json.put("sign", "test1");
//		json.put("mobile", "13926290129");
//		json.put("remarks", "填具体原因");
//		json.put("sdkappid", 1400012165);
//		json.put("reason", 2);
//		//System.out.println(json.toString());
//		//System.out.println(json.toJSONString());
//		//sig: md5sum(string(appkey) + string(mobile)))
//		InputStream is =IOUtils.toInputStream("23e584f754d751ab9d016aad1cd0f6ad"+"13926290129");
//		String sig= "";
//	
//			
//			try {
//				sig=UMD5.md5sums(is);
//			} catch (NoSuchAlgorithmException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (FileNotFoundException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} 
//		
//		json.put("sig", sig);
//		try {
//			String str = HttpUtil.postData("https://test.tim.qq.com/v3/smsblacklist/add?sdkappid=1400012165", json.toJSONString());
//			System.out.println(str);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//
//
//
//	
//	
}
