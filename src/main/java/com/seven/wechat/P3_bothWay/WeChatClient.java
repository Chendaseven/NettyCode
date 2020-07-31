/**
 * meituan.com Inc.
 * Copyright (c) 2010-2020 All Rights Reserved.
 */

package com.seven.wechat.P3_bothWay;

import com.seven.wechat.P5_cheat.client.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 * @author chenpeng
 * @version :WeChatClient.java v1.0 2020/7/10 3:32 下午 chenpeng Exp $
 */
public class WeChatClient {

    public static void main(String[] args) {
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                //指定线程模型
                .group(workGroup)
                //指定IO类型为NIO
                .channel(NioSocketChannel.class)
                //IO处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //逻辑处理器，负责向服务端写数据
                        socketChannel.pipeline().addLast(new FirstClientHandler());
                        socketChannel.pipeline().addLast(new ClientHandler());
                    }
                });
        //建立连接
        bootstrap.connect("127.0.0.1",8000).addListener(future -> {
                connect(bootstrap,"127.0.0.1",1001,5,5);
        });
    }

    public static void connect(Bootstrap bootstrap,String host,int port,int retry,int MAX_RETRY){
        bootstrap.connect(host,port).addListener(future -> {
            if(future.isSuccess()){
                System.out.println("连接成功！");
            }else if(retry == 0){
                System.err.println("重试次数已用完，放弃连接");
            }else {
                //计算第几次重连
                int order = (MAX_RETRY - retry) + 1;
                //重连的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ":连接失败，第"+order+"次重连");
                bootstrap.config().group().schedule(() -> connect(bootstrap,host,port,retry-1,MAX_RETRY),delay,
                        TimeUnit.SECONDS);
            }

        });
    }

    static class FirstClientHandler extends ChannelInboundHandlerAdapter{
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println(new Date() + "：客户端写出数据");
            //1.获取数据
            ByteBuf buffer = getByteBuf(ctx);
            //2.写数据
            ctx.channel().writeAndFlush(buffer);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf byteBuf = (ByteBuf) msg;

            System.out.println(new Date() + ": 客户端读到数据 -> " + byteBuf.toString(Charset.forName("utf-8")));
        }

        private ByteBuf getByteBuf(ChannelHandlerContext ctx){
            //1.获取二进制抽象 ByteBuf
            ByteBuf byteBuf = ctx.alloc().buffer();

            //2.准备数据，指定utf-8
            byte[] bytes = "超人你好".getBytes(Charset.forName("utf-8"));

            //3.填充数据到ByteBuf
            byteBuf.writeBytes(bytes);

            return byteBuf;
        }
    }
}
