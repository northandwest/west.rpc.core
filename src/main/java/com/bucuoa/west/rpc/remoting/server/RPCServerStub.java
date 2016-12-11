package com.bucuoa.west.rpc.remoting.server;

import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bucuoa.west.rpc.core.Constants;
import com.bucuoa.west.rpc.remoting.comuication.RequestMessage;
import com.bucuoa.west.rpc.remoting.route.Invocation;


public class RPCServerStub implements Server{
	static Logger logger = LoggerFactory.getLogger(RPCServerStub.class);
	final Semaphore sp = new Semaphore(Constants.SERVER_SOCKET_THREAD_SEMPHORE_MAX, true);
//	private transient ApplicationContext applicationContext;

	public RPCServerStub()
	{
//		this.register(EchoService.class, EchoServiceImpl.class);
//		init() spring init
		
		this.start();
	}
	
	private int port = Constants.PORT;
//	private ServerListener listener; 
	private boolean isRuning = true;
	
	/**
	 * @param isRuning the isRuning to set
	 */
	public void setRuning(boolean isRuning) {
		this.isRuning = isRuning;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public  void  call(Invocation invo) {
		
		String serviceName = "echoService";//invo.getClazzName();
		
		ProviderStubInvoker serviceInvoker = RemoteServiceCenter.getService(serviceName);
		RequestMessage  requestMessage = new RequestMessage();
		requestMessage.setInvocationBody(invo);
		serviceInvoker.invoke(requestMessage);
		
//		String serviceBean = null;//RemoteServiceCenter.getService(serviceName);
//		Object obj = null;//applicationContext.getBean(serviceBean);// RemoteServiceCenter.getService(serviceName);
//		if(obj != null) {
//			try {
//				Class<? extends Object> clazz = obj.getClass();
//				MethodBean method = invo.getMethod();
//				
//				Method serverMethod = clazz.getMethod(method.getMethodName(), method.getParams());
//				Object result = serverMethod.invoke(obj, invo.getParams());
//				
//				invo.setResult(result);
//			} catch (Throwable th) {
//				th.printStackTrace();
//			}
//		} else {
//			throw new IllegalArgumentException("has no these class");
//		}
	}


	@Override
	public void start() {
		logger.info("start rpc server");
//		listener = new ServerListener(this);
		PoolMultiServer poolMultiServer = new PoolMultiServer(Constants.CLIENT_SOCKET_THREAD_MAX, Constants.PORT,this,sp);
		poolMultiServer.setUpHandlers();
		poolMultiServer.acceptConnection();
//		this.isRuning = true;
//		listener.start();
	}

	@Override
	public void stop() {
		this.setRuning(false);
	}

	@Override
	public boolean isRunning() {
		return isRuning;
	}
	
}