package cc.ymsoft.heart;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartBeatRequestHandler extends ChannelHandlerAdapter //ChannelDuplexHandler 
{

	/**
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#userEventTriggered(io.netty.channel.ChannelHandlerContext,
	 *      java.lang.Object)
	 */

	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				System.out.println("request read 空闲,disconnect");
				ctx.disconnect();
			} else if (event.state() == IdleState.WRITER_IDLE) {
				System.out.println("request write 空闲,heartBeat");
				ctx.writeAndFlush(buildHeartBeat(MessageType.HEARTBEAT_REQ.getType()));
			}
		}
	}

	/**
	 * 
	 * @return
	 * @author zhangwei<wei.zw@corp.netease.com>
	 */
	private NettyMessage buildHeartBeat(byte type) {
		NettyMessage msg = new NettyMessage();
		Header header = new Header();
		header.setType(type);
		msg.setHeader(header);
		return msg;
	}

}
