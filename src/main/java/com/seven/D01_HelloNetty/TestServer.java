package com.seven.D01_HelloNetty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TestServer {
    public static void main(String[] args) {
        //定义两个事件循环组
        EventLoopGroup boseGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            //启动服务端
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //服务端配置
            serverBootstrap.group(boseGroup,workGroup)
                            .channel(NioServerSocketChannel.class)      //定义通道
                            .childHandler(new TestServerInitializer()); //定义子处理器

            //绑定到端口号
            ChannelFuture channelFuture = serverBootstrap.bind(9001).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boseGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
