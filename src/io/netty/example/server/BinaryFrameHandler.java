package io.netty.example.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

public class BinaryFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {

	@Override
	protected void channelRead0(ChannelHandlerContext arg0,
			BinaryWebSocketFrame arg1) throws Exception {
		System.out.println("binary"+arg1.toString());
		
	}


}
