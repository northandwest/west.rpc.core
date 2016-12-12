package com.bucuoa.west.rpc.remoting.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bucuoa.west.rpc.core.Constants;
import com.bucuoa.west.rpc.remoting.comuication.ResponseMessage;
import com.bucuoa.west.rpc.remoting.protocal.RpcClose;
import com.bucuoa.west.rpc.remoting.protocal.RpcConnect;
import com.bucuoa.west.rpc.remoting.route.Invocation;

public class MulitSocketThread implements Runnable {
	static Logger logger = LoggerFactory.getLogger(MulitSocketThread.class);

	private static Queue<Socket> pool = new ConcurrentLinkedQueue<Socket>();

	private static Server serverStub;
	private Socket request = null;
	private static Semaphore sp;

	public MulitSocketThread() {
		// logger.info(" init thread :{}",Thread.currentThread().getName());
	}

	public static void processRequest(Socket socket, Server server2, Semaphore sp2) {
		serverStub = server2;
		sp = sp2;

		synchronized (pool) {
//			if (pool.size() <= Constants.SERVER_SOCKET_THREAD_MAX)
			{
				pool.offer(socket);
//				logger.info("pool full,pool size{} <= max {}", pool.size(), Constants.SERVER_SOCKET_THREAD_MAX);
			} 
//			else {
//				try {
//					socket.close();
//				} catch (IOException e) {
//					logger.error("pool full close socket", e);
//				}
//			}
			pool.notifyAll();
		}
	}

	public void handleConnection() {
		ObjectInputStream requestSocketStream = null;
		ObjectOutputStream responseStream = null;
		try {
			// sp.acquire();

			request.setKeepAlive(true);

			String name = Thread.currentThread().getName();

			logger.info("2 client thread: {} connect success", name);
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				requestSocketStream = new ObjectInputStream(inputStream);
				Object object = requestSocketStream.readObject();

				if (object instanceof Invocation) {
					Invocation invocation = (Invocation) object;
					if (invocation != null) {
						logger.info("3 client thread: {} execute:{}", name, invocation);

						serverStub.call(invocation);
						ResponseMessage response = new ResponseMessage();
						response.setResponse(invocation.getResult());
						
						responseStream = new ObjectOutputStream(request.getOutputStream());
						responseStream.writeObject(response);
						responseStream.flush();
					}
				} else if (object instanceof RpcClose) {
					if (object != null) {
						logger.info("3 client thread: {} execute:close", name);
						responseStream = new ObjectOutputStream(request.getOutputStream());
						responseStream.writeObject("success");
						responseStream.flush();
					}
				} else if (object instanceof RpcConnect) {
					if (object != null) {
						logger.info("3 client thread: {} execute:connect", name);
						responseStream = new ObjectOutputStream(request.getOutputStream());
						responseStream.writeObject("success");
						responseStream.flush();
					}
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (requestSocketStream != null) {
					requestSocketStream.close();
				}
				if(responseStream != null)
				{
					responseStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void run() {

		while (true) {

			synchronized (pool) {
				while (pool.isEmpty()) {
					try {
						pool.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				request = (Socket) pool.poll();
			}

			String name = Thread.currentThread().getName();

			try {
				sp.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.debug("线程{}进入，已有{}并发", name, (Constants.SERVER_SOCKET_THREAD_SEMPHORE_MAX - sp.availablePermits()));

			handleConnection();

			sp.release();
			logger.debug("线程{}离开 ，已有{}并发", name, (Constants.SERVER_SOCKET_THREAD_SEMPHORE_MAX - sp.availablePermits()));
		}
	}

}
