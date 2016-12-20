package cc.ymsoft.Framework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class RpcFramework {

	
	public RpcFramework() {
		int port = 14527;
		String ip = "127.0.0.1";
		
		int BIZ_GROUP_SIZE = Runtime.getRuntime().availableProcessors() * 2;

		int BIZ_THREAD_SIZE = 100;
		EventLoopGroup bossGroup = new NioEventLoopGroup(BIZ_GROUP_SIZE);
		EventLoopGroup workerGroup = new NioEventLoopGroup(BIZ_THREAD_SIZE);
		
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.childHandler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
				pipeline.addLast(new LengthFieldPrepender(4));
				pipeline.addLast("encoder", new ObjectEncoder());
				pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
				// pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
				// pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
				pipeline.addLast(new TcpServerHandler());
			}
		});
		
		try {
			bootstrap.bind(ip, port);
			
//			ChannelFuture future = bootstrap.bind(ip, port).sync();
//			future.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
//			bossGroup.shutdownGracefully();
//			workerGroup.shutdownGracefully();
		}

		System.out.println("TCP服务器已启动");
	}
	/**
	 * 服务注册
	 * 
	 * @param obj
	 *            需要注册的服务对象
	 * @param port
	 *            端口
	 * @param ip
	 *            地址
	 * @throws InterruptedException
	 */
	public static void regist(final Object obj, int port, String ip) throws InterruptedException {
		int BIZ_GROUP_SIZE = Runtime.getRuntime().availableProcessors() * 2;

		int BIZ_THREAD_SIZE = 100;
		EventLoopGroup bossGroup = new NioEventLoopGroup(BIZ_GROUP_SIZE);
		EventLoopGroup workerGroup = new NioEventLoopGroup(BIZ_THREAD_SIZE);
		
		if (obj == null) {
			throw new IllegalArgumentException("对象不能为null");
		}
		if (port <= 0 || port > 65535) {
			throw new IllegalArgumentException("错误的端口" + port);
		}
		
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.childHandler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
				pipeline.addLast(new LengthFieldPrepender(4));
				pipeline.addLast("encoder", new ObjectEncoder());
				pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
				// pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
				// pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
				pipeline.addLast(new TcpServerHandler(obj));
			}
		});
		
		ChannelFuture future = bootstrap.bind(ip, port).sync();
		future.channel().closeFuture().sync();

		System.out.println("TCP服务器已启动");
	}

	@SuppressWarnings("unchecked")
	public static <T> T getObj(Class<T> interfaceClass, final String host, final int port) {
		if (interfaceClass == null)
			throw new IllegalArgumentException("接口类型不能为空");
		if (!interfaceClass.isInterface())
			throw new IllegalArgumentException("类名" + interfaceClass.getName() + "必须是接口");
		if (host == null || host.length() == 0)
			throw new IllegalArgumentException("目标主机不能为空");
		if (port <= 0 || port > 65535)
			throw new IllegalArgumentException("端口错误：" + port);

		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] { interfaceClass },
				new InvocationHandler() {

					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

						MethodAndArgs mArgs = new MethodAndArgs(method.getName(), method.getParameterTypes(), args);
					
						final TcpClientHander tcpClientHander = new TcpClientHander();
						
						EventLoopGroup group = new NioEventLoopGroup();
						try {
							Bootstrap bootstrap = new Bootstrap();
							bootstrap.group(group);
							// b.channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY,true);
							bootstrap.channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true);

							bootstrap.handler(new ChannelInitializer<SocketChannel>() {
								@Override
								protected void initChannel(SocketChannel ch) throws Exception {
									ChannelPipeline pipeline = ch.pipeline();
									pipeline.addLast("frameDecoder",
											new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
									pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
									pipeline.addLast("encoder", new ObjectEncoder());
									pipeline.addLast("decoder",
											new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
									pipeline.addLast("handler", tcpClientHander);
								}
							});

							ChannelFuture f = bootstrap.connect(host, port).sync();

							f.channel().writeAndFlush(mArgs).sync();
							f.channel().closeFuture().sync();

						} catch (Exception e) {

						} finally {

							group.shutdownGracefully();
						}
						return tcpClientHander.getResponse();

					}
				});
	}
}
