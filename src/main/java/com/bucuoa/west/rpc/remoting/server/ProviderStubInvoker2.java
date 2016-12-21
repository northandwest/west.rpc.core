package com.bucuoa.west.rpc.remoting.server;

import java.lang.reflect.Method;

import com.bucuoa.west.rpc.init.ProviderBean;
import com.bucuoa.west.rpc.remoting.comuication.BaseMessage;
import com.bucuoa.west.rpc.remoting.comuication.RequestMessage;
import com.bucuoa.west.rpc.remoting.comuication.ResponseMessage;
import com.bucuoa.west.rpc.remoting.route.Invocation;

public class ProviderStubInvoker2 {
	private ProviderBean providerConfig;

	public  ProviderStubInvoker2(ProviderBean providerConfig) {
		this.providerConfig = providerConfig;
	}

	public ResponseMessage invoke(BaseMessage requestMessage) {
		RequestMessage request = (RequestMessage) requestMessage;
		Invocation invocationBody = request.getInvocationBody();
		ResponseMessage response = null;
		// 得到结果
		Object serviceBean = providerConfig.getRefBean();
		
		Class<? extends Object> clazz = serviceBean.getClass();

		Object result = null;
		if (serviceBean != null) {
			try {
				
				Method serverMethod = clazz.getMethod(invocationBody.getMethodName(), invocationBody.getParameterTypes());
				result = serverMethod.invoke(serviceBean, invocationBody.getArgs());

//				invo.setResult(result);
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
		
		response = new ResponseMessage();
		response.setResponse(result);
		
		return response;
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
