package com.bucuoa.west.rpc.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.bucuoa.west.rpc.bean.Application;


public class ApplicationBean <T> extends Application implements InitializingBean, DisposableBean, ApplicationContextAware, BeanNameAware{
	static Logger logger = LoggerFactory.getLogger(ApplicationBean.class);

	@Override
	public void setBeanName(String name) {
		logger.info("beanname:{}",name);
	}


	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		
	}

	@Override
	public void destroy() throws Exception {
		
	}



	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

}
