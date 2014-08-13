package io.netty.example.server;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.io.IOException;

public class HelloClient {
    
    public static String host = "localhost";
    public static int port = 8080;

    /**
     * @param args
     * @throws InterruptedException 
     * @throws IOException 
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
            .channel(NioSocketChannel.class)
            .handler(new HelloClientInitializer());

            Channel ch = b.connect(host, port).sync().channel();
            ch.writeAndFlush(new TextWebSocketFrame("aa----"));
            
//            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//            for (;;) {
//                String line = in.readLine();
//                if (line == null) {
//                    continue;
//                }
//                ch.writeAndFlush(line + "\r\n");
//            }
        } finally {
            group.shutdownGracefully();
        }
    }
}