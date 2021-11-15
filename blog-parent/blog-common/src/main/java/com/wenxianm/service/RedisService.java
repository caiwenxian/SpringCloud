package com.wenxianm.service;

import com.google.common.primitives.Primitives;
//import io.lettuce.core.SetArgs;
//import io.lettuce.core.api.async.RedisAsyncCommands;
//import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * redis服务
 * @ClassName RedisUtil
 * @Author cwx
 * @Date 2021/10/26 17:53
 **/
@Component
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Serializable> jdkRedisTemplate;

    public void setJdkRedisTemplate(RedisTemplate<String, Serializable> jdkRedisTemplate) {
        this.jdkRedisTemplate = jdkRedisTemplate;
    }

    public void setStringRedisTemplate(RedisTemplate<String, String> stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void delete(String key) {
        jdkRedisTemplate.delete(key);
    }

    public void delete(Collection keys) {
        jdkRedisTemplate.delete(keys);
    }

    public int getInt(String key) {
        try {
            String obj = stringRedisTemplate.opsForValue().get(key);
            return Integer.parseInt(obj);
        } catch (Exception e) {
            log.error(String.format("exception=%s,key=%s", e.getMessage(), key), e);
        }
        return 0;
    }

    public void setInt(String key, int value) {
        stringRedisTemplate.opsForValue().set(key, String.valueOf(value));
    }

    public void setInt(String key, int value, long seconds) {
        stringRedisTemplate.opsForValue().set(key, String.valueOf(value));
        stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    public boolean exists(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    public Boolean setNx(String key, String value) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, value);
    }

    public Boolean setNxUseMilliseconds(String key, String value, long expire) {
        Boolean resultBoolean = null;
        try {
            resultBoolean = stringRedisTemplate.execute((RedisCallback<Boolean>) connection -> {
                Object nativeConnection = connection.getNativeConnection();
                String redisResult = "";
                RedisSerializer<String> stringRedisSerializer = (RedisSerializer<String>) stringRedisTemplate
                        .getKeySerializer();
                //lettuce连接包下序列化键值，否知无法用默认的ByteArrayCodec解析
                byte[] keyByte = stringRedisSerializer.serialize(key);
                byte[] valueByte = stringRedisSerializer.serialize(value);
                // lettuce连接包下 redis 单机模式setnx
                /*if (nativeConnection instanceof RedisAsyncCommands) {
                    RedisAsyncCommands commands = (RedisAsyncCommands) nativeConnection;
                    //同步方法执行、setnx禁止异步
                    redisResult = commands
                            .getStatefulConnection()
                            .sync()
                            .set(keyByte, valueByte, SetArgs.Builder.nx().px(expire));
                }*/
                if (nativeConnection instanceof JedisConnection) {
                    JedisConnection clusterAsyncCommands = (JedisConnection) nativeConnection;
                    redisResult = clusterAsyncCommands
                            .getNativeConnection()
                            .set(keyByte, valueByte, "NX".getBytes(), "PX".getBytes(), expire);
                }
                //返回加锁结果
                return "OK".equalsIgnoreCase(redisResult);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultBoolean != null && resultBoolean;
    }

    public Boolean setNx(String key, String value, long expire) {
        Boolean resultBoolean = null;
        try {
            resultBoolean = stringRedisTemplate.execute((RedisCallback<Boolean>) connection -> {
                Object nativeConnection = connection.getNativeConnection();
                String redisResult = "";
                RedisSerializer<String> stringRedisSerializer = (RedisSerializer<String>) stringRedisTemplate
                        .getKeySerializer();
                //lettuce连接包下序列化键值，否知无法用默认的ByteArrayCodec解析
                byte[] keyByte = stringRedisSerializer.serialize(key);
                byte[] valueByte = stringRedisSerializer.serialize(value);
                // lettuce连接包下 redis 单机模式setnx
                /*if (nativeConnection instanceof RedisAsyncCommands) {
                    RedisAsyncCommands commands = (RedisAsyncCommands) nativeConnection;
                    //同步方法执行、setnx禁止异步
                    redisResult = commands
                            .getStatefulConnection()
                            .sync()
                            .set(keyByte, valueByte, SetArgs.Builder.nx().ex(expire));
                }*/
                if (nativeConnection instanceof JedisConnection) {
                    JedisConnection clusterAsyncCommands = (JedisConnection) nativeConnection;
                    redisResult = clusterAsyncCommands
                            .getNativeConnection()
                            .set(keyByte, valueByte, "NX".getBytes(), "EX".getBytes(), expire);
                }
                //返回加锁结果
                return "OK".equalsIgnoreCase(redisResult);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultBoolean != null && resultBoolean;
    }

    public void expire(String key, long seconds) {
        jdkRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    public <T> T getObject(String key, Class<T> cls) {
        try {
            Serializable obj = jdkRedisTemplate.opsForValue().get(key);
            return Primitives.wrap(cls).cast(obj);
        } catch (Exception e) {
            log.error(String.format("exception=%s,key=%s", e.getMessage(), key), e);
        }
        return null;
    }

    public void setObject(String key, Object value) {
        setObject(key,value,null);
    }

    public void setObject(String key, Object value, Long seconds) {
        if (value == null || !(value instanceof Serializable)) {
            throw new RuntimeException("对象没有实现序列化");
        }
        try {
            jdkRedisTemplate.opsForValue().set(key, (Serializable) value);
            if (seconds != null) {
                jdkRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error(String.format("exception=%s,key=%s", e.getMessage(), key), e);
        }
    }

    public void setString(String key, String value) {
        setString(key,value,null);
    }

    public void setString(String key, String value, Long seconds) {
        stringRedisTemplate.opsForValue().set(key, value);
        if (seconds != null) {
            stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
        }
    }

    public String getString(String key) {
        try {
            Object obj = stringRedisTemplate.opsForValue().get(key);
            return (String) obj;
        } catch (Exception e) {
            log.error(String.format("exception=%s,key=%s", e.getMessage(), key), e);
        }
        return null;
    }

}
