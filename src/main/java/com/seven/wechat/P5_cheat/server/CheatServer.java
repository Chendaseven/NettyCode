/**
 * meituan.com Inc.
 * Copyright (c) 2010-2020 All Rights Reserved.
 */

package com.seven.wechat.P5_cheat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * <p>
 *
 * </p>
 * @author chenpeng
 * @version :cheatServer.java v1.0 2020/7/31 3:24 下午 chenpeng Exp $
 */
public class CheatServer {
    public static void main(String[] args) {
        //获取连接
         EventLoopGroup bossGroup = new NioEventLoopGroup();
         EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            //启动服务端
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //服务端配置
            serverBootstrap.group(bossGroup,workGroup)
                            .channel(NioServerSocketChannel.class)
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel nioSocketChannel) throws Exception {
                                    nioSocketChannel.pipeline().addLast(new ServerHandler());
                                }
                            });

            serverBootstrap.bind(1000).addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    bind(serverBootstrap,1000);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void bind(final ServerBootstrap serverBootstrap,final int port){
        serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if(future.isSuccess())
                    System.out.println("端口["+port+"]端口绑定成功");
                else{
                    System.err.println("端口["+port+"]端口绑定失败");
                    bind(serverBootstrap,port+1);
                }
            }
        });
    }
}
