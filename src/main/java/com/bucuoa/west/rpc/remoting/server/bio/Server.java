package com.bucuoa.west.rpc.remoting.server.bio;

import com.bucuoa.west.rpc.remoting.route.Invocation;

public interface Server {
	public void stop();
	public void start();
//	public void register(Class<?> interfaceDefiner,Class<?> impl);
	public void call(Invocation invo);
	public boolean isRunning();
	public int getPort();
}
