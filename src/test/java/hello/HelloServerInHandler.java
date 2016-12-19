package hello;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

// 该handler是InboundHandler类型
public class HelloServerInHandler extends ChannelInboundHandlerAdapter {
	@Override
	public boolean isSharable() {
		System.out.println("==============handler-sharable==============");
		return super.isSharable();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("==============channel-register==============");
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("==============channel-unregister==============");
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("==============channel-active==============");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("==============channel-inactive==============");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		System.out.println("==============channel-read==============");

		ByteBuf result = (ByteBuf) msg;
		byte[] result1 = new byte[result.readableBytes()];
		// msg中存储的是ByteBuf类型的数据，把数据读取到byte[]中
		result.readBytes(result1);
		String resultStr = new String(result1);
		// 接收并打印客户端的信息
		System.out.println("Client said:" + resultStr);
		// 释放资源，这行很关键
		result.release();

		// 向客户端发送消息
		String response = "I am ok!";
		// 在当前场景下，发送的数据必须转换成ByteBuf数组
		ByteBuf encoded = ctx.alloc().buffer(4 * response.length());
		encoded.writeBytes(response.getBytes());
		ctx.writeAndFlush(encoded);
		Thread.sleep(10000);
		System.out.println("thread sleep end");

		ctx.close();

		// Thread.sleep(10000);
		// System.out.println("thread sleep end");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("==============channel-read-complete==============");
		ctx.flush();
	}
}