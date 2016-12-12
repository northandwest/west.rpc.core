package com.bucuoa.west.rpc.remoting.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bucuoa.west.rpc.core.Constants;
import com.bucuoa.west.rpc.remoting.protocal.MethodBean;
import com.bucuoa.west.rpc.remoting.route.Invocation;

public class ClientRemoteCall {
	final static Logger logger = LoggerFactory.getLogger(ClientRemoteCall.class);
//	static ConfigSingleton confInstance = ConfigSingleton.getInstance();

//	static String host = confInstance.getProperties("server_host");
	static String service_server_host = "127.0.0.1";
	final static int rpc_server_port = Constants.PORT;

	@SuppressWarnings("unchecked")
	public static <T> T getProxy(final Class<T> clazz,final Client client) {
		
		
		InvocationHandler handler = new InvocationHandler() {
			//调用服务端服务核心逻辑
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//				MethodBean methodBean = new MethodBean(method.getName(), method.getParameterTypes());

				Invocation invocation = new Invocation();
				invocation.setClazzName(clazz.getCanonicalName());
				invocation.setMethodName(method.getName());
				invocation.setArgs(args);
				invocation.setParameterTypes(method.getParameterTypes());
//				invocation.setArgsType(argsType);
				client.invoke(invocation);
				
				return invocation.getResult();
			}
		};
		
		ClassLoader classLoader = ClientRemoteCall.class.getClassLoader();
		
		T resultObject = (T) Proxy.newProxyInstance(classLoader, new Class[] { clazz }, handler);
		
		return resultObject;
	}

//	public static <T> T getProxy(final Class<T> clazz,Client client) {
//
//		return getProxy(clazz,service_server_host,rpc_server_port);
//	}

}
