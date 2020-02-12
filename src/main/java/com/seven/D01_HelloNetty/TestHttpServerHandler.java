package com.seven.D01_HelloNetty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

//创建子处理器
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    //读取客户端发送的请求，并提供响应
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {

        if(httpObject instanceof HttpRequest) {
            //获取远程地址
            System.out.println(channelHandlerContext.channel().remoteAddress());
            //获取HttpObject类
            System.out.println(httpObject.getClass());

            HttpRequest httpRequest = (HttpRequest) httpObject;
            //打印请求方法名
            System.out.println(httpRequest.method().name());
            URI uri = new URI(httpRequest.uri());
            //浏览器会响应两次，其中favicon为网页图标
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求favicon.ico");
                return;
            }

            //构建响应,向客户端响应
            ByteBuf content = Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            //设置http响应头
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            channelHandlerContext.writeAndFlush(response);
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
        super.channelActive(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegister");
        super.channelRegistered(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdd");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregister");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
        super.channelInactive(ctx);
    }
}
