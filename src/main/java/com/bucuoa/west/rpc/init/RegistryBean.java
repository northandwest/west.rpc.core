package com.bucuoa.west.rpc.init;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bucuoa.west.rpc.tags.Registry;


public class RegistryBean <T> extends Registry implements InitializingBean, DisposableBean, ApplicationContextAware,BeanNameAware{

	private ApplicationContext context;
	
	@Override
	public void setBeanName(String arg0) {
		
	}


	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	@Override
	public void destroy() throws Exception {
		
	}



	@Override
	public void afterPropertiesSet() throws Exception {
		
		
	}

}
