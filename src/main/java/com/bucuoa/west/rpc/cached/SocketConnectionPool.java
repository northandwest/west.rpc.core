package com.bucuoa.west.rpc.cached;

import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SocketConnectionPool{

	private static BlockingQueue<Socket> queue =null;  

	public SocketConnectionPool() {
		if (queue == null) {
			queue = new ArrayBlockingQueue<Socket>(1000);
		}
	}

	public synchronized Socket borrowObject() throws Exception {
		return queue.poll();
	}

	public synchronized void returnObject(Socket socket) {
		queue.offer(socket);
	}

}
