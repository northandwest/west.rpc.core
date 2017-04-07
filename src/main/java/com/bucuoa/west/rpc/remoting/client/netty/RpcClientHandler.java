package com.bucuoa.west.rpc.remoting.client.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bucuoa.west.rpc.core.RpcResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RpcClientHandler.class);

	@Override
	public void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
//		this.response = response;
		LOGGER.debug("response channelRead0: {}", response.getResult());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.error("api caught exception", cause);
		ctx.close();
	}
	
	/*
	 * 覆盖channelActive 方法在channel被启用的时候触发（在建立连接的时候） 覆盖了 channelActive()
	 * 事件处理方法。服务端监听到客户端活动
	 */
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		LOGGER.debug("RpcClientHandler channelActive:{}",ctx.name());

	}

	/*
	 * (non-Javadoc) 覆盖了 handlerAdded() 事件处理方法。 每当从服务端收到新的客户端连接时
	 */
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		super.handlerAdded(ctx);
		LOGGER.debug("RpcClientHandler handlerAdded:{}",ctx.name());
	}

	/*
	 * (non-Javadoc) .覆盖了 handlerRemoved() 事件处理方法。 每当从服务端收到客户端断开时
	 */
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		super.handlerRemoved(ctx);
		LOGGER.debug(" RpcClientHandler handlerRemoved:{}",ctx.name());

	}

	
}
