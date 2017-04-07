package com.bucuoa.west.rpc.remoting.server.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bucuoa.west.rpc.core.RpcRequest;
import com.bucuoa.west.rpc.core.RpcResponse;
import com.bucuoa.west.rpc.remoting.server.ProviderStubInvoker;
import com.bucuoa.west.rpc.remoting.server.RemoteServiceCenter;
import com.bucuoa.west.rpc.utils.StringUtil;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerHandler.class);

	public RpcServerHandler() {

	}

	/*
	 * 覆盖channelActive 方法在channel被启用的时候触发（在建立连接的时候） 覆盖了 channelActive()
	 * 事件处理方法。服务端监听到客户端活动
	 */
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		LOGGER.debug("channelActive:{}",ctx.name());

	}

	/*
	 * (non-Javadoc) 覆盖了 handlerAdded() 事件处理方法。 每当从服务端收到新的客户端连接时
	 */
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		super.handlerAdded(ctx);
		LOGGER.debug("handlerAdded:{}",ctx.name());
	}

	/*
	 * (non-Javadoc) .覆盖了 handlerRemoved() 事件处理方法。 每当从服务端收到客户端断开时
	 */
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		super.handlerRemoved(ctx);
		LOGGER.debug("handlerRemoved:{}",ctx.name());

	}

	@Override
	public void channelRead0(final ChannelHandlerContext ctx, RpcRequest request) throws Exception {
		// 创建并初始化 RPC 响应对象
		RpcResponse response = new RpcResponse();
		response.setRequestId(request.getRequestId());
		try {
			Object result = handle(request);

			response.setResult(result);
		} catch (Exception e) {
			LOGGER.error("handle result failure", e);
			response.setException(e);
		}
		// 写入 RPC 响应对象并自动关闭连接
		ctx.writeAndFlush(response);//.addListener(ChannelFutureListener.CLOSE);
		
//        ctx.writeAndFlush(response).addListener(new FutureListener() {
//            @Override
//            public void operationComplete(Future future) throws Exception {
//                if (future.isSuccess()) {
//                    if (LOGGER.isTraceEnabled()) {
//                    	LOGGER.trace("Response write back {}", future.isSuccess());
//                    }
//                    
//                } else if (!future.isSuccess()) {
//                    Throwable throwable = future.cause();
//                    LOGGER.error("[west rpc-23009]Failed to send response to {} for msg id: {}, Cause by:",
//                    		throwable,"","");
//                }
//            }
//        });
        
	}

	private Object handle(RpcRequest request) throws Exception {
		// 获取服务对象
		String serviceName = request.getInterfaceName();
		String serviceVersion = request.getServiceVersion();
		if (StringUtil.isNotEmpty(serviceVersion)) {
			serviceName += "-" + serviceVersion;
		}

		String beanid = RemoteServiceCenter.getInterface(serviceName);
		ProviderStubInvoker serviceInvoker = RemoteServiceCenter.getService(beanid);

		return serviceInvoker.invoke(request);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		LOGGER.error("server caught exception", cause);
		ctx.close();
	}
}
