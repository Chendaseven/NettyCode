package com.seven.D03_chatExample;

import com.seven.D02_serverAndclient.MyServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

public class MyChatServer {
    //需要使用channelGroup定位为static，才能检测其他channel的加入与退出
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

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
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()));
                            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                            pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                            pipeline.addLast(new SimpleChannelInboundHandler<String>() {

                                //建立channelGroup存储channel链接
                                //private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                    Channel channel = ctx.channel();
                                    channelGroup.forEach(ch->{
                                        if(channel != ch){
                                            ch.writeAndFlush(channel.remoteAddress()+"，发送的消息："+msg);
                                        }else
                                            ch.writeAndFlush(channel.remoteAddress()+"自己发送的消息："+msg);
                                    });
                                }
                                //表示服务端和客户端已建立好链接
                                @Override
                                public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                                    Channel channel = ctx.channel();
                                    //客户端加入时广播加入信息
                                    channelGroup.writeAndFlush("【服务器】-"+channel.remoteAddress()+"加入\n");
                                    channelGroup.add(channel);
                                }
                                //channel断开时监听
                                @Override
                                public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
                                    Channel channel = ctx.channel();
                                    channelGroup.writeAndFlush("【服务器】-"+channel.remoteAddress()+"离开\n");
                                    //channelGroup.remove(channel);无需手动移除channel，channelGroup会自己调用remove方法

                                }
                                //channel活跃监听
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    Channel channel = ctx.channel();
                                    System.out.println(channel.remoteAddress()+"——上线");
                                }

                                @Override
                                public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                    Channel channel = ctx.channel();
                                    System.out.println(channel.remoteAddress()+"——下线");
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    super.exceptionCaught(ctx, cause);
                                }
                            });
                        }
                    });
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
