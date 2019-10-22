package com.ceshizhuanyong.druidFilters;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.*;

import javax.sql.DataSource;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableConfigurationProperties(DruidProperties.class)
public class DruidDataSourceConfig {
	private static final int TX_METHOD_TIMEOUT = 5;
	private static final String AOP_POINTCUT_EXPRESSION = "execution (* com.ceshizhuanyong.service.*.*(..))";

	@Autowired
	private DruidProperties datasource;

	@Bean
	public DataSource dataSource(){
		DruidDataSource druidDataSource = new DruidDataSource();
		//druidDataSource.setDbType(datasource.getType());
		druidDataSource.setUrl(datasource.getUrl());
		druidDataSource.setUsername(datasource.getUsername());
		druidDataSource.setPassword(datasource.getPassword());
		druidDataSource.setDriverClassName(datasource.getDriverClassName());
		druidDataSource.setInitialSize(datasource.getInitialSize());
		druidDataSource.setMaxActive(datasource.getMaxActive());
		druidDataSource.setMinIdle(datasource.getMinIdle());
		druidDataSource.setMaxWait(datasource.getMaxWait());
		druidDataSource.setTimeBetweenEvictionRunsMillis(datasource.getTimeBetweenEvictionRunsMillis());
		druidDataSource.setMinEvictableIdleTimeMillis(datasource.getMinEvictableIdleTimeMillis());
		druidDataSource.setValidationQuery(datasource.getValidationQuery());
		druidDataSource.setTestWhileIdle(datasource.isTestWhileIdle());
		druidDataSource.setTestOnBorrow(datasource.isTestOnBorrow());
		druidDataSource.setTestOnReturn(datasource.isTestOnReturn());
		druidDataSource.setPoolPreparedStatements(datasource.isPoolPreparedStatements());
		druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(datasource.getMaxPoolPreparedStatementPerConnectionSize());
        Properties properties = new Properties();
        properties.put("druid.stat.slowSqlMillis", "3000");
        properties.put("druid.stat.logSlowSql", "true");
        properties.put("druid.stat.mergeSql", "true");
		druidDataSource.setConnectProperties(properties);
		druidDataSource.setUseGlobalDataSourceStat(datasource.isUseGlobalDataSourceStat());
		try {
			druidDataSource.setFilters(datasource.getFilters());
		} catch (SQLException e) {
			System.err.println("druid configuration initialization filter: "+ e);
		}
		return druidDataSource;
	}

	@Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:com/ceshizhuanyong/mapper/*.xml"));
		URL url = DruidDataSourceConfig.class.getClassLoader().getResource("mybatis-config.xml");
        sqlSessionFactoryBean.setConfigLocation(new UrlResource(url));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException {
        return new DataSourceTransactionManager(dataSource());
    }

	@Bean
	public TransactionInterceptor txAdvice() throws SQLException {
		NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
		/*只读事务，不做更新操作*/
		RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
		readOnlyTx.setReadOnly(true);
		readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED );

		/*当前存在事务就使用当前事务，当前不存在事务就创建一个新的事务*/
		RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
		requiredTx.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
		requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		requiredTx.setTimeout(TX_METHOD_TIMEOUT);

		Map<String, TransactionAttribute> txMap = new HashMap<>();
		txMap.put("add*", requiredTx);
		txMap.put("save*", requiredTx);
		txMap.put("insert*", requiredTx);
		txMap.put("update*", requiredTx);
		txMap.put("delete*", requiredTx);

		//对查询数据开启只读
		txMap.put("get*", readOnlyTx);
		txMap.put("query*", readOnlyTx);

		source.setNameMap( txMap );
		TransactionInterceptor txAdvice = new TransactionInterceptor();
		txAdvice.setTransactionManager(transactionManager());
		txAdvice.setTransactionAttributeSource(source);
		return txAdvice;

	}

	@Bean
	public Advisor txAdviceAdvisor() throws SQLException {
		//配置切点
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(AOP_POINTCUT_EXPRESSION);

		//将通知织入切点
		DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
		defaultPointcutAdvisor.setAdvice(txAdvice());
		defaultPointcutAdvisor.setPointcut(pointcut);
		//defaultPointcutAdvisor.setOrder(1);
		return defaultPointcutAdvisor;
	}
}
