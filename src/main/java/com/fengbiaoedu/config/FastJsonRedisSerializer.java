package com.fengbiaoedu.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;

import java.nio.charset.Charset;

/**
 * Created by Administrator on 2018/5/17.
 */
public class FastJsonRedisSerializer<T> implements RedisSerializer<T> {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private Class<T> clazz;

    public FastJsonRedisSerializer(Class<T> clazz){
        this.clazz = clazz;
    }

    /**
     * 自定义序列化工具通过类名来序列化
     * @param t
     * @return
     * @throws SerializationException
     */
    @Nullable
    @Override
    public byte[] serialize(@Nullable T t) throws SerializationException {
        if(null == t){
            return new byte[0];
        }
        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    @Nullable
    @Override
    public T deserialize(@Nullable byte[] bytes) throws SerializationException {
        if(null == bytes || bytes.length <= 0){
            return null;
        }
        String str = new String(bytes,DEFAULT_CHARSET);
        return (T) JSON.parseObject(str,clazz);
    }
}
