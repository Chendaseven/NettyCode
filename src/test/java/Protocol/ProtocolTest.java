/**
 * meituan.com Inc.
 * Copyright (c) 2010-2020 All Rights Reserved.
 */

package Protocol;

import com.seven.Protocol.Example.PacketCode;
import com.seven.Protocol.Packet;
import com.seven.Protocol.Request.LoginRequestPacket;
import com.seven.Protocol.Serialize.JSONSerializer;
import com.seven.Protocol.Serialize.Serializer;
import io.netty.buffer.ByteBuf;
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
        loginRequestPacket.setUserId(123);
        loginRequestPacket.setUserName("zhangsan");
        loginRequestPacket.setPassWord("password");

        PacketCode packetCode = new PacketCode();
        ByteBuf byteBuf = packetCode.encode(loginRequestPacket);

        Packet packet = packetCode.decode(byteBuf);
        Assert.assertArrayEquals(serializer.serialize(loginRequestPacket), serializer.serialize(packet));
    }

}
