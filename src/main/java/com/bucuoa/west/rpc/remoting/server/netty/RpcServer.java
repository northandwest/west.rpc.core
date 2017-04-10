package com.bucuoa.west.rpc.remoting.server.netty;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bucuoa.west.rpc.core.RpcRequest;
import com.bucuoa.west.rpc.core.RpcResponse;
import com.bucuoa.west.rpc.remoting.protocal.netty.RpcDecoder;
import com.bucuoa.west.rpc.remoting.protocal.netty.RpcEncoder;
import com.bucuoa.west.rpc.serializer.Serializer;
import com.bucuoa.west.rpc.serializer.protostuff.ProtostuffSerializer;
import com.bucuoa.west.rpc.utils.StringUtil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

public class RpcServer {

	private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

	private String serviceAddress;
	private Serializer serializer;

	/**
	 * 存放 服务名 与 服务对象 之间的映射关系
	 */
	// private Map<String, Object> handlerMap = new HashMap<>();

	public RpcServer() {
		this.serviceAddress = "127.0.0.1:14527";
	}

	public RpcServer(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	public boolean stop() throws Exception {
		// todo 关闭
		return true;
	}

	public boolean start() throws Exception {

		
		serializer = new ProtostuffSerializer();

		boolean flag = Boolean.FALSE;

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		// 创建并初始化 Netty 服务端 Bootstrap 对象
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel channel) throws Exception {
				ChannelPipeline pipeline = channel.pipeline();
				pipeline.addLast(new RpcDecoder(RpcRequest.class, serializer)); // 解码RPC请求
				pipeline.addLast(new RpcEncoder(RpcResponse.class, serializer)); // 编码
																					// RPC响应
				pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, -4, 0)); // 粘包问题
				pipeline.addLast(new IdleStateHandler(120, 0, 0, TimeUnit.SECONDS));
				pipeline.addLast(new RpcServerHandler()); // 处理RPC请求
			}
		});
		bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		
		bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);//内存池
		
		// 获取 RPC 服务器的 IP 地址与端口号
		String[] addressArray = StringUtil.split(serviceAddress, ":");

		final String ip = addressArray[0];
		int port = 14527;
		if(addressArray.length > 1)
		{
			port = Integer.parseInt(addressArray[1]);
		}
		LOGGER.debug("server started on HOST:{} port:{}", ip, port);

		// 启动 RPC 服务器
		ChannelFuture future = bootstrap.bind(ip, port);

		final int tport = port;
		ChannelFuture channelFuture = future.addListener(new ChannelFutureListener() {

			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {
					LOGGER.info("Server have success bind to {}:{}", ip, tport);

				} else {
					LOGGER.error("Server fail bind to {}:{}", ip, tport);

					throw new Exception("Server start fail !", future.cause());
				}

			}
		});

		try {
			channelFuture.await(5000, TimeUnit.MILLISECONDS);
			if (channelFuture.isSuccess()) {
				flag = Boolean.TRUE;
			}

		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		}

//		FixedChannelPool

		return flag;
	}
}
