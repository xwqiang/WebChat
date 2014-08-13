package io.netty.example.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class WebSocketServer {

	protected static final ChannelHandler ContinuationFrameHandler = null;
	private int port;

	public WebSocketServer(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		System.out.println("server started port:" + port);

		EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap(); // (2)
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class) // (3)
					.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
								@Override
								public void initChannel(SocketChannel ch)
										throws Exception {
									ChannelPipeline pipeline = ch.pipeline();
									pipeline.addLast("newHttpServerCodec", new HttpServerCodec());
									pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
									pipeline.addLast("httpHandler",new HttpProtococolHandler());//自定义处理http转化为websocket请求之前的逻辑
									pipeline.addLast("websocket", new WebSocketServerProtocolHandler("/websocket"));
									
									pipeline.addLast("binaryFrameHandler", new BinaryFrameHandler());
									pipeline.addLast("textFrameHandler", new TextFrameHandler());
							        pipeline.addLast("ContinuationFrameHandler",new ContinuationFrameHandler());
								}

							}).option(ChannelOption.SO_BACKLOG, 128) // (5)
					.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

			// Bind and start to accept incoming connections.
			ChannelFuture f = b.bind(port).sync(); // (7)
			System.out.println("server started");
			// Wait until the server socket is closed.
			// In this example, this does not happen, but you can do that to
			// gracefully
			// shut down your server.
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port = 8080;
		new WebSocketServer(port).run();
	}
}