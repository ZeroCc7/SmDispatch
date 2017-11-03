package com.dispatchtask.dao;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.dispatchtask.util.PublicConstants;
import com.dispatchtask.util.PublicFunctions;


/** 
 * 根据mybatis.xml中配置的不同的environment创建对应的SqlSessionFactory 
 * @author zero 
 * @version 2014-3-27 
 */ 
public class MyBatisConnectionFactory {

	private static SqlSessionFactory smsSqlSessionFactory;
	private static SqlSessionFactory efdSqlSessionFactory;

//	static {
//        InputStream inputStream = null;  
//
//		try {
//
//			String resource = "com/dispatchtask/dao/sql/SqlMapConfig.xml";
//            inputStream = Resources.getResourceAsStream(resource);  
//            if (efdSqlSessionFactory == null) {
//				efdSqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream,"efddevelopment");
//			}
//			if (smsSqlSessionFactory == null) {
//				smsSqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream,"development");
//			}
//			
//		}
//
//		catch (FileNotFoundException fileNotFoundException) {
//			fileNotFoundException.printStackTrace();
//		}
//		catch (IOException iOException) {
//			iOException.printStackTrace();
//		}
//	}
	
	private static final String CONFIGURATION_PATH = "com/dispatchtask/dao/sql/SqlMapConfig.xml";  
	protected static final String SMSDB = "development";
	protected static final String EFDDB = "efddevelopment";

    private static final Map<String, SqlSessionFactory> SQLSESSIONFACTORYS   
        = new HashMap<String, SqlSessionFactory>();  
	/** 
     * 根据指定的DataSourceEnvironment获取对应的SqlSessionFactory 
     * @param environment 数据源environment 
     * @return SqlSessionFactory 
     */  
    public static SqlSessionFactory getSqlSessionFactory(String environment) {  
          
        SqlSessionFactory sqlSessionFactory = SQLSESSIONFACTORYS.get(environment);  
        if (sqlSessionFactory != null)  
            return sqlSessionFactory;  
        else {  
            InputStream inputStream = null;  
            try {  
                inputStream = Resources.getResourceAsStream(CONFIGURATION_PATH);  
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, environment);  
                System.out.println("Get "+environment+" SqlSessionFactory successfully.");  
            } catch (IOException e) {  
            	 System.out.println("Get "+environment+" SqlSessionFactory error.");  
				// PublicConstants.tasklog.error(e.getMessage(), e);  
            }  
//            finally {  
//                IOUtils.closeQuietly(inputStream);  
//            }  
            SQLSESSIONFACTORYS.put(environment, sqlSessionFactory);  
            return sqlSessionFactory;  
        }  
    }  

}