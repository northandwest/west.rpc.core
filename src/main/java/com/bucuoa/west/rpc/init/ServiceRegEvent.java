//package com.bucuoa.west.rpc.init;
//
//import org.springframework.context.ApplicationEvent;
//
//import com.bucuoa.west.rpc.remoting.server.ProviderStubInvoker;
//
//public class ServiceRegEvent extends  ApplicationEvent {
//
//	private static final long serialVersionUID = 8362914540154590275L;
//	
//	private String beanName;
//	private ProviderStubInvoker invoker;
//	
//	public ServiceRegEvent(Object source,ProviderStubInvoker invoker,String beanName) {
//		
//		super(source);
//		
//		this.invoker = invoker;
//		this.beanName = beanName;
//	}
//
//	public String getBeanName() {
//		return beanName;
//	}
//
//	public void setBeanName(String beanName) {
//		this.beanName = beanName;
//	}
//
//	public ProviderStubInvoker getInvoker() {
//		return invoker;
//	}
//
//	public void setInvoker(ProviderStubInvoker invoker) {
//		this.invoker = invoker;
//	}
//	
//	
//
//}
