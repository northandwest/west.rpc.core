package com.bucuoa.west.rpc.init;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.bucuoa.west.rpc.spring.ApplicationBean;
import com.bucuoa.west.rpc.spring.ConsumerBean;
import com.bucuoa.west.rpc.spring.ProviderBean;
import com.bucuoa.west.rpc.spring.RegistryBean;
import com.bucuoa.west.rpc.spring.ServerBean;


public class InitNamespaceHandler extends NamespaceHandlerSupport {
	public void init() {
		this.registerBeanDefinitionParser("provider", new WestConsumerBeanDefinitionParser(ProviderBean.class,true));
		this.registerBeanDefinitionParser("consumer", new WestConsumerBeanDefinitionParser(ConsumerBean.class,true));
		this.registerBeanDefinitionParser("server", new WestConsumerBeanDefinitionParser(ServerBean.class,true));
		this.registerBeanDefinitionParser("registry", new WestConsumerBeanDefinitionParser(RegistryBean.class,true));
		this.registerBeanDefinitionParser("application", new WestConsumerBeanDefinitionParser(ApplicationBean.class,true));

	}
}