package com.bucuoa.west.rpc.remoting.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bucuoa.west.rpc.remoting.comuication.ResponseMessage;
import com.bucuoa.west.rpc.remoting.route.Invocation;


public class Client {
	static Logger logger = LoggerFactory.getLogger(Client.class);
//	static SocketConnectionPool pool = new SocketConnectionPool();
	private String host;
	private int port;
	private Socket request;
	private ObjectOutputStream requestStream;
	private ObjectInputStream responseStream;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void init() throws Exception {

	}

	public synchronized void invoke(Invocation invo)
			throws Exception, UnknownHostException, IOException, ClassNotFoundException {
		init();

		long start = System.currentTimeMillis();
		// request = pool.borrowObject();
		// if (request == null)
		{
			request = new Socket(host, port);
		}

		long end = System.currentTimeMillis();
		logger.debug("get Socket use:{}ms", (end - start));

		logger.info(" client call remote service:{}", invo.getClazzName());

		requestStream = new ObjectOutputStream(request.getOutputStream());

		requestStream.writeObject(invo);
		// requestStream.reset();
		requestStream.flush();

		responseStream = new ObjectInputStream(request.getInputStream());

		ResponseMessage result = (ResponseMessage) responseStream.readObject();
		System.out.println("response ok!");
		invo.setResult(result.getResponse());

		responseStream.close();
		// responseSocketStream.close();

		// pool.returnObject(request);
	}

}
