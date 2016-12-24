package com.bucuoa.west.rpc.remoting.client.netty;

import java.lang.reflect.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcProxy {

	private static final Logger LOGGER = LoggerFactory.getLogger(RpcProxy.class);

	private String serviceAddress;

	public RpcProxy(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	@SuppressWarnings("unchecked")
	public <T> T create(final Class<?> interfaceClass) {
		return create(interfaceClass, "");
	}

	@SuppressWarnings("unchecked")
	public <T> T create(final Class<?> interfaceClass, final String serviceVersion) {
		if (interfaceClass == null) {
			throw new IllegalArgumentException("接口类型不能为空");
		}
		if (!interfaceClass.isInterface()) {
			throw new IllegalArgumentException("类名" + interfaceClass.getName() + "必须是接口");
		}
		if (serviceAddress == null || serviceAddress.length() == 0) {
			throw new IllegalArgumentException("目标主机不能为空");
		}

		// 创建动态代理对象
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] { interfaceClass },
				new ClientInvocationHandler(serviceVersion, serviceAddress));
	}
}
