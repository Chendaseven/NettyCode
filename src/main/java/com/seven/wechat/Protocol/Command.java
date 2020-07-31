/**
 * meituan.com Inc.
 * Copyright (c) 2010-2020 All Rights Reserved.
 */

package com.seven.wechat.Protocol;

/**
 * <p>
 *
 * </p>
 * @author chenpeng
 * @version :Command.java v1.0 2020/7/30 3:29 下午 chenpeng Exp $
 * 指令接口
 */
public interface Command {
    //登录指令
    byte LOGIN_REQUEST = 1;
    //登录返回指令
    byte LOGIN_RESPONSE = 2;
}
