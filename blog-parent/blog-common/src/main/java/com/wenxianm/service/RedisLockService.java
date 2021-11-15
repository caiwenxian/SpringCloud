package com.wenxianm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * redis加锁服务
 * @ClassName RedisLockService
 * @Author cwx
 * @Date 2021/10/26 18:05
 **/
@Component
@Slf4j
public class RedisLockService {

    @Autowired
    RedisService redisService;

    /** 默认锁过期时间 */
    private static final Long DEFAULT_SINGLE_EXPIRE_TIME = 5L;

    /**
     * 获取锁，非阻塞
     * @param key
     * @param value
     * @author caiwx
     * @date 2021/10/26 - 18:05
     * @return boolean
     **/
    /*public boolean tryLock(String key, String value) {
        return tryLock(key, value, 0L, null);
    }

    *//**
     * 指定时间内获取锁，阻塞
     * @param key
     * @param value
     * @param timeout 获取锁的时长
     * @param unit 时间单位
     * @author caiwx
     * @date 2021/10/26 - 18:07
     * @return boolean
     **//*
    public boolean tryLock(String key, String value, long timeout, TimeUnit unit) {
        return tryLock(key, value, timeout, unit, DEFAULT_SINGLE_EXPIRE_TIME);
    }

    *//**
     * 指定时间内获取锁，阻塞
     * @param key
     * @param value
     * @param timeout 获取锁的时长
     * @param unit 时间单位
     * @param expire 锁的过期时间
     * @author caiwx
     * @date 2021/10/26 - 18:10
     * @return
     **/
    public boolean tryLock(String key, String value, long timeout, TimeUnit unit, long expire) {
        // 纳秒
        long begin = System.nanoTime();
        try {
            do {
                expire = expire > 0 ? expire : DEFAULT_SINGLE_EXPIRE_TIME;
                boolean nx = redisService.setNx(getLockKey(key), value, expire);
                if (nx) {
                    log.debug("{}成功获取{}的锁,设置锁过期时间为{}秒 ", value, key, expire);
                    return true;
                }
                if (timeout == 0) {
                    break;
                }
                Thread.sleep(100);
            } while ((System.nanoTime() - begin) < unit.toNanos(timeout));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 释放锁，只能由持有锁的线程释放
     * @param key
     * @param value
     * @author caiwx
     * @date 2021/10/26 - 18:12
     **/
    public void unLock(String key, String value) {
        try {
            String hold = redisService.getString(getLockKey(key));
            if (value.equals(hold)) {
                redisService.delete(getLockKey(key));
            } else {
                log.info("{}锁非持有者不能释放,value:{},hold:{} .", key, value, hold);
            }
            log.debug("{}锁被释放 .", key);
        } catch (Exception e) {
            log.error("释放锁异常", e);
        }
    }

    /**
     * 获取锁的键
     * @param key
     * @author caiwx
     * @date 2021/10/26 - 18:14
     * @return String
     **/
    public String getLockKey(String key) {
        return ("R_LOCK_" + key);
    }

}
