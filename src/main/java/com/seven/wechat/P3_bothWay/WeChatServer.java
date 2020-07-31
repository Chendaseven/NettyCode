/**
 * meituan.com Inc.
 * Copyright (c) 2010-2020 All Rights Reserved.
 */

package com.seven.wechat.P3_bothWay;

import com.seven.wechat.P5_cheat.server.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 * @author chenpeng
 * @version :ChatServer.java v1.0 2020/7/10 3:21 下午 chenpeng Exp $
 */
public class WeChatServer {

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        //启动服务器
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //服务器配置
        serverBootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new FirstServerHandler());
                        nioSocketChannel.pipeline().addLast(new ServerHandler());
                    }
                });
        serverBootstrap.bind(1000).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                bind(serverBootstrap,1000);
            }
        });
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

    static class FirstServerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf byteBuf = (ByteBuf) msg;
            System.out.println(new Date() + "：服务端读取数据 ——>" + byteBuf.toString(Charset.forName("utf-8")));

            //回复数据到client
            System.out.println(new Date()+"：服务端写出数据");
            ByteBuf out = getByteBuf(ctx);
            ctx.channel().writeAndFlush(out);
        }

        private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
            byte[] bytes = "你好，欢迎关注我的微信公众号，《CSRobot》!".getBytes(Charset.forName("utf-8"));

            ByteBuf buffer = ctx.alloc().buffer();

            buffer.writeBytes(bytes);

            return buffer;
        }
    }
}
