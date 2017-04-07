package com.bucuoa.west.rpc.remoting.client.netty;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bucuoa.west.rpc.core.RpcRequest;
import com.bucuoa.west.rpc.core.RpcResponse;
import com.bucuoa.west.rpc.utils.StringUtil;

public class ClientInvocationHandler implements InvocationHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RpcProxy.class);
	String serviceVersion;
	String serviceAddress;

	public ClientInvocationHandler(String serviceVersion, String serviceAddress) {
		this.serviceVersion = serviceVersion;
		this.serviceAddress = serviceAddress;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// 创建 RPC 请求对象并设置请求属性
		RpcRequest request = new RpcRequest();
		request.setRequestId(UUID.randomUUID().toString());
		request.setInterfaceName(method.getDeclaringClass().getName());
		request.setServiceVersion(serviceVersion);
		request.setMethodName(method.getName());
		request.setParameterTypes(method.getParameterTypes());
		request.setParameters(args);
		// 获取 RPC 服务地址
		// if (serviceDiscovery != null) {
		// String serviceName = interfaceClass.getName();
		// if (StringUtil.isNotEmpty(serviceVersion)) {
		// serviceName += "-" + serviceVersion;
		// }
		// serviceAddress = serviceDiscovery.discover(serviceName);
		// LOGGER.debug("discover service: {} => {}", serviceName,
		// serviceAddress);
		// }
		// if (StringUtil.isEmpty(serviceAddress)) {
		// throw new RuntimeException("server address is empty");
		// }
		// 从 RPC 服务地址中解析主机名与端口号
		String[] array = StringUtil.split(serviceAddress, ":");
		String host = "127.0.0.1";
		try {
			host = array[0];
		} catch (Exception e1) {
//			e1.printStackTrace();
		}
		
		int port = 14527;
		try {
			port = Integer.parseInt(array[1]);
		} catch (Exception e) {
//			e.printStackTrace();
		}
		// 创建 RPC 客户端对象并发送 RPC 请求
		RpcClient client = new RpcClient(host, port);
		
		long time = System.currentTimeMillis();
		RpcResponse response = client.send(request);
		
		LOGGER.debug("time: {}ms", System.currentTimeMillis() - time);
		
		if (response == null) {
			throw new RuntimeException("response is null");
		}
		// 返回 RPC 响应结果
		if (response.hasException()) {
			throw response.getException();
		} else {
			return response.getResult();
		}
	}
}
