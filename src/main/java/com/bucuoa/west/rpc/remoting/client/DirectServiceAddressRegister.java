package com.bucuoa.west.rpc.remoting.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DirectServiceAddressRegister {
	private static Map<String,String> consumers = new ConcurrentHashMap<String,String>();

	public  static void setConsumerUrl(String key,String obj)
	{
		consumers.put(key, obj);
	}
	
	public  static String getConsumerUrl(String key)
	{
		return consumers.get(key);
	}
	
	

}
