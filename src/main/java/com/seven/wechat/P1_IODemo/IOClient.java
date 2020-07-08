package com.seven.wechat.P1_IODemo; /**
 * meituan.com Inc.
 * Copyright (c) 2010-2020 All Rights Reserved.
 */

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 * @author chenpeng
 * @version :com.seven.wechat2.P1_IODemo.IOClient.java v1.0 2020/7/8 3:05 下午 chenpeng Exp $
 */
public class IOClient {

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1",8000);
                while (true){
                    socket.getOutputStream().write((new Date() + ": hello world").getBytes());
                    Thread.sleep(2000);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
