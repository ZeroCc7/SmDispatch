package com.dispatchtask.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.dispatchtask.dao.DataAccessDao;
import com.dispatchtask.dao.DataAccessDaoImpl;
import com.dispatchtask.vo.Customer;

/**
 * 公共全局常量类
 * @author cheney
 *
 */
public class PublicConstants {

	public static Logger tasklog;
	
    private static Properties prop1;
	private static DataAccessDao dao = new DataAccessDaoImpl();

	//用户业务主任务池参数
	public static int datCoreSize = 20;
	public static int datMaxSize = 50;
	public static int datQueueSize = 100000;
	
	public static int maxSendRate = 5000;

	public static String localSmsDir = "";
	
	public static int custId = 1;
	
	public static int firstSmsCustId = 100;

	public static String custCode = "";
	public static String custPassword = "";

	public static String firstCustCode = "";
	public static String firstCustPassword = "";

	public static String baseUrl = "127.0.0.1:8860";
    /**
     * 初始化系统变量，在smstask工程下的目录下的app.properties
     */
    public static void initPublicConstants() {
		tasklog = Logger.getLogger(PublicConstants.class);

    	Properties prop = new Properties();  
		try {
			if (System.getProperties().getProperty("CONFIG_FILE") != null) {
				FileInputStream fis = null;
				try {
					// 正式运营时，通过指定参数来指定配置文件的路径
					fis = new FileInputStream(System.getProperties().getProperty("CONFIG_FILE"));
					prop.load(fis);
				} catch (FileNotFoundException e) {
					tasklog.info("app.properties not foud... ",e);
				} catch (IOException e) {
					tasklog.info("read app.properties error...",e);
				}
			}else {
				// 本地运行时，读取工程里面的资源配置文件
				InputStream fis = Object.class.getResourceAsStream("/app.properties");
				if (fis == null) {
					tasklog.info("app.properties not foud...");
				}
				try {
					prop.load(fis);
				} catch (IOException e1) {
					tasklog.info("app.properties not foud... ",e1);
				}
				prop.load(fis);  
			}
			prop1 = prop;
			datCoreSize = Integer.parseInt(prop1.getProperty("app.datCoreSize", "10"));
			datMaxSize = Integer.parseInt(prop1.getProperty("app.datMaxSize", "50"));
			datQueueSize = Integer.parseInt(prop1.getProperty("app.datQueueSize", "100000"));
			custId = Integer.parseInt(prop1.getProperty("app.smscustid", "1"));
			firstSmsCustId = Integer.parseInt(prop1.getProperty("app.firstsmscustid", "100"));
			baseUrl = prop1.getProperty("app.baseUrl", "127.0.0.1:8860");
			Customer customer = null;
			Customer firstCustomer = null;
			try {
				customer = dao.getCusomerById(custId);
				firstCustomer = dao.getCusomerById(firstSmsCustId);
			} catch (SQLException e) {
				tasklog.info("获取客户参数异常",e);
				e.printStackTrace();
			}
			if(customer!=null){
				custCode = customer.getCust_code();
				custPassword = customer.getPasswd();
			}
			
			if(firstCustomer!=null){
				firstCustCode = firstCustomer.getCust_code();
				firstCustPassword = firstCustomer.getPasswd();
			}
		} catch (FileNotFoundException e) {
			tasklog.info("初始化系统变量出现异常！", e);
		} catch (IOException e) {
			tasklog.info("初始化系统变量出现异常！", e);
		}  
		tasklog.info("初始化系统变量成功！");

    }
    
}

