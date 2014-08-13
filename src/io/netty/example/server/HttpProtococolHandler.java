package io.netty.example.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.List;

public class HttpProtococolHandler extends MessageToMessageDecoder<FullHttpRequest> {

	@Override
	protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg,
			List<Object> out) throws Exception {
		System.out.println(msg.uri()+msg.hashCode());
//		if(msg.uri().equals("/websocket")){
//			return ;
//		}
		out.add(msg.retain());
	}


}
