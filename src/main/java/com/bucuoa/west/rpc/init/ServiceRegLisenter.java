//package com.bucuoa.west.rpc.init;
//
//import org.springframework.context.ApplicationListener;
//
//import com.bucuoa.west.rpc.remoting.server.RPCServerStub;
//import com.bucuoa.west.rpc.remoting.server.RemoteServiceCenter;
////@Component
//public class ServiceRegLisenter implements ApplicationListener<ServiceRegEvent> {
//
//	@Override
//	public void onApplicationEvent(ServiceRegEvent event) {
////		if (event instanceof ContextStartedEvent) 
//		{
//			System.out.println("==server start===>it was ServiceRegLisenter");
//
////			RPCServerStub serverstub = new RPCServerStub();
////			serverstub.start();
//			
//			RemoteServiceCenter.setService(event.getBeanName(), event.getInvoker());
//
//		}
//	}
//
//}
