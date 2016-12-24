package com.bucuoa.west.rpc.spring.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.bucuoa.west.rpc.registry.ServiceRegistry;
import com.bucuoa.west.rpc.registry.zookeeper.ZooKeeperServiceRegistry;

public class ServerStartLisenter implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerStartLisenter.class);

	private ServiceRegistry serviceRegistry;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		
		// 注册 RPC 服务地址
/*		if (serviceRegistry != null) {

			Map<String, ProviderStubInvoker> serviceEngine = RemoteServiceCenter.getServiceEngine();
			for (Map.Entry<String, ProviderStubInvoker> entry : serviceEngine.entrySet()) {
				String interfaceName = entry.getKey();
				ProviderStubInvoker invoker = entry.getValue();

				String serviceAddress = invoker.getProviderConfig().getServer();
				serviceRegistry.register(interfaceName, serviceAddress);
				LOGGER.debug("register service: {} => {}", interfaceName, serviceAddress);
			}
		}*/
	}

}
