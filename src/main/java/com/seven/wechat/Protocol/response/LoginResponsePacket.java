/**
 * meituan.com Inc.
 * Copyright (c) 2010-2020 All Rights Reserved.
 */

package com.seven.wechat.Protocol.response;

import com.seven.wechat.Protocol.Command;
import com.seven.wechat.Protocol.Packet;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 * @author chenpeng
 * @version :LoginResponsePacket.java v1.0 2020/7/31 5:26 下午 chenpeng Exp $
 */
@Data
public class LoginResponsePacket extends Packet {
    private boolean success;
    private String reason;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }
}
