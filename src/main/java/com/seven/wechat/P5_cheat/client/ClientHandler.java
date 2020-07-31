/**
 * meituan.com Inc.
 * Copyright (c) 2010-2020 All Rights Reserved.
 */

package com.seven.wechat.P5_cheat.client;

import com.seven.wechat.Protocol.Example.PacketCode;
import com.seven.wechat.Protocol.Packet;
import com.seven.wechat.Protocol.Request.LoginRequestPacket;
import com.seven.wechat.Protocol.response.LoginResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.UUID;

/**
 * <p>
 *
 * </p>
 * @author chenpeng
 * @version :ClientHandler.java v1.0 2020/7/31 3:37 下午 chenpeng Exp $
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + "：客户端开始登录");

        //创建登录对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUserName("flash");
        loginRequestPacket.setPassWord("pwd");

        //编码
        ByteBuf byteBuf = PacketCode.getINSTANCE().encode(ctx.alloc(), loginRequestPacket);
        //写数据
        ctx.channel().writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        Packet packet = PacketCode.getINSTANCE().decode(byteBuf);
        if(packet instanceof LoginResponsePacket){
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;
            if(loginResponsePacket.isSuccess())
                System.out.println(new Date() + "：客户端登录成功");
            else
                System.out.println(new Date() + "：客户端登录失败，原因：" + loginResponsePacket.getReason());
        }
    }
}
