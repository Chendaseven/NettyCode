/**
 * meituan.com Inc.
 * Copyright (c) 2010-2020 All Rights Reserved.
 */

package com.seven.wechat.Protocol.Serialize;

/**
 * <p>
 *
 * </p>
 * @author chenpeng
 * @version :Serializer.java v1.0 2020/7/30 3:35 下午 chenpeng Exp $
 * 序列化接口
 */
public interface Serializer {
    /**
     * 序列化算法
     */
    byte getSerializerAlgorithm();

    /**
     * java 对象转换成二进制
     */
    byte[] serialize(Object object);

    /**
     * 二进制转换成 java 对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);

    //定义一下序列化算法的类型以及默认序列化算法
    byte JSON_SERIALIZER = 1;
    Serializer DEFAULT = new JSONSerializer();

}
