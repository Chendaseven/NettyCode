/**
 * meituan.com Inc.
 * Copyright (c) 2010-2020 All Rights Reserved.
 */

package com.seven.Protocol;

import io.netty.buffer.ByteBufAllocator;

/**
 * <p>
 *
 * </p>
 * @author chenpeng
 * @version :Packet.java v1.0 2020/7/30 3:27 下午 chenpeng Exp $
 * 定义通信过程中的Java对象
 */
public abstract class Packet {
    //协议版本
    private Byte version = 1;
    //指令
    public abstract Byte getCommand();

    public Byte getVersion() {
        return version;
    }

    public void setVersion(Byte version) {
        this.version = version;
    }
}
