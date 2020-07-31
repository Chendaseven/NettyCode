/**
 * meituan.com Inc.
 * Copyright (c) 2010-2020 All Rights Reserved.
 */

package com.seven.wechat.Protocol.Serialize;

import com.alibaba.fastjson.JSON;

/**
 * <p>
 *
 * </p>
 * @author chenpeng
 * @version :JSONSerializer.java v1.0 2020/7/30 3:39 下午 chenpeng Exp $
 */
public class JSONSerializer implements Serializer{
    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        //使用fastJSON序列化
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        //使用fastJSON反序列化
        return JSON.parseObject(bytes,clazz);
    }
}
