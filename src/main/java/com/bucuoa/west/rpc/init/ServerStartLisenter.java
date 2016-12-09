package com.bucuoa.west.rpc.init;

import org.springframework.context.ApplicationListener;

import com.bucuoa.west.rpc.remoting.server.RPCServerStub;
//@Component
public class ServerStartLisenter implements ApplicationListener<StartServerEvent> {

	@Override
	public void onApplicationEvent(StartServerEvent event) {
//		if (event instanceof ContextStartedEvent) 
		{
			System.out.println("==server start===>it was contextStartedEvent");


		}
	}

}
