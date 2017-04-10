package cc.ymsoft.ping;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import cc.ymsoft.heart.Header;
import cc.ymsoft.heart.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.ScheduledFuture;

public class HeartBeatReqHandler extends ChannelHandlerAdapter {

	private volatile ScheduledFuture<?> heartBeat;
	
	//线程安全心跳失败计数器
	private AtomicInteger unRecPongTimes = new AtomicInteger(1);

    public void channelRead0(ChannelHandlerContext ctx, Object msg)  
            throws Exception {
        NettyMessage message = (NettyMessage)msg;  
        //判断是否握手成功
        if(message.getHeader() != null && message.getHeader().getType() == (byte)4){
        //若握手成功，则启动无限循环定时器，定期发送心跳包
        	heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx), 0, 5000, TimeUnit.MILLISECONDS);
	        System.out.println("client not receive server pong msg，send heartbeat！");
        }else if(message.getHeader() != null  && message.getHeader().getType() == (byte)6){
        //如果服务器进行pong心跳回复，则清零失败心跳计数器
	        unRecPongTimes = new AtomicInteger(1);
	        System.out.println("client receive server pong msg :---->"+message);
        }else{
        	ctx.fireChannelRead(msg);
        }
    }  
    
    /**
     * 事件触发器，该处用来处理客户端空闲超时,发送心跳维持连接。
     */
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {  
            IdleStateEvent event = (IdleStateEvent) evt;  
            if (event.state() == IdleState.READER_IDLE) {
                /*读超时*/  
                System.out.println("===客户端===(READER_IDLE 读超时)");  
            } else if (event.state() == IdleState.WRITER_IDLE) {
                /*客户端写超时*/     
                System.out.println("===客户端===(WRITER_IDLE 写超时)");
                unRecPongTimes.getAndIncrement();  
                //服务端未进行pong心跳响应的次数小于3,则进行发送心跳，否则则断开连接。
                if(unRecPongTimes.intValue() < 3){
                //发送心跳，维持连接
                    ctx.channel().writeAndFlush(buildHeartBeat()) ; 
                    System.out.println("客户端：发送心跳");
                }else{  
                    ctx.channel().close();
                }  
            } else if (event.state() == IdleState.ALL_IDLE) {
                /*总超时*/
                System.out.println("===客户端===(ALL_IDLE 总超时)");  
            }  
        }  
    }
        
    private NettyMessage buildHeartBeat(){
		NettyMessage  message = new NettyMessage();
		Header header = new Header();
		header.setType((byte)5);//心跳请求
		message.setHeader(header);
		return message;
	}
    
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception{
	    if(heartBeat != null){
	    heartBeat.cancel(true);
	    heartBeat = null;
	    }
	    ctx.fireExceptionCaught(cause);
    }
    
  
    private class HeartBeatTask implements  Runnable{
    
	    private ChannelHandlerContext ctx;
		    public HeartBeatTask(final ChannelHandlerContext ctx){
		    this.ctx = ctx;
	    }
		public void run() {
			NettyMessage hearBeat = buildHeartBeat();
			System.out.println("client send heart beat to server:--->"+hearBeat);
			ctx.writeAndFlush(hearBeat);
		}
	
		private NettyMessage buildHeartBeat(){
			NettyMessage  message = new NettyMessage();
			Header header = new Header();
			header.setType((byte)5);//心跳请求
			message.setHeader(header);
			return message;
		}
	}


}