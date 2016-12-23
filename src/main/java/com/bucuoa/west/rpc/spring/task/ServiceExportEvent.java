package com.bucuoa.west.rpc.spring.task;

import org.springframework.context.ApplicationEvent;

import com.bucuoa.west.rpc.remoting.server.ProviderStubInvoker;

public class ServiceExportEvent extends  ApplicationEvent {
	ProviderStubInvoker invoker;
	String beanName;
	
	private static final long serialVersionUID = 8362914540154590275L;

	public ServiceExportEvent(Object source,ProviderStubInvoker invoker,String beanName) {
		
		super(source);
		this.beanName = beanName;
		this.invoker = invoker;
	}

	public ProviderStubInvoker getInvoker() {
		return invoker;
	}

	public void setInvoker(ProviderStubInvoker invoker) {
		this.invoker = invoker;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	

}
