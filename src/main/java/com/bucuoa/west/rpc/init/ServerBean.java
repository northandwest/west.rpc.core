package com.bucuoa.west.rpc.init;

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

import com.bucuoa.west.rpc.tags.Server;


public class ServerBean <T> extends Server implements InitializingBean, DisposableBean, ApplicationContextAware,  BeanNameAware{
	static Logger logger = LoggerFactory.getLogger(ServerBean.class);

	@Override
	public void setBeanName(String name) {
		logger.debug("setBeanName=>{}", name);
	}


	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
//		RPCServerStub serverstub = new RPCServerStub();
		System.out.println("hello init server bean event");
//		StartServerEvent event = new StartServerEvent(context);
//		context.publishEvent(event);
	}

	@Override
	public void destroy() throws Exception {
		
	}



	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

}
