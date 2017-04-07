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


public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerHandler.class);

    public RpcServerHandler() {
       
    }

    /*
     * (non-Javadoc)
     * 覆盖了 handlerAdded() 事件处理方法。
     * 每当从服务端收到新的客户端连接时
     */
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }

    /*
     * (non-Javadoc)
     * .覆盖了 handlerRemoved() 事件处理方法。
     * 每当从服务端收到客户端断开时
     */
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
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
        // 写入 RPC 响应对象不自动关闭连接
        ctx.writeAndFlush(response);//.addListener(ChannelFutureListener.CLOSE);
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
