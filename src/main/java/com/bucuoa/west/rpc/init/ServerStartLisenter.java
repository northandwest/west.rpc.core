package com.bucuoa.west.rpc.init;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.xxx.rpc.server.RpcServer;
@Service
public class ServerStartLisenter implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
			System.out.println("==server start===>it was contextStartedEvent");

			new RpcServer();
	}

}
