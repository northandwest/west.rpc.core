package cc.ymsoft.ping;

import java.util.concurrent.atomic.AtomicInteger;

import cc.ymsoft.heart.Header;
import cc.ymsoft.heart.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartBeatRespHandler extends ChannelHandlerAdapter { 
	  //线程安全心跳失败计数器
	  private AtomicInteger unRecPingTimes = new AtomicInteger(1);
	  
	  public void channelRead0(ChannelHandlerContext ctx, Object msg)  
	           throws Exception {
	  NettyMessage message = (NettyMessage)msg;
	  //接收客户端心跳信息
	      if(message.getHeader() != null  && message.getHeader().getType() == (byte)5){
	       //清零心跳失败计数器
	    	 unRecPingTimes = new AtomicInteger(1);
	         System.out.println("server receive client ping msg :---->"+message);
	         //接收客户端心跳后,进行心跳响应
	         NettyMessage replyMsg = buildHeartBeat();
	         ctx.writeAndFlush(replyMsg);
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
	               System.out.println("===服务器端===(READER_IDLE 读超时)");
	               unRecPingTimes.getAndIncrement(); 
	             //客户端未进行ping心跳发送的次数等于3,断开此连接
	               if(unRecPingTimes.intValue() == 10){  
	            	   	ctx.disconnect();
	            	   	System.out.println("此客户端连接超时，服务器主动关闭此连接....");
	               } 
	           } else if (event.state() == IdleState.WRITER_IDLE) {  
	               /*服务端写超时*/     
	               System.out.println("===服务器端===(WRITER_IDLE 写超时)");
	               
	           } else if (event.state() == IdleState.ALL_IDLE) {  
	               /*总超时*/  
	               System.out.println("===服务器端===(ALL_IDLE 总超时)");  
	           }  
	       }  
	   }
	   
	  
	  /**
	   * 创建心跳响应消息
	   * @return
	   */
	  private NettyMessage buildHeartBeat(){
	  NettyMessage message = new NettyMessage();
	  Header header = new Header();
	  header.setType((byte)6);
	  message.setHeader(header);
	  return message;
	  }

	}