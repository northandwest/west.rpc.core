package com.bucuoa.west.rpc.registry;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bucuoa.west.rpc.registry.zookeeper.ZooKeeperServiceRegistry;
import com.bucuoa.west.rpc.remoting.server.ProviderStubInvoker;
import com.bucuoa.west.rpc.remoting.server.RemoteServiceCenter;
//适配器模式
public class RegistryServiceProgram {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegistryServiceProgram.class);

	private ServiceRegistry serviceRegistry;
	public static boolean allow = false;

	public RegistryServiceProgram() {
		String zkAddress = "127.0.0.1:2181";
		serviceRegistry = new ZooKeeperServiceRegistry(zkAddress);
	}

	public void registry() {
		if (serviceRegistry != null && allow) {

			Map<String, ProviderStubInvoker> serviceEngine = RemoteServiceCenter.getServiceEngine();
			
			for (Map.Entry<String, ProviderStubInvoker> entry : serviceEngine.entrySet()) {
				String interfaceName = entry.getKey();
				ProviderStubInvoker invoker = entry.getValue();

				String serviceAddress = invoker.getProviderConfig().getServer();
				serviceRegistry.register(interfaceName, serviceAddress);
				LOGGER.debug("register service: {} => {}", interfaceName, serviceAddress);
			}
		}
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}
	
}
