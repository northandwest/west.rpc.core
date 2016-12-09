package com.bucuoa.west.rpc.remoting.server;

import java.util.HashMap;
import java.util.Map;

public class RemoteServiceCenter {
	private static Map<String ,ProviderStubInvoker> serviceEngine = new HashMap<String, ProviderStubInvoker>();

	public static void setService(String key,ProviderStubInvoker value) {
		RemoteServiceCenter.serviceEngine.put(key, value);
	}
	
	public static ProviderStubInvoker getService(String key) {

		ProviderStubInvoker object = RemoteServiceCenter.serviceEngine.get(key);
		return object;
	}
}
