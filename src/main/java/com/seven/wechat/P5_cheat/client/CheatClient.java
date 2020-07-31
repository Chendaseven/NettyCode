/**
 * meituan.com Inc.
 * Copyright (c) 2010-2020 All Rights Reserved.
 */

package com.seven.wechat.P5_cheat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 * @author chenpeng
 * @version :CheatClient.java v1.0 2020/7/31 3:34 下午 chenpeng Exp $
 */
public class CheatClient {

    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ClientHandler());
                        }
                    });
            //建立连接
            bootstrap.connect("127.0.0.1",8000).addListener(future -> {
                connect(bootstrap,"127.0.0.1",1001,5,5);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                System.err.println(new Date() + ":连接失败，第" + order + "次重连");
                bootstrap.config().group().schedule(() -> connect(bootstrap,host,port,retry-1,MAX_RETRY),delay,
                        TimeUnit.SECONDS);
            }

        });
    }
}
