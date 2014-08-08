package io.netty.example.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;


public class TextFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	@Override
	protected void channelRead0(ChannelHandlerContext arg0,
			TextWebSocketFrame arg1) throws Exception {
		System.out.println("TextWebSocketFrame"+arg1.text());
		
	}


}
