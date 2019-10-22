package com.ceshizhuanyong.aopWebLog;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Aspect
@Component
@Slf4j
public class WebLogAspect implements Ordered{

    @Pointcut("execution(public * com.ceshizhuanyong.controller.*.*(..))")
    public void webLog(){}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint){
        //接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //记录请求内容
        log.info("URL:"+request.getRequestURL().toString());
        log.info("HTTP_METHOD:"+request.getMethod());
        log.info("IP:"+request.getRemoteAddr());
        Enumeration<String> enu = request.getParameterNames();
        while (enu.hasMoreElements()){
            String name = enu.nextElement();
            log.info("name:"+name+",value:"+request.getParameter(name));
        }
    }

    @AfterReturning(returning = "ret",pointcut = "webLog()")
    public void doAfterReturning(Object ret){
        //请求处理完，返回内容
        log.info("RESPONE:"+ret);
    }

    //自定义aop后，order 的值越小，说明越先被执行
    @Override
    public int getOrder() {
        // TODO Auto-generated method stub
        return 1;
    }
}
