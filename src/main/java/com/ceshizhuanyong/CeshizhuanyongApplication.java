package com.ceshizhuanyong;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan(basePackages = "com.ceshizhuanyong.mapper")
//<aop:aspectj-autoproxy proxy-target-class="true" />
@EnableAspectJAutoProxy(exposeProxy =true,proxyTargetClass = false)//proxyTargetClass表示为true表示使用cglib代理
@EnableTransactionManagement //相当于之前在xml中配置的<tx:annotation-driven />注解驱动。<!-- 注解方式配置事物 -->
//<tx:annotation-driven transaction-manager="transactionManager" order="2"/>这样就实现了我们自己写的aop在事务介入之前就执行了！
public class CeshizhuanyongApplication {

	public static void main(String[] args) {
		SpringApplication.run(CeshizhuanyongApplication.class, args);
	}

}

