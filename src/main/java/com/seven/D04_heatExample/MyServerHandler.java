package com.seven.D04_heatExample;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.UUID;

public class MyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;

            String evenType=null;
            switch (event.state()){
                case READER_IDLE:
                    evenType="读空闲";
                    break;
                case WRITER_IDLE:
                    evenType="写空闲";
                    break;
                case ALL_IDLE:
                    evenType="读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + "超时事件" + evenType);
            ctx.channel().close();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        super.channelActive(ctx);
    }
}
