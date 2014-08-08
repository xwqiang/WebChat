package io.netty.example.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;

public class ContinuationFrameHandler extends SimpleChannelInboundHandler<ContinuationWebSocketFrame> {

	@Override
	protected void channelRead0(ChannelHandlerContext arg0,
			ContinuationWebSocketFrame arg1) throws Exception {
		System.out.println("continuation"+arg1.text());
	}


}
