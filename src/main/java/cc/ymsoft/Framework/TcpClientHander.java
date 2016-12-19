package cc.ymsoft.Framework;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 客户端处理
 * 
 * @author hadoop
 *
 */
public class TcpClientHander extends ChannelInboundHandlerAdapter {
	private Object response;

	public Object getResponse() {
		return response;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		response = msg;
		System.out.println("client接收到服务器返回的消息:" + msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("client exception is general");
	}
}