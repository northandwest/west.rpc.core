package com.bucuoa.west.rpc.remoting.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class PoolMultiServer {
	private int maxConnections;// 最大连接数
	private int listenerPort;// 监听端口号
	private ServerSocket serverSocket;
	private Server server;
//	private Semaphore sp;

	/**
	 * 构造方法
	 * 
	 * @param maxConnections
	 *            ：最大连接数
	 * @param listenerPort
	 *            ：监听端口号
	 */
	public PoolMultiServer(int maxConnections, int listenerPort,Server server) {
		this.maxConnections = maxConnections;
		this.listenerPort = listenerPort;
		this.server = server;
//		this.sp = sp;
	}

	/**
	 * 接受连接
	 */
	public void acceptConnection() {
		try {
			serverSocket = new ServerSocket(listenerPort, maxConnections);
			while (true) {
				Socket socket = serverSocket.accept();
				handleConnection(socket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理连接
	 * 
	 * @param socket
	 *            ：套接字
	 */
	public void handleConnection(Socket socket) {
		MulitSocketThread.processRequest(socket,server);
	}

	public void setUpHandlers() {
		for (int i = 0; i < maxConnections; i++) {
			MulitSocketThread connectionPoolHandler = new MulitSocketThread();
			
			new Thread(connectionPoolHandler, "rpc server socket thread-" + i).start();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		PoolMultiServer poolMultiServer = new PoolMultiServer(5, 9999,);
//		poolMultiServer.setUpHandlers();
//		poolMultiServer.acceptConnection();
	}
}