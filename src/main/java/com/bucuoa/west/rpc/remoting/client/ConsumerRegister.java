package com.bucuoa.west.rpc.remoting.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConsumerRegister {

	private static Map<String,String> consumers = new ConcurrentHashMap<String,String>();
	
	public  static void setConsumer(String key,String obj)
	{
		consumers.put(key, obj);
	}
	
	public  static String getConsumer(String key)
	{
		return consumers.get(key);
	}
}
