package com.bucuoa.west.rpc.init;

import java.lang.reflect.Method;

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
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;

import com.bucuoa.west.rpc.remoting.server.ProviderStubInvoker;
import com.bucuoa.west.rpc.remoting.server.RemoteServiceCenter;
import com.bucuoa.west.rpc.tags.Provider;

public class ProviderBean<T> extends Provider implements InitializingBean, DisposableBean, ApplicationContextAware, ApplicationListener, BeanNameAware {
	
	static Logger logger = LoggerFactory.getLogger(ProviderBean.class);

	private transient String beanName;
//	private transient ApplicationContext applicationContext;
	private static transient ApplicationContext SPRING_CONTEXT;
	private transient boolean supportedApplicationListener;

	protected int delay = 1;

	private T refBean;

	public T getRefBean() {
		return refBean;
	}

	public void setRefBean(T refBean) {
		this.refBean = refBean;
	}

	@Override
	public void setBeanName(String name) {
		logger.debug("setBeanName=>{}", name);
		this.beanName = name;
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {

		if (event instanceof ContextRefreshedEvent) { // spring加载完毕

			logger.info("West RPC export provider with beanName {} after spring context refreshed.", beanName);
			if (delay < -1) { // 小于-1表示延迟更长时间加载
				
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(-delay);
						} catch (Throwable e) {
						}
						export();
					}
				});
				thread.setDaemon(true);
				thread.setName("DelayExportThread");
				thread.start();
			} else { // 等于-1表示延迟立即加载
				export();
			}

		}

	}

	private synchronized void export() {

		try {
			ProviderStubInvoker invoker = new ProviderStubInvoker(this);
			RemoteServiceCenter.setService(beanName, invoker);
//			ServiceRegEvent event = new ServiceRegEvent(SPRING_CONTEXT,invoker,beanName);
//			SPRING_CONTEXT.publishEvent(event);
			
			logger.info("=======>注册：" + beanName);
		} catch (Exception e) {
			logger.error("WestConsumerBeanDefinitionParser error", e);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		logger.info("setApplicationContext=>{}", applicationContext.getApplicationName());

//		this.applicationContext = applicationContext;
		// SpringExtensionFactory.addApplicationContext(applicationContext);
		if (applicationContext != null) {
			SPRING_CONTEXT = applicationContext;
			try {
				Method method = applicationContext.getClass().getMethod("addApplicationListener",
						new Class<?>[] { ApplicationListener.class }); // 兼容Spring2.0.1
				method.invoke(applicationContext, new Object[] { this });
				supportedApplicationListener = true;
			} catch (Throwable t) {
				if (applicationContext instanceof AbstractApplicationContext) {
					try {
						Method method = AbstractApplicationContext.class.getDeclaredMethod("addListener",
								new Class<?>[] { ApplicationListener.class }); // 兼容Spring2.0.1
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
		logger.info("destroy=>{}", System.currentTimeMillis());

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("afterPropertiesSet=>{}", System.currentTimeMillis());
		System.out.println("init providerbean"+beanName);
	}

}
