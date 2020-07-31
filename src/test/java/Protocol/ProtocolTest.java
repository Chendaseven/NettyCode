/**
 * meituan.com Inc.
 * Copyright (c) 2010-2020 All Rights Reserved.
 */

package Protocol;

import com.seven.wechat.Protocol.Example.PacketCode;
import com.seven.wechat.Protocol.Packet;
import com.seven.wechat.Protocol.Request.LoginRequestPacket;
import com.seven.wechat.Protocol.Serialize.JSONSerializer;
import com.seven.wechat.Protocol.Serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 *
 * </p>
 * @author chenpeng
 * @version :ProtocolTest.java v1.0 2020/7/30 4:37 下午 chenpeng Exp $
 */
public class ProtocolTest {
    @Test
    public void encode(){
        Serializer serializer = new JSONSerializer();

        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        loginRequestPacket.setVersion(((byte) 1));
        loginRequestPacket.setUserId("123");
        loginRequestPacket.setUserName("zhangsan");
        loginRequestPacket.setPassWord("password");

        ByteBuf byteBuf = PacketCode.getINSTANCE().encode((ByteBufAllocator) ByteBufAllocator.DEFAULT.ioBuffer(),loginRequestPacket);

        Packet packet = PacketCode.getINSTANCE().decode(byteBuf);
        Assert.assertArrayEquals(serializer.serialize(loginRequestPacket), serializer.serialize(packet));
    }

}
