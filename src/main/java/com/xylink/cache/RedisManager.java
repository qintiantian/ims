package com.xylink.cache;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.xylink.protobuf.Protocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Field;

/**
 * Created by konglk on 2018/8/15.
 */
@Component
public class RedisManager {

    @Autowired
    private JedisPool pool;
    @Autowired
    private RedisTemplate redisTemplate;

    public <T extends Message> void  set(Object key, T value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(SerializerHelper.serialize(key), value.toByteArray());
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }

    public <T extends Message> void lpush(Object key, T value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.lpush(SerializerHelper.serialize(key), value.toByteArray());
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }

    public Protocol.CPrivateChat rpop(Object key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            byte[] value = jedis.rpop(SerializerHelper.serialize(key));
            if(value == null || value.length == 0)
                return null;
            return Protocol.CPrivateChat.parseFrom(value);
        }catch (InvalidProtocolBufferException e) {
            return null;
        }finally {
            if(jedis != null)
                jedis.close();
        }
    }

}
