package com.bucuoa.west.rpc.remoting.client.netty;

import java.lang.reflect.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bucuoa.west.rpc.registry.ServiceDiscovery;


public class RpcProxy {

	private static final Logger LOGGER = LoggerFactory.getLogger(RpcProxy.class);

	private String serviceAddress;

	private ServiceDiscovery serviceDiscovery;

	public RpcProxy(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	public RpcProxy(ServiceDiscovery serviceDiscovery) {
		this.serviceDiscovery = serviceDiscovery;
	}

	@SuppressWarnings("unchecked")
	public <T> T create(final Class<?> interfaceClass) {
		return create(interfaceClass, "");
	}

	@SuppressWarnings("unchecked")
	public <T> T create(final Class<?> interfaceClass, final String serviceVersion) {
		// 创建动态代理对象
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] { interfaceClass },new ClientInvocationHandler(serviceVersion, serviceAddress));
	}
}
