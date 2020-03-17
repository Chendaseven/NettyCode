package com.seven.D04_heatExample;

import com.seven.D02_serverAndclient.MyServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class MyServer {
    public static void main(String[] args) {
        //获取链接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            //启动服务端
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //服务端配置
            serverBootstrap.group(bossGroup,workGroup)
                            .channel(NioServerSocketChannel.class)
                            .handler(new LoggingHandler(LogLevel.INFO))
                            .childHandler(new MyServerInitializer()); //针对workGroup起作用
//                            .handler() 针对bossGroup起作用
            //绑定到端口号

            ChannelFuture channelFuture = serverBootstrap.bind(9001).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
