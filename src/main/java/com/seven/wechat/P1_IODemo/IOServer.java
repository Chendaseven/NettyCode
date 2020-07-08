package com.seven.wechat.P1_IODemo; /**
 * meituan.com Inc.
 * Copyright (c) 2010-2020 All Rights Reserved.
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p>
 *
 * </p>
 * @author chenpeng
 * @version :com.seven.wechat2.P1_IODemo.IOServer.java v1.0 2020/7/8 2:57 下午 chenpeng Exp $
 */
public class IOServer {
    /*
    * Server 端首先创建了一个serverSocket来监听 8000 端口，
    * 然后创建一个线程，线程里面不断调用阻塞方法 serversocket.accept();获取新的连接，见1，
    * 当获取到新的连接之后，给每条连接创建一个新的线程，这个线程负责从该连接中读取数据，见2，
    * 然后读取数据是以字节流的方式，见3
    *
    * */
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);
        //1、接收新连接线程
        new Thread(() -> {
            while (true) {
                try {
                    //2、阻塞方法获取新的连接
                    Socket socket = serverSocket.accept();
                    //3、每一个新的连接都创建一个线程，负责读取数据
                    new Thread(() -> {
                        try {
                            int len;
                            byte[] data = new byte[1024];
                            InputStream inputStream = socket.getInputStream();
                            while ((len = inputStream.read(data)) != -1){
                                System.out.println(new String(data,0,len));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
