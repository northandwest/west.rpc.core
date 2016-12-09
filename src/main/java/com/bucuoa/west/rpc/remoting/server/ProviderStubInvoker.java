package com.bucuoa.west.rpc.remoting.server;

import java.lang.reflect.Method;

import com.bucuoa.west.rpc.init.ProviderBean;
import com.bucuoa.west.rpc.remoting.comuication.BaseMessage;
import com.bucuoa.west.rpc.remoting.comuication.RequestMessage;
import com.bucuoa.west.rpc.remoting.comuication.ResponseMessage;
import com.bucuoa.west.rpc.remoting.protocal.Invocation;
import com.bucuoa.west.rpc.remoting.protocal.MethodBean;

public class ProviderStubInvoker {
	private ProviderBean providerConfig;

	public  ProviderStubInvoker(ProviderBean providerConfig) {
		this.providerConfig = providerConfig;
	}

	public ResponseMessage invoke(BaseMessage requestMessage) {
		RequestMessage request = (RequestMessage) requestMessage;
		com.bucuoa.west.rpc.remoting.route.Invocation invocationBody = request.getInvocationBody();
		ResponseMessage response = null;// filterChain.invoke(request);
		// 得到结果
		return response;
	}

	public ResponseMessage call(Invocation invo) {
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
	}
}
