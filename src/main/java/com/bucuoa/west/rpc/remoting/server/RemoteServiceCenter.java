package com.bucuoa.west.rpc.remoting.server;

import java.util.HashMap;
import java.util.Map;

public class RemoteServiceCenter {
	private static Map<String ,ProviderStubInvoker> serviceEngine = new HashMap<String, ProviderStubInvoker>();
	
	private static Map<String ,String> interfacemap = new HashMap<String, String>();

	public static void setInterface(String key,String value) {
		RemoteServiceCenter.interfacemap.put(key, value);
	}
	public static String getInterface(String key) {

		String object = RemoteServiceCenter.interfacemap.get(key);
		return object;
	}
	
	public static void setService(String key,ProviderStubInvoker value) {
		RemoteServiceCenter.serviceEngine.put(key, value);
	}
	
	public static ProviderStubInvoker getService(String key) {

		ProviderStubInvoker object = RemoteServiceCenter.serviceEngine.get(key);
		return object;
	}
}
