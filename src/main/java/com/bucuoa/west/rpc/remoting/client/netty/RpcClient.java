package com.bucuoa.west.rpc.remoting.client.netty;

import java.util.concurrent.TimeUnit;

import com.bucuoa.west.rpc.core.RpcRequest;
import com.bucuoa.west.rpc.core.RpcResponse;
import com.bucuoa.west.rpc.remoting.protocal.netty.RpcDecoder;
import com.bucuoa.west.rpc.remoting.protocal.netty.RpcEncoder;
import com.bucuoa.west.rpc.serializer.Serializer;
import com.bucuoa.west.rpc.serializer.protostuff.ProtostuffSerializer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class RpcClient {
	
	private Serializer serializer;

	private final String host;
	private final int port;
	
	public RpcClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public RpcResponse send(RpcRequest request) throws Exception {

		serializer = new ProtostuffSerializer();
		EventLoopGroup group = new NioEventLoopGroup();

		try {

			// 创建并初始化 Netty 客户端 Bootstrap 对象
			final RpcClientHandler rpcClientHandler = new RpcClientHandler();
			
			Bootstrap bootstrap = initBootstrap(group, rpcClientHandler);
			
			ChannelFuture future = bootstrap.connect(host, port).sync();

			Channel channel = future.channel();
			channel.writeAndFlush(request).sync();
			channel.closeFuture().sync();
			// 返回 RPC 响应对象
			return rpcClientHandler.getResponse();
		} finally {
			 group.shutdownGracefully();
		}
	}

	private Bootstrap initBootstrap(EventLoopGroup group, final RpcClientHandler rpcClientHandler) {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group);
		bootstrap.channel(NioSocketChannel.class);

		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel channel) throws Exception {
				ChannelPipeline pipeline = channel.pipeline();
				pipeline.addLast(new RpcEncoder(RpcRequest.class, serializer)); // 编码RPC请求
				pipeline.addLast(new RpcDecoder(RpcResponse.class, serializer)); // 解码RPC响应
				pipeline.addLast(new IdleStateHandler(120, 0, 0, TimeUnit.SECONDS));
				pipeline.addLast(rpcClientHandler); // 处理 RPC 响应
			}
		});
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		
		return bootstrap;
	}
}
