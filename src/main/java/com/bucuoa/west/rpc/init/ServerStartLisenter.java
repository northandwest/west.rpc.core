package com.bucuoa.west.rpc.init;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import cc.ymsoft.Framework.RpcFramework;
@Component
public class ServerStartLisenter implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
			System.out.println("==server start===>it was contextStartedEvent");

			new RpcFramework();
	}

}
