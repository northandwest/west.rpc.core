package com.bucuoa.west.rpc.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtil implements ApplicationContextAware{

	private static ApplicationContext context = null;

//	public static void setContext(ApplicationContext applicationContext) throws BeansException {
//		SpringContextUtil.context = applicationContext;
//	}

	public static Object getBean(String name) {
		return context.getBean(name);
	}

	@Override
	public void setApplicationContext(ApplicationContext contextx) throws BeansException {
		
		context = contextx;
		System.out.println("==============>SpringContextUtil init success!");
		
	}
}