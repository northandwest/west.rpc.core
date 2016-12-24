package com.bucuoa.west.rpc.remoting.server;

import java.util.HashMap;
import java.util.Map;

public class RemoteServiceCenter {
	//serviceid
	private static Map<String, ProviderStubInvoker> serviceEngine = new HashMap<String, ProviderStubInvoker>();

	public static void setService(String serviceid, ProviderStubInvoker invoker) {
		RemoteServiceCenter.serviceEngine.put(serviceid, invoker);
	}

	public static ProviderStubInvoker getService(String serviceid) {

		return RemoteServiceCenter.serviceEngine.get(serviceid);
	}
	
	////////////////////////////////////////////////////////////////////////////

	private static Map<String, String> interfacemap = new HashMap<String, String>();

	public static void setInterface(String serviceid, String interfaceName) {
		RemoteServiceCenter.interfacemap.put(serviceid, interfaceName);
	}

	public static String getInterface(String interfaceName) {

		return RemoteServiceCenter.interfacemap.get(interfaceName);
	}

	public static Map<String, ProviderStubInvoker> getServiceEngine() {
		return serviceEngine;
	}

}
