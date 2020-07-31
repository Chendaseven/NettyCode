/**
 * meituan.com Inc.
 * Copyright (c) 2010-2020 All Rights Reserved.
 */

package com.seven.wechat.P5_cheat.server;

import com.seven.wechat.Protocol.Example.PacketCode;
import com.seven.wechat.Protocol.Packet;
import com.seven.wechat.Protocol.Request.LoginRequestPacket;
import com.seven.wechat.Protocol.response.LoginResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 * @author chenpeng
 * @version :ServerHandler.java v1.0 2020/7/31 3:31 下午 chenpeng Exp $
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf requestByteBuf = (ByteBuf) msg;

        //解码
        Packet packet = PacketCode.getINSTANCE().decode(requestByteBuf);
        //判断是否是登录请求数据包
        if(packet instanceof LoginRequestPacket){
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
            System.out.println(new Date() + "：客户端开始登录");

            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            // 登录校验
            if (valid(loginRequestPacket)) {
                // 校验成功
                loginResponsePacket.setSuccess(true);
                System.out.println(new Date() + "：客户端登录成功");
            } else {
                // 校验失败
                loginResponsePacket.setReason("账号密码校验失败");
                loginResponsePacket.setSuccess(false);
            }
            ByteBuf responseByteBuf = PacketCode.getINSTANCE().encode(ctx.alloc(), loginResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }

    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }

/*    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        if(channel.isActive())
            ctx.close();
    }*/
}
