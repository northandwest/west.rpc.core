package com.bucuoa.west.rpc.spring.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import com.bucuoa.west.rpc.remoting.server.ProviderStubInvoker;
import com.bucuoa.west.rpc.remoting.server.RemoteServiceCenter;
//@Component
public class ServiceExportLisenter implements ApplicationListener<ServiceExportEvent> {
	static Logger logger = LoggerFactory.getLogger(ServiceExportLisenter.class);

	ProviderStubInvoker invoker;
	String beanName;
	
	@Override
	public void onApplicationEvent(ServiceExportEvent event) {
//		if (event instanceof ContextStartedEvent) 
		
			System.out.println("==server start===>it was contextStartedEvent");
			invoker = event.getInvoker();
			this.beanName = event.getBeanName();
			
			export();
		
	}
	
	private synchronized void export() {

		try {
			RemoteServiceCenter.setService(beanName, invoker);
			
			logger.info("=======>注册：" + beanName);
		} catch (Exception e) {
			logger.error("WestConsumerBeanDefinitionParser error", e);
		}
	}

}
