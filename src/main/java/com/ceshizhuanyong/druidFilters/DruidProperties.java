package com.ceshizhuanyong.druidFilters;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
//@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix="druid.jdbc")
public class DruidProperties {

	private String type;

	private String url;
	
	private String username;
	
	private String password;

	private String driverClassName;

	private int initialSize;

	private int maxActive;

	private int minIdle;

	private int maxWait;

	private int timeBetweenEvictionRunsMillis;
	
	private int minEvictableIdleTimeMillis;
	
	private String validationQuery;
	
	private boolean testWhileIdle;
	
	private boolean testOnBorrow;
	
	private boolean testOnReturn;
	
	private boolean poolPreparedStatements;
	
	private int maxPoolPreparedStatementPerConnectionSize;

	private String filters;

	//private Map<String,Object> connectionProperties;

	private boolean useGlobalDataSourceStat;
}
