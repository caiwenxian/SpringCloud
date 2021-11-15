package com.wenxianm.service;

import com.wenxianm.model.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;

/**
 * redis阻塞队列
 * @ClassName RedisQueue
 * @Author cwx
 * @Date 2021/11/11 10:41
 **/
@Component
@AutoConfigureAfter(RedisAutoConfiguration.class)
@Slf4j
public class RedisQueueService {

    @Resource
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 推送到队列
     * @param key 队列名称
     * @param value 元素
     * @author caiwx
     * @date 2021/11/11 - 10:47
     **/
    public void lPush(@Nonnull String key, @Nonnull String value) {
        RedisConnection connection = RedisConnectionUtils.getConnection(redisConnectionFactory);
        try {
            byte[] byteKey = new StringRedisSerializer().serialize(getKey(key));
            byte[] byteValue = new StringRedisSerializer().serialize(value);
            assert byteKey != null;
            connection.lPush(byteKey, byteValue);
            log.info("[{}]队列push元素[{}]", getKey(key), value);
        } finally {
            RedisConnectionUtils.releaseConnection(connection, redisConnectionFactory);
        }
    }

    /**
     * 消费队列
     * @param key 队列名称
     * @param consumer 消费者
     * @author caiwx
     * @date 2021/11/11 - 10:50
     **/
    public void bRPopLPush(@Nonnull String key, Consumer<String> consumer) {
        CompletableFuture.runAsync(() -> {
            RedisConnection connection = RedisConnectionUtils.getConnection(redisConnectionFactory);
            try {
                byte[] srcKey = new StringRedisSerializer().serialize(getKey(key));
                byte[] dstKey = new StringRedisSerializer().serialize(getBackupKey(key));
                assert srcKey != null;
                assert dstKey != null;
                while (true) {
                    byte[] byteValue = new byte[0];
                    boolean success = false;
                    try {
                        byteValue = connection.bRPopLPush(0, srcKey, dstKey);
                        if (byteValue != null && byteValue.length != 0) {
                            consumer.accept(new String(byteValue));
                            success = true;
                            log.info("[{}]队列pop元素[{}]", getKey(key), new String(byteValue));
                        }
                    } catch (Exception ignored) {
                        // 防止获取 key 达到超时时间抛出 QueryTimeoutException 异常退出
                        log.info("[{}]队列pop元素[{}]失败", getKey(key), byteValue);
                        log.info("error:", ignored);
                    } finally {
                        if (success) {
                            connection.lRem(dstKey, 1, byteValue);
                        }
                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                RedisConnectionUtils.releaseConnection(connection, redisConnectionFactory);
            }
        }, threadPoolExecutor);
    }

    public String getKey(String key) {
        return Constants.REDIS_QUEUE + key;
    }

    public String getBackupKey(String key) {
        return Constants.REDIS_QUEUE_BACKUP + key;
    }
}
