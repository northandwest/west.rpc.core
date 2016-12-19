package cc.ymsoft.Framework;

import java.lang.reflect.Method;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TcpServerHandler extends ChannelInboundHandlerAdapter {

	private Object obj;
	private Object response;
	
	public TcpServerHandler()
	{
		
	}

	public TcpServerHandler(Object obj) {
		super();
		this.obj = obj;

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//		MethodAndArgs methodAndArgs = (MethodAndArgs) msg;
//		Method method = obj.getClass().getMethod(methodAndArgs.getMethodName(), methodAndArgs.getTypes());
//		response = method.invoke(obj, methodAndArgs.getObjects());
		ctx.writeAndFlush("hi i am ok!");
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("client die");
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelActive>>>>>>>>");
		ctx.writeAndFlush("success ok");
		ctx.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("server fail");
	}
}