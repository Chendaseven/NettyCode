/**
 * meituan.com Inc.
 * Copyright (c) 2010-2020 All Rights Reserved.
 */

package com.seven.Protocol.Request;

import com.seven.Protocol.Command;
import com.seven.Protocol.Packet;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 * @author chenpeng
 * @version :LoginRequestPacket.java v1.0 2020/7/30 3:30 下午 chenpeng Exp $
 * 定义登录请求数据包
 */
@Data
public class LoginRequestPacket extends Packet {
    private Integer userId;
    private String userName;
    private String passWord;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
