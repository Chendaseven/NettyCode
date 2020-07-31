/**
 * meituan.com Inc.
 * Copyright (c) 2010-2020 All Rights Reserved.
 */

package com.seven.wechat.Protocol.Example;

import com.seven.wechat.Protocol.Command;
import com.seven.wechat.Protocol.Packet;
import com.seven.wechat.Protocol.Request.LoginRequestPacket;
import com.seven.wechat.Protocol.Serialize.JSONSerializer;
import com.seven.wechat.Protocol.Serialize.Serializer;
import com.seven.wechat.Protocol.Serialize.SerializerAlgorithm;
import com.seven.wechat.Protocol.response.LoginResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * <p>
 *
 * </p>
 * @author chenpeng
 * @version :encode.java v1.0 2020/7/30 3:49 下午 chenpeng Exp $
 */
public class PacketCode {
    private static final int MAGIC_NUMBER = 0x12345678;

    //改为单例模式，
    private volatile static PacketCode INSTANCE = null;
    private PacketCode(){}

    public static PacketCode getINSTANCE(){
        if(INSTANCE == null){
            synchronized (PacketCode.class){
                if (INSTANCE == null) {
                    INSTANCE = new PacketCode();
                }
            }
        }
        return INSTANCE;
    }

    public ByteBuf encode(ByteBufAllocator alloc,Packet packet){

        //创建ByteBuf对象
        ByteBuf byteBuf = alloc.DEFAULT.ioBuffer();
        //序列化java对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        //数据通过协议写入
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    public Packet decode(ByteBuf byteBuf){
        // 跳过 magic number
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        //序列化算法标识
        byte serializeAlgorithm = byteBuf.readByte();

        // 指令
        byte command = byteBuf.readByte();

        // 数据包长度
        int length = byteBuf.readInt();
        //读取数据
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }
        return null;
    }

    public Serializer getSerializer(byte serializeAlgorithm){
        Serializer serializer = Serializer.DEFAULT;
        switch (serializeAlgorithm){
            case SerializerAlgorithm.JSON:
                return new JSONSerializer();
        }
        return serializer;
    }

    public Class<? extends Packet> getRequestType(byte command){
        switch (command){
            case Command.LOGIN_REQUEST:
                return LoginRequestPacket.class;
            case Command.LOGIN_RESPONSE:
                return LoginResponsePacket.class;
        }
        return Packet.class;
    }

}
