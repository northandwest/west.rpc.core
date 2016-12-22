 package com.bucuoa.west.rpc.remoting.server.netty;


import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bucuoa.west.rpc.core.RpcRequest;
import com.bucuoa.west.rpc.core.RpcResponse;
import com.bucuoa.west.rpc.registry.ServiceRegistry;
import com.bucuoa.west.rpc.remoting.protocal.netty.RpcDecoder;
import com.bucuoa.west.rpc.remoting.protocal.netty.RpcEncoder;
import com.bucuoa.west.rpc.utils.StringUtil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class RpcServer  {

	private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

	private String serviceAddress;

	private ServiceRegistry serviceRegistry;

	/**
	 * 存放 服务名 与 服务对象 之间的映射关系
	 */
//	private Map<String, Object> handlerMap = new HashMap<>();

	public RpcServer() {
		this.serviceAddress = "127.0.0.1:14527";
	}

	public RpcServer(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	public RpcServer(String serviceAddress, ServiceRegistry serviceRegistry) {
		this.serviceAddress = serviceAddress;
		this.serviceRegistry = serviceRegistry;
	}
	
	public boolean stop() throws Exception {
		//todo 关闭
		return true;
	}

	public boolean start() throws Exception {
		 boolean flag = Boolean.FALSE;
		 
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			// 创建并初始化 Netty 服务端 Bootstrap 对象
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel channel) throws Exception {
					ChannelPipeline pipeline = channel.pipeline();
					pipeline.addLast(new RpcDecoder(RpcRequest.class)); // 解码RPC请求
					pipeline.addLast(new RpcEncoder(RpcResponse.class)); // 编码 RPC响应
					pipeline.addLast(new RpcServerHandler()); // 处理RPC请求
				}
			});
			bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			// 获取 RPC 服务器的 IP 地址与端口号
			String[] addressArray = StringUtil.split(serviceAddress, ":");
	
			final String ip = addressArray[0];
			final int port = Integer.parseInt(addressArray[1]);
			System.out.println("server started on HOST:"+ip+" port:"+port);
			
			LOGGER.debug("server started on HOST:{} port:{}",ip, port);

			// 启动 RPC 服务器
			ChannelFuture future = bootstrap.bind(ip, port);
			
			ChannelFuture channelFuture = future.addListener(new ChannelFutureListener() {

		            @Override
		            public void operationComplete(ChannelFuture future) throws Exception {
		                if (future.isSuccess()) {
		                	LOGGER.info("Server have success bind to {}:{}", ip, port);

		                } else {
		                	LOGGER.error("Server fail bind to {}:{}", ip, port);
		                	
		                    throw new Exception("Server start fail !", future.cause());
		                }

		            }
		        });

		        try {
		            channelFuture.await(5000,TimeUnit.MILLISECONDS);
		            if(channelFuture.isSuccess()){
		                flag = Boolean.TRUE;
		            }

		        } catch (InterruptedException e) {
		        	LOGGER.error(e.getMessage(),e);
		        }
		        
			// 注册 RPC 服务地址
			// if (serviceRegistry != null) {
			// for (String interfaceName : handlerMap.keySet()) {
			// serviceRegistry.register(interfaceName, serviceAddress);
			// LOGGER.debug("register service: {} => {}", interfaceName,
			// serviceAddress);
			// }
			// }
			// 关闭 RPC 服务器
//			future.channel().closeFuture().sync();
		} finally {
//			workerGroup.shutdownGracefully();
//			bossGroup.shutdownGracefully();
		}
		
		return flag;
	}
}
