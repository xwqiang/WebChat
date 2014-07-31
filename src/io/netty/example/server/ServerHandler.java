package io.netty.example.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

import java.util.Date;

public class ServerHandler extends SimpleChannelInboundHandler<Object> {
	private WebSocketServerHandshaker handshaker;
	
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("active");
    	
    }
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if (msg instanceof FullHttpRequest) {
        	FullHttpRequest fullHttpRequest = (FullHttpRequest)msg;
            handleHttpRequest(ctx, (FullHttpRequest) fullHttpRequest);
            String uri = ((FullHttpRequest) msg).getUri();
            String uris[] = uri.split("/");
            ctx.channel().writeAndFlush(new TextWebSocketFrame("Http服务端返回信息 "+ uri));
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
	}

	
	
	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        // Handle a bad request.
        if (!req.getDecoderResult().isSuccess()) {
//            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }

        // Allow only GET methods.
        if (req.getMethod() != io.netty.handler.codec.http.HttpMethod.GET) {
//            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN));
            return;
        }


        // Handshake
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "wss://localhost:8080/websocket", null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }
	
	private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // Check for closing frame
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            //Room.leaveRoom(ctx);
            return;
        }
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass()
                    .getName()));
        }
        if ((frame instanceof TextWebSocketFrame)) {
        	String text = ((TextWebSocketFrame)frame).text();
        	System.out.println(text);
        	ctx.channel().writeAndFlush(new TextWebSocketFrame("服务端返回信息 "+ new Date()));
        }

    }

}