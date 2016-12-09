package com.bucuoa.west.rpc.init;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.AbstractApplicationContext;

import com.bucuoa.west.rpc.conf.ConfigSingleton;
import com.bucuoa.west.rpc.core.Constants;
import com.bucuoa.west.rpc.remoting.client.Client;
import com.bucuoa.west.rpc.remoting.client.ClientRemoteCall;
import com.bucuoa.west.rpc.remoting.client.ConsumerRegister;
import com.bucuoa.west.rpc.remoting.client.DirectServiceAddressRegister;
import com.bucuoa.west.rpc.tags.Consumer;
import com.bucuoa.west.rpc.utils.ReflectUtils;

public class ConsumerBean<T> extends Consumer implements InitializingBean,FactoryBean, DisposableBean, ApplicationContextAware, BeanNameAware {
	
	static Logger logger = LoggerFactory.getLogger(ConsumerBean.class);

	private transient String beanName;
	private transient String interfaceName;
	private transient Class<?> interfaceClass;

//	private transient ApplicationContext applicationContext;
	private static transient ApplicationContext SPRING_CONTEXT;
	private transient boolean supportedApplicationListener;

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	@Override
	public void setBeanName(String name) {
		logger.debug("setBeanName=>{}" , name);
		this.beanName = name;
	}



	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		logger.debug("setApplicationContext=>{}" , applicationContext.getApplicationName());

//		this.applicationContext = applicationContext;
		// SpringExtensionFactory.addApplicationContext(applicationContext);
		if (applicationContext != null) {
			SPRING_CONTEXT = applicationContext;
			try {
				Method method = applicationContext.getClass().getMethod("addApplicationListener",	new Class<?>[] { ApplicationListener.class }); // 兼容Spring2.0.1
				method.invoke(applicationContext, new Object[] { this });
				supportedApplicationListener = true;
			} catch (Throwable t) {
				if (applicationContext instanceof AbstractApplicationContext) {
					try {
						Method method = AbstractApplicationContext.class.getDeclaredMethod("addListener",new Class<?>[] { ApplicationListener.class }); // 兼容Spring2.0.1
						if (!method.isAccessible()) {
							method.setAccessible(true);
						}
						method.invoke(applicationContext, new Object[] { this });
						supportedApplicationListener = true;
					} catch (Throwable t2) {
					}
				}
			}
		}

	}

	@Override
	public void destroy() throws Exception {
		logger.debug("destroy=>{}" , System.currentTimeMillis());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.debug("afterPropertiesSet=>{}", System.currentTimeMillis());
	}

	@Override
	public Object getObject() throws Exception {
		String consumer = ConsumerRegister.getConsumer(beanName);
		ConfigSingleton confInstance = ConfigSingleton.getInstance();
		
		String consumerUrl = DirectServiceAddressRegister.getConsumerUrl(beanName);
		
		String host = "";
		if(consumerUrl !=null && !consumerUrl.equals(""))
		{
			host = consumerUrl;
		}else
		{
			host = confInstance.getProperties("server_host");
		}
		
		int port = Constants.PORT;
		this.interfaceName = consumer;
		Class<?> clazz = ReflectUtils.forName(this.interfaceName);
		this.interfaceClass = clazz;
		
		 Client client = new Client(host, port);
		
		Object proxy = ClientRemoteCall.getProxy(clazz,client);
		return proxy;
	}

	@Override
	public Class<?> getObjectType() {
//		Class<?> clazz = ReflectUtils.forName("com.bucuoa.west.rpc.service.EchoService");
	
		return this.interfaceClass;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
