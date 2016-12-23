package com.bucuoa.west.rpc.remoting.server;

import java.lang.reflect.Method;

import com.bucuoa.west.rpc.core.RpcRequest;
import com.bucuoa.west.rpc.core.RpcResponse;
import com.bucuoa.west.rpc.spring.ProviderBean;

public class ProviderStubInvoker {
	private ProviderBean providerConfig;

	public  ProviderStubInvoker(ProviderBean providerConfig) {
		this.providerConfig = providerConfig;
	}

	public Object invoke(RpcRequest requestMessage) {
		RpcRequest request = (RpcRequest) requestMessage;
//		Invocation invocationBody = request.getInvocationBody();
//		RpcResponse response = null;
		// 得到结果
		Object serviceBean = providerConfig.getRefBean();
		Object[] parameters = request.getParameters();
		
		Class<? extends Object> clazz = serviceBean.getClass();

		Object result = null;
		if (serviceBean != null) {
			try {
				
				Method serverMethod = clazz.getMethod(requestMessage.getMethodName(), requestMessage.getParameterTypes());
				result = serverMethod.invoke(serviceBean, parameters);

//				invo.setResult(result);
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
		
//		response = new ResponseMessage();
//		response.setResponse(result);
		
		return result;
	}

/*	public ResponseMessage call(Invocation invo) {
//		String ref = providerConfig.getRef();
		Object serviceBean = providerConfig.getRefBean();
		String serviceName = invo.getInterfaces().getName();
//		String serviceBean = RemoteServiceCenter.getService(serviceName);
		Object obj = null;// applicationContext.getBean(serviceBean);//
							// RemoteServiceCenter.getService(serviceName);
		Class<? extends Object> clazz = serviceBean.getClass();
		MethodBean method = invo.getMethod();

		Object result = null;
		if (obj != null) {
			try {
				
				Method serverMethod = clazz.getMethod(method.getMethodName(), method.getParams());
				result = serverMethod.invoke(serviceBean, invo.getParams());

//				invo.setResult(result);
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
		
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setResponse(result);
		
		return responseMessage;
	}*/
}
