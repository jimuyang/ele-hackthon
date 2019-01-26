package me.ele.hackathon.example.ghost;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by lanjiangang on 2018/12/23.
 */
public class WebServer {

    EventLoopGroup eventLoopGroup = null;
    Channel ch = null;

    public void start(int port) throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap().group(eventLoopGroup).childHandler(new HttpServerInitializer())
                .channel(NioServerSocketChannel.class);

         ch = bootstrap.bind(port).sync().channel();

    }

    public void sync() throws InterruptedException {
        ch.closeFuture().sync();
    }

    public void stop() {
        eventLoopGroup.shutdownGracefully();

    }
}
