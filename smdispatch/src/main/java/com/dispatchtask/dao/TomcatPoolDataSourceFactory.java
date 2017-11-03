package com.dispatchtask.dao;

import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import javax.sql.DataSource;

import java.util.Properties;

public class TomcatPoolDataSourceFactory implements DataSourceFactory {
	 private  Properties props;
	  @Override
	  public void setProperties(Properties props) {
	    this.props = props;
	  }

	  @Override
	  public DataSource getDataSource() {
		  PoolConfiguration config = new PoolProperties();
		  config.setDbProperties(props);
		  config.setDriverClassName(this.props.getProperty("driverClassName"));
		  config.setUrl(this.props.getProperty("url"));
		  config.setUsername(this.props.getProperty("username"));
		  config.setPassword(this.props.getProperty("password"));
		  config.setTestWhileIdle(Boolean.valueOf(this.props.getProperty("testWhileIdle","true")));
		  config.setTestOnBorrow(Boolean.valueOf(this.props.getProperty("testOnBorrow","true")));
		  config.setTestOnReturn(Boolean.valueOf(this.props.getProperty("testOnReturn","true")));
		  config.setValidationQuery(this.props.getProperty("validationQuery"));
		  config.setInitSQL(this.props.getProperty("validationQuery"));
		  config.setValidationInterval(Long.valueOf(this.props.getProperty("validationInterval","30000")));
		  config.setTimeBetweenEvictionRunsMillis(Integer.valueOf(this.props.getProperty("timeBetweenEvictionRunsMillis","5000")));
		  config.setMaxActive(Integer.valueOf(this.props.getProperty("maxActive","30")));
		  config.setMaxIdle(Integer.valueOf(this.props.getProperty("maxIdle","10")));
		  config.setMinIdle(Integer.valueOf(this.props.getProperty("minIdle","5")));
		  config.setMaxWait(Integer.valueOf(this.props.getProperty("maxWait","60000")));
		  config.setInitialSize(Integer.valueOf(this.props.getProperty("initialSize","10")));
		  config.setMinEvictableIdleTimeMillis(Integer.valueOf(this.props.getProperty("minEvictableIdleTimeMillis","60000")));
	      org.apache.tomcat.jdbc.pool.DataSource dds =  new org.apache.tomcat.jdbc.pool.DataSource(config);

	    //其他配置可以根据MyBatis主配置文件进行配置
//	    try {
//	      dds.
//	    } catch (SQLException e) {
//	      e.printStackTrace();
//	    }
	    return dds;
	  }
}
