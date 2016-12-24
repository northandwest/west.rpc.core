package com.bucuoa.west.rpc.remoting.server;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bucuoa.west.rpc.core.RpcRequest;
import com.bucuoa.west.rpc.spring.ProviderBean;

public class ProviderStubInvoker {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProviderStubInvoker.class);

	private ProviderBean providerConfig;

	public ProviderStubInvoker(ProviderBean providerConfig) {
		this.providerConfig = providerConfig;
	}

	public ProviderBean getProviderConfig() {
		return providerConfig;
	}

	public Object invoke(RpcRequest request) {
		// 从spring配置里得到ref 实体对象
		Object serviceBean = providerConfig.getRefBean();

		Object result = null;

		if (serviceBean != null) {
			try {

				Class<? extends Object> clazz = serviceBean.getClass();

				Method serverMethod = clazz.getMethod(request.getMethodName(), request.getParameterTypes());
				result = serverMethod.invoke(serviceBean, request.getParameters());

			} catch (Throwable th) {
				LOGGER.warn("method:{} not found invoker service!Please check {} spring bean configuration!",request.getMethodName(), request.getMethodName());
			}
		}

		return result;
	}

}
